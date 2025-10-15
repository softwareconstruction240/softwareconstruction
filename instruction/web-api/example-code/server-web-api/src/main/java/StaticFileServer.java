import io.javalin.Javalin;

public class StaticFileServer {
    public static void main(String[] args) {
        int requestedPort = Integer.parseInt(args[0]);

        var server = new StaticFileServer();
        int port = server.run(requestedPort);
        System.out.println("Running on port " + port);
    }

    public int run(int requestedPort) {
        Javalin javalinServer = Javalin.create(
                config -> config.staticFiles.add("web")
        );

        createHandlers(javalinServer);

        javalinServer.start(requestedPort);
        return javalinServer.port();
    }

    private void createHandlers(Javalin javalinServer) {
        javalinServer.get("/hello", new HelloBYUHandler());
        // Other routes here
    }
}
