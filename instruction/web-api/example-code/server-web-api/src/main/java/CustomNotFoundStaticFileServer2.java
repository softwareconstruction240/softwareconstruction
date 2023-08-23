import spark.Spark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CustomNotFoundStaticFileServer2 {
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            Spark.port(port);

            // Must be done before mapping routes
            Spark.staticFiles.location("/public");

            Spark.notFound((req, res) -> {
                res.type("text/html");
                return readFromClasspathDirectory("/public/404.html");
            });

            createRoutes();

            Spark.awaitInitialization();
            System.out.println("Listening on port " + port);
        } catch(ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            System.err.println("Specify the port number as a command line parameter");
        }
    }

    private static void createRoutes() {
        Spark.get("/hello", (req, res) -> "Hello BYU!");
    }

    private static String readFromClasspathDirectory(String file) {
        try {
            InputStream inputStream = CustomNotFoundStaticFileServer2.class.getResourceAsStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            return content.toString();
        } catch (IOException e) {
            return file + " file not found.";
        }
    }
}
