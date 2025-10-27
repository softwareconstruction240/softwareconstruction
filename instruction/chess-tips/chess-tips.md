# Chess Tips

This is a collection of tips that the TAs have compiled for solving common problems.
# General - all phases

## I don't have enough GitHub commits to pass the autograder

Try to avoid this by following these rules of thumb:
1. **Work in small chunks.** One common mistake is only committing when you hit a big milestone, like passing an entire test file - this is a sign that you could break the problem into smaller pieces. Commit when you hit small milestones, like finishing a function or passing a test.
2. **Always set a clear goal for your next commit.** Something like "pass this test" or "create this piece of logic." Let the flow of regular commits drive your development process.
3. Commits should **represent your changes in one short sentence.** If your commits are feeling hard to articulate because you don't remember all the things you changed, that's a good indicator that you should be committing more often.
4. **Start working early.** The autograder has a requirement for number of days, so it'll flag you if you do everything on the same day.

If you turn in your code without enough commits, the autograder will let you know that you need TA approval. We recommend that you bring your number of commits up to the required amount before coming in. You can make further changes to your code by focusing on code quality and organization: analyze your program structure and work on applying principles you're learning in class, like decomposition, abstraction, and encapsulation.

# Phase 0 - Chess moves

## These collections look identical to each other, but Java says they aren’t

Look at the [specification](../../chess/0-chess-moves/chess-moves.md#object-overrides) for mentions of the `equals()` and `hashCode()` methods. It might also be worthwhile to implement a `toString()` method. Additionally, check the promotion piece (null in 99% of cases). Also, review the getters and setters for each class if that doesn’t work.

## JUnit - No test events received

Go into File -> Project Structure -> Modules -> Paths and select “Inherit project compile output path” for each module.

## I can’t run any tests, there is no green button

First, check if your `test/java` folder is highlighted green. If it isn’t highlighted green, right-click on the `java` folder, then select "Mark Directory As…" and select "Test Sources Root". Now that IntelliJ understands that this is a test folder, there should be an option to run the tests inside of it.

If that doesn’t work, make sure that the shared folder is a module. You can check this by clicking File -> Project Structure. Then, in Project Structure select the Module option on the left. Here you can see a section of Modules, in which you should see shared, server, and client. If you only see chess or nothing at all, IntelliJ didn’t set it up right, so you should delete everything, reclone the repository, and set it up again.

## I don't have enough GitHub commits to pass the autograder

See [previous](/instruction/chess-tips/chess-tips.md#General---all-phases)

# Phase 1 - Chess Game

## toString

If you are struggling to understand how the board is looking during your code, and especially when comparing it with the expected test cases, what you can do is override a toString to your code. This means that instead of printing ChessGame@12345, it will print out the variables, and you can have it print out a mock chess board so you can visually see and understand what the current layout of the chessBoard. 

## `static` keyword

If your IDE is telling you to use static, you probably should change your code to not use it. Static classes and variables mean that there will only be one single instance, making it like a global variable, and there will not be any different variations of it. ChessGame nor most of the main classes you work on should be cosidered static, because you should have multiple different ChessGames, or ChessBoards, or even ChessPieces. If you know what you are doing, you can use static, but only use it if you can know what you are using it for. If your IDE is telling you to use static, you should see why it is thinking it should be static, and fix it because 90-100% of your code shouldn't be static.

## Clone and Copy

When in your `ChessGame.validMoves`, you may want to create a copy/clone of the ChessBoard so that you can make a piece move and see if you are still in check to know if that is a valid move or not. If you create a shallow copy, the ChessBoard will be the exact same, and will keep any changes you make. This needs to be a DeepCopy or clone so that it can be unique and different, so that if a chance happens on one instance, the other will stay the same. If you would like some explanations on how to incorporate clone and copy, look here for [copying objects](../copying-objects/copying-objects.md). One such method is to have ChessBoard implement Cloneable, then in the override clone method, you loop through the 2d ChessPiece array, and do `Arrays.copyOf` to copy the chess board row by row, then finally putting the 2d array into the cloned ChessBoard. 

## `==` vs `.equals()` comparison

If you are trying to see if a king is in check by doing `endPosition == kingPosition`, the answer will always return false. Instead, you should use `endPosition.equals(kingPosition)`. To understand why Objects require `.equals()` instead, please refer to [Java Object Class](../java-object-class/java-object-class.md).

## I don't have enough GitHub commits to pass the autograder

See [previous](/instruction/chess-tips/chess-tips.md#General---all-phases)

# Phase 2 - Server Diagram

# Phase 3 - Web API

## 5 Pro tips

5 Pro tips for solving your own coding problems (#4 will surprise you):

1. **Read the error messages.** The computer doesn’t lie, and it even tells you exactly which line caused the problem!
2. **Read the specs.** The document is long, but it’s not redundant— it’s helpful. All the information has been carefully placed in there to help YOU as a student.
3. **Search Slack.** It’s really to ask a new question, but first try to use the search feature to find a pre-existing answer.
4. **Write your own tests.** Yes, it was convenient in the first phases when we had done that for you, but now we’re expecting you to do this yourself. The Phase 3 tests are intentionally less helpful than before. Rely on manual testing, or write your own tests from the beginning.
5. **Appreciate the learning opportunity.** We (the instructors) designed this course to teach you new things. You are supposed to wrestle with new concepts and challenges, but it is possible. Think through the problems; you can do it!

## Why is there no distinction between `Unauthorized` and `Unregistered` errors?

> **Question:**
> Would trying to sign in with an unregistered username generate a 401 error or a 500 error?
> my guess is that it's a 401, since it was a user error, but calling it "Unauthorized" when it's more "Unregistered" doesn't feel quite right

While the user isn't registered, you still want to return `Unauthorized`, otherwise you would be "leaking" information.

For example, lets say you return `Unregistered` for users that don't exist and `Unauthorized` for users that do exist but just have the wrong password. If I'm a bad actor and I want to try and log in to someone else's account, if I get an `Unregistered` exception, that tells me that the account I'm trying to log into doesn't exist, and if I get an `Unauthorized` exception that means I have an account that exists, but I don't have the right password. So I could just spam different passwords at accounts I know exist, rather than waste my time with accounts that don't exist.

Returning `Unauthorized` in both cases doesn't "leak" the information whether an account exists or not. Just think about when you try to log into somewhere, and if you put the wrong password, it usually says "wrong username/password" rather than just saying "wrong password."

## "Variable is being received by server/tests as null” OR “Variable from the tests is arriving in my server as null”

A variable in a request/result object is named incorrectly (casing matters with gson: authToken and authtoken are treated differently). Make sure that all class variable names in classes used for serialization exactly match the spec.

Make sure the resources folder/directory is marked as the main resources root

## SyntaxError: Unexpected token ‘<’, “<html><bod”... is not valid JSON

If an error is thrown and not caught, this can be encountered. You must fix the problem that is being thrown.
Or if this problem is happening with error handling, try to return the error in valid json format like with the following code

```java
Return new Gson().toJson(Map.of(“message”, ex.getMessage()));
```

## Can’t find authToken from the request

String authToken = context.header(“authorization”)

## AutoGrader can’t find my Unit Tests

Make sure the unit tests are in server/src/test/java/service and the Class has the word Tests in it.

## I don't have enough GitHub commits to pass the autograder

See [previous](/instruction/chess-tips/chess-tips.md#General---all-phases)

# Phase 4 - Database

Since Phase 4 requires interacting with an external program (the MySQL database), the likelihood of having auto-grader test output different from your local output is higher than previous phases. The most common issues are summarized here to save you time spent frustrated with a new technology.

## Database Schema Changes

### Database and Table Creation

Your code must create the SQL database and tables correctly if they do not exist. This includes when the server is run from tests. This is done with the `IF NOT EXISTS` syntax for your database and table creation SQL statements. This must happend before any method tries to access the database. For example, if the register user endpoint is called, The user table must already exist. Otherwise the SQL call to inset the user will fail.

Also make sure that your create statements are in correct SQL Format. A good example can be found in the [db-sql](../../instruction/db-sql/db-sql.md) instructions.

### Altering Tables After Creation

Since your database and tables are created the first time your server detected that they didn't exist, it **will not** automatically alter the tables if you change your table definitions in your code. That means that you will have to do one of the following in order to migrate your table scheme after a table is created.

1. Use a MySQL client to drop the database or table. This will cause your service to recreate the table the next time it starts up.
2. Use a MySQL client to execute SQL `ALTER` statements to match the changes you have made in the code. (Not recommended.)

## Tests Not Passing on the AutoGrader

### Database and Table Creation

If the tests pass in your development environment but not with the autograder isn't passing, your create database and table code may be incorrect.

Since your code will only create the database and tables if it isn't created already, it might be possible that you altered your create database and table code after it has been created, meaning that your local SQL database doesn't match the schema that is being used on the autograder.

Try the following steps to reproduce the problem in your development environment:

1. Drop your database/schema using a SQL client.
2. Rerun the tests locally.
3. If they fail, double-check the code for creating the database and tables.

### Hardcoded Database Name

It's fun to assume that your database will always be named `chess`, but the AutoGrader will break that assumption!

The auto-grader inserts a new `db.properties` file for grading with most of the values different from those in your file. For example, instead of calling the database `chess`, it may call it `chess123456`.

Run through the following verifications in your code:

- Check for any place you may have hardcoded any values from `db.properties`. Instead you must use the variables loaded from `db.properties`
- Do not include the database name in any SQL statement. The database is already set by the `DatabaseManager` and therefore does not need to be explicitly set.
  - For example, use `SELECT * FROM table` instead of `SELECT * FROM database.table`.

### CasE SEnSiTiVitY

SQL table and column names are **case-sensitive** on the AutoGrader. However, some operating systems, like Windows, treat names as **case-insensitive**, which can cause issues when submitting your project. Double-check capitalization in your SQL code to avoid errors.

- Double-check the casing in all of your SQL statements.
  - For example, if you have table named `foobar`, then you cannot later attempt to access it as `FooBar` (notice the capitalization change).
- Make sure each **table** and **column** name uses the same casing each time you use it.
  - Verify that the capitalization used in your `CREATE TABLE tablename` statements exactly matches all other uses, like in `INSERT INTO tablename`.
  - Verify that the capitalization used in all references to table names are exactly the same. This includes references in:
    - `CREATE TABLE` statement
    - `INSERT INTO` statements
    - Any other statements making reference to column names like `SELECT` or `WHERE` clauses.
    - `ResultSet.get*()` or similar functions which require a column name
- Storing table names and/or column names in **variables** gives a reliable foundation to avoid this kind of problem.

## I don't have enough GitHub commits to pass the autograder

See [previous](/instruction/chess-tips/chess-tips.md#General---all-phases)

## Abstract Classes can’t be instantiated (ChessPiece)

You’ll probably need to set up a TypeAdapter/JsonDeserializer. Alternatively you could switch your phase 0 code to use the move calculator strategy and only have one concrete ChessPiece class with no child classes.

## Interfaces can’t be instantiated (Move Calculator)

You probably made the move calculator a class variable inside ChessPiece. Remove that variable and decide on the calculator each time pieceMoves gets called.

## Access denied to database chess or Access denied for user ‘dbUser173910573’@’%’ to database ‘chess’

This means they hardcoded the database to chess. Or they need to call createDatabase. Look here for more information: [Ititializing Your Database and Tables](../../chess/4-database/database.md#initializing-your-database-and-tables)

## No driver provided error

Make sure they add both dependencies (specifically the mysql-connector one)

## Failed to Bind to /0.0.0.0:3306

This is usually due to the fact that port 3306 is already being used by something. First thing is to make sure that the server isn’t running on port 3306 (for example sev.run(3306) instead of sev.run(8080)). If the server is running on port 3306, then mySQL can’t make connections on port 3306. Simple fix is to just make sure they are using different ports -> sev.run(8080) and db.properties has a db.port of 3306.

## Invalid SQL Format

The last line of the SQL String before the end parentheses should not have a comma, if it does that will throw an error

```java
String createUserTable = """
CREATE TABLE IF NOT EXISTS user (
username VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL,
PRIMARY KEY (username),
)""";
```

As a side note, you don’t need the Engine that is shown in Pet Shop.
Primary Keys mean that it will be unique, and can’t have any duplicates

## Too Many Connections

If you are running into this error, that means the connections aren’t being created and then closed. This can be done by simply putting the getConnection call in a try-with-resources block, as it will automatically close the connection afterwards. If you don’t want to use a try-with-resources block for whatever reason, you can instead call close on the connection afterwards.

## Failing the Database Error Handling Test

First off, if the test is failing at the line with getDeclaredMethod(“loadProperties”) then they need to update their DatabaseManager so that it has the loadProperties method in it.

If that is not their problem, this test will basically run through all the endpoints, crash the server in order to have the DatabaseManager throw a DataAccessException either on the createDatabase call or getConnection call. This DataAccessException should be caught to create a 500 server error, and the error message should start with “Error: “. So make sure that the student is catching these DataAccessExceptions, setting the status code to 500, and having the error message start with “Error: ”

A great way to debug this is to put a breakpoint in the DatabaseTests at line 128 (TestResult result = operation.get();), then pressing the step over button so you can see what is in result. Here you can see the error message (to make sure it has “Error: “ in the front, and if you press the step over button again, it will pass through the Assertions.assertEquals(500, serverFacade.getStatusCode()) and you will know if it did have a 500 status code or not. The order these operations go through are (Clear, Register, Login, Logout, CreateGame, ListGames, then JoinGame) so if they fail the 3rd check, result should have null value for authToken and username, but a message with “Error:”, so you can look into their Logout endpoint to see where there may be an issue.

If it says “Error: Unauthorized” with a 401 error, that probably means that they have a catch (DataAccessException) into throw new DataAccessException (“Error: Unauthorized”), overriding the error message of failed to connect to the database and changing it into an Unauthorized exception. This makes the student’s code think this is a 401 error instead of a 500 server error.

# Phase 5 - Pregame

## I can’t import ServerFacade in the tests

Your ServerFacade is probably not in a package. Put it in a package (probably not your UI package, is it a UI class?) and you should be able to import it

## Passoff Frequently Encountered Problems

Here are a couple of things that students commonly forget to include as part of their code which causes them to fail their passoff. This is not a complete list of everything that your code needs to do in order to pass, just some of the common problems. 

- After registering, you will automatically enter the signed-in state, you don't need to login afterwards. [Prelogin UI Command Descriptions](../../chess/5-pregame/pregame.md#prelogin-ui)
- Make your ListGames numbering be independent of the game IDs. [Postlogin UI Command Descriptions](../../chess/5-pregame/pregame.md#postlogin-ui)
- Make sure your board is printed correctly! [Gameplay UI Description](../../chess/5-pregame/pregame.md#gameplay-ui)
- Print readable errors and make sure your code doesn't crash. For example, make sure you handle trying to join or observe a game with an invalid game number input (`1000`, `-10`, `two`). [UI Requirements](../../chess/5-pregame/pregame.md#ui-requirements)

## I don't have enough GitHub commits to pass the autograder

See [previous](/instruction/chess-tips/chess-tips.md#General---all-phases)

# Phase 6 - Gameplay

## Unknown opcode: 7

If your server is throwing exceptions related to “opcode 7”, try changing your server port away from 8080 (or whatever port you are using). The cause of this is currently not known, but it seems to be more likely to occur on computers running macOS Sonoma.

## Variable is being received by server/tests as null

A variable in a ServerMessage/UserCommand object is named incorrectly (casing matters with gson). Make sure that all class variable names in classes used for serialization exactly match the spec.

## message.toString

Note, make sure that students aren’t copying Pet Shop code, but specifically the message.toString method, as Pet Shop will override that and turn it into a Gson.toJson of the class, while student’s code will just make it a string message of the whole Message class, which isn’t json. They need to make sure that

## Message is sent from Server but doesn’t look like it’s being received in the client

It’s possible that the code for receiving messages in the client could be throwing an exception (especially if the message isn’t formatted correctly in JSON). Tyrus has a bad habit of just eating those exceptions without logging them anywhere useful. I recommend wrapping the contents of the method with a try/catch block with a catch for any Exception or Throwable. Up to the student what to do in the catch block, but don’t throw a different exception otherwise they’ll be back at square one.

## Tests are failing because there are too many messages

This could be from a race condition, if you send a notification that a move was made, then do a mysql check to see if the game is in check, checkmate, or stalemate, the mysql will take too long and send a message as part of the next tests. So do all checking first, then send the websocket messages afterwards to eliminate all race conditions.

## Running into a ClosedChannelException

This exception is thrown when you are trying to send a message to a closed channel. Have the student make sure they are checking that the session is open (session.isOpen()) before sending it a message.

## Passoff Frequently Encountered Problems

Here are a couple of things that students commonly forget to include as part of their code which causes them to fail their passoff. This is not a complete list of everything that your code needs to do in order to pass, just some of the common problems. 

- Resigning should require a confirmation, and does **not** kick players from the game. [Gameplay Functionality](../../chess/6-gameplay/gameplay.md#gameplay-functionality)
- Anyone can highlight any piece, independent of whose turn it is. In addition, a user trying to highlight a position with no piece shouldn't break your code. [Gameplay Functionality](../../chess/6-gameplay/gameplay.md#gameplay-functionality)
- Make sure you implement pawn promotion. [Pawn Functionality](../../chess/0-chess-moves/the-game-of-chess.md#pawn)
- All messages should contain player usernames. Move messages should have a description of the move such as a2 to a4. [Notifications](../../chess/6-gameplay/gameplay.md#notifications)

## I don't have enough GitHub commits to pass the autograder

See [previous](/instruction/chess-tips/chess-tips.md#General---all-phases)
