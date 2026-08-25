# TA Tips

This is a collection of tips compiled by the TAs to help you solve common problems throughout the project.

# General - All Phases

## I don't have enough GitHub commits to pass the autograder

The autograder requires a history of regular commits to verify your development process. Try to avoid falling short by following these guidelines:

1.  **Work in small chunks.** A common mistake is committing only when hitting a major milestone, such as passing an entire test file. This usually indicates that you should break the problem into smaller pieces. Commit when you finish a single function or pass a specific test case.
2.  **Set a clear goal for every commit.** Aim for specific targets like "implement pawn moves" or "fix board initialization logic." Let the flow of regular commits drive your development process.
3.  **Keep commit messages concise.** Commits should represent your changes in one short sentence. If you find it hard to articulate what you changed because you modified too many things, you are waiting too long to commit.
4.  **Start working early.** The autograder requires commits over a minimum number of days. If you complete the entire project in a single day, you will likely be flagged.

If you submit code without enough commits, the autograder will require TA approval. We recommend reaching the required commit count before seeking help. You can increase your commit count by focusing on code quality: analyze your program structure and apply principles like decomposition, abstraction, and encapsulation to refactor your code.

# Phase 0 - Chess Moves

## These collections look identical, but Java says they aren't

Check the [specification](../../chess/0-chess-moves/chess-moves.md#object-overrides) regarding the `equals()` and `hashCode()` methods. If these aren't implemented correctly, Java will compare the memory addresses of the objects rather than their contents. 

**Common pitfalls:**
*   Ensure the `promotionPiece` is included in your equality checks (it is `null` in most cases, but still matters).
*   Review your getters and setters to ensure data is being stored and retrieved as expected.
*   Implement a `toString()` method to help you see the actual contents of the collections during debugging.

## JUnit - No test events received

If IntelliJ fails to find or run tests, go to **File -> Project Structure -> Modules -> Paths** and select **"Inherit project compile output path"** for each module.

## I can’t run any tests; there is no green button

First, check if your `test/java` folder is highlighted in green. If it isn't:
1.  Right-click the `java` folder.
2.  Select **"Mark Directory As..."** 
3.  Select **"Test Sources Root"**.

If the button still doesn't appear, ensure the `shared` folder is recognized as a module:
1.  Go to **File -> Project Structure -> Modules**.
2.  You should see `shared`, `server`, and `client`.
3.  If you only see `chess` or nothing at all, the project structure is incorrect. You may need to re-import the project or re-clone the repository to ensure IntelliJ sets up the modules correctly.

## I don't have enough GitHub commits to pass the autograder

See the [General](#general---all-phases) section.

# Phase 1 - Chess Game

## toString

If you are struggling to visualize the board state during debugging—especially when comparing it with expected test results—override the `toString()` method in your `ChessBoard` class. Instead of a generic memory address like `ChessGame@12345`, you can make it print a text-based representation of the board layout.

## The `static` keyword

In most cases, you should **not** use the `static` keyword for your classes and methods. Static variables act as global variables, meaning only one instance exists for the entire program. This can cause significant issues when the autograder runs multiple tests in sequence. 

If your IDE suggests making something static, it’s usually because you are trying to access an instance member from a static context. Rather than following the suggestion, fix the underlying architectural issue. The vast majority of your project logic should be instance-based.

## Clone and Copy

In `ChessGame.validMoves`, you often need to create a copy of the `ChessBoard` to simulate a move and check if the king remains in check. 

*   **Shallow Copy:** If you simply copy the reference or the outer array, the underlying pieces remain the same. Changes to the "copy" will affect the original board.
*   **Deep Copy:** You must create a new `ChessBoard` and new `ChessPiece` objects (or use a structure that ensures independence).
*   **Implementation Tip:** Have `ChessBoard` implement `Cloneable`. In the overridden `clone()` method, create a new 2D array and copy each piece. For more details, see the documentation on [copying objects](../copying-objects/copying-objects.md).

## `==` vs `.equals()` comparison

If you compare positions using `endPosition == kingPosition`, the result will almost always be `false` because `==` compares memory references. You must use `endPosition.equals(kingPosition)`. To understand why objects require `.equals()`, refer to the [Java Object Class](../java-object-class/java-object-class.md) documentation.

## I don't have enough GitHub commits to pass the autograder

See the [General](#general---all-phases) section.

# Phase 2 - Server Diagram
*(No specific tips for this phase yet.)*

# Phase 3 - Web API

## 5 Pro Tips for Debugging

1.  **Read the error messages.** The compiler and debugger provide specific line numbers and descriptions. Trust the output!
2.  **Read the specs.** The documentation is detailed and intentionally structured to guide you through common pitfalls.
3.  **Search Discord.** Use the search feature to see if your question has already been answered in previous semesters or by other students.
4.  **Write your own tests.** Phase 3 tests are intentionally less granular than earlier phases. Rely on manual testing or write your own unit tests to isolate issues.
5.  **Embrace the challenge.** This course is designed to push you into new concepts. Wrestling with these problems is where the actual learning happens.

## Why is there no distinction between "Unauthorized" and "Unregistered" errors?

Returning a generic `401 Unauthorized` for both an incorrect password and a non-existent username is a security best practice known as preventing **information leakage**.

If your server returned `User Not Found`, a malicious actor could use that information to harvest a list of valid usernames. By returning the same error for both cases, you keep the existence of accounts private. This is why most professional login systems use generic "Invalid username or password" messages.

## "Variable is being received by server as null"

This is almost always a naming mismatch during JSON serialization. GSON is case-sensitive. If your Java variable is named `authtoken` but the JSON expects `authToken`, the field will not be populated. Ensure your class variable names exactly match the naming conventions in the [specification](../../chess/3-web-api/web-api.md).

Also, ensure your `resources` directory is correctly marked as the **Resources Root** in IntelliJ.

## SyntaxError: Unexpected token ‘<’, “<html><bod”... is not valid JSON

This happens when your client expects JSON but receives an HTML page instead. This usually occurs when the server throws an unhandled exception and returns a default 500 error page, or when a 404 error occurs.

To fix this, ensure your server catches exceptions and returns a valid JSON error object:
```java
return new Gson().toJson(Map.of("message", ex.getMessage()));
```

## How do I read the authToken from an HTTP request?

Refer to the [Web API instruction](../web-api/web-api.md#http-headers) regarding HTTP headers.

## AutoGrader can’t find my Unit Tests

Ensure your unit tests are located in `server/src/test/java/service` and that the class names end with `Test` or `Tests`.

## I don't have enough GitHub commits to pass the autograder

See the [General](#general---all-phases) section.

# Phase 4 - Database

Phase 4 involves external dependencies (MySQL), which increases the chance of local behavior differing from the autograder.

## Database Schema Changes

### Database and Table Creation
Your code must programmatically create the database and tables if they do not exist. This should happen during server startup, before any endpoint is accessed. Use the `IF NOT EXISTS` syntax in your SQL.

### Altering Tables After Creation
If you change your table schema in your Java code, MySQL **will not** automatically update your existing tables. You must either:
1.  Use a MySQL client to `DROP` the table so your code can recreate it.
2.  Manually `ALTER` the table (not recommended for beginners).

## Tests Not Passing on the AutoGrader

### Hardcoded Database Name
Do **not** hardcode the database name (e.g., `chess`) in your SQL strings. The autograder uses a dynamic database name (like `chess123`).
*   Load the database name from `db.properties`.
*   Use `SELECT * FROM table` rather than `SELECT * FROM chess.table`.

### Case Sensitivity
SQL table and column names are **case-sensitive** on the Linux environments used by the autograder, even if they appear case-insensitive on Windows or macOS.
*   Ensure `CREATE TABLE myTable` and `INSERT INTO mytable` use the exact same casing.
*   Be consistent in your `ResultSet.get*()` calls.

## Abstract Classes and Interfaces (GSON)

You cannot directly instantiate abstract classes like `ChessPiece` or interfaces like `MoveCalculator` using GSON.
*   **Option 1:** Use a `TypeAdapter` or `JsonDeserializer` to handle polymorphic types.
*   **Option 2:** Refactor your Phase 0 code to use a single concrete `ChessPiece` class with a strategy pattern for move calculation that doesn't rely on inheritance.

## Access Denied to Database

This usually means you have hardcoded the database name to `chess` or failed to call your `createDatabase` logic. Ensure you are using the credentials provided in `db.properties`.

## No Driver Provided Error

Ensure you have added the `mysql-connector-java` dependency to your `pom.xml` or project configuration.

## Failed to Bind to /0.0.0.0:3306

This happens if you try to run your **Spark server** on port 3306. Port 3306 is reserved for MySQL. Your Spark server should typically run on port 8080, while it connects to MySQL on 3306.

## Invalid SQL Format

Ensure you don't have trailing commas in your SQL statements.
```java
// WRONG: Trailing comma after PRIMARY KEY
String sql = "CREATE TABLE user (username VARCHAR(255), PRIMARY KEY (username), )"; 
```

## Too Many Connections

Always use **try-with-resources** when getting a connection. This ensures the connection is closed even if an exception is thrown. If you don't close connections, the database will eventually refuse new ones.

## Failing the Database Error Handling Test

If the test fails on `getDeclaredMethod("loadProperties")`, ensure your `DatabaseManager` has that specific method.

The error handling test simulates database failures. Your server should:
1.  Catch `DataAccessException`.
2.  Set the HTTP status code to `500`.
3.  Return a JSON message starting with `"Error: "`.

# Phase 5 - Pregame

## I can’t import ServerFacade in the tests

Ensure `ServerFacade` is in a proper package (e.g., `client.ui` or `client.net`). If it's in the default (empty) package, it cannot be imported by classes in other packages.

## Passoff Frequently Encountered Problems

*   **Registration Flow:** After registering, the user should be automatically logged in.
*   **List Games:** The numbers shown to the user (1, 2, 3...) should be independent of the actual Database IDs.
*   **Board Printing:** Ensure the board displays correctly for both white and black perspectives.
*   **Error Handling:** If a user enters an invalid game number (like "abc"), your client should print a helpful error rather than crashing.

## Chess Board UI

To ensure the board looks like a square:
*   Use the Unicode "Em Space" (`\u2003`) to match the width of chess piece characters.
*   If the alignment is still off, change your IntelliJ console font to a **monospaced** font (Settings -> Editor -> Font).

# Phase 6 - Gameplay

## Unknown opcode: 7

If you see "opcode 7" errors on macOS, try changing your server port from `8080` to something else (like `8081`). This is a known issue with macOS Sonoma's "AirPlay Receiver" service.

## Message is sent from Server but not received by Client

The `Tyrus` WebSocket library often suppresses exceptions that occur during message arrival. If your client-side JSON parsing fails, the message will simply disappear. Wrap your client's `onMessage` logic in a `try-catch (Throwable t)` block and print the stack trace to find the error.

## Too Many Messages / Race Conditions

If you send a "Move" notification and then perform a database check for checkmate, a race condition might occur. Perform all game logic and database updates **before** sending out WebSocket notifications to ensure the client receives messages in the correct order.

## ClosedChannelException

Always check `session.isOpen()` before attempting to send a message to a WebSocket session.

## Gameplay Requirements Checklist

*   **Resignation:** Requires a confirmation prompt and does **not** remove the players from the session.
*   **Highlighting:** Should work for any piece regardless of whose turn it is.
*   **Pawn Promotion:** Ensure the user can choose which piece to promote to.
*   **Usernames:** All notifications should include the relevant usernames (e.g., "Player 'Alice' moved a2 to a4").