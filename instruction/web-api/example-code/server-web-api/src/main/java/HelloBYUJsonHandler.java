import io.javalin.http.Context;
import io.javalin.http.Handler;

public class HelloBYUJsonHandler implements Handler {
    public void handle(Context context) {
        context.status(200);
        context.contentType("application/json");
        context.header("CS240", "Awesome!");
        context.result("{message: Hello BYU!}");
    }
}
