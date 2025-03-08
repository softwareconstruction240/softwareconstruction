import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;

public class PostExample {
    private static final int TIMEOUT_MILLIS = 5000;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length == 4) {
            String host = args[0];
            String portString = args[1];
            String path = args[2];
            String message= args[3];

            var client = new PostExample();

            try {
                client.doPost(host, Integer.parseInt(portString), path, message);
            } catch (URISyntaxException | NumberFormatException e) {
                // Print usage if port is not an int or path is invalid
                printUsage();
            }
        } else {
            printUsage();
        }
    }

    private static void printUsage() {
        System.err.println("USAGE: java GetExample_FluentApi <host> <port> <path> <message>");
    }

    public void doPost(String host, int port, String urlPath, String message) throws URISyntaxException, IOException, InterruptedException {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", host, port, urlPath);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                .header("authorization", "abc123")
                .POST(HttpRequest.BodyPublishers.ofString(message, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if(httpResponse.statusCode() == 200) {
            HttpHeaders headers = httpResponse.headers();
            Optional<String> lengthHeader = headers.firstValue("Content-Length");

            System.out.printf("Received %s bytes%n", lengthHeader.orElse("unknown"));
            System.out.println(httpResponse.body());
        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
            System.out.println(httpResponse.body());
        }
    }
}
