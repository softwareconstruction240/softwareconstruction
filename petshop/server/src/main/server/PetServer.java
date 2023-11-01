package server;

import com.google.gson.Gson;
import spark.*;

import java.util.ArrayList;
import java.util.Map;

public class PetServer {
    final private ArrayList<String> names = new ArrayList<>();

    public PetServer run(int port) {
        Spark.port(port);

        Spark.externalStaticFileLocation("public");

        Spark.post("/pet/:name", this::addPet);
        Spark.get("/pet", this::listPets);
        Spark.delete("/pet/:name", this::deletePet);

        return this;
    }

    public int port() {
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }

    private Object addPet(Request req, Response res) {
        names.add(req.params(":name"));
        return listPets(req, res);
    }

    private Object listPets(Request req, Response res) {
        res.type("application/json");
        return new Gson().toJson(Map.of("pet", names));
    }


    private Object deletePet(Request req, Response res) {
        names.remove(req.params(":name"));
        return listPets(req, res);
    }
}
