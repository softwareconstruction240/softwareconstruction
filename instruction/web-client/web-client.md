
# Web Client

🖥️ [Slides: Client](https://docs.google.com/presentation/d/11UOJixn1VLDDcmioRWEpIfPXvBXFT8cR/edit?usp=sharing&ouid=110961336761942794636&rtpof=true&sd=true)


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
