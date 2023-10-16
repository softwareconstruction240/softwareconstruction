# Relational Databases - SQL

🖥️ [Slides](https://docs.google.com/presentation/d/19nC7v6SDqoEeK75Mb-f6L3QhnbuP6Xfo/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

`Structure Query Language` (SQL) is a programming language that is specifically designed to interact with relational databases. It contains instructions for inserting, updating, reading, and deleting data. It also provides instructions for managing the database and the users that have access to the database.

Here is an example of SQL statements for creating a database, creating at table, and inserting some sample data.

```sql
> CREATE DATABASE pet_store;
> USE pet_store;
> CREATE TABLE pet (id int, name varchar(255), type varchar(255));
> INSERT INTO pet VALUES (93, "Fido", "dog");
> INSERT INTO pet VALUES (14, "Puddles", "cat");
> INSERT INTO pet VALUES (77, "Chip", "bird");
> DESCRIBE pet;

+-------+--------------+------+-----+---------+-------+
| Field | Type         | Null | Key | Default | Extra |
+-------+--------------+------+-----+---------+-------+
| id    | int          | YES  |     | NULL    |       |
| name  | varchar(255) | YES  |     | NULL    |       |
| type  | varchar(255) | YES  |     | NULL    |       |
+-------+--------------+------+-----+---------+-------+
```

The following demonstrates a simple query to read all of the pets in a database.

```sql
> SELECT id, name, type FROM pet;

+--------+----------+---------------+
| id     | name     | type          |
+--------+----------+---------------+
| 93     | Fido|    | dog           |
| 14     | Puddles  | cat           |
| 77     | Chip|    | bird          |
+--------+----------+---------------+
```

SQL is a declarative programming language. This means that you declare what you want rather than providing a series of imperative commands that define how to do something.

In order to completely understand the above example we need to take a step back and examine some of the common SQL statements.

## Working with Databases

A database server, or RDBMS, can host one or more databases. You create a database with the `CREATE DATABASE` statement. After you have created the database you use the `USE` statement to select the database for use with future commands.

```sql
CREATE DATABASE pet_store;
USE pet_store;
```

All databases have a default character set that is used for representing bytes as text. Normally if you would specify the character set when you execute the CREATE request, but if you didn't then you can alter the database with the `ALTER DATABASE` statement. For example, if you wanted to represent text with UTF-8, we would run the following ALTER statement.

```sql
ALTER DATABASE pet_store CHARACTER SET utf8mb4;
```

If you want to delete a database, and all of the data it represents, you use the `DROP DATABASE` statement. Be careful with this statement. Once you run it there is no going back. The data is gone forever.

```sql
DROP DATABASE pet_store
```

## Working with Tables

Now that you have a database you can create a table to hold the rows that represent your objects. Remember that a table is similar to declaring a Java class. When you create a table, you must specify each field and the type of the field.

```sql
CREATE TABLE pet (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
```

Notice that each field is followed by the `NOT NULL` clause. That means each of the fields must be provided for every row that is inserted.

The `id` field is also annotated with the `AUTO_INCREMENT` keyword. This means that you don't actually provide the `id` field when you insert a row. The database will do that for you using an automatically increase integer.

If you need to alter your table you can use an `ALTER TABLE` statement. The following example shows you how to add a `nickname` field after the table is created. This alteration does not use the `NOT NULL` clause and so all of the existing nickname fields will be set to NULL. If a new row is added without specifying the nickname field, it will also be set to NULL.

```sql
ALTER TABLE pet ADD COLUMN nickname VARCHAR(255);
```

### Primary Keys and Indexes

Along with specifying each field, the `CREATE TABLE` statement specifies which field is the primary key of the table. Primary keys are required to be unique. If you attempt to insert two rows with the same key, an error will occur. Primary keys are also indexed by default since it is assumed that you will use the key to query the table.

With an index on a field, the performance of your queries will significantly increase, but you also consume storage and memory for each index you allocate. That means you should only create indexes when you can demonstrate that it is necessary for performance reasons.

In addition to creating an index by specifying the primary key field, you can create indexes on other fields by specifying the `INDEX` keyword followed by the field you want to index. In the example below we added an index on the `name` field with the last clause in the statement.

```sql
CREATE TABLE pet (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    INDEX (name)
);
```

If you determine you need an index after you create a table, you can use the `CREATE INDEX` statement along with the table and field you want to index.

```sql
CREATE INDEX index_name ON pet (name);
```

If you decide that you want to delete a table then you execute a `DROP TABLE` statement.

```sql
DROP TABLE pet;
```

Make sure you really want to drop the table before you execute the command, because there is no recovery from this one.

### Types

Here is a list of the common SQL data types that you can use when you create a table.

| Data type | Description                              | Example                                                   |
| --------- | ---------------------------------------- | --------------------------------------------------------- |
| INT       | Integer numbers.                         | 1, 10, -100                                               |
| DECIMAL   | Fixed-point decimal numbers.             | 1.23, 100.00, -5.6789                                     |
| FLOAT     | Single-precision floating-point numbers. | 1.23456789, 100.000000, -5.67890123                       |
| DOUBLE    | Double-precision floating-point numbers. | 1.234567890123456, 100.00000000000000, -5.678901234567890 |
| CHAR      | Fixed-length character strings.          | 'A', 'Hello, world!', '1234567890'                        |
| VARCHAR   | Variable-length character strings.       | 'A', 'Hello, world!', '1234567890'                        |
| TEXT      | Variable-length text data.               | 'This is a long text string that can be any length.'      |
| DATE      | Date values.                             | '2023-10-14'                                              |
| TIME      | Time values.                             | '15:43:45'                                                |
| DATETIME  | Date and time values.                    | '2023-10-14 15:43:45'                                     |

## Inserting, Updating, and Deleting Data

Now that you have a database and a table, it is time to insert some data. You can insert data into a table with an `INSERT` statement. This statement takes the name of the table, the fields you want to insert, and the values for those fields. If you created the fields with the `NOT NULL` annotation then you must supply all the non-null fields during the insertion.

```sql
INSERT INTO pet (name, type) VALUES ('Puddles', 'cat');
```

If you need to update an existing row then you use an `UPDATE` statement along with the names and values of the fields you want to update. When updating a row you want to be careful to specify which rows to update with a `WHERE` clause. If you don't specify which rows to update, then all the rows will be updated. In the example below, only the row with an `id` equal to 1 will be updated to set the pet name to `fido`.

```sql
UPDATE pet SET name = 'fido' WHERE id = 1;
```

When you want to delete some rows, you use a `DELETE` statement and specify a `WHERE` clause to select the rows to delete. The following will delete all rows where the pet is a `cat`.

```sql
DELETE FROM pet WHERE type = 'cat';
```

If you want to delete **all** data from a table, then use the `TRUNCATE` statement. This will delete all of the table's data, but not the table itself.

```sql
TRUNCATE TABLE pet;
```

## Selecting Data

The `SELECT` statement provides the primary mechanism for querying data from a SQL compliant RDBMS. Here is an example of a simple SELECT that returns all the names and types for every pet.

```sql
SELECT name, type FROM pet;
```

If you want to select specific pets, then you include a WHERE clause. WHERE clauses can be very complex. They can include boolean predicates, wildcards. You can also supply a `LIMIT` on the amount of data to return. Here is an example of a SELECT that returns a maximum of two rows that are either dogs with any name, or cats named Puddle.

```sql
SELECT name, type FROM pet WHERE type='dog' OR (type='cat' AND name='Puddle') LIMIT 2;
```

You can select all rows in a table with the `*` query. You can also use the `COUNT` predicate to tell you how many rows there are in a table.

```sql
SELECT * from pet;
SELECT COUNT(0) from pet;
```

## Joining Tables

If you want to combine tables to compute a temporary different view of the data then you would use a `JOIN` clause along with the names of the two tables that you want to join. The following example would join together the purchase and pet tables into rows with matching pet IDs.

```sql
SELECT purchase.id AS purchaseId, purchase.ownerId, pet.id AS petId, pet.name, pet.type
 FROM purchase JOIN pet WHERE purchase.petId = pet.id;
```

Given the follow source tables,

**Source**

```sql
+----+-------+---------+
| id | petId | ownerId |
+----+-------+---------+
|  1 |   890 |       3 |
|  2 |   891 |       3 |
|  3 |   895 |       4 |
+----+-------+---------+

+-----+---------+------+----------+
| id  | name    | type | nickname |
+-----+---------+------+----------+
| 890 | Puddles | cat  | NULL     |
| 891 | Fluffy  | cat  | NULL     |
| 892 | Willie  | cat  | NULL     |
| 893 | George  | bird | NULL     |
| 894 | Buddy   | dog  | NULL     |
| 895 | Fido    | dog  | NULL     |
+-----+---------+------+----------+
```

this would be the result of our JOIN SELECT statement.

**Result**

```sql
+------------+---------+-------+---------+------+
| purchaseId | ownerId | petId | name    | type |
+------------+---------+-------+---------+------+
|          1 |       3 |   890 | Puddles | cat  |
|          2 |       3 |   891 | Fluffy  | cat  |
|          3 |       4 |   895 | Fido    | dog  |
+------------+---------+-------+---------+------+
```

## Initializing Your Database

When you are using a database to store your application data, it is often useful to make sure all of your databases and tables exist when you start up. You can do this by executing `CREATE DATABASE` and `CREATE TABLE` calls at the beginning of your application. You can conditionally create these objects with the `IF NOT EXISTS` clause. With that clause, the statement is simply ignored if the structure already exists.

```sql
CREATE DATABASE IF NOT EXISTS petshop;
USE petshop;
CREATE TABLE  IF NOT EXISTS pet (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
```

By following this pattern, your application will always work even when it starts up using a database server that has not yet been initialized. This pattern is called `infrastructure as code` because it treats your configuration as code, removes the human error factor, and tracks the history of the infrastructure changes with the same version control process that your code uses.

## Table of Common SQL Commands

The follow table summaries all of the commands that were used in this instruction topic.

| Command         | Purpose                                                                 | Example statement                                                                                                            |
| --------------- | ----------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------- |
| CREATE DATABASE | Creates a new database.                                                 | CREATE DATABASE pet_store;                                                                                                   |
| ALTER DATABASE  | Modifies the structure of a database.                                   | ALTER DATABASE pet_store CHARACTER SET utf8mb4;                                                                              |
| DROP DATABASE   | Deletes a database.                                                     | DROP DATABASE pet_store;                                                                                                     |
| USE DATABASE    | Selects a database for use with future commands.                        | USE pet_store;                                                                                                               |
| CREATE TABLE    | Creates a new table in a database.                                      | CREATE TABLE pet (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, PRIMARY KEY (id)); |
| DESCRIBE TABLE  | Describes the fields in a table.                                        | DESCRIBE pet;                                                                                                                |
| ALTER TABLE     | Modifies the structure of a table.                                      | ALTER TABLE pet ADD COLUMN nickname VARCHAR(255);                                                                            |
| DROP TABLE      | Deletes a table from a database.                                        | DROP TABLE pet;                                                                                                              |
| INSERT INTO     | Inserts new data into a table.                                          | INSERT INTO pet (name, type) VALUES ('Puddles', 'cat');                                                                      |
| SELECT          | Select data from a table.                                               | SELECT name, type FROM pet;                                                                                                  |
| UPDATE          | Updates existing data in a table.                                       | UPDATE pet SET name = 'fido' WHERE id = 1;                                                                                   |
| DELETE          | Deletes data from a table.                                              | DELETE FROM pet WHERE id = 1;                                                                                                |
| CREATE INDEX    | Creates an index on a column or columns in a table.                     | CREATE INDEX pet_name_index ON pet (name);                                                                                   |
| DROP INDEX      | Deletes an index from a table.                                          | DROP INDEX pet_name_index;                                                                                                   |
| TRUNCATE TABLE  | Deletes all rows from a table, but preserves the table structure.       | TRUNCATE TABLE pet;                                                                                                          |
| CREATE VIEW     | Creates a virtual table that is based on one or more underlying tables. | CREATE VIEW cats AS SELECT \* FROM pet WHERE type = 'cat';                                                                   |
| DROP VIEW       | Deletes a view.                                                         | DROP VIEW cats;                                                                                                              |

## Things to Understand

- How to create and drop tables
- Why drop table if exists is useful
- How to insert, update, delete, and retrieve data from a database table
- How to retrieve data from multiple related tables using a join
- What database transactions are and why we need them

## Videos

- 🎥 [Structure Query Language](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=4f5a6bfe-5170-4c3c-97e8-ad660148d05a&start=0)
- 🎥 [SQL Data Types](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=91cb451f-fc88-426d-9656-ad660149a253&start=0)
- 🎥 [Creating and Dropping Tables](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=2b252adc-3c54-4f7e-bea0-ad66014b3c16&start=0)
- 🎥 [Inserting, Updating, and Deleting Rows](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=02edabc8-3424-4d56-a86b-ad66014f13c3&start=0)
- 🎥 [Retrieving Data with SQL Queries](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=e7083e5f-66a8-425f-be92-ad6601513cbd&start=0)
- 🎥 [Database Transactions](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6e75d23e-4075-4a27-85d0-ad6601548134&start=0)

## Demonstration code

📁 [Book.java](example-code/Book.java)

📁 [DatabaseAccessExample.java](example-code/DatabaseAccessExample.java)

📁 [create-db.sql.txt](example-code/create-db.sql.txt)
