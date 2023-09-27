# Web API

🖥️ [Slides: Server](https://docs.google.com/presentation/d/1Nvb0fUObt-An0nMOFEhgIufnSN6qpixF/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

🖥️ [Slides: Client](https://docs.google.com/presentation/d/1P-qIn-6mrZ28UuRFtFMIvGnjygOtOzZ5/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

## Writing a Web Service in Java

Now that you understand how HTTP works at a theoretical level you can write Java code to make requests from an HTTP client and respond from an HTTP server.

### Web Server

For our server code, we will use a library called [JavaSpark](https://sparkjava.com/). `JavaSpark` makes it very easy to write an HTTP server that handles multiple endpoint requests. An endpoint is the code that handles a specific HTTP resource request. A simple code example for handling the `list`: `[GET] /name`, `create`: `[POST] /name/:name`, and `delete`: `[DELETE] /name/:name` endpoints, would look like the following.

```java
import com.google.gson.Gson;
import spark.*;
import java.util.*;

public class ExampleServer {
    private ArrayList<String> names = new ArrayList<>();

    public static void main(String[] args) {
        new ExampleServer().run();
    }

    private void run() {
        // Specify the port you want the server to listen on
        Spark.port(8080);

        // Register a directory for hosting static files
        Spark.staticFileLocation("public");

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

You should also notice the call to `staticFileLocation`. This loads static files from the directory given by the parameter. In the example above, if you have a file named `hello.html` in the `public` directory, then it will be served up to your HTTP client if a request to `localhost:8080/hello.html` is made.

### Web Client

For our client code, we can use the standard JDK `java.net` library to make HTTP requests. The following example hard codes the URL, but the terse nature of the example helps to demonstrate the essential pieces of the request.

```java
import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.util.Map;

public class ExampleClient {
    public static void main(String[] args) throws Exception {
        // Specify the desired endpoint
        URL url = new URL("http://localhost:8080/name");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
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
