import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Map;

public class EchoJsonServer {
    public static void main(String[] args) {
        new EchoJsonServer().run();
    }

    private void run() {
        Javalin.create()
                .post("/echo", this::echo)
                .start(8080);
    }

    private void echo(Context context) {
        // Convert body json to object
        Map bodyObject = getBodyObject(context, Map.class);

        // Convert bodyObject back to json and send to client
        String json = new Gson().toJson(bodyObject);
        context.json(json);
    }

    private static <T> T getBodyObject(Context context, Class<T> clazz) {
        var bodyObject = new Gson().fromJson(context.body(), clazz);

        if (bodyObject == null) {
            throw new RuntimeException("missing required body");
        }

        return bodyObject;
    }
}
