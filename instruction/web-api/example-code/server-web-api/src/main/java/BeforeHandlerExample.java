import io.javalin.Javalin;

public class BeforeHandlerExample {
    public static void main(String[] args) {
        int requestedPort = Integer.parseInt(args[0]);

        var server = new BeforeHandlerExample();
        int port = server.run(requestedPort);
        System.out.println("Running on port " + port);
    }

    protected int run(int requestedPort) {
        Javalin javalinServer = Javalin.create();

        createHandlers(javalinServer);

        javalinServer.start(requestedPort);
        return javalinServer.port();
    }

    private void createHandlers(Javalin javalinServer) {
        javalinServer.before(context ->
            System.out.println("Executing route " + context.path()));

        javalinServer.get("/hello", new HelloBYUHandler());
    }
}
