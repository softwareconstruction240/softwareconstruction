package passoffTests.serverTests;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestException;
import passoffTests.testClasses.TestModels;
import server.Server;


public class PersistenceTests {

    private static TestServerFacade serverFacade;
    private static Server server;


    @BeforeAll
    public static void init() {
        startServer();
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    public static void startServer() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        serverFacade = new TestServerFacade("localhost", Integer.toString(port));
    }


    @Test
    @DisplayName("Persistence Test")
    public void persistenceTest() throws TestException {
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = "ExistingUser";
        registerRequest.password = "existingUserPassword";
        registerRequest.email = "eu@mail.com";

        TestModels.TestLoginRegisterResult regResult = serverFacade.register(registerRequest);
        var auth = regResult.authToken;

        //create a game
        TestModels.TestCreateRequest createRequest = new TestModels.TestCreateRequest();
        createRequest.gameName = "Test Game";
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, auth);

        //join the game
        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = ChessGame.TeamColor.WHITE;
        serverFacade.verifyJoinPlayer(joinRequest, auth);

        // Restart the server to make sure it actually is persisted.
        stopServer();
        startServer();

        //list games using the auth
        TestModels.TestListResult listResult = serverFacade.listGames(auth);
        Assertions.assertEquals(200, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertEquals(1, listResult.games.length, "Missing game(s) in database after restart");

        TestModels.TestListResult.TestListEntry game1 = listResult.games[0];
        Assertions.assertEquals(game1.gameID, createResult.gameID);
        Assertions.assertEquals(createRequest.gameName, game1.gameName, "Game name changed after restart");
        Assertions.assertEquals(registerRequest.username, game1.whiteUsername,
                "White player username changed after restart");

        //test that we can still log in
        TestModels.TestLoginRequest loginRequest = new TestModels.TestLoginRequest();
        loginRequest.username = registerRequest.username;
        loginRequest.password = registerRequest.password;
        serverFacade.login(loginRequest);
        Assertions.assertEquals(200, serverFacade.getStatusCode(), "Unable to login");
    }

}
