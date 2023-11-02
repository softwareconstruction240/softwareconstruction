package client;

import com.google.gson.Gson;
import model.ArrayFriendList;
import model.Pet;
import model.PetType;
import util.ResponseException;
import util.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

import static client.EscapeSequences.*;

public class Repl {
    private final PetClient client;

    public Repl(String serverUrl) {
        client = new PetClient(serverUrl);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to the pet store. Press enter to get help.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(BLUE + result);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }

}
