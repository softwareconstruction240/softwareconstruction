import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class Alternate2SimpleHelloBYUServer {
    public static void main(String[] args) {
        Javalin.create()
                .get("/hello", new HelloHandler())
                .start(8080);
    }

    private static class HelloHandler implements Handler {
        @Override
        public void handle(@NotNull Context ctx) throws Exception {
            ctx.result("Hello BYU!");
        }
    }
}
