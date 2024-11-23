# Phase 4 Database — Debugging Tips

Since Phase 4 requires interacting with an external program (the MySQL database), the likelihood of having auto-grader test output different from your local output is higher than previous phases. The most common issues are summarized here to save you time spent frustrated with a new technology.

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

> [!CAUTION]
> **Windows** users are especially susceptible to this issue since Windows machines behave differently than the AutoGrader. Read this section carefully.

On the AutoGrader, SQL table names and SQL column names are **case-sensitive**. Some personal computers treat SQL names with case insensitivity; this leads to disparities when submitting your code.

- Double-check the casing in all of your SQL statements.
  - For example, if you have table `foobar`,
  - your machine may accept `INSERT INTO FooBar`,
  - but the auto-grader machine will not.
- Make sure each table and column name uses the same casing each time you use it.
