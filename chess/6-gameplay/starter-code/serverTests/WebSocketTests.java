package passoffTests.serverTests;

import passoffTests.testClasses.TestClient;
import passoffTests.TestFactory;
import passoffTests.testClasses.TestModels;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WebSocketTests {
    static TestClient bobClient;
    static TestClient jamesClient;
    static TestClient alfredClient;

    static ExecutorService bobExecutor;
    static ExecutorService jamesExecutor;
    static ExecutorService alfredExecutor;
    static ExecutorService testExecutor;

    static TestModels.TestUser userBob;
    static TestModels.TestUser userJames;
    static TestModels.TestUser userAlfred;

    static TestServerFacade serverFacade;

    static String bobAuth;
    static String jamesAuth;
    static String alfredAuth;

    static Integer emptyGame;
    static Integer fullGame;
    static Integer soloGame;
    static Integer lonelyGame;

    static Long waitTime;

    @BeforeAll
    static void init(){
        serverFacade = new TestServerFacade("localhost", TestFactory
                .getServerPort());
        serverFacade.clear();

        bobClient = new TestClient();
        jamesClient = new TestClient();
        alfredClient = new TestClient();

        //set bob details
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
        userAlfred.password = "Brucy";
        userAlfred.email = "Batman@Mr.Wayne";

        waitTime = TestFactory.getMessageTime();
    }

    @BeforeEach
    public void setup(){

        bobExecutor = Executors.newSingleThreadExecutor();
        jamesExecutor = Executors.newSingleThreadExecutor();
        alfredExecutor = Executors.newSingleThreadExecutor();
        testExecutor = Executors.newSingleThreadExecutor();

        bobClient.setServerHost("localhost");
        bobClient.setServerPort(TestFactory.getServerPort());
        bobClient.setContext("/connect");

        jamesClient.setServerHost("localhost");
        jamesClient.setServerPort(TestFactory.getServerPort());
        jamesClient.setContext("/connect");

        alfredClient.setServerHost("localhost");
        alfredClient.setServerPort(TestFactory.getServerPort());
        alfredClient.setContext("/connect");


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

        //emptyGame;
        createResult = serverFacade.createGame(createRequest, bobAuth);
        emptyGame = createResult.gameID;

        //full game
        createRequest.gameName = "full";
        createResult = serverFacade.createGame(createRequest, bobAuth);
        fullGame = createResult.gameID;

        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = WHITE;
        serverFacade.verifyJoinPlayer(joinRequest, bobAuth);
        joinRequest.playerColor = BLACK;
        serverFacade.verifyJoinPlayer(joinRequest, jamesAuth);

        //solo game
        createRequest.gameName = "solo";
        createResult = serverFacade.createGame(createRequest, bobAuth);
        soloGame = createResult.gameID;

        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = WHITE;
        serverFacade.verifyJoinPlayer(joinRequest, alfredAuth);
        joinRequest.playerColor = BLACK;
        serverFacade.verifyJoinPlayer(joinRequest, alfredAuth);

        //lonely game
        createRequest.gameName = "lonely";
        createResult = serverFacade.createGame(createRequest, jamesAuth);
        lonelyGame = createResult.gameID;

        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = BLACK;
        serverFacade.verifyJoinPlayer(joinRequest, jamesAuth);
    }

    @AfterEach
    public void tearDown(){
        bobClient.disconnect();
        jamesClient.disconnect();
        alfredClient.disconnect();
    }

    @Test
    @Order(1)
    public void joinPlayerGood() throws InterruptedException, ExecutionException {

        //try join valid reserved spot
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = WHITE;
        joinCommand.gameID = fullGame;

        //make sure got load board message
        CountDownLatch readyLatch = new CountDownLatch(1);
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        bobClient.connect();
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult = bobExecutor.submit(new GetServerMessages(1,
                bobClient, readyLatch));

        try{
            bobMessages = bobResult.get(waitTime, TimeUnit.SECONDS);
        } catch (TimeoutException ignore) {
        }

        //check received message
        assertEquals(1, bobMessages.size(), "Bob didn't get a message");
        assertEquals(TestModels.TestServerMessageType.LOAD_GAME, bobMessages.get(0).serverMessageType, "Bob's message was not a LOAD_GAME message");
        assertNotNull(bobMessages.get(0).game, "Bob's LOAD_GAME message did not contain a game");

        //join empty game
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = alfredAuth;
        joinCommand.playerColor = BLACK;
        joinCommand.gameID = emptyGame;

        //make sure got load board message
        readyLatch = new CountDownLatch(1);
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult = bobExecutor.submit(new GetServerMessages(1,
                alfredClient, readyLatch));
        try{
            alfredMessages = alfredResult.get(waitTime, TimeUnit.SECONDS);
        } catch (TimeoutException ignore) {
        }

        //check received messages
        assertEquals(1, alfredMessages.size(), "Alfred did not get a message");
        assertEquals(TestModels.TestServerMessageType.LOAD_GAME, alfredMessages.get(0).serverMessageType, "Alfred's message was not a LOAD_GAME message");
        assertNotNull(alfredMessages.get(0).game, "Alfreds LOAD_GAME message did not contain a game");




        //join other spot on game
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = jamesAuth;
        joinCommand.playerColor = BLACK;
        joinCommand.gameID = fullGame;


        //expect load board for james & Text message for bob
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult = jamesExecutor.submit(new GetServerMessages(1,
                jamesClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1,
                bobClient, readyLatch));

        //get messages
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        try{
            jamesMessages = jamesResult.get(waitTime*2, TimeUnit.SECONDS);
            bobMessages = bobResult.get(waitTime*2, TimeUnit.SECONDS);
        } catch (TimeoutException ignore) {
        }

        //check james messages
        assertEquals(1, jamesMessages.size(), "James did not get a message");
        assertEquals(TestModels.TestServerMessageType.LOAD_GAME, jamesMessages.get(0).serverMessageType, "James message was not a LOAD_GAME message");
        assertNotNull(jamesMessages.get(0).game, "James LOAD_GAME message did not contain a game");

        //check bob messages
        assertEquals(1, bobMessages.size(), "Bob did not get a message");
        assertEquals(TestModels.TestServerMessageType.NOTIFICATION, bobMessages.get(0).serverMessageType, "Bob's message was not a NOTIFICATION message");
        assertNotNull(bobMessages.get(0).message, "Bobs NOTIFICATION message did not contain a message");
    }

    @Test
    @Order(2)
    public void joinPlayerBad() throws InterruptedException, ExecutionException {
        //try join someone else's reserved spot
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = BLACK;
        joinCommand.gameID = fullGame;

        //make sure got load board message
        CountDownLatch readyLatch = new CountDownLatch(1);
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        bobClient.connect();
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult = bobExecutor.submit(new GetServerMessages(1,
                bobClient, readyLatch));

        try{
            bobMessages = bobResult.get(waitTime, TimeUnit.SECONDS);
        } catch (TimeoutException ignore) {
        }

        //check received message
        assertEquals(1, bobMessages.size(), "Bob did not get a message");
        assertEquals(TestModels.TestServerMessageType.ERROR, bobMessages.get(0).serverMessageType, "Bob's message was not an ERROR message");
        assertNull(bobMessages.get(0).game, "Bob's ERROR message contained a game");
        assertNotNull(bobMessages.get(0).errorMessage, "Bob's ERROR message did not contain an error message");

    }

    @Test
    @Order(3)
    public void joinObserverGood() throws InterruptedException, ExecutionException {
        //try observe full game
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = bobAuth;
        joinCommand.gameID = fullGame;

        //make sure got load board message
        CountDownLatch readyLatch = new CountDownLatch(1);
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        bobClient.connect();
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult = bobExecutor.submit(new GetServerMessages(1,
                bobClient, readyLatch));

        try{
            bobMessages = bobResult.get(waitTime, TimeUnit.SECONDS);
        } catch (TimeoutException ignore) {
        }

        //check received message
        assertEquals(1, bobMessages.size(), "Bob did not get a message");
        assertEquals(TestModels.TestServerMessageType.LOAD_GAME, bobMessages.get(0).serverMessageType, "Bob's message was not a LOAD_GAME message");
        assertNotNull(bobMessages.get(0).game, "Bob's LOAD_GAME message did not contain a game");


        //watch empty game
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = emptyGame;

        //make sure got load board message
        readyLatch = new CountDownLatch(1);
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult = bobExecutor.submit(new GetServerMessages(1,
                alfredClient, readyLatch));
        try{
            alfredMessages = alfredResult.get(waitTime, TimeUnit.SECONDS);
        } catch (TimeoutException ignore) {
        }

        //check received messages
        assertEquals(1, alfredMessages.size(), "Alfred did not get a message");
        assertEquals(TestModels.TestServerMessageType.LOAD_GAME, alfredMessages.get(0).serverMessageType, "Alfred's message was not a LOAD_GAME message");
        assertNotNull(alfredMessages.get(0).game, "Alfred's LOAD_GAME message did not contain a game");



        //watch game with active player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;


        //expect load board for james & Text message for bob
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult = jamesExecutor.submit(new GetServerMessages(1,
                jamesClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1,
                bobClient, readyLatch));

        //get messages
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        try{
            jamesMessages = jamesResult.get(waitTime*2, TimeUnit.SECONDS);
            bobMessages = bobResult.get(waitTime*2, TimeUnit.SECONDS);
        } catch (TimeoutException ignore) {
        }

        //check james messages
        assertEquals(1, jamesMessages.size(), "James did not get a message");
        assertEquals(TestModels.TestServerMessageType.LOAD_GAME, jamesMessages.get(0).serverMessageType, "James' message was not a LOAD_GAME message");
        assertNotNull(jamesMessages.get(0).game, "James LOAD_GAME message did not contain a game");

        //check bob messages
        assertEquals(1, bobMessages.size(), "Bob did not get a message");
        assertEquals(TestModels.TestServerMessageType.NOTIFICATION, bobMessages.get(0).serverMessageType, "Bob's message was not a NOTIFICATION message");
        assertNotNull(bobMessages.get(0).message, "Bob's NOTIFICATION message did not contain a message");
    }

    @Test
    @Order(4)
    public void multipleJoinObserver() throws InterruptedException, ExecutionException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch)).get(waitTime, TimeUnit.SECONDS);

        //have james join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as an observer to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult = jamesExecutor.submit(
                new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult = bobExecutor.submit(
                new GetServerMessages(1, bobClient, readyLatch));
        jamesResult.get(2*waitTime, TimeUnit.SECONDS);
        bobResult.get(2*waitTime, TimeUnit.SECONDS);

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
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult = alfredExecutor.submit(
                new GetServerMessages(1, alfredClient, readyLatch));

        //prep messages lists
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        try{
            bobMessages = bobResult.get(waitTime*3, TimeUnit.SECONDS);
            jamesMessages = jamesResult.get(waitTime*3, TimeUnit.SECONDS);
            alfredMessages = alfredResult.get(waitTime*3, TimeUnit.SECONDS);

        } catch (TimeoutException ignore) {
        }

        //check message contents
        //alfred gets Load Game, everyone else gets notification
        assertEquals(1, bobMessages.size(), "Bob did not get a message");
        assertEquals(TestModels.TestServerMessageType.NOTIFICATION, bobMessages.get(0).serverMessageType, "Bob's message was not a NOTIFICATION message");
        assertNotNull(bobMessages.get(0).message, "Bob's NOTIFICATION message did not contain a message");

        assertEquals(1, jamesMessages.size(), "James did not get a message");
        assertEquals(TestModels.TestServerMessageType.NOTIFICATION, jamesMessages.get(0).serverMessageType, "James' message was not a NOTIFICATION message");
        assertNotNull(jamesMessages.get(0).message, "James' NOTIFICATION message didn't contain a message");

        assertEquals(1, alfredMessages.size(), "Alfred did not get a message");
        assertEquals(TestModels.TestServerMessageType.LOAD_GAME, alfredMessages.get(0).serverMessageType, "Alfred's message was not a LOAD_GAME message");
        assertNotNull(alfredMessages.get(0).game, "Alfred's LOAD_GAME message did not contain a message");
    }

    @Test
    @Order(5)
    public void validMove() throws InterruptedException, ExecutionException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch)).get(waitTime, TimeUnit.SECONDS);

        //have james join as a player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.playerColor = BLACK;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as a player to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult = jamesExecutor.submit(
                new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult = bobExecutor.submit(
                new GetServerMessages(1, bobClient, readyLatch));
        bobResult.get(2*waitTime, TimeUnit.SECONDS);
        jamesResult.get(2*waitTime, TimeUnit.SECONDS);


        //have alfred join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //wait for alfred joining as an observer to go through
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult = alfredExecutor.submit(
                new GetServerMessages(1, alfredClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult.get(3*waitTime, TimeUnit.SECONDS);
        bobResult.get(3*waitTime, TimeUnit.SECONDS);
        jamesResult.get(3*waitTime, TimeUnit.SECONDS);


        //have bob make a move
        //create pawn move
        ChessPosition startingPosition = TestFactory
                .getNewPosition(2, 5);
        ChessPosition endingPosition = TestFactory
                .getNewPosition(3, 5);
        ChessMove move = TestFactory
                .getNewMove(startingPosition, endingPosition, null);

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
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(2, jamesClient, readyLatch));
        alfredResult = alfredExecutor.submit(new GetServerMessages(2, alfredClient, readyLatch));

        //prep messages lists
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        try{
            bobMessages = bobResult.get(waitTime*3, TimeUnit.SECONDS);
            jamesMessages = jamesResult.get(waitTime*3, TimeUnit.SECONDS);
            alfredMessages = alfredResult.get(waitTime*3, TimeUnit.SECONDS);

        } catch (TimeoutException ignore) {
        }

        //check message contents
        //bob should get load game, everyone else should get load game & notification
        assertEquals(1, bobMessages.size(), "Bob did not get a message");
        assertEquals(TestModels.TestServerMessageType.LOAD_GAME, bobMessages.get(0).serverMessageType, "Bob's message was not a LOAD_GAME message");
        assertNotNull(bobMessages.get(0).game, "Bob's LOAD_GAME message did not contain a game");

        assertEquals(2, jamesMessages.size(), "Expected 2 messages for James, got " + jamesMessages.size());
        boolean isLoadGameFirst = jamesMessages.get(0).serverMessageType == TestModels.TestServerMessageType.LOAD_GAME;
        assertEquals(TestModels.TestServerMessageType.LOAD_GAME, jamesMessages.get(isLoadGameFirst?0:1).serverMessageType, "Didn't get load game message");
        assertNotNull(jamesMessages.get(isLoadGameFirst?0:1).game, "James LOAD_GAME message didn't contain a game");
        assertEquals(TestModels.TestServerMessageType.NOTIFICATION, jamesMessages.get(isLoadGameFirst?1:0).serverMessageType, "Didn't get notification");
        assertNotNull(jamesMessages.get(isLoadGameFirst?1:0).message, "James NOTIFICATION message didn't contain a message");

        assertEquals(2, alfredMessages.size(), "Expected 2 messages for Alfred, got " + alfredMessages.size());
        isLoadGameFirst = alfredMessages.get(0).serverMessageType == TestModels.TestServerMessageType.LOAD_GAME;
        assertEquals(TestModels.TestServerMessageType.LOAD_GAME, alfredMessages.get(isLoadGameFirst?0:1).serverMessageType, "Didn't get load game message");
        assertNotNull(alfredMessages.get(isLoadGameFirst?0:1).game, "Alfred's LOAD_GAME message didn't contain a game");
        assertEquals(TestModels.TestServerMessageType.NOTIFICATION, alfredMessages.get(isLoadGameFirst?1:0).serverMessageType, "Didn't get notification");
        assertNotNull(alfredMessages.get(isLoadGameFirst?1:0).message, "Alfred's NOTIFICATION message didn't contain a message");
    }

    @Test
    @Order(6)
    public void invalidMove() throws ExecutionException, InterruptedException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch)).get(waitTime, TimeUnit.SECONDS);

        //have james join as a player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.playerColor = BLACK;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as a player to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult = jamesExecutor.submit(
                new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult = bobExecutor.submit(
                new GetServerMessages(1, bobClient, readyLatch));
        bobResult.get(2*waitTime, TimeUnit.SECONDS);
        jamesResult.get(2*waitTime, TimeUnit.SECONDS);


        //have alfred join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //wait for alfred joining as an observer to go through
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<
                List<TestModels.TestMessage>> alfredResult = alfredExecutor.submit(
                new GetServerMessages(1, alfredClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult.get(3*waitTime, TimeUnit.SECONDS);
        bobResult.get(3*waitTime, TimeUnit.SECONDS);
        jamesResult.get(3*waitTime, TimeUnit.SECONDS);

        //have bob make an invalid move
        //try move rook
        ChessPosition startingPosition = TestFactory
                .getNewPosition(1, 1);
        ChessPosition endingPosition = TestFactory
                .getNewPosition(1, 5);
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
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(0, jamesClient, readyLatch));
        alfredResult = alfredExecutor.submit(new GetServerMessages(0, alfredClient, readyLatch));

        //prep messages lists
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        //only bob should get a message
        try{
            bobMessages = bobResult.get(waitTime*3, TimeUnit.SECONDS);
            jamesMessages = jamesResult.get(waitTime*3, TimeUnit.SECONDS);
            alfredMessages = alfredResult.get(waitTime*3, TimeUnit.SECONDS);

        } catch (TimeoutException ignore) {
        }

        //check message contents
        //bob should have gotten error message
        assertEquals(1, bobMessages.size(), "Didn't get a message for Bob");
        assertEquals(TestModels.TestServerMessageType.ERROR, bobMessages.get(0).serverMessageType, "Bob's message wasn't an error message");

        //everyone else should have gotten nothing
        assertTrue(jamesMessages.isEmpty(), "James got a message after leaving the game");
        assertTrue(alfredMessages.isEmpty(), "alfred got a message after leaving the game");
    }

    @Test
    @Order(7)
    public void validResign() throws ExecutionException, InterruptedException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch)).get(waitTime, TimeUnit.SECONDS);

        //have james join as a player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.playerColor = BLACK;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as a player to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult = jamesExecutor.submit(
                new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult = bobExecutor.submit(
                new GetServerMessages(1, bobClient, readyLatch));
        bobResult.get(2*waitTime, TimeUnit.SECONDS);
        jamesResult.get(2*waitTime, TimeUnit.SECONDS);


        //have alfred join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //wait for alfred joining as an observer to go through
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult = alfredExecutor.submit(
                new GetServerMessages(1, alfredClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult.get(3*waitTime, TimeUnit.SECONDS);
        bobResult.get(3*waitTime, TimeUnit.SECONDS);
        jamesResult.get(3*waitTime, TimeUnit.SECONDS);

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
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult = alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch));

        //prep messages lists
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        //only bob should get a message
        try{
            bobMessages = bobResult.get(waitTime*3, TimeUnit.SECONDS);
            jamesMessages = jamesResult.get(waitTime*3, TimeUnit.SECONDS);
            alfredMessages = alfredResult.get(waitTime*3, TimeUnit.SECONDS);

        } catch (TimeoutException ignore) {
        }

        //check message contents
        //everyone should have gotten a single NOTIFICATION
        assertEquals(1, bobMessages.size(), "Bob didn't get a message");
        assertEquals(TestModels.TestServerMessageType.NOTIFICATION, bobMessages.get(0).serverMessageType, "Bob didn't get NOTIFICATION message");

        assertEquals(1, jamesMessages.size(), "James didn't get a message");
        assertEquals(TestModels.TestServerMessageType.NOTIFICATION, jamesMessages.get(0).serverMessageType, "James didn't get NOTIFICATION message");

        assertEquals(1, alfredMessages.size(), "Alfred didn't get a message");
        assertEquals(TestModels.TestServerMessageType.NOTIFICATION, alfredMessages.get(0).serverMessageType, "Alfred didn't get NOTIFICATION message");
    }

    @Test
    @Order(8)
    public void invalidResign()  throws ExecutionException, InterruptedException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch)).get(waitTime, TimeUnit.SECONDS);

        //have james join as a player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.playerColor = BLACK;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as a player to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult = jamesExecutor.submit(
                new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult = bobExecutor.submit(
                new GetServerMessages(1, bobClient, readyLatch));
        bobResult.get(2*waitTime, TimeUnit.SECONDS);
        jamesResult.get(2*waitTime, TimeUnit.SECONDS);


        //have alfred join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //wait for alfred joining as an observer to go through
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult = alfredExecutor.submit(
                new GetServerMessages(1, alfredClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult.get(3*waitTime, TimeUnit.SECONDS);
        bobResult.get(3*waitTime, TimeUnit.SECONDS);
        jamesResult.get(3*waitTime, TimeUnit.SECONDS);

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
        bobResult = bobExecutor.submit(new GetServerMessages(0, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(0, jamesClient, readyLatch));
        alfredResult = alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch));

        //prep messages lists
        List<TestModels.TestMessage> bobMessages = new ArrayList<>();
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        //only alfred should get a message
        try{
            bobMessages = bobResult.get(waitTime*3, TimeUnit.SECONDS);
            jamesMessages = jamesResult.get(waitTime*3, TimeUnit.SECONDS);
            alfredMessages = alfredResult.get(waitTime*3, TimeUnit.SECONDS);

        } catch (TimeoutException ignore) {
        }

        //check message contents
        //bob should have gotten error message
        assertEquals(1, alfredMessages.size(), "Alfred didn't get a message");
        assertEquals(TestModels.TestServerMessageType.ERROR, alfredMessages.get(0).serverMessageType, "Alfred didn't get an ERROR message");

        //everyone else should have gotten nothing
        assertTrue(jamesMessages.isEmpty(), "James got a message after leaving the game");
        assertTrue(bobMessages.isEmpty(), "Bob got a message after leaving the game");
    }

    @Test
    @Order(9)
    public void leaveGame()  throws ExecutionException, InterruptedException, TimeoutException {
        //have bob join the game as a player
        TestModels.TestCommand joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.authToken = bobAuth;
        joinCommand.playerColor = WHITE;
        joinCommand.gameID = fullGame;

        //wait for bob joining as a player to go through
        bobClient.connect();
        CountDownLatch readyLatch = new CountDownLatch(1);
        testExecutor.submit(bobClient.getSendMessageRunnable(joinCommand, readyLatch));
        bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch)).get(waitTime, TimeUnit.SECONDS);

        //have james join as a player
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_PLAYER;
        joinCommand.playerColor = BLACK;
        joinCommand.authToken = jamesAuth;
        joinCommand.gameID = fullGame;

        //wait for james joining as a player to go through
        jamesClient.connect();
        readyLatch = new CountDownLatch(2);
        testExecutor.submit(jamesClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> jamesResult = jamesExecutor.submit(
                new GetServerMessages(1, jamesClient, readyLatch));
        Future<List<TestModels.TestMessage>> bobResult = bobExecutor.submit(
                new GetServerMessages(1, bobClient, readyLatch));
        bobResult.get(2*waitTime, TimeUnit.SECONDS);
        jamesResult.get(2*waitTime, TimeUnit.SECONDS);


        //have alfred join as an observer
        joinCommand = new TestModels.TestCommand();
        joinCommand.commandType = TestModels.TestCommandType.JOIN_OBSERVER;
        joinCommand.authToken = alfredAuth;
        joinCommand.gameID = fullGame;


        //wait for alfred joining as an observer to go through
        alfredClient.connect();
        readyLatch = new CountDownLatch(3);
        testExecutor.submit(alfredClient.getSendMessageRunnable(joinCommand, readyLatch));
        Future<List<TestModels.TestMessage>> alfredResult = alfredExecutor.submit(
                new GetServerMessages(1, alfredClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(1, bobClient, readyLatch));
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult.get(3*waitTime, TimeUnit.SECONDS);
        bobResult.get(3*waitTime, TimeUnit.SECONDS);
        jamesResult.get(3*waitTime, TimeUnit.SECONDS);


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
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        alfredResult = alfredExecutor.submit(new GetServerMessages(1, alfredClient, readyLatch));

        //prep messages lists
        List<TestModels.TestMessage> jamesMessages = new ArrayList<>();
        List<TestModels.TestMessage> alfredMessages = new ArrayList<>();

        //wait to get all messages
        //Bob can get message if you want, but probably not. Everyone else needs to get notified
        try{
            jamesMessages = jamesResult.get(waitTime*3, TimeUnit.SECONDS);
            alfredMessages = alfredResult.get(waitTime*3, TimeUnit.SECONDS);

        } catch (TimeoutException ignore) {
        }

        //check message contents
        assertEquals(1, alfredMessages.size(), "Alfred didn't get a message");
        assertEquals(TestModels.TestServerMessageType.NOTIFICATION, alfredMessages.get(0).serverMessageType, "Alfred didn't get a NOTIFICATION message");

        //check message contents
        //bob should have gotten error message
        assertEquals(1, jamesMessages.size(), "James didn't get a message");
        assertEquals(TestModels.TestServerMessageType.NOTIFICATION, jamesMessages.get(0).serverMessageType, "James didn't get NOTIFICATION message");


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
        jamesResult = jamesExecutor.submit(new GetServerMessages(1, jamesClient, readyLatch));
        bobResult = bobExecutor.submit(new GetServerMessages(0, bobClient, readyLatch));

        //wait to get all messages
        //Bob should not get message (he already left). James must get notified
        try{
            jamesMessages = jamesResult.get(waitTime*3, TimeUnit.SECONDS);
            bobMessages = bobResult.get(waitTime*2, TimeUnit.SECONDS);

        } catch (TimeoutException ignore) {
        }


        //try message contents
        //james should have been notified
        assertEquals(1, jamesMessages.size(), "James didn't get a message");
        assertEquals(TestModels.TestServerMessageType.NOTIFICATION, jamesMessages.get(0).serverMessageType, "James didn't get a NOTIFICATION message");

        //bob should have gotten nothing
        assertTrue(bobMessages.isEmpty(), "Bob got a message after leaving the game");
    }


    //helper code
    //------------------------------------------------------------------------------------------------------------------
    public static class GetServerMessages implements Callable<List<TestModels.TestMessage>>,
            TestClient.TestListener {

        Integer numMessagesExpected;
        List<TestModels.TestMessage> messages;
        CountDownLatch latch;
        CountDownLatch readyLatch;
        public GetServerMessages(Integer numMessagesExpected, TestClient socket,
                                 CountDownLatch readyLatch) {
            this.numMessagesExpected = numMessagesExpected;
            messages = new ArrayList<>();
            socket.registerListener(this);
            this.readyLatch = readyLatch;
        }

        @Override
        public List<TestModels.TestMessage> call() throws Exception {
            latch = new CountDownLatch(numMessagesExpected);
            readyLatch.countDown();
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
