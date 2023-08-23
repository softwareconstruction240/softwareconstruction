import spark.Spark;

public class HelloBYUServer {
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            Spark.port(port);

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
}
