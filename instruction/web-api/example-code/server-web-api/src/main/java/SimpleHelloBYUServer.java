import io.javalin.Javalin;

public class SimpleHelloBYUServer {
    public static void main(String[] args) {
        Javalin.create()
                .get("/hello", ctx -> ctx.result("Hello BYU!"))
                .start(8080);
    }
}
