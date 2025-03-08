import io.javalin.Javalin;
import io.javalin.http.Context;

public class AlternateSimpleHelloBYUServer {
    public static void main(String[] args) {
        Javalin.create()
                .get("/hello", AlternateSimpleHelloBYUServer::handleHello)
                .start(8080);
    }

    private static void handleHello(Context ctx) {
        ctx.result("Hello BYU!");
    }
}
