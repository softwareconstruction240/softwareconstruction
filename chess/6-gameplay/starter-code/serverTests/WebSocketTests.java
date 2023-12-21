package passoffTests.serverTests;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import org.junit.jupiter.api.*;
import passoffTests.TestFactory;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestException;
import passoffTests.testClasses.TestModels;
import passoffTests.testClasses.WebsocketTestingEnvironment;
import server.Server;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WebSocketTests {

    private static WebsocketTestingEnvironment environment;
    private static TestServerFacade serverFacade;
    private static Server server;
    private static Long waitTime;
    private TestUser white;
    private TestUser black;
    private TestUser observer;
    private Integer gameID;


    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @BeforeAll
    public static void init() throws URISyntaxException {
        server = new Server();
        var port = Integer.toString(server.run(0));
        System.out.println("Started test HTTP server on " + port);

        serverFacade = new TestServerFacade("localhost", port);
        serverFacade.clear();

        environment = new WebsocketTestingEnvironment("localhost", port, "/connect");

        waitTime = TestFactory.getMessageTime();
    }


    @BeforeEach
    public void setup() throws TestException {
        //populate database
        serverFacade.clear();

        white = registerUser("white", "WHITE", "white@chess.com");
        black = registerUser("black", "BLACK", "black@chess.com");
        observer = registerUser("observer", "OBSERVER", "observer@chess.com");

        gameID = createGame(white, "testGame");

        joinGame(gameID, white, ChessGame.TeamColor.WHITE);
        joinGame(gameID, black, ChessGame.TeamColor.BLACK);
        joinGame(gameID, observer, null);
    }

    @AfterEach
    public void tearDown() {
        environment.disconnectAll();
    }


    @Test
    @Order(1)
    @DisplayName("Normal Join Player")
    public void joinPlayerGood() {
        //try join valid reserved spot
        Map<String, List<TestModels.TestMessage>> messages =
                joinPlayer(white.user, white.authToken, gameID, ChessGame.TeamColor.WHITE, Set.of(), Set.of());

        //check received message
        assertLoadGameMessage(messages.get(white.user));


        //join other spot on game
        messages = joinPlayer(black.user, black.authToken, gameID, ChessGame.TeamColor.BLACK,
                Set.of(white.user), Set.of());

        //check received messages
        assertLoadGameMessage(messages.get(black.user));
        assertNotificationMessage(messages.get(white.user));
    }


    @Test
    @Order(2)
    @DisplayName("Join Player Wrong Team")
    public void joinPlayerSteal() {
        //try join someone else's reserved spot
        Map<String, List<TestModels.TestMessage>> messages =
                joinPlayer(white.user, white.authToken, gameID, ChessGame.TeamColor.BLACK, Set.of(), Set.of());

        //check received message
        assertErrorMessage(messages.get(white.user));
    }


    @Test
    @Order(3)
    @DisplayName("Join Player Empty Team")
    public void joinPlayerEmpty() {
        //This test sends a JOIN_PLAYER message for a game without calling Http join endpoint first
        //create empty game
        TestModels.TestCreateRequest createRequest = new TestModels.TestCreateRequest();
        createRequest.gameName = "testGameEmpty";
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, white.authToken);

        //join empty game
        Map<String, List<TestModels.TestMessage>> messages =
                joinPlayer(white.user, white.authToken, createResult.gameID, ChessGame.TeamColor.WHITE,
                        Set.of(), Set.of());

        assertErrorMessage(messages.get(white.user));
    }


    @Test
    @Order(4)
    @DisplayName("Join Player Bad GameID")
    public void joinPlayerBadGameID() {
        Map<String, List<TestModels.TestMessage>> messages =
                joinPlayer(white.user, white.authToken, gameID + 1, ChessGame.TeamColor.WHITE, Set.of(),
                        Set.of());

        assertErrorMessage(messages.get(white.user));
    }


    @Test
    @Order(5)
    @DisplayName("Join Player Bad AuthToken")
    public void joinPlayerBadAuthToken() {
        Map<String, List<TestModels.TestMessage>> messages =
                joinPlayer(white.user, "badAuth", gameID, ChessGame.TeamColor.WHITE, Set.of(), Set.of());

        assertErrorMessage(messages.get(white.user));
    }


    @Test
    @Order(6)
    @DisplayName("Normal Join Observer")
    public void joinObserverGood() {
        //have white player watch their own game
        Map<String, List<TestModels.TestMessage>> messages =
                joinObserver(white.user, white.authToken, gameID, Set.of(), Set.of());

        //should get a load game message
        assertLoadGameMessage(messages.get(white.user));

        //have player join
        messages = joinPlayer(black.user, black.authToken, gameID, ChessGame.TeamColor.BLACK,
                Set.of(white.user), Set.of());

        //observer should get a notification
        assertNotificationMessage(messages.get(white.user));

        //watch game
        messages =
                joinObserver(observer.user, observer.authToken, gameID, Set.of(white.user, black.user),
                        Set.of());

        //check messages
        assertLoadGameMessage(messages.get(observer.user));
        assertNotificationMessage(messages.get(white.user));
        assertNotificationMessage(messages.get(black.user));
    }


    @Test
    @Order(7)
    @DisplayName("Join Observer Bad GameID")
    public void joinObserverBadGameID() {
        Map<String, List<TestModels.TestMessage>> messages =
                joinObserver(observer.user, observer.authToken, gameID + 1, Set.of(), Set.of());

        assertErrorMessage(messages.get(observer.user));
    }


    @Test
    @Order(8)
    @DisplayName("Join Observer Bad AuthToken")
    public void joinObserverBadAuthToken() {
        Map<String, List<TestModels.TestMessage>> messages =
                joinObserver(observer.user, "badAuth", gameID, Set.of(), Set.of());

        assertErrorMessage(messages.get(observer.user));
    }


    @Test
    @Order(9)
    @DisplayName("Normal Make Move")
    public void validMove() {
        setupNormalGame();

        //create pawn move
        ChessPosition startingPosition = TestFactory.getNewPosition(2, 5);
        ChessPosition endingPosition = TestFactory.getNewPosition(3, 5);
        ChessMove move = TestFactory.getNewMove(startingPosition, endingPosition, null);

        //send command
        Map<String, List<TestModels.TestMessage>> messages =
                makeMove(white.user, white.authToken, gameID, move,
                        Set.of(black.user, observer.user), Set.of());

        assertLoadGameMessage(messages.get(white.user));
        assertMoveMadePair(messages.get(black.user));
        assertMoveMadePair(messages.get(observer.user));
    }


    @Test
    @Order(10)
    @DisplayName("Make Invalid Move")
    public void invalidMoveBadMove() {
        setupNormalGame();

        //try to move rook - invalid move
        ChessPosition startingPosition = TestFactory.getNewPosition(1, 1);
        ChessPosition endingPosition = TestFactory.getNewPosition(1, 5);
        ChessMove move = TestFactory.getNewMove(startingPosition, endingPosition, null);

        //send command
        Map<String, List<TestModels.TestMessage>> messages =
                makeMove(white.user, white.authToken, gameID, move, Set.of(),
                        Set.of(black.user, observer.user));

        assertErrorMessage(messages.get(white.user));
        Assertions.assertTrue(messages.get(black.user).isEmpty(),
                "Player got a message after the other player sent an invalid command");
        Assertions.assertTrue(messages.get(observer.user).isEmpty(),
                "Observer got a message after player sent an invalid command");
    }


    @Test
    @Order(11)
    @DisplayName("Make Move Wrong Turn")
    public void invalidMoveWrongTurn() {
        setupNormalGame();

        //try to move pawn out of turn
        ChessPosition startingPosition = TestFactory.getNewPosition(7, 5);
        ChessPosition endingPosition = TestFactory.getNewPosition(5, 5);
        ChessMove move = TestFactory.getNewMove(startingPosition, endingPosition, null);

        Map<String, List<TestModels.TestMessage>> messages =
                makeMove(black.user, black.authToken, gameID, move, Set.of(),
                        Set.of(white.user, observer.user));

        assertErrorMessage(messages.get(black.user));
        Assertions.assertTrue(messages.get(white.user).isEmpty(),
                "Player got a message after the other player sent an invalid command");
        Assertions.assertTrue(messages.get(observer.user).isEmpty(),
                "Observer got a message after player sent an invalid command");
    }


    @Test
    @Order(12)
    @DisplayName("Make Move for Opponent")
    public void invalidMoveOpponent() {
        setupNormalGame();

        //try to move pawn of other player
        ChessPosition startingPosition = TestFactory.getNewPosition(2, 5);
        ChessPosition endingPosition = TestFactory.getNewPosition(4, 5);
        ChessMove move = TestFactory.getNewMove(startingPosition, endingPosition, null);

        Map<String, List<TestModels.TestMessage>> messages =
                makeMove(black.user, black.authToken, gameID, move, Set.of(),
                        Set.of(white.user, observer.user));

        assertErrorMessage(messages.get(black.user));
        Assertions.assertTrue(messages.get(white.user).isEmpty(),
                "Player got a message after the other player sent an invalid command");
        Assertions.assertTrue(messages.get(observer.user).isEmpty(),
                "Observer got a message after player sent an invalid command");
    }


    @Test
    @Order(13)
    @DisplayName("Make Move Observer")
    public void invalidMoveObserver() {
        setupNormalGame();

        //have observer attempt to make a move
        ChessPosition startingPosition = TestFactory.getNewPosition(2, 5);
        ChessPosition endingPosition = TestFactory.getNewPosition(4, 5);
        ChessMove move = TestFactory.getNewMove(startingPosition, endingPosition, null);

        Map<String, List<TestModels.TestMessage>> messages =
                makeMove(observer.user, observer.authToken, gameID, move, Set.of(),
                        Set.of(white.user, black.user));

        assertErrorMessage(messages.get(observer.user));
        Assertions.assertTrue(messages.get(white.user).isEmpty(),
                "Player got a message after observer sent an invalid command");
        Assertions.assertTrue(messages.get(black.user).isEmpty(),
                "Player got a message after observer sent an invalid command");
    }


    @Test
    @Order(14)
    @DisplayName("Make Move Game Over")
    public void invalidMoveGameOver() {
        setupNormalGame();

        ChessPosition startingPosition = TestFactory.getNewPosition(2, 7);
        ChessPosition endingPosition = TestFactory.getNewPosition(4, 7);
        ChessMove move = TestFactory.getNewMove(startingPosition, endingPosition, null);
        makeMove(white.user, white.authToken, gameID, move, Set.of(black.user, observer.user),
                Set.of());

        startingPosition = TestFactory.getNewPosition(7, 5);
        endingPosition = TestFactory.getNewPosition(6, 5);
        move = TestFactory.getNewMove(startingPosition, endingPosition, null);
        makeMove(black.user, black.authToken, gameID, move, Set.of(white.user, observer.user),
                Set.of());

        startingPosition = TestFactory.getNewPosition(2, 6);
        endingPosition = TestFactory.getNewPosition(3, 6);
        move = TestFactory.getNewMove(startingPosition, endingPosition, null);
        makeMove(white.user, white.authToken, gameID, move, Set.of(black.user, observer.user),
                Set.of());

        startingPosition = TestFactory.getNewPosition(8, 4);
        endingPosition = TestFactory.getNewPosition(4, 8);
        move = TestFactory.getNewMove(startingPosition, endingPosition, null);
        makeMove(black.user, black.authToken, gameID, move, Set.of(white.user, observer.user),
                Set.of());
        //checkmate

        //attempt another move
        startingPosition = TestFactory.getNewPosition(2, 5);
        endingPosition = TestFactory.getNewPosition(4, 5);
        move = TestFactory.getNewMove(startingPosition, endingPosition, null);
        Map<String, List<TestModels.TestMessage>> messages =
                makeMove(white.user, white.authToken, gameID, move, Set.of(),
                        Set.of(black.user, observer.user));


        assertErrorMessage(messages.get(white.user));
        Assertions.assertTrue(messages.get(black.user).isEmpty(),
                "Player got a message after the other player sent an invalid command");
        Assertions.assertTrue(messages.get(observer.user).isEmpty(),
                "Observer got a message after player sent an invalid command");
    }


    @Test
    @Order(15)
    @DisplayName("Normal Resign")
    public void validResign() {
        setupNormalGame();

        Map<String, List<TestModels.TestMessage>> messages =
                resign(white.user, white.authToken, gameID, Set.of(black.user, observer.user),
                        Set.of());

        assertNotificationMessage(messages.get(white.user));
        assertNotificationMessage(messages.get(black.user));
        assertNotificationMessage(messages.get(observer.user));

    }


    @Test
    @Order(16)
    @DisplayName("Move After Resign")
    public void moveAfterResign() {
        setupNormalGame();

        resign(black.user, black.authToken, gameID, Set.of(white.user, observer.user), Set.of());

        //attempt to make a move after other player resigns
        ChessPosition startingPosition = TestFactory.getNewPosition(2, 5);
        ChessPosition endingPosition = TestFactory.getNewPosition(4, 5);
        ChessMove move = TestFactory.getNewMove(startingPosition, endingPosition, null);
        Map<String, List<TestModels.TestMessage>> messages =
                makeMove(white.user, white.authToken, gameID, move, Set.of(),
                        Set.of(black.user, observer.user));


        assertErrorMessage(messages.get(white.user));
        Assertions.assertTrue(messages.get(black.user).isEmpty(),
                "Player got a message after the other player sent an invalid command");
        Assertions.assertTrue(messages.get(observer.user).isEmpty(),
                "Observer got a message after player sent an invalid command");
    }


    @Test
    @Order(17)
    @DisplayName("Observer Resign")
    public void invalidResignObserver() {
        setupNormalGame();

        //have observer try to resign
        Map<String, List<TestModels.TestMessage>> messages = resign(observer.user, observer.authToken, gameID, Set.of(),
                Set.of(white.user, black.user));

        assertErrorMessage(messages.get(observer.user));
        Assertions.assertTrue(messages.get(white.user).isEmpty(),
                "Player got a message after observer sent an invalid command");
        Assertions.assertTrue(messages.get(black.user).isEmpty(),
                "Player got a message after observer sent an invalid command");
    }


    @Test
    @Order(18)
    @DisplayName("Double Resign")
    public void invalidResignGameOver() {
        setupNormalGame();

        resign(black.user, black.authToken, gameID, Set.of(white.user, observer.user), Set.of());

        //attempt to resign after other player resigns
        Map<String, List<TestModels.TestMessage>> messages =
                resign(white.user, white.authToken, gameID, Set.of(),
                        Set.of(black.user, observer.user));

        assertErrorMessage(messages.get(white.user));
        Assertions.assertTrue(messages.get(black.user).isEmpty(),
                "Player got a message after the other player sent an invalid command");
        Assertions.assertTrue(messages.get(observer.user).isEmpty(),
                "Observer got a message after player sent an invalid command");
    }


    @Test
    @Order(19)
    @DisplayName("Leave Game")
    public void leaveGame() {
        setupNormalGame();

        //have white player leave
        //all other players get notified, white player can but doesn't have to be
        Map<String, List<TestModels.TestMessage>> messages =
                leave(white.user, white.authToken, gameID, Set.of(black.user, observer.user),
                        Set.of());

        assertNotificationMessage(messages.get(black.user));
        assertNotificationMessage(messages.get(observer.user));


        //observer leaves
        messages =
                leave(observer.user, observer.authToken, gameID, Set.of(black.user), Set.of(white.user));

        assertNotificationMessage(messages.get(black.user));
        Assertions.assertTrue(messages.get(white.user).isEmpty(), "Player got a message after leaving");
    }


    @Test
    @Order(20)
    @DisplayName("Multiple Lobbies")
    public void multipleLobbies() {
        var white2 = registerUser("white2", "WHITE", "white2@chess.com");
        var black2 = registerUser("black2", "BLACK", "black2@chess.com");
        var observer2 = registerUser("observer2", "OBSERVER", "observer2@chess.com");

        var otherGameID = createGame(white, "testGame2");

        joinGame(otherGameID, white2, ChessGame.TeamColor.WHITE);
        joinGame(otherGameID, black2, ChessGame.TeamColor.BLACK);
        joinGame(otherGameID, observer2, null);

        setupNormalGame();

        Map<String, List<TestModels.TestMessage>> messages =
                joinPlayer(white2.user, white2.authToken, otherGameID, ChessGame.TeamColor.WHITE, Set.of(),
                        Set.of(white.user, black.user, observer.user));
        assertLoadGameMessage(messages.get(white2.user));
        Assertions.assertTrue(messages.get(white.user).isEmpty(),
                "Player got a message after player from another lobby sent a command");
        Assertions.assertTrue(messages.get(black.user).isEmpty(),
                "Player got a message after player from another lobby sent a command");
        Assertions.assertTrue(messages.get(observer.user).isEmpty(),
                "Observer got a message after player from another lobby sent a command");

        messages = joinPlayer(black2.user, black2.authToken, otherGameID, ChessGame.TeamColor.BLACK, Set.of(white2.user),
                Set.of(white.user, black.user, observer.user));
        assertLoadGameMessage(messages.get(black2.user));
        assertNotificationMessage(messages.get(white2.user));
        Assertions.assertTrue(messages.get(white.user).isEmpty(),
                "Player got a message after player from another lobby sent a command");
        Assertions.assertTrue(messages.get(black.user).isEmpty(),
                "Player got a message after player from another lobby sent a command");
        Assertions.assertTrue(messages.get(observer.user).isEmpty(),
                "Observer got a message after player from another lobby sent a command");

        messages = joinObserver(observer2.user, observer2.authToken, otherGameID, Set.of(white2.user, black2.user),
                Set.of(white.user, black.user, observer.user));
        assertLoadGameMessage(messages.get(observer2.user));
        assertNotificationMessage(messages.get(white2.user));
        assertNotificationMessage(messages.get(black2.user));
        Assertions.assertTrue(messages.get(white.user).isEmpty(),
                "Player got a message after player from another lobby sent a command");
        Assertions.assertTrue(messages.get(black.user).isEmpty(),
                "Player got a message after player from another lobby sent a command");
        Assertions.assertTrue(messages.get(observer.user).isEmpty(),
                "Observer got a message after player from another lobby sent a command");

        ChessPosition startingPosition = TestFactory.getNewPosition(2, 5);
        ChessPosition endingPosition = TestFactory.getNewPosition(3, 5);
        ChessMove move = TestFactory.getNewMove(startingPosition, endingPosition, null);
        messages = makeMove(white.user, white.authToken, gameID, move,
                Set.of(black.user, observer.user), Set.of(white2.user, black2.user, observer2.user));
        assertLoadGameMessage(messages.get(white.user));
        assertMoveMadePair(messages.get(black.user));
        assertMoveMadePair(messages.get(observer.user));
        Assertions.assertTrue(messages.get(white2.user).isEmpty(),
                "Player got a message after player from another lobby sent a command");
        Assertions.assertTrue(messages.get(black2.user).isEmpty(),
                "Player got a message after player from another lobby sent a command");
        Assertions.assertTrue(messages.get(observer2.user).isEmpty(),
                "Observer got a message after player from another lobby sent a command");

        messages = resign(white2.user, white2.authToken, otherGameID, Set.of(black2.user, observer2.user),
                Set.of(white.user, black.user, observer.user));
        assertNotificationMessage(messages.get(white2.user));
        assertNotificationMessage(messages.get(black2.user));
        assertNotificationMessage(messages.get(observer2.user));
        Assertions.assertTrue(messages.get(white.user).isEmpty(),
                "Player got a message after player from another lobby sent a command");
        Assertions.assertTrue(messages.get(black.user).isEmpty(),
                "Player got a message after player from another lobby sent a command");
        Assertions.assertTrue(messages.get(observer.user).isEmpty(),
                "Observer got a message after player from another lobby sent a command");

        messages = leave(white.user, white.authToken, gameID, Set.of(black.user, observer.user),
                Set.of(white2.user, black2.user, observer2.user));
        assertNotificationMessage(messages.get(black.user));
        assertNotificationMessage(messages.get(observer.user));
        Assertions.assertTrue(messages.get(white2.user).isEmpty(),
                "Player got a message after player from another lobby sent a command");
        Assertions.assertTrue(messages.get(black2.user).isEmpty(),
                "Player got a message after player from another lobby sent a command");
        Assertions.assertTrue(messages.get(observer2.user).isEmpty(),
                "Observer got a message after player from another lobby sent a command");
    }


    record TestUser(String user, String authToken) {
    }

    TestUser registerUser(String name, String password, String email) {
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        TestModels.TestLoginRegisterResult result;
        registerRequest.username = name;
        registerRequest.password = password;
        registerRequest.email = email;
        result = serverFacade.register(registerRequest);
        return new TestUser(result.username, result.authToken);
    }


    int createGame(TestUser user, String name) {
        TestModels.TestCreateRequest createRequest = new TestModels.TestCreateRequest();
        createRequest.gameName = name;
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, user.authToken);
        return createResult.gameID;
    }

    void joinGame(int gameID, TestUser user, ChessGame.TeamColor color) {
        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        joinRequest.gameID = gameID;
        joinRequest.playerColor = color;
        serverFacade.verifyJoinPlayer(joinRequest, user.authToken);
    }

    private Map<String, List<TestModels.TestMessage>> joinPlayer(String sendingUsername, String sendingAuth, int gameID,
                                                                 ChessGame.TeamColor playerColor,
                                                                 Set<String> recipients, Set<String> others) {
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = sendingAuth;
        joinCommand.playerColor = playerColor;
        joinCommand.gameID = gameID;

        Map<String, Integer> expectedMessages =
                recipients.stream().collect(Collectors.toMap(Function.identity(), s -> 1));
        expectedMessages.putAll(others.stream().collect(Collectors.toMap(Function.identity(), s -> 0)));
        expectedMessages.put(sendingUsername, 1);
        return environment.exchange(sendingUsername, joinCommand, expectedMessages, waitTime);
    }


    private Map<String, List<TestModels.TestMessage>> joinObserver(String sendingUsername, String sendingAuth,
                                                                   int gameID, Set<String> recipients,
                                                                   Set<String> others) {
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = sendingAuth;
        joinCommand.gameID = gameID;

        Map<String, Integer> expectedMessages =
                recipients.stream().collect(Collectors.toMap(Function.identity(), s -> 1));
        expectedMessages.putAll(others.stream().collect(Collectors.toMap(Function.identity(), s -> 0)));
        expectedMessages.put(sendingUsername, 1);
        return environment.exchange(sendingUsername, joinCommand, expectedMessages, waitTime);
    }


    private Map<String, List<TestModels.TestMessage>> makeMove(String sendingUsername, String sendingAuth, int gameID,
                                                               ChessMove move, Set<String> recipients,
                                                               Set<String> others) {
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.MAKE_MOVE;
        joinCommand.authToken = sendingAuth;
        joinCommand.move = move;
        joinCommand.gameID = gameID;

        Map<String, Integer> expectedMessages =
                recipients.stream().collect(Collectors.toMap(Function.identity(), s -> 2));
        expectedMessages.putAll(others.stream().collect(Collectors.toMap(Function.identity(), s -> 0)));
        expectedMessages.put(sendingUsername, 1);
        return environment.exchange(sendingUsername, joinCommand, expectedMessages, waitTime);
    }


    private Map<String, List<TestModels.TestMessage>> resign(String sendingUsername, String sendingAuth, int gameID,
                                                             Set<String> recipients, Set<String> others) {
        TestModels.TestCommand resignation = new TestModels.TestCommand();
        resignation.commandType = TestModels.TestCommandType.RESIGN;
        resignation.authToken = sendingAuth;
        resignation.gameID = gameID;

        Map<String, Integer> expectedMessages =
                recipients.stream().collect(Collectors.toMap(Function.identity(), s -> 1));
        expectedMessages.putAll(others.stream().collect(Collectors.toMap(Function.identity(), s -> 0)));
        expectedMessages.put(sendingUsername, 1);
        return environment.exchange(sendingUsername, resignation, expectedMessages, waitTime);
    }


    private Map<String, List<TestModels.TestMessage>> leave(String sendingUsername, String sendingAuth, int gameID,
                                                            Set<String> recipients, Set<String> others) {
        TestModels.TestCommand resignation = new TestModels.TestCommand();
        resignation.commandType = TestModels.TestCommandType.LEAVE;
        resignation.authToken = sendingAuth;
        resignation.gameID = gameID;

        Map<String, Integer> expectedMessages =
                recipients.stream().collect(Collectors.toMap(Function.identity(), s -> 1));
        expectedMessages.putAll(others.stream().collect(Collectors.toMap(Function.identity(), s -> 0)));
        expectedMessages.put(sendingUsername, 0);
        return environment.exchange(sendingUsername, resignation, expectedMessages, waitTime);
    }


    private void setupNormalGame() {
        //join WHITE
        joinPlayer(white.user, white.authToken, gameID, ChessGame.TeamColor.WHITE, Set.of(), Set.of());

        //join BLACK
        joinPlayer(black.user, black.authToken, gameID, ChessGame.TeamColor.BLACK, Set.of(white.user),
                Set.of());

        //observer
        joinObserver(observer.user, observer.authToken, gameID, Set.of(white.user, black.user),
                Set.of());
    }


    private void assertLoadGameMessage(List<TestModels.TestMessage> messages) {
        Assertions.assertEquals(1, messages.size(), "Expected 1 message, got " + messages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.LOAD_GAME, messages.get(0).serverMessageType,
                "Message was not a LOAD_GAME message");
        Assertions.assertNotNull(messages.get(0).game, "LOAD_GAME message did not contain a game");
    }


    private void assertNotificationMessage(List<TestModels.TestMessage> messages) {
        Assertions.assertEquals(1, messages.size(), "Expected 1 message, got " + messages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.NOTIFICATION, messages.get(0).serverMessageType,
                "Message was not a NOTIFICATION message");
        Assertions.assertNotNull(messages.get(0).message, "Bobs NOTIFICATION message did not contain a message");
    }


    private void assertErrorMessage(List<TestModels.TestMessage> messages) {
        Assertions.assertEquals(1, messages.size(), "Expected 1 message, got " + messages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.ERROR, messages.get(0).serverMessageType,
                "Message was not an ERROR message");
        Assertions.assertNull(messages.get(0).game, "ERROR message contained a game");
        Assertions.assertNotNull(messages.get(0).errorMessage, "ERROR message did not contain an error message");
    }


    private void assertMoveMadePair(List<TestModels.TestMessage> messages) {
        Assertions.assertEquals(2, messages.size(), "Expected 2 messages, got " + messages.size());
        boolean isLoadGameFirst = messages.get(0).serverMessageType == TestModels.TestServerMessageType.LOAD_GAME;
        Assertions.assertEquals(TestModels.TestServerMessageType.LOAD_GAME,
                messages.get(isLoadGameFirst ? 0 : 1).serverMessageType, "Didn't get load game message");
        Assertions.assertNotNull(messages.get(isLoadGameFirst ? 0 : 1).game, "LOAD_GAME message didn't contain a game");
        Assertions.assertEquals(TestModels.TestServerMessageType.NOTIFICATION,
                messages.get(isLoadGameFirst ? 1 : 0).serverMessageType, "Didn't get notification message");
        Assertions.assertNotNull(messages.get(isLoadGameFirst ? 1 : 0).message,
                "NOTIFICATION message didn't contain a message");
    }

}