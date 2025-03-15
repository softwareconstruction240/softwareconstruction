package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.Pet;
import io.javalin.Javalin;
import io.javalin.http.Context;
import server.websocket.WebSocketHandler;
import service.PetService;

import java.util.Map;

public class PetServer {
    private final PetService service;
    private final WebSocketHandler webSocketHandler;
    private final Javalin javalin;

    public PetServer(PetService service) {
        this.service = service;
        this.webSocketHandler = new WebSocketHandler();
        this.javalin = initializeJavalin();
    }

    private Javalin initializeJavalin() {
        return Javalin.create(config -> config.staticFiles.add("public"))
                .post("/pet", this::addPet)
                .get("/pet", this::listPets)
                .delete("/pet/{id}", this::deletePet)
                .delete("/pet", this::deleteAllPets)
                .exception(ResponseException.class, this::exceptionHandler)
                .ws("/ws", ws -> {
                    ws.onConnect(webSocketHandler);
                    ws.onMessage(webSocketHandler);
                    ws.onClose(webSocketHandler);
                });
    }

    public PetServer run(int port) {
        javalin.start(port);
        return this;
    }

    public int port() {
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void exceptionHandler(ResponseException ex, Context ctx) {
        ctx.status(ex.StatusCode());
        ctx.json(ex.toJson());
    }

    private void addPet(Context ctx) throws ResponseException {
        var pet = new Gson().fromJson(ctx.body(), Pet.class);
        pet = service.addPet(pet);
        webSocketHandler.makeNoise(pet.name(), pet.sound());
        ctx.json(new Gson().toJson(pet));
    }

    private void listPets(Context ctx) throws ResponseException {
        var list = service.listPets().toArray();
        ctx.json(new Gson().toJson(Map.of("pet", list)));
    }

    private void deletePet(Context ctx) throws ResponseException {
        var id = Integer.parseInt(ctx.pathParam("id"));
        var pet = service.getPet(id);
        if (pet != null) {
            service.deletePet(id);
            webSocketHandler.makeNoise(pet.name(), pet.sound());
            ctx.status(204);
        } else {
            ctx.status(404);
        }
    }

    private void deleteAllPets(Context ctx) throws ResponseException {
        service.deleteAllPets();
        ctx.status(204);
    }
}
