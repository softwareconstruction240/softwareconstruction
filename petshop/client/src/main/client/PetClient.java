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
        System.out.println("Welcome to the pet store. Press enter to get help. \uD83D\uDC36");
        Scanner scanner = new Scanner(System.in);

        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_YELLOW + result);
            } catch (Throwable e) {
                System.out.println(e.getMessage());
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
                    case "quit" -> "quit";
                    default -> help();
                };
            }
        } catch (ResponseException ex) {
            result = ex.getMessage();
        }
        return result;
    }

    private String listPets() throws ResponseException {
        var pets = server.listPets();
        return new Gson().toJson(pets);
    }

    private String deletePet(String[] tokens) throws ResponseException {
        try {
            if (tokens.length == 2) {
                var id = Integer.parseInt(tokens[1]);
                server.deletePet(id);
                return String.format("Deleted %d", id);
            }
        } catch (Exception ignore) {
        }
        throw new ResponseException(400, "Expected: <pet id>");
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
                return pet.toString();
            }
        } catch (Exception ignore) {
        }
        throw new ResponseException(400, "Expected: <name> <CAT|DOG|FROG> [<friend name>]*");
    }

    private String help() {
        return """
                list
                delete <pet id>
                add <name> <CAT|DOG|FROG|FISH> [<friend name>]*
                """;
    }


    private void printPrompt() {
        System.out.print(RESET_TEXT_COLOR + "\n >>> " + SET_TEXT_COLOR_GREEN);
    }

}
