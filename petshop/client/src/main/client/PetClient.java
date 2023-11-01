package client;

import java.util.Scanner;

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
            String input = scanner.nextLine();

            try {
                result = eval(input);
                System.out.print(RESET_TEXT_COLOR + result);
            } catch (Throwable e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String eval(String input) {
        var result = "Invalid input";
        try {
            var tokens = input.toLowerCase().split(" ");
            if (tokens.length > 0) {
                var cmd = tokens[0];
                result = switch (cmd) {
                    case "add" -> server.addPet(tokens[1]);
                    case "list" -> server.listPets();
                    case "delete" -> server.deletePet(tokens[1]);
                    default -> "Unknown command";
                };
            }
        } catch (ResponseException ex) {
            result = ex.getMessage();
        }
        return result;
    }


    private void printPrompt() {
        System.out.print(RESET_TEXT_COLOR + "\n >>> " + SET_TEXT_COLOR_GREEN);
    }

}
