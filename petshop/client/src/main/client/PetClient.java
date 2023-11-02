package client;

import java.util.Arrays;
import java.util.Scanner;

import com.google.gson.Gson;
import model.ArrayFriendList;
import model.Pet;
import model.PetType;
import util.ResponseException;
import util.ServerFacade;

import static client.EscapeSequences.*;

public class PetClient {
    private final ServerFacade server;

    public PetClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to the pet store. Press enter to get help.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(BLUE + result);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }

    private String eval(String input) {
        var result = "Invalid input";
        try {
            var tokens = input.toLowerCase().split(" ");
            if (tokens.length > 0) {
                var cmd = tokens[0];
                result = switch (cmd) {
                    case "add" -> addPet(tokens);
                    case "list" -> listPets();
                    case "delete" -> deletePet(tokens);
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

    private String addPet(String[] tokens) throws ResponseException {
        try {
            if (tokens.length >= 3) {
                var name = tokens[1];
                var type = PetType.valueOf(tokens[2].toUpperCase());
                var friendArray = Arrays.copyOfRange(tokens, 3, tokens.length);
                var friends = new ArrayFriendList(friendArray);
                var pet = new Pet(0, name, type, friends);
                pet = server.addPet(pet);
                return pet.toString() + "\n";
            }
        } catch (Exception ignore) {
        }
        throw new ResponseException(400, "Expected: <name> <CAT|DOG|FROG> [<friend name>]*");
    }

    private String listPets() throws ResponseException {
        var pets = server.listPets();
        var result = new StringBuilder();
        var gson = new Gson();
        for (var pet : pets) {
            result.append(gson.toJson(pet)).append('\n');
        }
        return result.toString();
    }

    private String deletePet(String[] tokens) throws ResponseException {
        try {
            if (tokens.length == 2) {
                var id = Integer.parseInt(tokens[1]);
                server.deletePet(id);
                return String.format("Deleted %d\n", id);
            }
        } catch (Exception ignore) {
        }
        throw new ResponseException(400, "Expected: <pet id>");
    }

    private String deleteAllPets() throws ResponseException {
        server.deleteAllPets();
        return "Deleted all\n";
    }

    private String help() {
        return """
                - list
                - delete <pet id>
                - add <name> <CAT|DOG|FROG|FISH> [<friend name>]*
                - clear
                - quit
                """;
    }


    private void printPrompt() {
        System.out.print(RESET + ">>> " + GREEN);
    }

}
