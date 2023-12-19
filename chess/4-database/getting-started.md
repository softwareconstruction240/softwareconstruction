# Getting Started

📁 [Starter code](starter-code)

The Starter Code has two folders, `dataAccess` and `serverTests`. Do the following:

1. Copy the `dataAccess` folder into your project’s `src/starter` folder. The `dataAccess` folder already exists in your project, but this will add more files to it.
1. Copy the `serverTests` folder into your project’s `src/test/passoffTests` folder. The `serverTests` folder already exists in your project, but this will add more files to it.

## Persistence Test

In your project’s `src/test/passoffTests/serverTests` folder there will now be a new class named PersistenceTest. This class tests that your server properly saves and loads data to/from your MySQL database. When this test runs, it first creates some data in your database by calling your server’s web APIs. Then, you will see a prompt in the IntelliJ test console window asking you to shut down your server, restart it, and then press `Enter`. By default, the IntelliJ test console window does not allow user input. If your test console window does not allow user input, do the following:

1. In IntelliJ go to the `Help` menu and select `Edit Custom VM Options…` This will load a text file in your editor that contains the Java virtual machine command line options that are used by IntelliJ.
2. Add the following line to this file and save it:

   ```sh
   -Deditable.java.test.console=true
   ```

3. Restart IntelliJ

Your test console window should now allow user input.

## Connect Database class to MySQL

Open to the `Database.java` file in your project’s `src/starter/dataAccess` folder and edit the `CONNECTION_URL`, `DB_USERNAME`, and `DB_PASSWORD` fields to match the configuration of your MySQL server. This class provides an example of how to manage a pool of database connections to your MySQL database. The usage of this class is completely optional, but if you do use it then here is an example of how you obtain and return connections.

```java
public boolean example(String selectStatement, Database db) throws DataAccessException{

  var conn = db.getConnection();

  try (var preparedStatement = conn.prepareStatement(selectStatement)) {
      return preparedStatement.execute();
  } catch (SQLException ex) {
      throw new DataAccessException(ex.toString());
  } finally {
      db.returnConnection(conn);
  }
}
```

## Dependencies

Add the dependency for the MySQL driver and associate it with your `server` module.

- mysql:mysql-connector-java:8.0.29
  - Scope: Compile
