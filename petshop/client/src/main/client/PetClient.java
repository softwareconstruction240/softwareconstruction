package client;

import java.util.Arrays;

import com.google.gson.Gson;
import model.ArrayFriendList;
import model.Pet;
import model.PetType;
import exception.ResponseException;
import client.websocket.NotificationHandler;
import server.ServerFacade;
import client.websocket.WebSocketFacade;

public class PetClient {
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private State state = State.UNKNOWN;

    public PetClient(String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    public String eval(String input) {
        var result = "Invalid input";
        try {
            var tokens = input.toLowerCase().split(" ");
            if (tokens.length > 0) {
                var cmd = tokens[0];
                var params = Arrays.copyOfRange(tokens, 1, tokens.length);
                result = switch (cmd) {
                    case "signin" -> signin(params);
                    case "rescue" -> rescuePet(params);
                    case "list" -> listPets();
                    case "enter" -> enterPetShop();
                    case "leave" -> leavePetShop();
                    case "adopt" -> adoptPet(params);
                    case "adoptall" -> adoptAllPets();
                    case "quit" -> "quit";
                    default -> help();
                };
            }
        } catch (ResponseException ex) {
            result = ex.getMessage();
        }
        return result;
    }

    public String signin(String... params) throws ResponseException {
        if (state != State.UNKNOWN) throw new ResponseException(400, "Already signed in");
        if (params.length >= 1) {
            state = State.OUTSIDE;
            visitorName = String.join("-", params);
            return String.format("You signed in as %s.", visitorName);
        }
        throw new ResponseException(400, "Expected: <yourname>");
    }

    public String rescuePet(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 2) {
            var name = params[0];
            var type = PetType.valueOf(params[1].toUpperCase());
            var friendArray = Arrays.copyOfRange(params, 2, params.length);
            var friends = new ArrayFriendList(friendArray);
            var pet = new Pet(0, name, type, friends);
            pet = server.addPet(pet);
            return String.format("You rescued %s. Assigned ID: %d", pet.name(), pet.id());
        }
        throw new ResponseException(400, "Expected: <name> <CAT|DOG|FROG> [<friend name>]*");
    }

    public String listPets() throws ResponseException {
        assertSignedIn();
        var pets = server.listPets();
        var result = new StringBuilder();
        var gson = new Gson();
        for (var pet : pets) {
            result.append(gson.toJson(pet)).append('\n');
        }
        return result.toString();
    }

    public String adoptPet(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            var id = Integer.parseInt(params[0]);
            var pet = getPet(id);
            if (pet != null) {
                server.deletePet(id);
                return String.format("%s says %s", pet.name(), pet.sound());
            }
        }
        throw new ResponseException(400, "Expected: <pet id>");
    }

    public String adoptAllPets() throws ResponseException {
        assertSignedIn();
        var buffer = new StringBuilder();
        for (var pet : server.listPets()) {
            buffer.append(String.format("%s says %s%n", pet.name(), pet.sound()));
        }

        server.deleteAllPets();
        return buffer.toString();
    }

    public String enterPetShop() throws ResponseException {
        assertSignedIn();
        if (state == State.OUTSIDE) {
            ws = new WebSocketFacade(serverUrl, notificationHandler);

            state = State.INSIDE;
            ws.enterPetShop(visitorName);
            return String.format("%s entered the shop", visitorName);
        }
        return "already in shop";
    }

    public String leavePetShop() throws ResponseException {
        assertSignedIn();
        if (state == State.INSIDE) {
            ws.leavePetShop(visitorName);
            ws = null;
            state = State.OUTSIDE;
            return String.format("%s left the shop", visitorName);
        }
        return "not in shop";
    }

    private Pet getPet(int id) throws ResponseException {
        for (var pet : server.listPets()) {
            if (pet.id() == id) {
                return pet;
            }
        }
        return null;
    }

    public String help() {
        if (state == State.UNKNOWN) {
            return """
                    - signin <yourname>
                    - quit
                    """;
        }
        return """
                - list
                - adopt <pet id>
                - rescue <name> <CAT|DOG|FROG|FISH> [<friend name>]*
                - enter
                - leave
                - adoptall
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.UNKNOWN) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
