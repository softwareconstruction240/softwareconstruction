package passoffTests.serverTests;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import passoffTests.TestFactory;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestClient;
import passoffTests.testClasses.TestModels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WebSocketTests {

    private static TestClient bobClient;
    private static TestClient jamesClient;
    private static TestClient alfredClient;


    private ExecutorService bobExecutor;
    private ExecutorService jamesExecutor;
    private ExecutorService alfredExecutor;
    private ExecutorService testExecutor;

    private static TestModels.TestUser userBob;
    private static TestModels.TestUser userJames;
    private static TestModels.TestUser userAlfred;

    private static TestServerFacade serverFacade;

    private static String bobAuth;
    private static String jamesAuth;
    private static String alfredAuth;

    private static Integer emptyGame;
    private static Integer fullGame;

    private static Long waitTime;


    @BeforeAll
    public static void init() {
        serverFacade = new TestServerFacade("localhost", TestFactory.getServerPort());
        serverFacade.clear();

        bobClient = new TestClient();
        bobClient.setServerHost("localhost");
        bobClient.setServerPort(TestFactory.getServerPort());
        bobClient.setContext("/connect");

        jamesClient = new TestClient();
        jamesClient.setServerHost("localhost");
        jamesClient.setServerPort(TestFactory.getServerPort());
        jamesClient.setContext("/connect");

        alfredClient = new TestClient();
        alfredClient.setServerHost("localhost");
        alfredClient.setServerPort(TestFactory.getServerPort());
        alfredClient.setContext("/connect");


        //set Bob details
        userBob = new TestModels.TestUser();
        userBob.username = "bob";
        userBob.password = "BOB";
        userBob.email = "bob@BOB";

        //set James details
        userJames = new TestModels.TestUser();
        userJames.username = "James";
        userJames.password = "1234";
        userJames.email = "pepporoni@pizza.net";

        //set Alfred details
        userAlfred = new TestModels.TestUser();
        userAlfred.username = "Alfred";
        userAlfred.password = "Bruce";
        userAlfred.email = "Batman@Mr.Wayne";

        waitTime = TestFactory.getMessageTime();
    }


    @BeforeEach
    public void setup() {
        bobExecutor = Executors.newSingleThreadExecutor();
        jamesExecutor = Executors.newSingleThreadExecutor();
        alfredExecutor = Executors.newSingleThreadExecutor();
        testExecutor = Executors.newSingleThreadExecutor();

        //populate database

        serverFacade.clear();

        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        TestModels.TestLoginRegisterResult result;

        //register bob
        registerRequest.username = userBob.username;
        registerRequest.password = userBob.password;
        registerRequest.email = userBob.email;
        result = serverFacade.register(registerRequest);
        bobAuth = result.authToken;

        //register james
        registerRequest.username = userJames.username;
        registerRequest.password = userJames.password;
        registerRequest.email = userJames.email;
        result = serverFacade.register(registerRequest);
        jamesAuth = result.authToken;

        //register alfred
        registerRequest.username = userAlfred.username;
        registerRequest.password = userAlfred.password;
        registerRequest.email = userAlfred.email;
        result = serverFacade.register(registerRequest);
        alfredAuth = result.authToken;


        //create games
        TestModels.TestCreateRequest createRequest = new TestModels.TestCreateRequest();
        createRequest.gameName = "testGame";
        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        TestModels.TestCreateResult createResult;

        //emptyGame
        createResult = serverFacade.createGame(createRequest, bobAuth);
        emptyGame = createResult.gameID;

        //full game
        createRequest.gameName = "full";
        createResult = serverFacade.createGame(createRequest, bobAuth);
        fullGame = createResult.gameID;

        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = ChessGame.TeamColor.WHITE;
        serverFacade.verifyJoinPlayer(joinRequest, bobAuth);
        joinRequest.playerColor = ChessGame.TeamColor.BLACK;
        serverFacade.verifyJoinPlayer(joinRequest, jamesAuth);
    }


    @AfterEach
    public void tearDown() {
        bobClient.disconnect();
        jamesClient.disconnect();
        alfredClient.disconnect();
    }


    @Test
    @Order(1)
    @DisplayName("Normal Join Player")
    public void joinPlayerGood() throws InterruptedException, ExecutionException {

        //try join valid reserved spot
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = ChessGame.TeamColor.WHITE;
        joinCommand.gameID = fullGame;

        //make sure got load board message
        CountDownLatch readyLatch = new CountDownLatch(1);
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        bobClient.connect();
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch, waitTime));

        try {
            bobMessages = bobResult.get(waitTime * 2, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {}

        //check received message
        Assertions.assertEquals(1, bobMessages.size(),
                "Expected 1 message for Bob, got " + bobMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.LOAD_GAME, bobMessages.get(0).serverMessageType,
                "Bob's message was not a LOAD_GAME message");
        Assertions.assertNotNull(bobMessages.get(0).game, "Bob's LOAD_GAME message did not contain a game");

        //join other spot on game
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = jamesAuth;
        joinCommand.playerColor = ChessGame.TeamColor.BLACK;
        joinCommand.gameID = fullGame;


        //expect load board for james & Text message for bob
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult =
                jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch, waitTime));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch, waitTime));

        //get messages
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        try {
            jamesMessages = jamesResult.get(waitTime * 3, TimeUnit.MILLISECONDS);
            bobMessages = bobResult.get(waitTime * 3, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {}

        //check james messages
        Assertions.assertEquals(1, jamesMessages.size(),
                "Expected 1 message for James, got " + jamesMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.LOAD_GAME, jamesMessages.get(0).serverMessageType,
                "James message was not a LOAD_GAME message");
        Assertions.assertNotNull(jamesMessages.get(0).game, "James LOAD_GAME message did not contain a game");

        //check bob messages
        Assertions.assertEquals(1, bobMessages.size(),
                "Expected 1 message for Bob, got " + bobMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.NOTIFICATION, bobMessages.get(0).serverMessageType,
                "Bob's message was not a NOTIFICATION message");
        Assertions.assertNotNull(bobMessages.get(0).message,
                "Bobs NOTIFICATION message did not contain a message");
    }


    @Test
    @Order(2)
    @DisplayName("Join Player Wrong Team")
    public void joinPlayerSteal() throws InterruptedException, ExecutionException {
        //try join someone else's reserved spot
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = ChessGame.TeamColor.BLACK; //bob is playing white, but james is playing black
        joinCommand.gameID = fullGame;

        //make sure got load board message
        CountDownLatch readyLatch = new CountDownLatch(1);
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        bobClient.connect();
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch, waitTime));

        try {
            bobMessages = bobResult.get(waitTime * 2, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {}

        //check received message
        Assertions.assertEquals(1, bobMessages.size(),
                "Expected 1 message for Bob, got " + bobMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.ERROR, bobMessages.get(0).serverMessageType,
                "Bob's message was not an ERROR message");
        Assertions.assertNull(bobMessages.get(0).game, "Bob's ERROR message contained a game");
        Assertions.assertNotNull(bobMessages.get(0).errorMessage,
                "Bob's ERROR message did not contain an error message");
    }


    @Test
    @Order(3)
    @DisplayName("Join Player Empty Team")
    public void joinPlayerEmpty() throws InterruptedException, ExecutionException {

        //join empty game
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = alfredAuth;
        joinCommand.playerColor = ChessGame.TeamColor.BLACK;
        joinCommand.gameID = emptyGame;

        //make sure got load board message
        CountDownLatch readyLatch = new CountDownLatch(1);
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult =
                alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch, waitTime));
        try{
            alfredMessages = alfredResult.get(waitTime * 2, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {}

        //check received messages
        Assertions.assertEquals(1, alfredMessages.size(),
                "Expected 1 message for Alfred, got " + alfredMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.ERROR, alfredMessages.get(0).serverMessageType,
                "Alfred's message was not an ERROR message");
        Assertions.assertNull(alfredMessages.get(0).game, "Alfred's ERROR message contained a game");
        Assertions.assertNotNull(alfredMessages.get(0).errorMessage,
                "Alfred's ERROR message did not contain an error message");
    }


    @Test
    @Order(4)
    @DisplayName("Join Player Bad GameID")
    public void joinPlayerBadGameID() throws InterruptedException, ExecutionException {

        //join empty game
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = jamesAuth;
        joinCommand.playerColor = ChessGame.TeamColor.BLACK;
        joinCommand.gameID = fullGame + emptyGame; //invalid ID

        //make sure got load board message
        CountDownLatch readyLatch = new CountDownLatch(1);
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult =
                jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch, waitTime));
        try{
            jamesMessages = jamesResult.get(waitTime * 2, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {}

        //check received messages
        Assertions.assertEquals(1, jamesMessages.size(),
                "Expected 1 message for James, got " + jamesMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.ERROR, jamesMessages.get(0).serverMessageType,
                "James's message was not an ERROR message");
        Assertions.assertNull(jamesMessages.get(0).game, "James's ERROR message contained a game");
        Assertions.assertNotNull(jamesMessages.get(0).errorMessage,
                "James's ERROR message did not contain an error message");
    }


    @Test
    @Order(5)
    @DisplayName("Join Player Bad AuthToken")
    public void joinPlayerBadAuthtoken() throws InterruptedException, ExecutionException {
        //try join someone else's reserved spot
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = "badBobAuth";
        joinCommand.playerColor = ChessGame.TeamColor.WHITE;
        joinCommand.gameID = fullGame;

        //make sure got load board message
        CountDownLatch readyLatch = new CountDownLatch(1);
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        bobClient.connect();
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch, waitTime));

        try {
            bobMessages = bobResult.get(waitTime * 2, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {}

        //check received message
        Assertions.assertEquals(1, bobMessages.size(),
                "Expected 1 message for Bob, got " + bobMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.ERROR, bobMessages.get(0).serverMessageType,
                "Bob's message was not an ERROR message");
        Assertions.assertNull(bobMessages.get(0).game, "Bob's ERROR message contained a game");
        Assertions.assertNotNull(bobMessages.get(0).errorMessage,
                "Bob's ERROR message did not contain an error message");
    }


    @Test
    @Order(6)
    @DisplayName("Normal Join Observer")
    public void joinObserverGood() throws InterruptedException, ExecutionException {
        //try to observe full game
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = bobAuth;
        joinCommand.gameID = fullGame;

        //make sure got load board message
        CountDownLatch readyLatch = new CountDownLatch(1);
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        bobClient.connect();
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));

        try {
            bobMessages = bobResult.get(waitTime, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {}

        //check received message
        Assertions.assertEquals(1, bobMessages.size(),
                "Expected 1 message for Bob, got " + bobMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.LOAD_GAME, bobMessages.get(0).serverMessageType,
                "Bob's message was not a LOAD_GAME message");
        Assertions.assertNotNull(bobMessages.get(0).game, "Bob's LOAD_GAME message did not contain a game");


        //watch empty game
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = emptyGame;

        //make sure got load board message
        readyLatch = new CountDownLatch(1);
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult =
                bobExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch, waitTime));
        try {
            alfredMessages = alfredResult.get(waitTime * 2, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {}

        //check received messages
        Assertions.assertEquals(1, alfredMessages.size(),
                "Expected 1 message for Alfred, got " + alfredMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.LOAD_GAME, alfredMessages.get(0).serverMessageType,
                "Alfred's message was not a LOAD_GAME message");
        Assertions.assertNotNull(alfredMessages.get(0).game,
                "Alfred's LOAD_GAME message did not contain a game");


        //watch game with active player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;


        //expect load board for james & Text message for bob
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult =
                jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch, waitTime));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch, waitTime));

        //get messages
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        try {
            jamesMessages = jamesResult.get(waitTime * 3, TimeUnit.MILLISECONDS);
            bobMessages = bobResult.get(waitTime * 3, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {}

        //check james messages
        Assertions.assertEquals(1, jamesMessages.size(),
                "Expected 1 message for James, got " + jamesMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.LOAD_GAME, jamesMessages.get(0).serverMessageType,
                "James' message was not a LOAD_GAME message");
        Assertions.assertNotNull(jamesMessages.get(0).game,
                "James LOAD_GAME message did not contain a game");

        //check bob messages
        Assertions.assertEquals(1, bobMessages.size(),
                "Expected 1 message for Bob, got " + bobMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.NOTIFICATION, bobMessages.get(0).serverMessageType,
                "Bob's message was not a NOTIFICATION message");
        Assertions.assertNotNull(bobMessages.get(0).message,
                "Bob's NOTIFICATION message did not contain a message");
    }


    @Test
    @Order(7)
    @DisplayName("Multiple Observers")
    public void multipleJoinObserver() throws InterruptedException, ExecutionException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = ChessGame.TeamColor.WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch))
                .get(waitTime, TimeUnit.MILLISECONDS);

        //have james join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as an observer to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult =
                jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult.get(2 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(2 * waitTime, TimeUnit.MILLISECONDS);

        //have alfred observe game
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //ready message
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));

        //start getting messages
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch, waitTime));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch, waitTime));
        Future<List<TestModels.TestMessage>> alfredResult =
                alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch, waitTime));

        //prep messages lists
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        try {
            bobMessages = bobResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            jamesMessages = jamesResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            alfredMessages = alfredResult.get(waitTime * 4, TimeUnit.MILLISECONDS);

        } catch (TimeoutException ignore) {}

        //check message contents
        //alfred gets Load Game, everyone else gets notification
        Assertions.assertEquals(1, bobMessages.size(),
                "Expected 1 message for Bob, got " + bobMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.NOTIFICATION, bobMessages.get(0).serverMessageType,
                "Bob's message was not a NOTIFICATION message");
        Assertions.assertNotNull(bobMessages.get(0).message,
                "Bob's NOTIFICATION message did not contain a message");

        Assertions.assertEquals(1, jamesMessages.size(),
                "Expected 1 message for James, got " + jamesMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.NOTIFICATION, jamesMessages.get(0).serverMessageType,
                "James' message was not a NOTIFICATION message");
        Assertions.assertNotNull(jamesMessages.get(0).message,
                "James' NOTIFICATION message didn't contain a message");

        Assertions.assertEquals(1, alfredMessages.size(),
                "Expected 1 message for Alfred, got " + alfredMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.LOAD_GAME, alfredMessages.get(0).serverMessageType,
                "Alfred's message was not a LOAD_GAME message");
        Assertions.assertNotNull(alfredMessages.get(0).game,
                "Alfred's LOAD_GAME message did not contain a message");
    }


    @Test
    @Order(8)
    @DisplayName("Join Observer Bad GameID")
    public void joinObserverBadGameID() throws InterruptedException, ExecutionException {
        //try join someone else's reserved spot
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = bobAuth;
        joinCommand.gameID = fullGame + emptyGame;

        //make sure got load board message
        CountDownLatch readyLatch = new CountDownLatch(1);
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        bobClient.connect();
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch, waitTime));

        try {
            bobMessages = bobResult.get(waitTime * 2, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {}

        //check received message
        Assertions.assertEquals(1, bobMessages.size(),
                "Expected 1 message for Bob, got " + bobMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.ERROR, bobMessages.get(0).serverMessageType,
                "Bob's message was not an ERROR message");
        Assertions.assertNull(bobMessages.get(0).game, "Bob's ERROR message contained a game");
        Assertions.assertNotNull(bobMessages.get(0).errorMessage,
                "Bob's ERROR message did not contain an error message");
    }


    @Test
    @Order(9)
    @DisplayName("Join Observer Bad AuthToken")
    public void joinObserverBadAuthtoken() throws InterruptedException, ExecutionException {
        //try join someone else's reserved spot
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = "badAlfredAuth";
        joinCommand.gameID = fullGame + emptyGame;

        //make sure got load board message
        CountDownLatch readyLatch = new CountDownLatch(1);
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();
        alfredClient.connect();
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult =
                alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch, waitTime));

        try {
            alfredMessages = alfredResult.get(waitTime * 2, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {
        }

        //check received message
        Assertions.assertEquals(1, alfredMessages.size(),
                "Expected 1 message for Alfred, got " + alfredMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.ERROR, alfredMessages.get(0).serverMessageType,
                "Alfred's message was not an ERROR message");
        Assertions.assertNull(alfredMessages.get(0).game, "Alfred's ERROR message contained a game");
        Assertions.assertNotNull(alfredMessages.get(0).errorMessage,
                "Alfred's ERROR message did not contain an error message");
    }


    @Test
    @Order(10)
    @DisplayName("Normal Make Move")
    public void validMove() throws InterruptedException, ExecutionException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = ChessGame.TeamColor.WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch))
                .get(waitTime, TimeUnit.MILLISECONDS);

        //have james join as a player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.playerColor = ChessGame.TeamColor.BLACK;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as a player to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult =
                jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        bobResult.get(2 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(2 * waitTime, TimeUnit.MILLISECONDS);


        //have alfred join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //wait for alfred joining as an observer to go through
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult =
                alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(3 * waitTime, TimeUnit.MILLISECONDS);


        //have bob make a move
        //create pawn move
        ChessPosition startingPosition = TestFactory.getNewPosition(2, 5);
        ChessPosition endingPosition = TestFactory.getNewPosition(3, 5);
        ChessMove move = TestFactory.getNewMove(startingPosition, endingPosition, null);

        //create command
        TestModels.TestCommand moveCommand = new TestModels.TestCommand();
        moveCommand.commandType = TestModels.TestCommandType.MAKE_MOVE;
        moveCommand.authToken = bobAuth;
        moveCommand.gameID = fullGame;
        moveCommand.move = move;

        //ready message
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(bobClient.getSendMessageRunnable(moveCommand, readyLatch));

        //start getting messages
        jamesClient.connect();
        alfredClient.connect();
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch, waitTime));
        jamesResult = jamesExecutor.submit(new GetServerMessages(2, jamesClient, readyLatch, waitTime));
        alfredResult = alfredExecutor.submit(new GetServerMessages(2, alfredClient, readyLatch, waitTime));

        //prep messages lists
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        try {
            bobMessages = bobResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            jamesMessages = jamesResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            alfredMessages = alfredResult.get(waitTime * 4, TimeUnit.MILLISECONDS);

        } catch (TimeoutException ignore) {}

        //check message contents
        //bob should get load game, everyone else should get load game & notification
        Assertions.assertEquals(1, bobMessages.size(),
                "Expected 1 message for Bob, got " + bobMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.LOAD_GAME, bobMessages.get(0).serverMessageType,
                "Bob's message was not a LOAD_GAME message");
        Assertions.assertNotNull(bobMessages.get(0).game,
                "Bob's LOAD_GAME message did not contain a game");

        Assertions.assertEquals(2, jamesMessages.size(),
                "Expected 2 messages for James, got " + jamesMessages.size());
        boolean isLoadGameFirst = jamesMessages.get(0).serverMessageType == TestModels.TestServerMessageType.LOAD_GAME;
        Assertions.assertEquals(TestModels.TestServerMessageType.LOAD_GAME,
                jamesMessages.get(isLoadGameFirst ? 0 : 1).serverMessageType,
                "James didn't get load game message");
        Assertions.assertNotNull(jamesMessages.get(isLoadGameFirst ? 0 : 1).game,
                "James LOAD_GAME message didn't contain a game");
        Assertions.assertEquals(TestModels.TestServerMessageType.NOTIFICATION,
                jamesMessages.get(isLoadGameFirst ? 1 : 0).serverMessageType,
                "James didn't get notification");
        Assertions.assertNotNull(jamesMessages.get(isLoadGameFirst ? 1 : 0).message,
                "James NOTIFICATION message didn't contain a message");

        Assertions.assertEquals(2, alfredMessages.size(),
                "Expected 2 messages for Alfred, got " + alfredMessages.size());
        isLoadGameFirst = alfredMessages.get(0).serverMessageType == TestModels.TestServerMessageType.LOAD_GAME;
        Assertions.assertEquals(TestModels.TestServerMessageType.LOAD_GAME,
                alfredMessages.get(isLoadGameFirst ? 0 : 1).serverMessageType,
                "Alfred didn't get load game message");
        Assertions.assertNotNull(alfredMessages.get(isLoadGameFirst ? 0 : 1).game,
                "Alfred's LOAD_GAME message didn't contain a game");
        Assertions.assertEquals(TestModels.TestServerMessageType.NOTIFICATION,
                alfredMessages.get(isLoadGameFirst ? 1 : 0).serverMessageType,
                "Alfred didn't get notification");
        Assertions.assertNotNull(alfredMessages.get(isLoadGameFirst ? 1 : 0).message,
                "Alfred's NOTIFICATION message didn't contain a message");
    }


    @Test
    @Order(11)
    @DisplayName("Make Invalid Move")
    public void invalidMoveBadMove() throws ExecutionException, InterruptedException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = ChessGame.TeamColor.WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch))
                .get(waitTime, TimeUnit.MILLISECONDS);

        //have james join as a player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.playerColor = ChessGame.TeamColor.BLACK;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as a player to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult =
                jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        bobResult.get(2 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(2 * waitTime, TimeUnit.MILLISECONDS);


        //have alfred join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //wait for alfred joining as an observer to go through
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult =
                alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(3 * waitTime, TimeUnit.MILLISECONDS);

        //have bob make an invalid move
        //try to move rook
        ChessPosition startingPosition = TestFactory.getNewPosition(1, 1);
        ChessPosition endingPosition = TestFactory.getNewPosition(1, 5);
        ChessMove move = TestFactory.getNewMove(startingPosition, endingPosition, null);

        //create command
        TestModels.TestCommand moveCommand = new TestModels.TestCommand();
        moveCommand.commandType = TestModels.TestCommandType.MAKE_MOVE;
        moveCommand.authToken = bobAuth;
        moveCommand.gameID = fullGame;
        moveCommand.move = move;

        //ready message
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(bobClient.getSendMessageRunnable(moveCommand, readyLatch));

        //start getting messages
        jamesClient.connect();
        alfredClient.connect();
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch, waitTime));
        jamesResult = jamesExecutor.submit(new GetServerMessages(0, jamesClient, readyLatch, waitTime));
        alfredResult = alfredExecutor.submit(new GetServerMessages(0, alfredClient, readyLatch, waitTime));

        //prep messages lists
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        //only bob should get a message
        try {
            bobMessages = bobResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            jamesMessages = jamesResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            alfredMessages = alfredResult.get(waitTime * 4, TimeUnit.MILLISECONDS);

        } catch (TimeoutException ignore) {}

        //check message contents
        //bob should have gotten error message
        Assertions.assertEquals(1, bobMessages.size(),
                "Expected 1 message for Bob, got " + bobMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.ERROR, bobMessages.get(0).serverMessageType,
                "Bob's message wasn't an error message");

        //everyone else should have gotten nothing
        Assertions.assertTrue(jamesMessages.isEmpty(),
                "James got a message after Bob sent an invalid command");
        Assertions.assertTrue(alfredMessages.isEmpty(),
                "Alfred got a message after Bob sent an invalid command");
    }


    @Test
    @Order(12)
    @DisplayName("Make Move Wrong Turn")
    public void invalidMoveWrongTurn() throws ExecutionException, InterruptedException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = ChessGame.TeamColor.WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch))
                .get(waitTime, TimeUnit.MILLISECONDS);

        //have james join as a player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.playerColor = ChessGame.TeamColor.BLACK;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as a player to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult =
                jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult.get(2 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(2 * waitTime, TimeUnit.MILLISECONDS);


        //have alfred join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //wait for alfred joining as an observer to go through
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult =
                alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(3 * waitTime, TimeUnit.MILLISECONDS);

        //have james make a valid move while it's not his turn
        //try to move pawn
        ChessPosition startingPosition = TestFactory.getNewPosition(7, 5);
        ChessPosition endingPosition = TestFactory.getNewPosition(5, 5);
        ChessMove move = TestFactory.getNewMove(startingPosition, endingPosition, null);

        //create command
        TestModels.TestCommand moveCommand = new TestModels.TestCommand();
        moveCommand.commandType = TestModels.TestCommandType.MAKE_MOVE;
        moveCommand.authToken = jamesAuth;
        moveCommand.gameID = fullGame;
        moveCommand.move = move;

        //ready message
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(jamesClient.getSendMessageRunnable(moveCommand, readyLatch));

        //start getting messages
        bobClient.connect();
        alfredClient.connect();
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch, waitTime));
        bobResult = bobExecutor.submit(new GetServerMessages(0, bobClient, readyLatch, waitTime));
        alfredResult = alfredExecutor.submit(new GetServerMessages(0, alfredClient, readyLatch, waitTime));

        //prep messages lists
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        //only james should get a message
        try {
            jamesMessages = jamesResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            bobMessages = bobResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            alfredMessages = alfredResult.get(waitTime * 4, TimeUnit.MILLISECONDS);

        } catch (TimeoutException ignore) {}

        //check message contents
        //james should have gotten error message
        Assertions.assertEquals(1, jamesMessages.size(),
                "Expected 1 message for James, got " + jamesMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.ERROR, jamesMessages.get(0).serverMessageType,
                "James's message wasn't an error message");

        //everyone else should have gotten nothing
        Assertions.assertTrue(bobMessages.isEmpty(),
                "Bob got a message after James sent an invalid command");
        Assertions.assertTrue(alfredMessages.isEmpty(),
                "Alfred got a message after James sent an invalid command");
    }


    @Test
    @Order(13)
    @DisplayName("Make Move for Opponent")
    public void invalidMoveOpponent() throws ExecutionException, InterruptedException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = ChessGame.TeamColor.WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch))
                .get(waitTime, TimeUnit.MILLISECONDS);

        //have james join as a player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.playerColor = ChessGame.TeamColor.BLACK;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as a player to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult =
                jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult.get(2 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(2 * waitTime, TimeUnit.MILLISECONDS);


        //have alfred join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //wait for alfred joining as an observer to go through
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult =
                alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(3 * waitTime, TimeUnit.MILLISECONDS);

        //have james try to move a piece for bob
        //try to move pawn
        ChessPosition startingPosition = TestFactory.getNewPosition(2, 5);
        ChessPosition endingPosition = TestFactory.getNewPosition(4, 5);
        ChessMove move = TestFactory.getNewMove(startingPosition, endingPosition, null);

        //create command
        TestModels.TestCommand moveCommand = new TestModels.TestCommand();
        moveCommand.commandType = TestModels.TestCommandType.MAKE_MOVE;
        moveCommand.authToken = jamesAuth;
        moveCommand.gameID = fullGame;
        moveCommand.move = move;

        //ready message
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(jamesClient.getSendMessageRunnable(moveCommand, readyLatch));

        //start getting messages
        bobClient.connect();
        alfredClient.connect();
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch, waitTime));
        bobResult = bobExecutor.submit(new GetServerMessages(0, bobClient, readyLatch, waitTime));
        alfredResult = alfredExecutor.submit(new GetServerMessages(0, alfredClient, readyLatch, waitTime));

        //prep messages lists
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        //only james should get a message
        try {
            jamesMessages = jamesResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            bobMessages = bobResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            alfredMessages = alfredResult.get(waitTime * 4, TimeUnit.MILLISECONDS);

        } catch (TimeoutException ignore) {}

        //check message contents
        //james should have gotten error message
        Assertions.assertEquals(1, jamesMessages.size(),
                "Expected 1 message for James, got " + jamesMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.ERROR, jamesMessages.get(0).serverMessageType,
                "James's message wasn't an error message");

        //everyone else should have gotten nothing
        Assertions.assertTrue(bobMessages.isEmpty(),
                "Bob got a message after James sent an invalid command");
        Assertions.assertTrue(alfredMessages.isEmpty(),
                "Alfred got a message after James sent an invalid command");
    }


    @Test
    @Order(14)
    @DisplayName("Make Move Observer")
    public void invalidMoveObserver() throws ExecutionException, InterruptedException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = ChessGame.TeamColor.WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch))
                .get(waitTime, TimeUnit.MILLISECONDS);

        //have james join as a player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.playerColor = ChessGame.TeamColor.BLACK;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as a player to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult =
                jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult.get(2 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(2 * waitTime, TimeUnit.MILLISECONDS);


        //have alfred join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //wait for alfred joining as an observer to go through
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult =
                alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(3 * waitTime, TimeUnit.MILLISECONDS);

        //have alfred attempt to make a move
        //try to move pawn
        ChessPosition startingPosition = TestFactory.getNewPosition(2, 5);
        ChessPosition endingPosition = TestFactory.getNewPosition(4, 5);
        ChessMove move = TestFactory.getNewMove(startingPosition, endingPosition, null);

        //create command
        TestModels.TestCommand moveCommand = new TestModels.TestCommand();
        moveCommand.commandType = TestModels.TestCommandType.MAKE_MOVE;
        moveCommand.authToken = alfredAuth;
        moveCommand.gameID = fullGame;
        moveCommand.move = move;

        //ready message
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(moveCommand, readyLatch));

        //start getting messages
        bobClient.connect();
        jamesClient.connect();
        alfredResult = alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch, waitTime));
        bobResult = bobExecutor.submit(new GetServerMessages(0, bobClient, readyLatch, waitTime));
        jamesResult = jamesExecutor.submit(new GetServerMessages(0, jamesClient, readyLatch, waitTime));

        //prep messages lists
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        //only alfred should get a message
        try {
            alfredMessages = alfredResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            jamesMessages = jamesResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            bobMessages = bobResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {}

        //check message contents
        //james should have gotten error message
        Assertions.assertEquals(1, alfredMessages.size(),
                "Expected 1 message for Alfred, got " + alfredMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.ERROR, alfredMessages.get(0).serverMessageType,
                "Alfred's message wasn't an error message");

        //everyone else should have gotten nothing
        Assertions.assertTrue(bobMessages.isEmpty(),
                "Bob got a message after Alfred sent an invalid command");
        Assertions.assertTrue(jamesMessages.isEmpty(),
                "James got a message after Alfred sent an invalid command");
    }


    @Test
    @Order(15)
    @DisplayName("Make Move Game Over")
    public void invalidMoveGameOver() throws ExecutionException, InterruptedException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = ChessGame.TeamColor.WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch))
                .get(waitTime, TimeUnit.MILLISECONDS);

        //have james join as a player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.playerColor = ChessGame.TeamColor.BLACK;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as a player to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult =
                jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult.get(2 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(2 * waitTime, TimeUnit.MILLISECONDS);


        //have alfred join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //wait for alfred joining as an observer to go through
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult =
                alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(3 * waitTime, TimeUnit.MILLISECONDS);

        //james resign
        TestModels.TestCommand resignation = new TestModels.TestCommand();
        resignation.commandType = TestModels.TestCommandType.RESIGN;
        resignation.authToken = jamesAuth;
        resignation.gameID = fullGame;

        //wait for james resigning to go through
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(jamesClient.getSendMessageRunnable(resignation, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        alfredResult = alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch));
        jamesResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        alfredResult.get(3 * waitTime, TimeUnit.MILLISECONDS);

        //have bob attempt to make a move
        //try to move pawn
        ChessPosition startingPosition = TestFactory.getNewPosition(2, 5);
        ChessPosition endingPosition = TestFactory.getNewPosition(4, 5);
        ChessMove move = TestFactory.getNewMove(startingPosition, endingPosition, null);

        //create command
        TestModels.TestCommand moveCommand = new TestModels.TestCommand();
        moveCommand.commandType = TestModels.TestCommandType.MAKE_MOVE;
        moveCommand.authToken = bobAuth;
        moveCommand.gameID = fullGame;
        moveCommand.move = move;

        //ready message
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(bobClient.getSendMessageRunnable(moveCommand, readyLatch));

        //start getting messages
        jamesClient.connect();
        alfredClient.connect();
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch, waitTime));
        jamesResult = jamesExecutor.submit(new GetServerMessages(0, jamesClient, readyLatch, waitTime));
        alfredResult = alfredExecutor.submit(new GetServerMessages(0, alfredClient, readyLatch, waitTime));

        //prep messages lists
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        //only bob should get a message
        try {
            bobMessages = bobResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            alfredMessages = alfredResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            jamesMessages = jamesResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {}

        //check message contents
        //james should have gotten error message
        Assertions.assertEquals(1, bobMessages.size(),
                "Expected 1 message for Bob, got " + bobMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.ERROR, bobMessages.get(0).serverMessageType,
                "Bob's message wasn't an error message");

        //everyone else should have gotten nothing
        Assertions.assertTrue(jamesMessages.isEmpty(),
                "James got a message after Bob sent an invalid command");
        Assertions.assertTrue(alfredMessages.isEmpty(),
                "Alfred got a message after Bob sent an invalid command");
    }


    @Test
    @Order(16)
    @DisplayName("Normal Resign")
    public void validResign() throws ExecutionException, InterruptedException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = ChessGame.TeamColor.WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch))
                .get(waitTime, TimeUnit.MILLISECONDS);

        //have james join as a player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.playerColor = ChessGame.TeamColor.BLACK;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as a player to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult =
                jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        bobResult.get(2 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(2 * waitTime, TimeUnit.MILLISECONDS);


        //have alfred join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //wait for alfred joining as an observer to go through
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult =
                alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(3 * waitTime, TimeUnit.MILLISECONDS);

        //have james resign
        //create command
        TestModels.TestCommand resignation = new TestModels.TestCommand();
        resignation.commandType = TestModels.TestCommandType.RESIGN;
        resignation.authToken = jamesAuth;
        resignation.gameID = fullGame;

        //ready message
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(jamesClient.getSendMessageRunnable(resignation, readyLatch));

        //start getting messages
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch, waitTime));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch, waitTime));
        alfredResult = alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch, waitTime));

        //prep messages lists
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        try {
            bobMessages = bobResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            jamesMessages = jamesResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            alfredMessages = alfredResult.get(waitTime * 4, TimeUnit.MILLISECONDS);

        } catch (TimeoutException ignore) {}

        //check message contents
        //everyone should have gotten a single NOTIFICATION
        Assertions.assertEquals(1, bobMessages.size(),
                "Expected 1 message for Bob, got " + bobMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.NOTIFICATION, bobMessages.get(0).serverMessageType,
                "Bob didn't get NOTIFICATION message");

        Assertions.assertEquals(1, jamesMessages.size(),
                "Expected 1 message for James, got " + jamesMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.NOTIFICATION, jamesMessages.get(0).serverMessageType,
                "James didn't get NOTIFICATION message");

        Assertions.assertEquals(1, alfredMessages.size(),
                "Expected 1 message for Alfred, got " + alfredMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.NOTIFICATION, alfredMessages.get(0).serverMessageType,
                "Alfred didn't get NOTIFICATION message");



        //bob attempts to make a move after james resigned
        ChessPosition startingPosition = TestFactory.getNewPosition(2, 5);
        ChessPosition endingPosition = TestFactory.getNewPosition(4, 5);
        ChessMove move = TestFactory.getNewMove(startingPosition, endingPosition, null);

        //create command
        TestModels.TestCommand moveCommand = new TestModels.TestCommand();
        moveCommand.commandType = TestModels.TestCommandType.MAKE_MOVE;
        moveCommand.authToken = bobAuth;
        moveCommand.gameID = fullGame;
        moveCommand.move = move;

        //ready message
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(bobClient.getSendMessageRunnable(moveCommand, readyLatch));

        //start getting messages
        jamesClient.connect();
        alfredClient.connect();
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch, waitTime));
        jamesResult = jamesExecutor.submit(new GetServerMessages(0, jamesClient, readyLatch, waitTime));
        alfredResult = alfredExecutor.submit(new GetServerMessages(0, alfredClient, readyLatch, waitTime));

        //prep messages lists
        bobMessages = new ArrayList<>();
        jamesMessages = new ArrayList<>();
        alfredMessages = new ArrayList<>();

        //wait to get all messages
        //only bob should get a message
        try {
            bobMessages = bobResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            jamesMessages = jamesResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            alfredMessages = alfredResult.get(waitTime * 4, TimeUnit.MILLISECONDS);

        } catch (TimeoutException ignore) {}

        //check message contents
        //bob should have gotten error message
        Assertions.assertEquals(1, bobMessages.size(),
                "Expected 1 message for Bob, got " + bobMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.ERROR, bobMessages.get(0).serverMessageType,
                "Bob's message wasn't an error message");

        //everyone else should have gotten nothing
        Assertions.assertTrue(jamesMessages.isEmpty(),
                "James got a message after Bob sent an invalid command");
        Assertions.assertTrue(alfredMessages.isEmpty(),
                "Alfred got a message after Bob sent an invalid command");
    }


    @Test
    @Order(17)
    @DisplayName("Observer Resign")
    public void invalidResignObserver() throws ExecutionException, InterruptedException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = ChessGame.TeamColor.WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch))
                .get(waitTime, TimeUnit.MILLISECONDS);

        //have james join as a player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.playerColor = ChessGame.TeamColor.BLACK;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as a player to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult =
                jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        bobResult.get(2 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(2 * waitTime, TimeUnit.MILLISECONDS);


        //have alfred join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //wait for alfred joining as an observer to go through
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult =
                alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(3 * waitTime, TimeUnit.MILLISECONDS);

        //have alfred try to resign
        //create command
        TestModels.TestCommand resignation = new TestModels.TestCommand();
        resignation.commandType = TestModels.TestCommandType.RESIGN;
        resignation.authToken = alfredAuth;
        resignation.gameID = fullGame;

        //ready message
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(resignation, readyLatch));

        //start getting messages
        alfredResult = alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch, waitTime));
        bobResult = bobExecutor.submit(new GetServerMessages(0, bobClient, readyLatch, waitTime));
        jamesResult = jamesExecutor.submit(new GetServerMessages(0, jamesClient, readyLatch, waitTime));

        //prep messages lists
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();

        //wait to get all messages
        //only alfred should get a message
        try {
            alfredMessages = alfredResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            bobMessages = bobResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            jamesMessages = jamesResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {}

        //check message contents
        //bob should have gotten error message
        Assertions.assertEquals(1, alfredMessages.size(), "Alfred didn't get a message");
        Assertions.assertEquals(TestModels.TestServerMessageType.ERROR, alfredMessages.get(0).serverMessageType,
                "Alfred didn't get an ERROR message");

        //everyone else should have gotten nothing
        Assertions.assertTrue(jamesMessages.isEmpty(),
                "James got a message after Alfred sent an invalid command");
        Assertions.assertTrue(bobMessages.isEmpty(),
                "Bob got a message after Alfred sent an invalid command");
    }


    @Test
    @Order(18)
    @DisplayName("Game Over Resign")
    public void invalidResignGameOver() throws ExecutionException, InterruptedException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = ChessGame.TeamColor.WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch))
                .get(waitTime, TimeUnit.MILLISECONDS);

        //have james join as a player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.playerColor = ChessGame.TeamColor.BLACK;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as a player to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult =
                jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        bobResult.get(2 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(2 * waitTime, TimeUnit.MILLISECONDS);


        //have alfred join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //wait for alfred joining as an observer to go through
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult =
                alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(3 * waitTime, TimeUnit.MILLISECONDS);

        //bob resign
        TestModels.TestCommand resignation = new TestModels.TestCommand();
        resignation.commandType = TestModels.TestCommandType.RESIGN;
        resignation.authToken = bobAuth;
        resignation.gameID = fullGame;

        //wait for bob resigning to go through
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(bobClient.getSendMessageRunnable(resignation, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult = alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch));


        bobResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        alfredResult.get(3 * waitTime, TimeUnit.MILLISECONDS);


        //have james try to resign
        resignation.authToken = jamesAuth;

        //ready message
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(jamesClient.getSendMessageRunnable(resignation, readyLatch));

        //start getting messages
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch, waitTime));
        bobResult = bobExecutor.submit(new GetServerMessages(0, bobClient, readyLatch, waitTime));
        alfredResult = alfredExecutor.submit(new GetServerMessages(0, alfredClient, readyLatch, waitTime));

        //prep messages lists
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        //only james should get a message
        try {
            jamesMessages = jamesResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            bobMessages = bobResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
            alfredMessages = alfredResult.get(waitTime * 4, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {}

        //check message contents
        //bob should have gotten error message
        Assertions.assertEquals(1, jamesMessages.size(),
                "Expected 1 message for James, got " + jamesMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.ERROR, jamesMessages.get(0).serverMessageType,
                "James didn't get an ERROR message");

        //everyone else should have gotten nothing
        Assertions.assertTrue(bobMessages.isEmpty(),
                "Bob got a message after James sent an invalid command");
        Assertions.assertTrue(alfredMessages.isEmpty(),
                "Alfred got a message after James sent an invalid command");
    }


    @Test
    @Order(19)
    @DisplayName("Leave Game")
    public void leaveGame() throws ExecutionException, InterruptedException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = ChessGame.TeamColor.WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch))
                .get(waitTime, TimeUnit.MILLISECONDS);

        //have james join as a player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.playerColor = ChessGame.TeamColor.BLACK;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as a player to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult =
                jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult =
                bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        bobResult.get(2 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(2 * waitTime, TimeUnit.MILLISECONDS);


        //have alfred join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //wait for alfred joining as an observer to go through
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult =
                alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        bobResult.get(3 * waitTime, TimeUnit.MILLISECONDS);
        jamesResult.get(3 * waitTime, TimeUnit.MILLISECONDS);


        //have bob leave game
        //create command
        TestModels.TestCommand leaveCommand = new TestModels.TestCommand();
        leaveCommand.commandType = TestModels.TestCommandType.LEAVE;
        leaveCommand.authToken = bobAuth;
        leaveCommand.gameID = fullGame;

        //ready message
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(bobClient.getSendMessageRunnable(leaveCommand, readyLatch));

        //start getting messages
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch, waitTime));
        alfredResult = alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch, waitTime));

        //prep messages lists
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        //Bob can get message if you want, but probably not. Everyone else needs to get notified
        try {
            jamesMessages = jamesResult.get(waitTime * 3, TimeUnit.MILLISECONDS);
            alfredMessages = alfredResult.get(waitTime * 3, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ignore) {}

        //check message contents
        Assertions.assertEquals(1, alfredMessages.size(),
                "Expected 1 message for Alfred, got " + alfredMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.NOTIFICATION, alfredMessages.get(0).serverMessageType,
                "Alfred didn't get a NOTIFICATION message");

        //check message contents
        //bob should have gotten error message
        Assertions.assertEquals(1, jamesMessages.size(),
                "Expected 1 message for James, got " + jamesMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.NOTIFICATION, jamesMessages.get(0).serverMessageType,
                "James didn't get NOTIFICATION message");


        //now have alfred leave the match
        //create command
        leaveCommand = new TestModels.TestCommand();
        leaveCommand.commandType = TestModels.TestCommandType.LEAVE;
        leaveCommand.authToken = alfredAuth;
        leaveCommand.gameID = fullGame;

        //prep messages lists
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        jamesMessages = new ArrayList<>();

        //ready message
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(alfredClient.getSendMessageRunnable(leaveCommand, readyLatch));

        //start getting messages
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch, waitTime));
        bobResult = bobExecutor.submit(new GetServerMessages(0, bobClient, readyLatch, waitTime));

        //wait to get all messages
        //Bob should not get message (he already left). James must get notified
        try {
            jamesMessages = jamesResult.get(waitTime * 3, TimeUnit.MILLISECONDS);
            bobMessages = bobResult.get(waitTime * 3, TimeUnit.MILLISECONDS);

        } catch (TimeoutException ignore) {}


        //try message contents
        //james should have been notified
        Assertions.assertEquals(1, jamesMessages.size(),
                "Expected 1 message for James, got " + jamesMessages.size());
        Assertions.assertEquals(TestModels.TestServerMessageType.NOTIFICATION, jamesMessages.get(0).serverMessageType,
                "James didn't get a NOTIFICATION message");

        //bob should have gotten nothing
        Assertions.assertTrue(bobMessages.isEmpty(), "Bob got a message after leaving the game");
    }


    //helper code
    //------------------------------------------------------------------------------------------------------------------
    public static class GetServerMessages implements Callable<List<TestModels.TestMessage>>, TestClient.TestListener {

        Integer numMessagesExpected;
        List<TestModels.TestMessage> messages;
        CountDownLatch latch;
        CountDownLatch readyLatch;
        Long waitTime;

        public GetServerMessages(Integer numMessagesExpected, TestClient socket,
                                 CountDownLatch readyLatch) {
            this.numMessagesExpected = numMessagesExpected;
            messages = new ArrayList<>();
            socket.registerListener(this);
            this.readyLatch = readyLatch;
        }

        public GetServerMessages(Integer numMessagesExpected, TestClient socket,
                                 CountDownLatch readyLatch, Long waitTime) {
            this(numMessagesExpected, socket, readyLatch);
            this.waitTime = waitTime;
        }

        @Override
        public List<TestModels.TestMessage> call() throws Exception {
            latch = new CountDownLatch(numMessagesExpected);
            readyLatch.countDown();
            if(waitTime != null) Thread.sleep(waitTime);
            latch.await();
            return messages;
        }

        @Override
        public void notifyMessage(String message) {
            messages.add(new Gson().fromJson(message, TestModels.TestMessage.class));
            latch.countDown();
        }

    }


}
