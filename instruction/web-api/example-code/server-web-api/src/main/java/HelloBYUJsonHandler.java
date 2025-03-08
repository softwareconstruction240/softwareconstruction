import io.javalin.http.Context;

public class HelloBYUJsonHandler {
    public void handleRequest(Context context) {
        context.status(200);
        context.contentType("application/json");
        context.header("CS240", "Awesome!");
        context.result("{message: Hello BYU!}");
    }
}
