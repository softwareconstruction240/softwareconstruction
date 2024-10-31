## ♕ Phase 5: Chess Pregame

- [Chess Application Overview](../chess.md)
- [Getting Started](getting-started.md)
- 🖥️ [Videos](#videos)

In this part of the Chess Project, you will create an initial version of your Chess client. Your Chess client will be a terminal-based (i.e., console-based) program that gives the user a simple interface for playing Chess. Your client should implement all user interactions that occur outside of actually playing a game. Game play interactions will be implemented in the next phase. This includes allowing the user to display help text, register, login, list existing games, create a new game, play a game, observe a game, logout, and exit. You will also write the client code that draws the chessboard.

To implement this, you will create a ServerFacade class to handle sending and recieving HTTP requests to and from your server. Your client code will then use your ServerFacade methods to make server API calls.

## Required Functionality

### Prelogin UI

When the user first opens your Chess client application they can execute any of the Prelogin commands.

| Command      | Description                                                                                                                                                                                                      |
| ------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Help**     | Displays text informing the user what actions they can take.                                                                                                                                                     |
| **Quit**     | Exits the program.                                                                                                                                                                                               |
| **Login**    | Prompts the user to input login information. Calls the server login API to login the user. When successfully logged in, the client should transition to the Postlogin UI.                                        |
| **Register** | Prompts the user to input registration information. Calls the server register API to register and login the user. If successfully registered, the client should be logged in and transition to the Postlogin UI. |

#### Example Prelogin UI

![prelogin](prelogin.png)

### Postlogin UI

After the user has registered or logged in they can then execute any of the Postlogin commands.

| Command          | Description                                                                                                                                                                                                                                                                                                                          |
| ---------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Help**         | Displays text informing the user what actions they can take.                                                                                                                                                                                                                                                                         |
| **Logout**       | Logs out the user. Calls the server logout API to logout the user. After logging out with the server, the client should transition to the Prelogin UI.                                                                                                                                                                               |
| **Create Game**  | Allows the user to input a name for the new game. Calls the server create API to create the game. This does not join the player to the created game; it only creates the new game in the server.                                                                                                                                     |
| **List Games**   | Lists all the games that currently exist on the server. Calls the server list API to get all the game data, and displays the games in a numbered list, including the game name and players (not observers) in the game. The numbering for the list should be independent of the game IDs and should start at 1.                      |
| **Play Game**    | Allows the user to specify which game they want to join and what color they want to play. They should be able to enter the number of the desired game. Your client will need to keep track of which number corresponds to which game from the last time it listed the games. Calls the server join API to join the user to the game. |
| **Observe Game** | Allows the user to specify which game they want to observe. They should be able to enter the number of the desired game. Your client will need to keep track of which number corresponds to which game from the last time it listed the games. Functionality will be added in Phase 6.                                               |

#### Example Postlogin UI

![postlogin](postlogin.png)

### Gameplay UI

As stated previously, gameplay will not be implemented until later. For now, when a user plays or observes a game, the client should draw the initial state of a Chess game in the terminal, but not actually enter gameplay mode. The chessboard should be drawn twice, once in each orientation (i.e., once with white pieces at the bottom and once with black pieces at the bottom).

An example of what the chessboard might look like is given below. You are free to make your chessboard look different as long as the essential information is displayed in an easily readable way, but it must "look like a chess board." You must have different colors for alternating squares, but they don't have to be white and black. Light and dark, or white and brown, or whatever you'd like is fine. Per official chess rules, the bottom-right and top-left squares (h1 and a8) must be the "lighter" color. This will mean each queen is "on her color" (white queen on a light square, black queen on a dark square). In addition, you must show the correct row numbers and column letters, but can do so however you would like.

![chessboard](ChessBoard.png)

Note that in the above image, the blue letters are black pieces and the red letters are white pieces. In this example the black team's perspective is shown before the white team's perspective. You can show the perspectives in either order, just make sure you can tell which is which.

### UI Requirements

As with any software project with a UI component, there are certain implementation details you should not display to users. These include:

- Anything in JSON - If you have something in JSON, parse it and then print out only the information you want the user to see.
- Authtokens and Game IDs - These are important to keep track of but the user should not be aware of these internal variables. See the requirements for the [Postlogin UI](#postlogin-ui) for what to display instead of Game IDs.
- HTTP status codes - The user should not be made aware of internal details like this. Ask yourself, apart from 404, when was the last time you saw a status code on a professional website?
- Exception stack traces - Hackers love these kinds of internal details as they show how your code works. These should never be displayed to users, although logging them to a place users will not find may be useful. Instead of a stack trace, a simple message informing the user an error occurred (and hopefully why the error occurred without too many details) should be sufficient.

Make sure that your client doesn't crash. If an exception occurs, catch it. A user may mistakenly put in all kinds of bad input and your client should be able to handle bad input without crashing. This includes incorrect number of arguments (too few or too many), wrong types of arguments (a word when the code expects a number, arguments in the wrong order, etc.), and arguments that the server rejects (register with an existing username, login with incorrect username/password, etc.). When you go to pass off, the TA will test for bad inputs, so please test all of these examples on your own first and ensure the program does not crash and provides reasonable error messages when each error occurs.

### Relevant Instruction Topics

- [Console UI](../../instruction/console-ui/console-ui.md): Reading from the keyboard and writing out fancy text.
- [Web API](../../instruction/web-api/web-api.md#web-client): Making HTTP client requests.

### Tips for using Unicode chess characters

Should you choose to use the unicode Chess characters, some machines by default don't render them in the console (this is often the case if you are using a .jar file). To fix this on Windows (it shouldn't be an issue on Mac/Linux), go to Settings > Time and Language > Language and Region > Administrative Language Settings and on the Administrative tab click Change System Locale and then check the box for using UTF-8 which requires system reboot. This should fix the issue.

Additionally, you may notice that the chess characters might render slightly wider than everything else. To balance this out, a wider version of a space called an em-space can be used. It is denoted by \u2003 and is currently being used as the middle space in the `EMPTY` escape sequence, so if you choose not to use the Unicode chess pieces, you will have to replace that with a regular space to get everything to line up vertically.

## ☑ Deliverable

### Pass Off Tests

There are no new pass off test cases for this assignment.

### Unit Tests

Write positive and negative unit tests for each method on your ServerFacade class (all the methods used to call your server).

Your tests must be located in the file `client/src/test/java/client/ServerFacadeTests.java`, provided in the starter code.

> [!TIP]
> `ServerFacadeTests.java` contains code that will automatically start and shutdown your server on a randomly assigned port as part of the test. However, you will still need to start your server using the `Main.main` function when you manually run your client.

```java
public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }
}
```

Replace the `sampleTest` method with your own unit tests.

Your server facade will need to take the port that the server is running on as part of its initialization when running your tests. You can accomplish this by modifying the `init` method to create and initialize your server facade with code that is something like the following:

```java
private static Server server;
static ServerFacade facade;

@BeforeAll
public static void init() {
    server = new Server();
    var port = server.run(0);
    System.out.println("Started test HTTP server on " + port);
    facade = new ServerFacade(port);
}
```

You can then directly test your facade with tests such as demonstrated in the following `register` unit test example.

```java
@Test
void register() throws Exception {
    var authData = facade.register("player1", "password", "p1@email.com");
    assertTrue(authData.authToken().length() > 10);
}
```

Make sure you clear your database between each test. You can do this in a method that has the `@BeforeEach` annotation.

### Code Quality

For this phase the TAs will grade the quality of your project's source code. The rubric used to evaluate code quality can be found here: [Rubric](../code-quality-rubric.md)

### Pass Off, Submission, and Grading

All of the tests in your project must succeed in order to complete this phase.

To pass off this assignment submit your work to the course [auto-grading](https://cs240.click/) tool. When that is done, meet with a TA and demonstrate that your Chess client and server meet all requirements and assign you a final grade.

### Grading Rubric

> [!IMPORTANT]
> You are required to commit to GitHub with every minor milestone. For example, after you successfully pass a test. This should result in a commit history that clearly details your work on this phase. If your Git history does not demonstrate your efforts then your submission may be rejected.

| Category       | Criteria                                                                                                                                                                                        |       Points |
| :------------- | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -----------: |
| GitHub History | At least 12 GitHub commits evenly spread over the assignment period that demonstrate proof of work                                                                                              | Prerequisite |
| Functionality  | Program supports all required functionality                                                                                                                                                     |          100 |
| Code Quality   | [Rubric](../code-quality-rubric.md)                                                                                                                                                             |           30 |
| Unit Tests     | All test cases pass<br/>Each public method on the Server Facade class has two test cases, one positive test and one negative test<br/>Every test case includes an Assert statement of some type |           25 |
|                | Total                                                                                                                                                                                           |          155 |

## <a name="videos"></a>Videos (38:10)

- 🎥 [Phase 5 Introduction (8:11)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6e2c9d2f-5a74-4b60-989e-b19a0150a134)
- 🎥 [Read-Eval-Print-Loop (REPL) (8:11)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=29364420-9c98-4778-ba7a-b19a015380c7)
- 🎥 [Drawing the Board (1:26)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6a77c895-f2b8-49d9-8b11-b19a0156aef8)
- 🎥 [Server Facade (5:00)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6f02ff21-d6bd-47f3-bddd-b19a01578159)
- 🎥 [Phase 5 Requirements (3:11)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=1a171c4d-c7dc-41d0-828f-b19a01594498)
- 🎥 [Client HTTP (12:11)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=781ae49b-6284-4e1a-836b-b1930162c54b)
