package passoffTests.serverTests;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestModels;

import java.util.Objects;
import java.util.Scanner;


public class PersistenceTest {

    private static TestModels.TestUser existingUser;
    private static TestModels.TestUser newUser;
    private static TestServerFacade serverFacade;
    private String existingAuth;

    @BeforeAll
    public static void init() {
        existingUser = new TestModels.TestUser();
        existingUser.username = "Joseph";
        existingUser.password = "Smith";
        existingUser.email = "urim@thummim.net";

        newUser = new TestModels.TestUser();
        newUser.username = "testUsername";
        newUser.password = "testPassword";
        newUser.email = "testEmail";

        serverFacade = new TestServerFacade("localhost", passoffTests.TestFactory.getServerPort());
    }


    @BeforeEach
    public void setup() {
        serverFacade.clear();

        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = existingUser.username;
        registerRequest.password = existingUser.password;
        registerRequest.email = existingUser.email;

        //one user already logged in
        TestModels.TestLoginRegisterResult regResult = serverFacade.register(registerRequest);
        existingAuth = regResult.authToken;
    }


    private TestModels.TestCreateRequest validCreateGameRequest() {
        var result = new TestModels.TestCreateRequest();
        result.gameName = "Test Game";
        return result;
    }


    @Test
    @DisplayName("Persistence Test")
    public void persistenceTest() {
        //insert a bunch of data
        //-------------------------------------------------------------------------------------------------------------
        //register 2nd user
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = newUser.username;
        registerRequest.password = newUser.password;
        registerRequest.email = newUser.email;

        TestModels.TestLoginRegisterResult regResult = serverFacade.register(registerRequest);
        String newAuth = regResult.authToken;

        //create 2 games
        TestModels.TestCreateRequest createRequest = validCreateGameRequest();

        //first one has both players in it
        createRequest.gameName = "test1";
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //have first user join
        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = ChessGame.TeamColor.WHITE;
        serverFacade.verifyJoinPlayer(joinRequest, existingAuth);

        //have second user join
        joinRequest.playerColor = ChessGame.TeamColor.BLACK;
        serverFacade.verifyJoinPlayer(joinRequest, newAuth);

        //second empty game
        createRequest.gameName = "test2";
        TestModels.TestCreateResult createResult2 = serverFacade.createGame(createRequest, newAuth);
        //-------------------------------------------------------------------------------------------------------------


        //Will wait on the statement scanner.nextLine() till you push enter in the terminal window.
        //You may need to follow the steps under the heading "Setting up for the Persistence Test" in the "How To Get Started"
        Scanner scanner = new Scanner(System.in);
        System.out.println("Shut down the server, wait a few seconds, then restart the server. Then press ENTER.");
        scanner.nextLine();


        //Verify Data still there
        //-------------------------------------------------------------------------------------------------------------
        //list games, see both user in game
        //also checks that first user still has auth in database
        TestModels.TestListResult listResult = serverFacade.listGames(existingAuth);

        Assertions.assertTrue(listResult.success, "User auth not found in database after restart");
        Assertions.assertEquals(2, listResult.games.length, "Missing game(s) in database after restart");

        //set games & check if swapped
        TestModels.TestListResult.TestListEntry game1 = listResult.games[0];
        TestModels.TestListResult.TestListEntry game2 = listResult.games[1];
        if (Objects.equals(game1.gameID, createResult2.gameID)) { //swap games if needed
            TestModels.TestListResult.TestListEntry tempGame = game1;
            game1 = game2;
            game2 = tempGame;
        }


        //check that both tests are there
        Assertions.assertEquals("test1", game1.gameName, "Game name changed after restart");
        Assertions.assertEquals(createResult.gameID, game1.gameID, "Game ID Changed after restart");
        Assertions.assertEquals("test2", game2.gameName, "Game name changed after restart");
        Assertions.assertEquals(createResult2.gameID, game2.gameID, "Game ID changed after restart");

        //check players in test1 game
        Assertions.assertEquals(existingUser.username, game1.whiteUsername,
                "White player username changed after restart");
        Assertions.assertEquals(newUser.username, game1.blackUsername, "Black player username changed after restart");

        //make sure second user can log in
        TestModels.TestLoginRequest loginRequest = new TestModels.TestLoginRequest();
        loginRequest.username = newUser.username;
        loginRequest.password = newUser.password;
        TestModels.TestLoginRegisterResult loginResult = serverFacade.login(loginRequest);

        Assertions.assertTrue(loginResult.success, "Second user not able to log in after restart");
        //-------------------------------------------------------------------------------------------------------------
    }

}
