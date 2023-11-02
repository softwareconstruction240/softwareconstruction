import server.PetServer;

public class ServerMain {
    /**
     * Starts the server on the given port. If port is 0 then a random port is used.
     */
    public static void main(String[] args) {
        var port = 8080;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }

        var server = new PetServer().run(port);
        port = server.port();
        System.out.printf("Server started on port %d%n", port);
    }
}