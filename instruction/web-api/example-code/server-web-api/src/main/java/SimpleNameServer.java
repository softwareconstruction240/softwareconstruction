import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.Map;

public class SimpleNameServer {
    private ArrayList<String> names = new ArrayList<>();

    public static void main(String[] args) {
        new SimpleNameServer().run();
    }

    private void run() {
        Javalin.create(config -> config.staticFiles.add("web"))
                .post("/name/{name}", this::addName)
                .get("/name", this::listNames)
                .delete("/name/{name}", this::deleteName)
                .start(8080);
    }

    private void addName(Context context) {
        names.add(context.pathParam("name"));
        listNames(context);
    }

    private void listNames(Context context) {
        String jsonNames = new Gson().toJson(Map.of("name", names));
        context.json(jsonNames);
    }

    private void deleteName(Context context) {
        names.remove(context.pathParam("name"));
        listNames(context);
    }
}
