import io.javalin.Javalin;

public class SimpleHttpEchoServer {
    public static void main(String[] args) {
        Javalin.create()
                .get("/echo/{msg}", ctx -> ctx.result("HTTP response: " + ctx.pathParam("msg")))
                .start(8080);
    }
}
