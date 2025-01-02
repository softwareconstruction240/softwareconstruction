import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

public class PostExample {
    private static final HttpClient client = HttpClient.newHttpClient();

    public String doPost(String urlString, String body) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(URI.create(urlString))
                // Use a different BodyPublisher for other types of input
                .POST(BodyPublishers.ofString(body))
                .timeout(Duration.ofSeconds(5))
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
        String responseBody = response.body();
        return responseBody;
    }
}
