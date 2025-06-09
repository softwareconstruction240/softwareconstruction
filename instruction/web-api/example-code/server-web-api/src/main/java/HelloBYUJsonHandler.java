import io.javalin.http.Context;
import io.javalin.http.Handler;

public class HelloBYUJsonHandler implements Handler {
    public void handle(Context context) {
        context.json("{message: Hello BYU!}");
    }
}
