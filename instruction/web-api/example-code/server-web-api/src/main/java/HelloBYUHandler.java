import io.javalin.http.Context;

public class HelloBYUHandler {
    public void handleRequest(Context context) {
        context.result("Hello BYU!");
    }
}
