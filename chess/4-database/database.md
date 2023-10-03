# ♕ Phase 4: Chess Database

- [Chess Application Overview](../chess.md)
- [Getting Started](getting-started.md)
- [Starter Code](starter-code)

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

The tests provided for this assignment are in the PersistenceTests class. To run the tests first start your server, and then run PersistenceTests. When PersistenceTest runs, it will first call your web APIs to create some data in your database. Next, it will pause and prompt you to restart your server. Once your server has been restarted, press ‘Enter’ to continue with the tests.

Additionally, run the StandardAPITests from the previous phase to make sure they still run successfully.

## Pass Off, Submission, and Grading

To pass off this assignment, meet with a TA and demonstrate that your code passes the provided test cases (PersistenceTests and StandardAPITests).

After you pass off your project with a TA, you should immediately submit your project source code for grading. Your grade on the project will be determined by the date you submitted your source code after passing off, not the date that you passed off. If we never receive your source code, you will not receive credit for the assignment. Here are the instructions for submitting your project source code:

- In Intellij, navigate to the "Build" menu at the top of the screen and select "Clean Project" to remove auto-generated build files (if this option is not available, you can skip this step).
- Create a ZIP file containing your whole project folder (not just the Java source files).
- Submit your ZIP file on Canvas under the `Chess Database` assignment.
- To demonstrate that your test cases execute successfully, you should run your unit tests inside Intellij and take a screenshot of the successful test results displayed by Intellij. Please take one screenshot that shows the result of all of your tests passing. The tests used for the screenshot must be the same as the ones submitted in the Code Quality assignment ZIP file.
- Submit your screenshot under the `Chess Database` assignment on Canvas. The screenshot is submitted separately from your code ZIP file.

### Grading Rubric

| Category      | Criteria                                                                                                                                                                            | Points |
| ------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -----: |
| Functionality | All pass off test cases succeed                                                                                                                                                     |    100 |
| Unit Tests    | All test cases pass<br/>Each public method on DAO classes has two test cases, one positive test and one negative test<br/>Every test case includes an Assert statement of some type |     25 |
|               | Total                                                                                                                                                                               |    125 |
