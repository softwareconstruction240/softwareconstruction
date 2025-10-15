import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class HttpEchoRequestHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        ctx.result("HTTP response: " + ctx.pathParam("msg"));
    }
}
