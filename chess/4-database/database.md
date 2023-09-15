# ♕ Phase 4: Chess Database

♟️ [Project Overview](../chess.md)

✅ [Phase 4 Getting Started](getting-started.md)

📁 [Phase 4 Starter code](starter-code)

In this part of the Chess project, you will modify your data access (DAO) classes to store data in a MySQL database instead of storing data in main memory. You will also write unit tests using JUnit for your DAO classes. This will be done in a few steps:

1. Design your database tables (i.e., your database `schema`)
2. Install the MYSQL database management system (DBMS) on your development machine
3. Create a database (or schema) in MYSQL. You might name it `chess` or something similar.
4. Rewrite your Chess server’s DAO classes to store and retrieve all data to/from MySQL
5. Write unit tests for your DAO classes
6. Ensure that all of your unit tests work, including the new DAO tests the Service tests you wrote in the previous assignment.
7. Ensure that all provided pass off tests work properly, including the PersistenceTests added for this assignment, and the StandardAPITests from the previous assignment.

To create your database you’ll need to create a MYSQL schema and write a SQL script for creating the needed tables. Keep in mind what data type each field will be, as well as whether they can be null. Refer to the `MYSQL setup tutorial` for help on getting MYSQL working.

## ChessGame Serialization/Deserialization

The easiest way to store the state of a ChessGame in MySQL is to serialize it to a JSON string, and then store the string in your database. Whenever your server needs to update the state of a game, it should:

1. Query the game’s state (JSON string) from the database
2. Deserialize the JSON string to a ChessGame Java object
3. Update the state of the ChessGame object
4. Re-serialize the Chess game to a JSON string
5. Update the game’s JSON string in the database

## Unit Tests

1. Write a positive and a negative JUNIT test case for each public method on your DAO classes, except for Clear methods which only need a positive test case. A positive test case is one for which the action happens successfully (e.g., creating a new user in the database). A negative test case is one for which the operation fails (e.g., creating a User that has the same username as an existing user).
2. Ensure that all of your unit tests work, including the new DAO tests the Service tests you wrote in the previous assignment.

## Pass Off Tests

Ensure that all provided pass off tests work properly, including the PersistenceTests added for this assignment, and the StandardAPITests from the previous assignment.

When PersistenceTest runs, it will first call your web APIs to create some data in your database. Next, it will pause and prompt you to restart your server. Once your server has been restarted, press ‘Enter’ to continue with the tests.
