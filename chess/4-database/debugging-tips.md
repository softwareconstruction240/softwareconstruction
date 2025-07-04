# Phase 4 Database — Debugging Tips

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


