package passoffTests.serverTests;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import passoffTests.TestFactory;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestModels;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

@SuppressWarnings("unused")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StandardAPITests {
    private static final int HTTP_OK = 200;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_FORBIDDEN = 403;
    
    private static TestModels.TestUser existingUser;
    private static TestModels.TestUser newUser;
    private static TestModels.TestCreateRequest createRequest;
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

        createRequest = new TestModels.TestCreateRequest();
        createRequest.gameName = "testGame";

        serverFacade = new TestServerFacade("localhost", TestFactory.getServerPort());
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


    @Test
    @Order(1)
    @DisplayName("Normal User Login")
    public void successLogin() {
        TestModels.TestLoginRequest loginRequest = new TestModels.TestLoginRequest();
        loginRequest.username = existingUser.username;
        loginRequest.password = existingUser.password;

        TestModels.TestLoginRegisterResult loginResult = serverFacade.login(loginRequest);

        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertTrue(loginResult.success, "Response returned not successful");
        Assertions.assertFalse(
                loginResult.message != null && loginResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Response returned error message");
        Assertions.assertEquals(existingUser.username, loginResult.username,
                "Response did not give the same username as user");
        Assertions.assertNotNull(loginResult.authToken, "Response did not return authentication String");
    }


    @Test
    @Order(2)
    @DisplayName("Login Invalid User")
    public void loginInvalidUser() {
        TestModels.TestLoginRequest loginRequest = new TestModels.TestLoginRequest();
        loginRequest.username = newUser.username;
        loginRequest.password = newUser.password;

        TestModels.TestLoginRegisterResult loginResult = serverFacade.login(loginRequest);

        Assertions.assertEquals(HTTP_UNAUTHORIZED, serverFacade.getStatusCode(),
                "Server response code was not 401 Unauthorized");
        Assertions.assertFalse(loginResult.success, "Response didn't return not successful");
        Assertions.assertTrue(loginResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Response missing error message");
        Assertions.assertNull(loginResult.username, "Response incorrectly returned username");
        Assertions.assertNull(loginResult.authToken, "Response incorrectly return authentication String");
    }

    @Test
    @Order(3)
    @DisplayName("Login Wrong Password")
    public void loginWrongPassword() {
        TestModels.TestLoginRequest loginRequest = new TestModels.TestLoginRequest();
        loginRequest.username = existingUser.username;
        loginRequest.password = newUser.password;

        TestModels.TestLoginRegisterResult loginResult = serverFacade.login(loginRequest);

        Assertions.assertEquals(HTTP_UNAUTHORIZED, serverFacade.getStatusCode(),
                "Server response code was not 401 Unauthorized");
        Assertions.assertFalse(loginResult.success, "Response didn't return not successful");
        Assertions.assertTrue(loginResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Response missing error message");
        Assertions.assertNull(loginResult.username, "Response incorrectly returned username");
        Assertions.assertNull(loginResult.authToken, "Response incorrectly return authentication String");
    }


    @Test
    @Order(4)
    @DisplayName("Unique Authtoken Each Login")
    public void uniqueAuthorizationTokens() {
        TestModels.TestLoginRequest loginRequest = new TestModels.TestLoginRequest();
        loginRequest.username = existingUser.username;
        loginRequest.password = existingUser.password;

        TestModels.TestLoginRegisterResult loginOne = serverFacade.login(loginRequest);
        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertNotNull(loginOne.authToken, "Login result did not contain an authToken");

        TestModels.TestLoginRegisterResult loginTwo = serverFacade.login(loginRequest);
        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertNotNull(loginTwo.authToken, "Login result did not contain an authToken");

        Assertions.assertNotEquals(existingAuth, loginOne.authToken,
                "Authtoken returned by login matched authtoken from prior register");
        Assertions.assertNotEquals(existingAuth, loginTwo.authToken,
                "Authtoken returned by login matched authtoken from prior register");
        Assertions.assertNotEquals(loginOne.authToken, loginTwo.authToken,
                "Authtoken returned by login matched authtoken from prior login");
    }


    @Test
    @Order(5)
    @DisplayName("Normal User Registration")
    public void successRegister() {
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = newUser.username;
        registerRequest.password = newUser.password;
        registerRequest.email = newUser.email;

        //submit register request
        TestModels.TestLoginRegisterResult registerResult = serverFacade.register(registerRequest);

        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertTrue(registerResult.success,
                "server.Server did not say registration was successful for new user.");
        Assertions.assertFalse(
                registerResult.message != null && registerResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Response gave an error message");
        Assertions.assertEquals(newUser.username, registerResult.username,
                "Response did not have the same username as was registered");
        Assertions.assertNotNull(registerResult.authToken, "Response did not contain an authentication string");
    }


    @Test
    @Order(6)
    @DisplayName("Re-Register User")
    public void registerTwice() {
        //create request trying to register existing user
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = existingUser.username;
        registerRequest.password = existingUser.password;
        registerRequest.email = existingUser.email;

        //submit register request
        TestModels.TestLoginRegisterResult registerResult = serverFacade.register(registerRequest);

        Assertions.assertEquals(HTTP_FORBIDDEN, serverFacade.getStatusCode(),
                "Server response code was not 401 Forbidden");
        Assertions.assertFalse(registerResult.success, "Response didn't return not successful");
        Assertions.assertTrue(registerResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Response missing error message");
        Assertions.assertNull(registerResult.username, "Response incorrectly contained username");
        Assertions.assertNull(registerResult.authToken, "Response incorrectly contained authentication string");
    }


    @Test
    @Order(7)
    @DisplayName("Register Bad Request")
    public void failRegister() {
        //attempt to register a user without a password
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = newUser.username;
        registerRequest.password = null;
        registerRequest.email = newUser.email;

        TestModels.TestLoginRegisterResult registerResult = serverFacade.register(registerRequest);

        Assertions.assertEquals(HTTP_BAD_REQUEST, serverFacade.getStatusCode(),
                "Server response code was not 400 Bad Request");
        Assertions.assertFalse(registerResult.success, "Response didn't return not successful");
        Assertions.assertTrue(registerResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Response missing error message");
        Assertions.assertNull(registerResult.username, "Response incorrectly contained username");
        Assertions.assertNull(registerResult.authToken, "Response incorrectly contained authentication string");
    }


    @Test
    @Order(8)
    @DisplayName("Normal Logout")
    public void successLogout() {
        //log out existing user
        TestModels.TestResult result = serverFacade.logout(existingAuth);

        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertTrue(result.success, "Response didn't return successful");
        Assertions.assertFalse(result.message != null &&
                        result.message.toLowerCase(Locale.ROOT).contains("error"),
                "Response gave an error message");
    }


    @Test
    @Order(9)
    @DisplayName("Invalid Auth Logout")
    public void failLogout() {
        //log out user twice
        //second logout should fail
        serverFacade.logout(existingAuth);
        TestModels.TestResult result = serverFacade.logout(existingAuth);

        Assertions.assertEquals(HTTP_UNAUTHORIZED, serverFacade.getStatusCode(),
                "Server response code was not 401 Unauthorized");
        Assertions.assertFalse(result.success, "Response didn't return not successful");
        Assertions.assertTrue(result.message.toLowerCase(Locale.ROOT).contains("error"),
                "Response did not return error message");
    }


    @Test
    @Order(10)
    @DisplayName("Valid Creation")
    public void goodCreate() {
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertTrue(createResult.success, "Result did not return successful");
        Assertions.assertFalse(
                createResult.message != null && createResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");
        Assertions.assertNotNull(createResult.gameID, "Result did not return a game ID");
        Assertions.assertTrue(createResult.gameID > 0, "Result returned invalid game ID");
    }


    @Test
    @Order(11)
    @DisplayName("Create with Bad Authentication")
    public void badAuthCreate() {
        //log out user so auth is invalid
        serverFacade.logout(existingAuth);

        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        Assertions.assertEquals(HTTP_UNAUTHORIZED, serverFacade.getStatusCode(),
                "Server response code was not 401 Unauthorized");
        Assertions.assertFalse(createResult.success, "Bad result didn't return not successful");
        Assertions.assertTrue(createResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Bad result did not return an error message");
        Assertions.assertNull(createResult.gameID, "Bad result returned a game ID");
    }


    @Test
    @Order(12)
    @DisplayName("Watch Game")
    public void goodWatch() {
        //create game
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //make watch request
        TestModels.TestJoinRequest watchRequest = new TestModels.TestJoinRequest();
        watchRequest.gameID = createResult.gameID;

        //try watch
        TestModels.TestResult watchResult = serverFacade.verifyJoinPlayer(watchRequest, existingAuth);

        //check succeeded
        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertTrue(watchResult.success, "Request returned not successful");
        Assertions.assertFalse(
                watchResult.message != null && watchResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");


        TestModels.TestListResult listResult = serverFacade.listGames(existingAuth);
        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertEquals(1, listResult.games.length,
                "List Games returned an incorrect number of games");
        Assertions.assertNull(listResult.games[0].whiteUsername,
                "Player present on a game that no player joined");
        Assertions.assertNull(listResult.games[0].blackUsername,
                "Player present on a game that no player joined");
    }


    @Test
    @Order(13)
    @DisplayName("Watch Bad Authentication")
    public void badAuthWatch() {
        //create game
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //make watch request
        TestModels.TestJoinRequest watchRequest = new TestModels.TestJoinRequest();
        watchRequest.gameID = createResult.gameID;

        //try watch
        TestModels.TestResult watchResult = serverFacade.verifyJoinPlayer(watchRequest, existingAuth + "bad stuff");

        //check failed
        Assertions.assertEquals(HTTP_UNAUTHORIZED, serverFacade.getStatusCode(),
                "Server response code was not 401 Unauthorized");
        Assertions.assertFalse(watchResult.success, "Request didn't return not successful");
        Assertions.assertTrue(
                watchResult.message != null && watchResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Invalid Request didn't return an error message");
    }


    @Test
    @Order(14)
    @DisplayName("Watch Bad Game ID")
    public void badGameIDWatch() {
        //create game
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //make watch request
        TestModels.TestJoinRequest watchRequest = new TestModels.TestJoinRequest();
        watchRequest.gameID = 0;

        //try watch
        TestModels.TestResult watchResult = serverFacade.verifyJoinPlayer(watchRequest, existingAuth);

        //check failed
        Assertions.assertEquals(HTTP_BAD_REQUEST, serverFacade.getStatusCode(),
                "Server response code was not 400 Bad Request");
        Assertions.assertFalse(watchResult.success, "Request didn't return not successful");
        Assertions.assertTrue(
                watchResult.message != null && watchResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Invalid Request didn't return an error message");
    }


    @Test
    @Order(15)
    @DisplayName("Many Watchers")
    public void manyWatch() {
        //create game
        createRequest = new TestModels.TestCreateRequest();
        createRequest.gameName = "Test Game";
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //make watch request
        TestModels.TestJoinRequest watchRequest = new TestModels.TestJoinRequest();
        watchRequest.gameID = createResult.gameID;

        //try watch
        TestModels.TestResult watchResult = serverFacade.verifyJoinPlayer(watchRequest, existingAuth);

        //check succeeded
        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertTrue(watchResult.success, "Request returned not successful");
        Assertions.assertFalse(
                watchResult.message != null && watchResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");


        //next watcher
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = "a";
        registerRequest.password = "A";
        registerRequest.email = "a.A";
        TestModels.TestLoginRegisterResult registerResult = serverFacade.register(registerRequest);
        watchResult = serverFacade.verifyJoinPlayer(watchRequest, registerResult.authToken);

        //check succeeded
        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertTrue(watchResult.success, "Request returned not successful");
        Assertions.assertFalse(
                watchResult.message != null && watchResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");


        //next watcher
        registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = "b";
        registerRequest.password = "B";
        registerRequest.email = "b.B";
        registerResult = serverFacade.register(registerRequest);
        watchResult = serverFacade.verifyJoinPlayer(watchRequest, registerResult.authToken);

        //check succeeded
        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertTrue(watchResult.success, "Request returned not successful");
        Assertions.assertFalse(
                watchResult.message != null && watchResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");


        //next watcher
        registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = "c";
        registerRequest.password = "C";
        registerRequest.email = "c.C";
        registerResult = serverFacade.register(registerRequest);
        watchResult = serverFacade.verifyJoinPlayer(watchRequest, registerResult.authToken);

        //check succeeded
        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertTrue(watchResult.success, "Request returned not successful");
        Assertions.assertFalse(
                watchResult.message != null && watchResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");
    }


    @Test
    @Order(16)
    @DisplayName("Join Created Game")
    public void goodJoin() {
        //create game
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //join as white
        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = ChessGame.TeamColor.WHITE;

        //try join
        TestModels.TestResult joinResult = serverFacade.verifyJoinPlayer(joinRequest, existingAuth);

        //check
        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertTrue(joinResult.success, "Request returned not successful");
        Assertions.assertFalse(
                joinResult.message != null && joinResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");

        TestModels.TestListResult listResult = serverFacade.listGames(existingAuth);

        Assertions.assertEquals(1, listResult.games.length);
        Assertions.assertEquals(existingUser.username, listResult.games[0].whiteUsername);
        Assertions.assertNull(listResult.games[0].blackUsername);
    }


    @Test
    @Order(17)
    @DisplayName("Join Bad Authentication")
    public void badAuthJoin() {
        //create game
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //join as white
        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = ChessGame.TeamColor.WHITE;

        //try join
        TestModels.TestResult joinResult = serverFacade.verifyJoinPlayer(joinRequest, existingAuth + "bad stuff");

        //check
        Assertions.assertEquals(HTTP_UNAUTHORIZED, serverFacade.getStatusCode(),
                "Server response code was not 401 Unauthorized");
        Assertions.assertFalse(joinResult.success, "Request didn't return not successful");
        Assertions.assertTrue(
                joinResult.message != null && joinResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Invalid Request didn't return an error message");
    }


    @Test
    @Order(18)
    @DisplayName("Join Bad Team Color")
    public void badColorJoin() {
        //create game
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //add existing user as black
        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = ChessGame.TeamColor.BLACK;
        serverFacade.verifyJoinPlayer(joinRequest, existingAuth);

        //register second user
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = newUser.username;
        registerRequest.password = newUser.password;
        registerRequest.email = newUser.email;
        TestModels.TestLoginRegisterResult registerResult = serverFacade.register(registerRequest);

        //join request trying to also join  as black
        joinRequest = new TestModels.TestJoinRequest();
        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = ChessGame.TeamColor.BLACK;
        TestModels.TestResult joinResult = serverFacade.verifyJoinPlayer(joinRequest, registerResult.authToken);

        //check failed
        Assertions.assertEquals(HTTP_FORBIDDEN, serverFacade.getStatusCode(),
                "Server response code was not 401 Forbidden");
        Assertions.assertFalse(joinResult.success, "Request didn't return not successful");
        Assertions.assertTrue(
                joinResult.message != null && joinResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Invalid Request didn't return an error message");
    }


    @Test
    @Order(19)
    @DisplayName("Join Bad Game ID")
    public void badGameIDJoin() {
        //create game
        createRequest = new TestModels.TestCreateRequest();
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //join as white
        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        joinRequest.gameID = 0;
        joinRequest.playerColor = ChessGame.TeamColor.WHITE;

        //try join
        TestModels.TestResult joinResult = serverFacade.verifyJoinPlayer(joinRequest, existingAuth);

        //check
        Assertions.assertEquals(HTTP_BAD_REQUEST, serverFacade.getStatusCode(),
                "Server response code was not 400 Bad Request");
        Assertions.assertFalse(joinResult.success, "Request didn't return not successful");
        Assertions.assertTrue(
                joinResult.message != null && joinResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Invalid Request didn't return an error message");
    }


    @Test
    @Order(20)
    @DisplayName("List No Games")
    public void noGamesList() {
        TestModels.TestListResult result = serverFacade.listGames(existingAuth);

        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertTrue(result.success, "Result returned not successful.");
        Assertions.assertTrue(result.games == null || result.games.length == 0,
                "Found games when none should be there");
        Assertions.assertFalse(result.message != null && result.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");
    }


    @Test
    @Order(21)
    @DisplayName("List Multiple Games")
    public void gamesList() {
        //register a few users to create games
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = "a";
        registerRequest.password = "A";
        registerRequest.email = "a.A";
        TestModels.TestLoginRegisterResult userA = serverFacade.register(registerRequest);

        registerRequest.username = "b";
        registerRequest.password = "B";
        registerRequest.email = "b.B";
        TestModels.TestLoginRegisterResult userB = serverFacade.register(registerRequest);

        registerRequest.username = "c";
        registerRequest.password = "C";
        registerRequest.email = "c.C";
        TestModels.TestLoginRegisterResult userC = serverFacade.register(registerRequest);

        //create games

        //1 as black from A
        createRequest.gameName = "I'm numbah one!";
        TestModels.TestCreateResult game1 = serverFacade.createGame(createRequest, userA.authToken);

        //1 as white from B
        createRequest.gameName = "Lonely";
        TestModels.TestCreateResult game2 = serverFacade.createGame(createRequest, userB.authToken);

        //1 of each from C
        createRequest.gameName = "GG";
        TestModels.TestCreateResult game3 = serverFacade.createGame(createRequest, userC.authToken);
        createRequest.gameName = "All by myself";
        TestModels.TestCreateResult game4 = serverFacade.createGame(createRequest, userC.authToken);

        //A join game 1 as black
        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        joinRequest.playerColor = ChessGame.TeamColor.BLACK;
        joinRequest.gameID = game1.gameID;
        serverFacade.verifyJoinPlayer(joinRequest, userA.authToken);

        //B join game 2 as white
        joinRequest.playerColor = ChessGame.TeamColor.WHITE;
        joinRequest.gameID = game2.gameID;
        serverFacade.verifyJoinPlayer(joinRequest, userB.authToken);

        //C join game 3 as white
        joinRequest.playerColor = ChessGame.TeamColor.WHITE;
        joinRequest.gameID = game3.gameID;
        serverFacade.verifyJoinPlayer(joinRequest, userC.authToken);

        //A join game3 as black
        joinRequest.playerColor = ChessGame.TeamColor.BLACK;
        joinRequest.gameID = game3.gameID;
        serverFacade.verifyJoinPlayer(joinRequest, userA.authToken);

        //C play self in game 4
        joinRequest.playerColor = ChessGame.TeamColor.WHITE;
        joinRequest.gameID = game4.gameID;
        serverFacade.verifyJoinPlayer(joinRequest, userC.authToken);
        joinRequest.playerColor = ChessGame.TeamColor.BLACK;
        joinRequest.gameID = game4.gameID;
        serverFacade.verifyJoinPlayer(joinRequest, userC.authToken);

        //create expected entry items
        Collection<TestModels.TestListResult.TestListEntry> expectedList = new HashSet<>();

        //game 1
        TestModels.TestListResult.TestListEntry entry = new TestModels.TestListResult.TestListEntry();
        entry.gameID = game1.gameID;
        entry.gameName = "I'm numbah one!";
        entry.blackUsername = userA.username;
        entry.whiteUsername = null;
        expectedList.add(entry);

        //game 2
        entry = new TestModels.TestListResult.TestListEntry();
        entry.gameID = game2.gameID;
        entry.gameName = "Lonely";
        entry.blackUsername = null;
        entry.whiteUsername = userB.username;
        expectedList.add(entry);

        //game 3
        entry = new TestModels.TestListResult.TestListEntry();
        entry.gameID = game3.gameID;
        entry.gameName = "GG";
        entry.blackUsername = userA.username;
        entry.whiteUsername = userC.username;
        expectedList.add(entry);

        //game 4
        entry = new TestModels.TestListResult.TestListEntry();
        entry.gameID = game4.gameID;
        entry.gameName = "All by myself";
        entry.blackUsername = userC.username;
        entry.whiteUsername = userC.username;
        expectedList.add(entry);

        //list games
        TestModels.TestListResult listResult = serverFacade.listGames(existingAuth);
        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Collection<TestModels.TestListResult.TestListEntry> returnedList =
                new HashSet<>(Arrays.asList(listResult.games));

        //check
        Assertions.assertEquals(expectedList, returnedList, "Returned Games list was incorrect");
    }


    @Test
    @Order(22)
    @DisplayName("Clear Test")
    public void clearData() {
        //create filler games
        createRequest.gameName = "Mr. Meeseeks";
        serverFacade.createGame(createRequest, existingAuth);

        createRequest.gameName = "Awesome game";
        serverFacade.createGame(createRequest, existingAuth);

        //log in new user
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = "Spongebob";
        registerRequest.password = "Squarepants";
        registerRequest.email = "pineapple@under.sea";
        TestModels.TestLoginRegisterResult registerResult = serverFacade.register(registerRequest);

        //create and join game for new user
        createRequest.gameName = "Patrick";
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, registerResult.authToken);

        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = ChessGame.TeamColor.WHITE;
        serverFacade.verifyJoinPlayer(joinRequest, registerResult.authToken);

        //do clear
        TestModels.TestResult clearResult = serverFacade.clear();

        //test clear successful
        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertTrue(clearResult.success, "Clear Response came back not successful");
        Assertions.assertFalse(
                clearResult.message != null && clearResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Clear Result returned an error message");

        //make sure neither user can log in
        //first user
        TestModels.TestLoginRequest loginRequest = new TestModels.TestLoginRequest();
        loginRequest.username = existingUser.username;
        loginRequest.password = existingUser.password;
        TestModels.TestLoginRegisterResult loginResult = serverFacade.login(loginRequest);
        Assertions.assertEquals(HTTP_UNAUTHORIZED, serverFacade.getStatusCode(),
                "Server response code was not 401 Unauthorized");
        Assertions.assertFalse(loginResult.success, "User was still able to log in");

        //second user
        loginRequest.username = "Spongebob";
        loginRequest.password = "Squarepants";
        loginResult = serverFacade.login(loginRequest);
        Assertions.assertEquals(HTTP_UNAUTHORIZED, serverFacade.getStatusCode(),
                "Server response code was not 401 Unauthorized");
        Assertions.assertFalse(loginResult.success, "User was still able to log in");

        //try to use old auth token to list games
        TestModels.TestListResult listResult = serverFacade.listGames(existingAuth);
        Assertions.assertEquals(HTTP_UNAUTHORIZED, serverFacade.getStatusCode(),
                "Server response code was not 401 Unauthorized");
        Assertions.assertFalse(listResult.success, "List Result returned successful using old authToken");

        //log in new user and check that list is empty
        registerResult = serverFacade.register(registerRequest);
        listResult = serverFacade.listGames(registerResult.authToken);

        //check listResult
        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertTrue(listResult.success, "List Result didn't return successful");
        Assertions.assertEquals(0, listResult.games.length, "list result did not return 0 games after clear");
    }


    @Test
    @Order(23)
    @DisplayName("Multiple Clears")
    public void multipleClear() {

        //clear multiple times
        serverFacade.clear();
        serverFacade.clear();
        TestModels.TestResult result = serverFacade.clear();

        //make sure returned good
        Assertions.assertEquals(HTTP_OK, serverFacade.getStatusCode(), "Server response code was not 200 OK");
        Assertions.assertTrue(result.success, "Clear Response came back not successful");
        Assertions.assertFalse(result.message != null && result.message.toLowerCase(Locale.ROOT).contains("error"),
                "Clear Result returned an error message");
    }

}

