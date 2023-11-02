package server;

import com.google.gson.Gson;
import model.ModelSerializer;
import model.Pet;
import service.PetService;
import spark.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PetServer {
    private PetService service;

    public PetServer() {
        service = new PetService();
    }

    public PetServer run(int port) {
        Spark.port(port);

        Spark.externalStaticFileLocation("public");

        Spark.post("/pet", this::addPet);
        Spark.get("/pet", this::listPets);
        Spark.delete("/pet/:id", this::deletePet);
        Spark.delete("/pet", this::deleteAllPets);
        Spark.exception(Exception.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return this;
    }

    public int port() {
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }

    private void exceptionHandler(Exception ex, Request req, Response res) {
        res.status(500);
    }

    private Object addPet(Request req, Response res) {
        var pet = ModelSerializer.deserialize(req.body(), Pet.class);
        pet = service.addPet(pet);
        return new Gson().toJson(pet);
    }

    private Object listPets(Request req, Response res) {
        res.type("application/json");
        var list = service.listPets().toArray();
        return new Gson().toJson(Map.of("pet", list));
    }


    private Object deletePet(Request req, Response res) {
        var id = Integer.parseInt(req.params(":id"));
        service.deletePet(id);
        res.status(204);
        return "";
    }

    private Object deleteAllPets(Request req, Response res) {
        service.deleteAllPets();
        res.status(204);
        return "";
    }
}
