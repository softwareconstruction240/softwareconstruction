import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import dataaccess.MySqlDataAccess;
import server.PetServer;
import service.PetService;

public class ServerMain {

    /**
     * Starts the server on the given port. If port is 0 then a random port is used.
     */
    public static void main(String[] args) {
        try {
            var port = 8080;
            if (args.length >= 1) {
                port = Integer.parseInt(args[0]);
            }

            DataAccess dataAccess = new MemoryDataAccess();
            if (args.length >= 2 && args[1].equals("sql")) {
                dataAccess = new MySqlDataAccess();
            }

            var service = new PetService(dataAccess);
            var server = new PetServer(service).run(port);
            port = server.port();
            System.out.printf("Server started on port %d with %s%n", port, dataAccess.getClass());
            return;
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
        System.out.println("""
                Pet Server:
                java ServerMain <port> [sql]
                """);
    }
}