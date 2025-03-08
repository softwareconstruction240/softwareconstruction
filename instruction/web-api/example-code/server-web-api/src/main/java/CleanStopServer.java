import io.javalin.Javalin;

public class CleanStopServer {
    public static void main(String[] args) {
        int requestedPort = Integer.parseInt(args[0]);

        var server = new CleanStopServer();
        int port = server.run(requestedPort);
        System.out.println("Running on port " + port);
    }

    public int run(int requestedPort) {
        Javalin javalinServer = Javalin.create();

        createHandlers(javalinServer);

        javalinServer.start(requestedPort);

        addShutdownHook(javalinServer);
        return javalinServer.port();
    }

    private void createHandlers(Javalin javalinServer) {
        HelloBYUHandler helloHandler = new HelloBYUHandler();
//        HelloBYUJsonHandler helloHandler = new HelloBYUJsonHandler();
        // Other handlers here

        javalinServer.get("/hello", helloHandler::handleRequest);
        // Other routes here
    }

    /**
     * This is not necessary for a simple server. It ensures that the Javalin server has a
     * chance to complete any requests in progress and release resources before shutting down.
     */
    private void addShutdownHook(Javalin javalinServer) {
        javalinServer.events(event -> {
            event.serverStopping(() -> System.out.println("Stopping the server..."));
            event.serverStopped(() -> System.out.println("Server stopped"));
        });

        Runtime.getRuntime().addShutdownHook(new Thread(javalinServer::stop));
    }
}
