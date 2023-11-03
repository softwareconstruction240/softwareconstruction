package client;

import java.util.Arrays;

import com.google.gson.Gson;
import model.ArrayFriendList;
import model.Pet;
import model.PetType;
import util.ResponseException;
import util.ServerFacade;

public class PetClient {
    private final ServerFacade server;

    public PetClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public String eval(String input) {
        var result = "Invalid input";
        try {
            var tokens = input.toLowerCase().split(" ");
            if (tokens.length > 0) {
                var cmd = tokens[0];
                var params = Arrays.copyOfRange(tokens, 1, tokens.length);
                result = switch (cmd) {
                    case "add" -> addPet(params);
                    case "list" -> listPets();
                    case "delete" -> deletePet(params);
                    case "clear" -> deleteAllPets();
                    case "quit" -> "quit";
                    default -> help();
                };
            }
        } catch (ResponseException ex) {
            result = ex.getMessage();
        }
        return result;
    }

    public String addPet(String... params) throws ResponseException {
        try {
            if (params.length >= 2) {
                var name = params[0];
                var type = PetType.valueOf(params[1].toUpperCase());
                var friendArray = Arrays.copyOfRange(params, 2, params.length);
                var friends = new ArrayFriendList(friendArray);
                var pet = new Pet(0, name, type, friends);
                pet = server.addPet(pet);
                return pet.toString();
            }
        } catch (Exception ignore) {
        }
        throw new ResponseException(400, "Expected: <name> <CAT|DOG|FROG> [<friend name>]*");
    }

    public String listPets() throws ResponseException {
        var pets = server.listPets();
        var result = new StringBuilder();
        var gson = new Gson();
        for (var pet : pets) {
            result.append(gson.toJson(pet)).append('\n');
        }
        return result.toString();
    }

    public String deletePet(String... params) throws ResponseException {
        try {
            if (params.length == 1) {
                var id = Integer.parseInt(params[0]);
                server.deletePet(id);
                return String.format("Deleted %d", id);
            }
        } catch (Exception ignore) {
        }
        throw new ResponseException(400, "Expected: <pet id>");
    }

    public String deleteAllPets() throws ResponseException {
        server.deleteAllPets();
        return "Deleted all";
    }

    public String help() {
        return """
                - list
                - delete <pet id>
                - add <name> <CAT|DOG|FROG|FISH> [<friend name>]*
                - clear
                - quit
                """;
    }
}
