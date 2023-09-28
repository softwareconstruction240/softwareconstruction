# Web API

🖥️ [Slides: Server](https://docs.google.com/presentation/d/1Nvb0fUObt-An0nMOFEhgIufnSN6qpixF/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

🖥️ [Slides: Client](https://docs.google.com/presentation/d/1P-qIn-6mrZ28UuRFtFMIvGnjygOtOzZ5/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

Now that you understand how HTTP works at a theoretical level you can write Java code to make requests from an HTTP client and respond from an HTTP server.

## Web Server

For our server code, we will use a library called [JavaSpark](https://sparkjava.com/). `JavaSpark` makes it very easy to write an HTTP server that handles multiple endpoint requests. An endpoint is the code that handles a specific HTTP resource request. You can think of the service endpoints as being the public methods of the service interface. A simple code example for handling the `list`: `[GET] /name`, `create`: `[POST] /name/:name`, and `delete`: `[DELETE] /name/:name` endpoints, would look like the following.

```java
import com.google.gson.Gson;
import spark.*;
import java.util.*;

public class ServerExample {
    private ArrayList<String> names = new ArrayList<>();

    public static void main(String[] args) {
        new ServerExample().run();
    }

    private void run() {
        // Specify the port you want the server to listen on
        Spark.port(8080);

        // Register a directory for hosting static files
        Spark.externalStaticFileLocation("public");

        // Register handlers for each endpoint using the method reference syntax
        Spark.get("/name", this::listNames);
        Spark.post("/name/:name", this::createName);
        Spark.delete("/name/:name", this::deleteName);
    }

    private Object listNames(Request req, Response res) {
        res.type("application/json");
        return new Gson().toJson(Map.of("name", names));
    }

    private Object createName(Request req, Response res) {
        names.add(req.params(":name"));
        return listNames(req, res);
    }


    private Object deleteName(Request req, Response res) {
        names.remove(req.params(":name"));
        return listNames(req, res);
    }
}
```

You should also notice the call to `externalStaticFileLocation`. This loads static files from the directory given by the parameter. In the example above, if you have a file named `hello.html` in the `public` directory, then it will be served up to your HTTP client if a request to `localhost:8080/hello.html` is made.

### Serializing Requests and Responses

JSON is commonly used to send serialized objects over HTTP requests. Therefore you will want to use Gson to parse the body of HTTP requests into objects, and to create JSON that represents your response. The following is an example of a server with an `echo` endpoint. It parses the request body into a Java `Map` object and then serializes it back into the endpoint response.

```java
public class JsonRequestResponseExample {
    public static void main(String[] args) {
        new JsonRequestResponseExample().run();
    }

    private void run() {
        Spark.port(8080);
        Spark.post("/echo", this::echoBody);
    }

    private Object echoBody(Request req, Response res) {
        var bodyObj = getBody(req, Map.class);

        res.type("application/json");
        return new Gson().toJson(bodyObj);
    }

    private static <T> T getBody(Request request, Class<T> clazz) {
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}
```

The `getBody` method is a generic method that will parse the request body into an object of the class that you specify. This pattern of combining generics, Gson, and HTTP bodies makes it easy to get data in and out of your service.

### Error Handling

In addition to representing endpoints, Spark provides methods for handling error cases. This includes the `Spark.exception` method for when an unhandled exception is thrown, and the `Spark.notFound` for when an unknown request is made. With both methods you provide the implementation of a functional interface for handling the error. The following code demonstrates how this is done.

```java
public class ServerErrorsExample {
    public static void main(String[] args) {
        new ServerErrorsExample().run();
    }

    private void run() {
        // Specify the port you want the server to listen on
        Spark.port(8080);

        // Register handlers for each endpoint using the method reference syntax
        Spark.get("/error", this::throwError);

        Spark.exception(Exception.class, this::errorHandler);
        Spark.notFound((req, res) -> {
            var msg = String.format("[%s] %s not found", req.requestMethod(), req.pathInfo());
            return errorHandler(new Exception(msg), req, res);
        });

    }

    private Object throwError(Request req, Response res) {
        throw new RuntimeException("Server on fire");
    }

    public Object errorHandler(Exception e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(500);
        res.body(body);
        return body;
    }
}
```

When this server is running you will get the following results when you make requests using Curl.

```sh
➜ curl -X GET localhost:8080/unknownendpoint
{"success":false,"message":"Error: [GET] /unknownendpoint not found"}%

➜ curl -X GET localhost:8080/error
{"success":false,"message":"Error: Server on fire"}%
```

## Web Client

For our client code, we can use the standard JDK `java.net` library to make HTTP requests. The following example hard codes the URL, but the terse nature of the example helps to demonstrate the essential pieces of the request.

```java
public class ClientExample {
    public static void main(String[] args) throws Exception {
        // Specify the desired endpoint
        URI url = new URI("http://localhost:8080/name");
        HttpURLConnection http = (HttpURLConnection) url.toURL().openConnection();
        http.setRequestMethod("GET");

        // Make the request
        http.connect();

        // Output the response body
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            System.out.println(new Gson().fromJson(inputStreamReader, Map.class));
        }
    }
}
```

## Things to Understand

- Server code example (Ticket to Ride)
- Writing the main Server class
- Writing HTTP handlers for GET and POST requests
- Implementing the Test Web Page using a FileHandler
- Writing a web client

## Videos

⚠ These videos use an application called Family Map to demonstrate the concepts of implementing HTTP for a client and server. This course no longer uses this application, but you may still find the concepts that they present of interest.

- 🎥 [Server Sample Code](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6d6b5c43-2521-4b25-8177-ad7201201c19&start=0)
- 🎥 [HTTP GET Handler Sample Code](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=1a90d240-18c0-4baa-9b63-ad7201268866&start=0)
- 🎥 [HTTP POST Handler Sample Code](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=4619d1c4-ac73-4237-90bd-ad72012b020a&start=0)
- 🎥 [Writing the File Handler](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=77ce21ae-8225-460d-8a9e-ad72012d3094&start=0)

## Demonstration code

📁 [Client Web API](example-code/client-web-api)

📁 [Server Web API](example-code/server-web-api)
