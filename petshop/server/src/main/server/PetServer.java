package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import exception.ResponseException;
import model.ModelSerializer;
import model.Pet;
import service.PetService;
import spark.*;

import java.util.Map;

public class PetServer {
    private final PetService service;

    public PetServer(DataAccess dataAccess) {
        service = new PetService(dataAccess);
    }

    public PetServer run(int port) {
        Spark.port(port);

        Spark.externalStaticFileLocation("public");

        Spark.post("/pet", this::addPet);
        Spark.get("/pet", this::listPets);
        Spark.delete("/pet/:id", this::deletePet);
        Spark.delete("/pet", this::deleteAllPets);
        Spark.exception(ResponseException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return this;
    }

    public int port() {
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
    }

    private Object addPet(Request req, Response res) throws ResponseException {
        var pet = ModelSerializer.deserialize(req.body(), Pet.class);
        pet = service.addPet(pet);
        return new Gson().toJson(pet);
    }

    private Object listPets(Request req, Response res) throws ResponseException {
        res.type("application/json");
        var list = service.listPets().toArray();
        return new Gson().toJson(Map.of("pet", list));
    }


    private Object deletePet(Request req, Response res) throws ResponseException {
        var id = Integer.parseInt(req.params(":id"));
        service.deletePet(id);
        res.status(204);
        return "";
    }

    private Object deleteAllPets(Request req, Response res) throws ResponseException {
        service.deleteAllPets();
        res.status(204);
        return "";
    }
}
