import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

public class GetExample {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) throws IOException, InterruptedException {
        var example = doGet("https://example.com/");
        System.out.println(example);
    }

    public static String doGet(String urlString) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(URI.create(urlString))
                .GET()
                .timeout(Duration.ofSeconds(5))
                // Set HTTP request headers, if necessary
                // .header("Accept", "text/html")
                // .header("Authorization", "fjaklc8sdfjklakl")
                .build();

        var response = client.send(request, BodyHandlers.ofString());
        var successful = response.statusCode() == 200;
        if (!successful) {
            return null;
        }

        // Get response headers, if necessary
        // HttpHeaders headers = response.headers();
        // Optional<String> contentLength = headers.firstValue("Content-Length")

        // String because we used BodyHandlers.ofString()
        // Can be a different type with a different BodyHandler
        String body = response.body();
        return body;
    }
}
