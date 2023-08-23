import spark.Spark;

public class CleanStopServer {
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            Spark.port(port);

            createRoutes();

            addShutdownHook();

            Spark.awaitInitialization();
            System.out.println("Listening on port " + port);
        } catch(ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            System.err.println("Specify the port number as a command line parameter");
        }
    }

    private static void createRoutes() {
        Spark.get("/hello", (req, res) -> "Hello BYU!");
    }

    /**
     * This is not necessary for a simple server. It ensures that the Spark server has a
     * chance to complete any routes in progress and release resources before shutting down.
     */
    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping the server...");
            Spark.stop();
            System.out.println("Server stopped");
        }));
    }
}
