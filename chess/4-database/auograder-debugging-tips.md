# Phase 4 Database — Auto Grader Debugging Tips

Since Phase 4 requires interacting with an external program (the MySQL database), the likelihood of having the tests on your local device pass while the auto-grader tests will fail is higher than previous phases. The most common issues are summarized here to save you time spent frustrated with a new technology.

> [!IMPORTANT]
> This guide assumes that your tests **are passing locally**. If your tests are not passing on your own computer, then you should definitely not assume they will pass on the AutoGrader.

#### Database and Table Creation

It's possible your server doesn't create the database and tables correctly when starting up.

Try the following steps to reproduce the problem on your local machine:
1. Drop your database/schema using an external tool (MySQL shell or workbench).
1. Rerun the tests locally.
1. If they fail, double-check the code for creating the database and tables.

#### Hardcoded Database Name

It's fun to assume that your database will always be named "chess", but the AutoGrader will break that assumption!

The auto-grader inserts a new `db.properties` file for grading with most of the values different from those in your file.

Run through the following verifications in your code:
- Check for any place you may have hardcoded any values from `db.properties`.
- Check each SQL statement, including where you create tables, for the database name.
  - For example, use `INSERT INTO table` instead of `INSERT INTO database.table`.
- The `getConnection` method inside `DatabaseManager` already sets up the connection to use your database,
  so you shouldn't need to specify the database name if you are using that method to obtain your connections.

#### CasE SEnSiTiVitY

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

#### Altering Tables

When your code creates the database and tables, it does so with the line of code `CREATE TABLE IF NOT EXISTS users`, this means that it will only reference the code afterwards if the table is not already made. 

- This means that if you change your create statement to store game as LONGTEXT instead of VARCHAR(100), this update will not be seen until you either `ALTER TABLE` or `DROP TABLE`
  - Alter Table allows you to change your table, such as to add a new column
  - Drop Table will completely delete your whole table, completely deleting all data currently on it, which would allow you to recreate it with your `CREATE TABLE` statement, which can include your newly updated column variable types
  - This [Git Hub Link](https://github.com/softwareconstruction240/softwareconstruction/blob/main/instruction/db-sql/db-sql.md#altering-tables) contains a bit more explanation and some examples, not only of altering tables, but other important information about mySQL Tables.


