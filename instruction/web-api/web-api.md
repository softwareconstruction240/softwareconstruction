# Web API

🖥️ [Slides: Server](https://docs.google.com/presentation/d/173oiXc1ahCnZ36NJHjp3RMHCS2Xle2NH/edit?usp=sharing&ouid=110961336761942794636&rtpof=true&sd=true)

🖥️ [Slides: Server Implementation Tips](https://docs.google.com/presentation/d/1am-_YWoZ1AX5_oZZAORjzsLbb8dy0_2S/edit?usp=sharing&ouid=110961336761942794636&rtpof=true&sd=true)

🖥️ [Slides: Client](https://docs.google.com/presentation/d/11UOJixn1VLDDcmioRWEpIfPXvBXFT8cR/edit?usp=sharing&ouid=110961336761942794636&rtpof=true&sd=true)

🖥️ [Lecture Videos](#videos)

Now that you understand how HTTP works at a theoretical level you can write Java code to make requests from an HTTP client and respond from an HTTP server.

## Web Server

For our server code, we will use a library called [Javalin](https://javalin.io/). `Javalin` makes it very easy to write an HTTP server that handles multiple endpoint requests. An endpoint is the code that handles a specific HTTP resource request. You can think of the service endpoints as being the public methods of the service interface.

As an example, let's write an HTTP service named `name list` that maintains a list of names. To make the service useful we will provide the following endpoints.

| Endpoint   | HTTP Method | HTTP path    | Purpose                                                   |
| ---------- | ----------- | ------------ | --------------------------------------------------------- |
| addName    | POST        | /name/{name} | Add the name represented by the `{name}` path variable    |
| listNames  | GET         | /name        | Get the list of names                                     |
| deleteName | DELETE      | /name/{name} | Delete the name represented by the `{name}` path variable |

### Implementing Endpoints

When you define an endpoint with `Javalin`, you supply the HTTP method, path, and a Functional Interface method implementation that is called when the matching HTTP request is made. The path definition may contain variables, designated with curly braces `{}` or angle brackets`<>`, that are assigned to the values provided by the caller. Angle bracket variables may contain slashes `/`, whereas curly braces may not. For example, you would register the endpoint to add a name with the following implementation.

```java
private void run() {
    ...
    Javalin.create()
        .post("/name/{name}", new Handler() {
             public void handle(Context context) throws Exception {
                names.add(context.pathParam("name"));
                listNames(context);
             }
        })
    ...
}

private void listNames(Context context) {
    String jsonNames = new Gson().toJson(Map.of("name", names));

    context.contentType("application/json");
    context.result(jsonNames);
}
```

In the above example, the `post` method is called to handle HTTP POST requests for the `/name/{name}` path. The `post` method takes two parameters, the HTTP path and an anonymous class implementation of the functional interface `io.javalin.http.Handler`. The interface has one method named `handle` that is called when the HTTP method and path is matched by an incoming HTTP request. The `handle` method receives a `Context` object that represent the HTTP request and response. Our implementation then reads the path `name` variable from the request and adds the name to an internal list of names.

The return value for the endpoint is speceified by calling the `listNames` method. This sets the `Content-Type` HTTP header to `application/json`, serializes the current name list using Gson, and sets the resulting JSON string as the HTTP response body by calling `context.result`.

We can simplify the representation of our post handler in two ways: 1) by using a lambda function to call a method that implements the `addName` endpoint, and 2) by calling `context.json(...)` in the `listNames` method, which will set the `Content-Type` to `application/json` and set the response body.

```java
private void run() {
    ...
    Javalin.create()
        .post("/name{name}", context -> addName(context))
    ...
}

private void addName(Context context) {
    names.add(context.pathParam("name"));
    listNames(context);
}

private void listNames(Context context) {
    String jsonNames = new Gson().toJson(Map.of("name", names));
    context.json(jsonNames);
}
```

Finally, since our lambda function is simply a passthrough to another function, we can replace it with the Java `method reference` syntax.

```java
Javalin.create()
    .post("/name{name}", this::addName)
```

## HTTP Headers

In addition to passing information through the HTTP path and body, you can pass information using HTTP headers. For example, if you needed a valid authorization token for some of your endpoints then you could write a method that gets a authorization HTTP header and checks the value against an existing list of valid tokens. If the token is not provided in the HTTP `authentication` header, or it is not valid then an HTTP 401 status code is returned.

```java
final private HashSet<String> validTokens = new HashSet<>(Set.of("secret1", "secret2"));

private boolean authorized(Context ctx) {
    String authToken = ctx.header("authentication");
    if (!validTokens.contains(authToken)) {
        ctx.contentType("application/json");
        ctx.status(401);
        ctx.result(new Gson().toJson(Map.of("msg", "invalid authorization")));
        return false;
    }
    return true;
}
```

You could then wrap your secure endpoints with an authorization test.

```java
private void addName(Context ctx) {
    if (authorized(ctx)) {
        names.add(ctx.pathParam("name"));
        listNames(ctx);
    }
}
```

## Serving Static Files

An HTTP resource can represent anything. In the above example we are representing an in memory representation of a name list, but we can also represent a directory structure of files in persistent storage. Javalin makes it easy to do this by specifying a configuration function with the name of a directory that contains the files we want to return over HTTP. Once the location is registered, Javalin will look in the directory for a file that matches the URL path. If it is found, it returns the contents of the file. Javalin will even examine the file to determine what `Content-Type` header to set.

```java
Javalin javalinServer = Javalin.create(config -> config.staticFiles.add("web"));
```

By adding the above code to your server you can now make a request to the server with a path like `/index.html` and it will return the `index.html` file found in a directory named `web` that is found in a parent directory on your application's Classpath. In Intellij, the parent directory is typically any directory marked as a `Rousources Root`.

## Complete Server Example

Here is the complete listing of server code for hosting static files and the `name list` service endpoints.

```java
import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.Map;

public class SimpleNameServer {
    private ArrayList<String> names = new ArrayList<>();

    public static void main(String[] args) {
        new SimpleNameServer().run();
    }

    private void run() {
        Javalin.create(config -> config.staticFiles.add("web"))
                .post("/name/{name}", this::addName)
                .get("/name", this::listNames)
                .delete("/name/{name}", this::deleteName)
                .start(8080);
    }

    private void addName(Context ctx) {
        if (authorized(ctx)) {
            names.add(ctx.pathParam("name"));
            listNames(ctx);
        }
    }

    private void listNames(Context ctx) {
        ctx.contentType("application/json");
        ctx.result(new Gson().toJson(Map.of("name", names)));
    }


    private void deleteName(Context ctx) {
        if (authorized(ctx)) {
            names.remove(ctx.pathParam("name"));
            listNames(ctx);
        }
    }

    final private HashSet<String> validTokens = new HashSet<>(Set.of("secret1", "secret2"));

    private boolean authorized(Context ctx) {
        String authToken = ctx.header("Authorization");
        if (!validTokens.contains(authToken)) {
            ctx.contentType("application/json");
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("msg", "invalid authorization")));
            return false;
        }
        return true;
    }
}
```

You can experiment with this code by doing the following.

1. Create a directory name `web` and put an `index.html` file in it that contains the text: `<h1>Hello World</h1>`.
1. Run the code from a directory relative to the directory that contains the `web` directory.<br/> Note: For this to work, you will also need to download the Javalin and Gson jar files and include them on your CLASSPATH when you run the code. You can download them from the Maven Repository and use the -classpath (or -cp) JVM flag.
1. Open your browser and point it to `localhost:8080`. This should display the contents of your `index.html` file.
1. Run the following commands with Curl
   1. `curl localhost:8080/name`, returns `{"name":[]}`
   1. `curl -X POST localhost:8080/name/cow -H "Authorization: secret1"`, returns `{"name":["cow"]}`
   1. `curl -X POST localhost:8080/name/dog -H "Authorization: secret1"`, returns `{"name":["cow","dog"]}`
   1. `curl localhost:8080/name`, returns `{"name":["cow","dog"]}`
   1. `curl -X DELETE localhost:8080/name/dog -H "Authorization: secret1"`, returns `{"name":["cow"]}`
   1. `curl localhost:8080/name`, returns `{"name":["cow"]}`

### Serializing Requests and Responses

JSON is commonly used to send serialized objects over HTTP requests. Therefore you will want to use Gson to parse the body of HTTP requests into objects, and to create JSON that represents your response. The following is an example of a server with an `echo` endpoint. It parses the request body into a Java `Map` object and then serializes it back into the endpoint response.

```java
import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Map;

public class EchoJsonServer {
    public static void main(String[] args) {
        new EchoJsonServer().run();
    }

    private void run() {
        Javalin.create()
                .post("/echo", this::echo)
                .start(8080);
    }

    private void echo(Context context) {
        // Convert body json to object
        Map bodyObject = getBodyObject(context, Map.class);

        // Convert bodyObject back to json and send to client
        String json = new Gson().toJson(bodyObject);
        context.json(json);
    }

    private static <T> T getBodyObject(Context context, Class<T> clazz) {
        var bodyObject = new Gson().fromJson(context.body(), clazz);

        if (bodyObject == null) {
            throw new RuntimeException("missing required body");
        }

        return bodyObject;
    }
}
```

The `getBodyObject` method is a generic method that will parse the request body into an object of the class that you specify. This pattern of combining generics, Gson, and HTTP bodies makes it easy to get data in and out of your service.

Build this code and try it out. Use curl to make your requests. Set breakpoints in your code and walk through what is happening. Step into the code if you want to understand how Javalin and Gson work.

```sh
➜  curl localhost:8080/echo -d '{"name":"dog", "count":3}'

{"name":"dog","count":3.0}
```

### Server Error Handling

In addition to representing endpoints, Javalin provides methods for handling error cases. This includes the `Javalin.exception` method for when an unhandled exception is thrown, and the `Javalin.error` method for when an unknown request is made. With both methods you provide the implementation of a functional interface for handling the error. The following code demonstrates how this is done.

```java
import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Map;

public class ErrorHandlingServer {
    public static void main(String[] args) {
        new ErrorHandlingServer().run();
    }

    private void run() {
        Javalin.create()
                .get("/error", this::throwException)
                .exception(Exception.class, this::exceptionHandler)
                .error(404, this::notFound)
                .start(8080);
    }

    private void throwException(Context context) {
        throw new RuntimeException("The server is on fire!");
    }

    private void exceptionHandler(Exception e, Context context) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        context.status(500);
        context.json(body);
    }

    private void notFound(Context context) {
        String msg = String.format("[%s] %s not found", context.method(), context.path());
        exceptionHandler(new Exception(msg), context);
    }
}
```

When this server is running you will get the following results when you make requests using Curl.

```sh
➜ curl -X GET localhost:8080/unknownendpoint
{"message":"Error: [GET] /unknownendpoint not found","success":false}%

➜ curl -X GET localhost:8080/error
{"message":"Error: The server is on fire!","success":false}%
```

## Web Client

For our client code we can use the `HttpClient` class from the standard JDK `java.net` library to make HTTP requests.

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

public class SimpleNameClient {
    // Create an HttpClient for making requests
    // This should be long-lived and shared, so a static final field is good here
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) throws Exception {
        new SimpleNameClient().get("localhost", 8080, "/name");
    }

    private void get(String host, int port, String path) throws Exception {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", host, port, path);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            System.out.println(httpResponse.body());
        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
        }
    }
}
```

If you first run the `name list` service defined above, then you can run the `ClientExample` and see the full round trip HTTP request being handled by your Java code.

```sh
➜  java -cp ../../lib/gson-2.10.1.jar ClientExample.java

{name=["dog", "cat"]}
```

The `response.statusCode()` method tells us what the HTTP status code was for the response. It's important to check the status code, because the body on its own won't tell you if there was an error or not!

### Using HTTP Headers on the Client

Request headers can be added to any request via the `header` method on `HttpRequest` builders. Response headers can be read via the `headers` method on `HttpResponse` objects.

```java
var request = HttpRequest.newBuilder(uri)
    .GET()
    .header("Authorization", "secret1")
    .build();

var response = client.send(request, BodyHandlers.ofString());
var headers = response.headers();
OptionalLong length = response.firstValueAsLong("Content-Length");
Optional<String> type = response.firstValue("Content-Type");
```

### Using BodyHandlers and BodyPublishers

The `client.send()` method takes a `BodyHandler` argument that determines the type of `response.body()`. Different `BodyHandler`s allow using the body in different ways. The `BodyHandlers` class contains several convenient `BodyHandler` factory functions. We've been using the `ofString` body handler, but we could also use other handlers like in the following example:

```java
var request = HttpRequest.newBuilder(uri)
    .GET()
    .build();

var response = client.send(request, BodyHandlers.ofInputStream());
InputStream body = response.body();
```

HTTP methods that require a body take an additional `BodyPublisher` argument. Like `BodyHandler`s, `BodyPublisher`s allow using different sources for the body, and there is a `BodyPublishers` class with factory functions.

```java
var body = Map.of("name", "joe", "type", "cat");
var jsonBody = new Gson().toJson(body);

var request = HttpRequest.newBuilder(uri)
    .POST(BodyPublishers.ofString(jsonBody))
    .header("Content-Type", "application/json")
    .build();
```

## Implementing a Simple Curl

We can expand our Web Client example to implement a simple version of Curl. This example reads the HTTP method, URL, and body from the command line parameters. Using that information, it makes an HTTP request and receives a response.

```java
public class ClientCurlExample {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) throws Exception {
        if (args.length >= 2) {
            var method = args[0];
            var url = args[1];
            var body = args.length == 3 ? args[2] : null;

            HttpResponse<String> response = sendRequest(url, method, body);
            System.out.printf("= Request =========\n[%s] %s\n\n%s\n\n", method, url, body);
            receiveResponse(response);
        } else {
            System.out.println("ClientCurlExample <method> <url> [<body>]");
        }
    }

    private static HttpResponse<String> sendRequest(String url, String method, String body)
            throws InterruptedException, IOException {
        var request = HttpRequest.newBuilder(URI.create(url))
                .method(method, requestBodyPublisher(body))
                .build();
        return client.send(request, BodyHandlers.ofString());
    }

    private static BodyPublisher requestBodyPublisher(String body) throws IOException {
        if (body != null) {
            return BodyPublishers.ofString(body);
        } else {
            return BodyPublishers.noBody();
        }
    }

    private static void receiveResponse(HttpResponse<String> response) {
        var statusCode = response.statusCode();

        var responseBody = new Gson().fromJson(response.body(), Map.class);
        System.out.printf("= Response =========\n[%d]\n\n%s\n\n", statusCode, responseBody);
    }
}
```

If we start up the `Echo Server` example that we created above, we can make a wide variety of HTTP service request using our simple Curl client.

```sh
➜  webapi git:(master) ✗
java -cp ../../lib/gson-2.10.1.jar ClientCurlExample.java POST 'http://localhost:8080/echo' '{"name":"joe", "count":3}'
= Request =========
[POST] http://localhost:8080/echo

{"name":"joe", "count":3}

= Response =========
[200]

{name=joe, count=3.0}
```

## Things to Understand

- Writing the main Server class
- Writing HTTP handlers for GET and POST requests
- Implementing the Test Web Page using a FileHandler
- Writing a web client
- Server and client code examples

## Videos

- Javalin
  - 🎥 [Javalin Overview (24:21)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=7ed95d83-d32e-4bab-ac9a-b2c60106df45) - [transcript]
  - 🎥 [Javalin Handlers (13:41)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=b88c8a46-d98b-4b7a-a07f-b2c6010e7338) - [transcript]
  - 🎥 [Javalin Error Handling (7:14)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=8e6bc974-7475-4ac8-a34c-b2c601129092) - [transcript]
  - 🎥 [Javalin Serving Static Files (4:27)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=bed4bbcd-f27e-4257-90b6-b2c601150a5b) - [transcript]
  - 🎥 [Javalin Installation (1:54)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=d78633c0-b476-4566-8d51-b2c60116b803) - [transcript]
- Pet Shop
  - 🎥 [Pet Shop Demo (4:51)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=41203fd1-89ca-4c47-b259-b2c601183227) - [transcript]
  - 🎥 [Pet Shop Project (4:10)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=097b2912-582f-41b3-a972-b2c6011ab229) - [transcript]
  - 🎥 [How to Use Pet Shop (1:44)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=88cef7c0-a84f-4ee6-b0b2-b2c6011c021f) - [transcript]
- Client-side HTTP
  - 🎥 [Client HTTP (12:25)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=5ddc54fd-39c3-4d01-a303-b2c6011cda0a) - [transcript]

## Demonstration code

📁 [Client Web API](example-code/client-web-api)

📁 [Server Web API](example-code/server-web-api)
