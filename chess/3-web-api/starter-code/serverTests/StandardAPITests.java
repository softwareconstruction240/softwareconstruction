package passoffTests.serverTests;

import passoffTests.testClasses.TestModels;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.TestFactory;
import org.junit.jupiter.api.*;

import java.util.*;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StandardAPITests {
    static TestModels.TestUser existingUser;
    static TestModels.TestUser newUser;
    static TestModels.TestCreateRequest createRequest;
    static TestServerFacade serverFacade;
    static String existingAuth;

    @BeforeAll
    static void init(){
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
    public void setup(){
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
    public void successLogin(){
        TestModels.TestLoginRequest loginRequest = new TestModels.TestLoginRequest();
        loginRequest.username = existingUser.username;
        loginRequest.password = existingUser.password;

        TestModels.TestLoginRegisterResult loginResult = serverFacade.login(loginRequest);

        assertEquals(200, serverFacade.getStatusCode());
        assertTrue(loginResult.success, "Response returned not successful");
        assertFalse(loginResult.message != null
                        && loginResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Response returned error message");
        assertEquals(existingUser.username, loginResult.username,
                "Response did not give the same username as user");
        assertNotNull(loginResult.authToken, "Response did not return authentication String");
    }

    @Test
    @Order(2)
    @DisplayName("Login Invalid User")
    public void failLogin(){
        TestModels.TestLoginRequest loginRequest = new TestModels.TestLoginRequest();
        loginRequest.username = newUser.username;
        loginRequest.password = newUser.password;

        TestModels.TestLoginRegisterResult loginResult = serverFacade.login(loginRequest);

        assertEquals(401, serverFacade.getStatusCode());
        assertFalse(loginResult.success, "Response didn't return not successful");
        assertTrue(loginResult.message.toLowerCase(Locale.ROOT).contains("error"), "Response missing error message");
        assertNull(loginResult.username, "Response incorrectly returned username");
        assertNull(loginResult.authToken, "Response incorrectly return authentication String");
    }

    @Test
    @Order(3)
    @DisplayName("Normal User Registration")
    public void successRegister(){
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = newUser.username;
        registerRequest.password = newUser.password;
        registerRequest.email = newUser.email;

        //submit register request
        TestModels.TestLoginRegisterResult registerResult = serverFacade.register(registerRequest);

        assertEquals(200, serverFacade.getStatusCode());
        assertTrue(registerResult.success, "server.Server did not say registration was successful for new user.");
        assertFalse(registerResult.message != null && registerResult.message.toLowerCase(Locale.ROOT).contains("error"), "Response gave an error message");
        assertEquals(newUser.username, registerResult.username, "Response did not have the same username as was registered");
        assertNotNull(registerResult.authToken, "Response did not contain an authentication string");
    }

    @Test
    @Order(4)
    @DisplayName("Register Existing User")
    public void failRegister(){
        //create request trying to register existing user
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = existingUser.username;
        registerRequest.password = existingUser.password;
        registerRequest.email = existingUser.email;

        //submit register request
        TestModels.TestLoginRegisterResult loginResult = serverFacade.register(registerRequest);

        assertEquals(403, serverFacade.getStatusCode());
        assertFalse(loginResult.success, "Response didn't return not successful");
        assertTrue(loginResult.message.toLowerCase(Locale.ROOT).contains("error"), "Response missing error message");
        assertNull(loginResult.username, "Response incorrectly contained username");
        assertNull(loginResult.authToken, "Response incorrectly contained authentication string");
    }

    @Test
    @Order(5)
    @DisplayName("Normal Logout")
    public void successLogout(){
        //log out existing user
        TestModels.TestResult result = serverFacade.logout(existingAuth);

        assertEquals(200, serverFacade.getStatusCode());
        assertTrue(result.success, "Response didn't return successful");
        assertFalse(result.message != null && result.message.toLowerCase(Locale.ROOT).contains("error"), "Response gave an error message");
    }

    @Test
    @Order(6)
    @DisplayName("Invalid Auth Logout")
    public void failLogout(){
        //log out user twice
        //second logout should fail
        serverFacade.logout(existingAuth);
        TestModels.TestResult result = serverFacade.logout(existingAuth);

        assertEquals(401, serverFacade.getStatusCode());
        assertFalse(result.success, "Response didn't return not successful");
        assertTrue(result.message.toLowerCase(Locale.ROOT).contains("error"),
                "Response did not return error message");
    }

    @Test
    @Order(7)
    @DisplayName("Valid Creation")
    public void goodCreate(){
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        assertEquals(200, serverFacade.getStatusCode());
        assertTrue(createResult.success, "Result did not return successful");
        assertFalse(createResult.message != null
                        && createResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");
        assertNotNull(createResult.gameID, "Result did not return a game ID");
        assertTrue(createResult.gameID > 0, "Result returned invalid game ID");
    }

    @Test
    @Order(8)
    @DisplayName("Create with Bad Auth")
    public void badAuthCreate(){
         //log out user so auth is invalid
        serverFacade.logout(existingAuth);

        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        assertEquals(401, serverFacade.getStatusCode());
        assertFalse(createResult.success, "Bad result didn't return not successful");
        assertTrue(createResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Bad result did not return an error message");
        assertNull(createResult.gameID, "Bad result returned a game ID");
    }

    @Test
    @Order(9)
    @DisplayName("Watch game as Observer")
    public void goodWatch(){
        //create game
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //make watch request
        TestModels.TestJoinRequest watchRequest = new TestModels.TestJoinRequest();
        watchRequest.gameID = createResult.gameID;

        //try watch
        TestModels.TestResult watchResult = serverFacade.verifyJoinPlayer(watchRequest, existingAuth);

        //check succeeded
        assertEquals(200, serverFacade.getStatusCode());
        assertTrue(watchResult.success, "Request returned not successful");
        assertFalse(watchResult.message != null
                        && watchResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");
    }

    @Test
    @Order(10)
    @DisplayName("Watch with bad Authentication")
    public void badAuthWatch(){
        //create game
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //make watch request
        TestModels.TestJoinRequest watchRequest = new TestModels.TestJoinRequest();
        watchRequest.gameID = createResult.gameID;

        //try watch
        TestModels.TestResult watchResult = serverFacade.verifyJoinPlayer(watchRequest, existingAuth+"bad stuff");

        //check failed
        assertEquals(401, serverFacade.getStatusCode());
        assertFalse(watchResult.success, "Request didn't return not successful");
        assertTrue(watchResult.message != null
                        && watchResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Invalid Request didn't return an error message");
    }

    @Test
    @Order(11)
    @DisplayName("Watch bad game ID")
    public void badGameIDWatch(){
        //create game
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //make watch request
        TestModels.TestJoinRequest watchRequest = new TestModels.TestJoinRequest();
        watchRequest.gameID = 0;

        //try watch
        TestModels.TestResult watchResult = serverFacade.verifyJoinPlayer(watchRequest, existingAuth);

        //check failed
        assertEquals(400, serverFacade.getStatusCode());
        assertFalse(watchResult.success, "Request didn't return not successful");
        assertTrue(watchResult.message != null
                        && watchResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Invalid Request didn't return an error message");
    }

    @Test
    @Order(12)
    @DisplayName("Many Watchers")
    public void manyWatch() {
        //create game
        TestModels.TestCreateRequest createRequest = new TestModels.TestCreateRequest();
        createRequest.gameName = "Test Game";
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //make watch request
        TestModels.TestJoinRequest watchRequest = new TestModels.TestJoinRequest();
        watchRequest.gameID = createResult.gameID;

        //try watch
        TestModels.TestResult watchResult = serverFacade.verifyJoinPlayer(watchRequest, existingAuth);

        //check succeeded
        assertEquals(200, serverFacade.getStatusCode());
        assertTrue(watchResult.success, "Request returned not successful");
        assertFalse(watchResult.message != null
                        && watchResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");


        //next watcher
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = "a";
        registerRequest.password = "A";
        registerRequest.email = "a.A";
        TestModels.TestLoginRegisterResult registerResult = serverFacade.register(registerRequest);
        watchResult = serverFacade.verifyJoinPlayer(watchRequest, registerResult.authToken);

        //check succeeded
        assertEquals(200, serverFacade.getStatusCode());
        assertTrue(watchResult.success, "Request returned not successful");
        assertFalse(watchResult.message != null
                        && watchResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");


        //next watcher
        registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = "b";
        registerRequest.password = "B";
        registerRequest.email = "b.B";
        registerResult = serverFacade.register(registerRequest);
        watchResult = serverFacade.verifyJoinPlayer(watchRequest, registerResult.authToken);

        //check succeeded
        assertEquals(200, serverFacade.getStatusCode());
        assertTrue(watchResult.success, "Request returned not successful");
        assertFalse(watchResult.message != null
                        && watchResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");


        //next watcher
        registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = "c";
        registerRequest.password = "C";
        registerRequest.email = "c.C";
        registerResult = serverFacade.register(registerRequest);
        watchResult = serverFacade.verifyJoinPlayer(watchRequest, registerResult.authToken);

        //check succeeded
        assertEquals(200, serverFacade.getStatusCode());
        assertTrue(watchResult.success, "Request returned not successful");
        assertFalse(watchResult.message != null
                        && watchResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");
    }

    @Test
    @Order(13)
    @DisplayName("Join created game")
    public void goodJoin(){
        //create game
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //join as white
        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = WHITE;

        //try join
        TestModels.TestResult joinResult = serverFacade.verifyJoinPlayer(joinRequest, existingAuth);

        //check
        assertEquals(200, serverFacade.getStatusCode());
        assertTrue(joinResult.success, "Request returned not successful");
        assertFalse(joinResult.message != null
                        && joinResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");
    }

    @Test
    @Order(14)
    @DisplayName("Join with bad Authentication")
    public void badAuthJoin(){
        //create game
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //join as white
        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = WHITE;

        //try join
        TestModels.TestResult joinResult = serverFacade.verifyJoinPlayer(joinRequest, existingAuth + "bad stuff");

        //check
        assertEquals(401, serverFacade.getStatusCode());
        assertFalse(joinResult.success, "Request didn't return not successful");
        assertTrue(joinResult.message != null
                        && joinResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Invalid Request didn't return an error message");
    }

    @Test
    @Order(15)
    @DisplayName("Join with bad team color")
    public void badColorJoin(){
        //create game
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //add existing user as black
        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = BLACK;
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
        joinRequest.playerColor = BLACK;
        TestModels.TestResult joinResult = serverFacade.verifyJoinPlayer(joinRequest, registerResult.authToken);

        //check failed
        assertEquals(403, serverFacade.getStatusCode());
        assertFalse(joinResult.success, "Request didn't return not successful");
        assertTrue(joinResult.message != null
                        && joinResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Invalid Request didn't return an error message");
    }

    @Test
    @Order(16)
    @DisplayName("Join nonexistent game")
    public void badGameIDJoin(){
        //create game
        TestModels.TestCreateRequest createRequest = new TestModels.TestCreateRequest();
        TestModels.TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //join as white
        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        joinRequest.gameID = 0;
        joinRequest.playerColor = WHITE;

        //try join
        TestModels.TestResult joinResult = serverFacade.verifyJoinPlayer(joinRequest, existingAuth);

        //check
        assertEquals(400, serverFacade.getStatusCode());
        assertFalse(joinResult.success, "Request didn't return not successful");
        assertTrue(joinResult.message != null
                        && joinResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Invalid Request didn't return an error message");
    }

    @Test
    @Order(17)
    @DisplayName("List no games")
    public void noGamesList(){
        TestModels.TestListResult result = serverFacade.listGames(existingAuth);

        assertEquals(200, serverFacade.getStatusCode());
        assertTrue(result.success, "Result returned not successful.");
        assertTrue(result.games == null || result.games.length == 0,
                "Found games when none should be there");
        assertFalse(result.message != null
                        && result.message.toLowerCase(Locale.ROOT).contains("error"),
                "Result returned an error message");
    }

    @Test
    @Order(18)
    @DisplayName("List games")
    public void gamesList(){
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
        joinRequest.playerColor = BLACK;
        joinRequest.gameID = game1.gameID;
        serverFacade.verifyJoinPlayer(joinRequest, userA.authToken);

        //B join game 2 as white
        joinRequest.playerColor = WHITE;
        joinRequest.gameID = game2.gameID;
        serverFacade.verifyJoinPlayer(joinRequest, userB.authToken);

        //C join game 3 as white
        joinRequest.playerColor = WHITE;
        joinRequest.gameID = game3.gameID;
        serverFacade.verifyJoinPlayer(joinRequest, userC.authToken);

        //A join game3 as black
        joinRequest.playerColor = BLACK;
        joinRequest.gameID = game3.gameID;
        serverFacade.verifyJoinPlayer(joinRequest, userA.authToken);

        //C play self in game 4
        joinRequest.playerColor = WHITE;
        joinRequest.gameID = game4.gameID;
        serverFacade.verifyJoinPlayer(joinRequest, userC.authToken);
        joinRequest.playerColor = BLACK;
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
        assertEquals(200, serverFacade.getStatusCode());
        Collection<TestModels.TestListResult.TestListEntry> returnedList = new HashSet<>(Arrays.asList(listResult.games));

        //check
        assertEquals(expectedList, returnedList, "Returned Games list was incorrect");
    }

    @Test
    @Order(19)
    @DisplayName("Clear Test")
    public void clearData(){
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
        joinRequest.playerColor = WHITE;
        serverFacade.verifyJoinPlayer(joinRequest, registerResult.authToken);

        //do clear
        TestModels.TestResult clearResult = serverFacade.clear();

        //test clear successful
        assertEquals(200, serverFacade.getStatusCode());
        assertTrue(clearResult.success, "Clear Response came back not successful");
        assertFalse(clearResult.message != null
                        && clearResult.message.toLowerCase(Locale.ROOT).contains("error"),
                "Clear Result returned an error message");

        //make sure neither user can log in
        //first user
        TestModels.TestLoginRequest loginRequest = new TestModels.TestLoginRequest();
        loginRequest.username = existingUser.username;
        loginRequest.password = existingUser.password;
        TestModels.TestLoginRegisterResult loginResult = serverFacade.login(loginRequest);
        assertEquals(401, serverFacade.getStatusCode());
        assertFalse(loginResult.success, "User was still able to log in");

        //second user
        loginRequest.username = "Spongebob";
        loginRequest.password = "Squarepants";
        loginResult = serverFacade.login(loginRequest);
        assertEquals(401, serverFacade.getStatusCode());
        assertFalse(loginResult.success, "User was still able to log in");

        //try to use old auth token to list games
        TestModels.TestListResult listResult = serverFacade.listGames(existingAuth);
        assertEquals(401, serverFacade.getStatusCode());
        assertFalse(listResult.success, "List Result returned successful using old authToken");

        //log in new user and check that list is empty
        registerResult = serverFacade.register(registerRequest);
        listResult = serverFacade.listGames(registerResult.authToken);

        //check listResult
        assertEquals(200, serverFacade.getStatusCode());
        assertTrue(listResult.success, "List Result didn't return successful");
        assertEquals(0, listResult.games.length, "list result did not return 0 games after clear");
    }

    @Test
    @Order(20)
    @DisplayName("Multiple Clears")
    public void multipleClear(){

        //clear multiple times
        serverFacade.clear();
        serverFacade.clear();
        TestModels.TestResult result = serverFacade.clear();

        //make sure returned good
        assertEquals(200, serverFacade.getStatusCode());
        assertTrue(result.success, "Clear Response came back not successful");
        assertFalse(result.message != null
                        && result.message.toLowerCase(Locale.ROOT).contains("error"),
                "Clear Result returned an error message");
    }
}

