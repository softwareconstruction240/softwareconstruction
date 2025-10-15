import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Map;

public class ErrorHandlingServer {
    public static void main(String[] args) {
        new ErrorHandlingServer().run();
    }

    private void run() {
        Javalin.create()
                .get("/error", this::throwException)
                .exception(Exception.class, this::exceptionHandler)
                .error(404, this::notFound)
                .start(8080);
    }

    private void throwException(Context context) {
        throw new RuntimeException("The server is on fire!");
    }

    private void exceptionHandler(Exception e, Context context) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        context.status(500);
        context.json(body);
    }

    private void notFound(Context context) {
        String msg = String.format("[%s] %s not found", context.method(), context.path());
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", msg), "success", false));
        context.json(body);
    }
}
