# Relational Databases - SQL

🖥️ [Slides](https://docs.google.com/presentation/d/1_kGyItbM3fp0PQSemvBocclT8EU87rBX)

🖥️ [Lecture Videos](#videos)

### 🔑 Key points

- Creating and dropping tables
- Why `DROP TABLE IF EXISTS` is useful for idempotency
- Inserting, updating, deleting, and retrieving data from a database table
- Retrieving data from multiple related tables using a `JOIN`
- Understanding database transactions and why they are necessary

---

**Structured Query Language** (SQL) is a domain-specific programming language designed for managing data held in a relational database management system (RDBMS). It includes statements for inserting, updating, reading, and deleting data, as well as commands for managing the database structure and user access.

SQL statements are commonly categorized into three main groups:

1.  **DDL** (Data Definition Language): Used to define or modify the database structure (e.g., `CREATE`, `ALTER`, `DROP`).
2.  **DML** (Data Manipulation Language): Used to manage data within the objects (e.g., `INSERT`, `UPDATE`, `DELETE`).
3.  **DQL** (Data Query Language): Used to retrieve data from the database (e.g., `SELECT`).

The following example demonstrates basic SQL statements used to create a database, define a table, and insert sample data.

```sql
-- Create and select the database
CREATE DATABASE pet_store;
USE pet_store;

-- Create a basic table structure
CREATE TABLE pet (
    id INT, 
    name VARCHAR(255), 
    type VARCHAR(255)
);

-- Insert sample records
INSERT INTO pet VALUES (93, 'Fido', 'dog');
INSERT INTO pet VALUES (14, 'Puddles', 'cat');
INSERT INTO pet VALUES (77, 'Chip', 'bird');

-- View the table schema
DESCRIBE pet;
```

**Output of DESCRIBE:**
```text
+-------+--------------+------+-----+---------+-------+
| Field | Type         | Null | Key | Default | Extra |
+-------+--------------+------+-----+---------+-------+
| id    | int          | YES  |     | NULL    |       |
| name  | varchar(255) | YES  |     | NULL    |       |
| type  | varchar(255) | YES  |     | NULL    |       |
+-------+--------------+------+-----+---------+-------+
```

The following query retrieves specific columns for all pets:

```sql
SELECT id, name, type FROM pet;
```

**Result:**
```text
+--------+----------+---------------+
| id     | name     | type          |
+--------+----------+---------------+
| 93     | Fido     | dog           |
| 14     | Puddles  | cat           |
| 77     | Chip     | bird          |
+--------+----------+---------------+
```

SQL is a **declarative** language. Unlike imperative languages (like Java or Python) where you write step-by-step instructions on *how* to perform a task, in SQL you declare *what* data you want, and the database engine determines the most efficient way to retrieve it.

## Working with Databases

A database server (RDBMS) can host multiple independent databases. You create a database with the `CREATE DATABASE` statement. After creation, use the `USE` statement to set it as the active database for subsequent commands.

```sql
CREATE DATABASE pet_store;
USE pet_store;
```

Databases use a character set to define how text is encoded. While you can specify this during creation, you can also modify an existing database using `ALTER DATABASE`. For full international character and emoji support, it is common to use `utf8mb4`:

```sql
ALTER DATABASE pet_store CHARACTER SET utf8mb4;
```

To delete a database and all of its data, use the `DROP DATABASE` statement. **Warning:** This action is permanent and cannot be undone.

```sql
DROP DATABASE pet_store;
```

## Working with Tables

Once a database is selected, you can create tables to organize your data into rows and columns. A table definition is conceptually similar to a class in Java; you must specify each field (column) and its data type.

```sql
CREATE TABLE pet (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
```

In this example:
- The `NOT NULL` constraint ensures that a value must be provided for that column in every row.
- The `AUTO_INCREMENT` keyword allows the database to automatically generate a unique, increasing integer for each new row.
- The `PRIMARY KEY` constraint uniquely identifies each record in the table.

### Altering Tables

To modify an existing table's structure, use `ALTER TABLE`. The following example adds a `nickname` column. Since this does not include a `NOT NULL` clause, existing rows will have their `nickname` set to `NULL` by default.

```sql
ALTER TABLE pet ADD COLUMN nickname VARCHAR(255);
```

To delete a table entirely, use `DROP TABLE`.

```sql
DROP TABLE pet;
```

Rerunning a `CREATE TABLE` statement will result in an error if the table already exists. To make scripts more robust, use the `IF NOT EXISTS` qualifier. To change a table's definition after it is created, you must either `DROP` and recreate it or use the `ALTER TABLE` command.

### Primary Keys and Indexes

A **primary key** is a column (or group of columns) that uniquely identifies each row. Attempting to insert a duplicate value into a primary key column will result in an error.

Primary keys are **indexed** by default. Indexes are data structures that significantly improve data retrieval performance, allowing the database to find rows without scanning the entire table. However, indexes consume extra storage space and can slightly slow down write operations (`INSERT`, `UPDATE`). You should create indexes on columns frequently used in `WHERE` clauses or `JOIN` conditions.

```sql
-- Creating an index during table creation
CREATE TABLE pet (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    INDEX (name)
);

-- Adding an index to an existing table
CREATE INDEX idx_pet_name ON pet (name);
```

### Common SQL Data Types

| Data type | Description | Example |
| :--- | :--- | :--- |
| **INT** | Whole numbers. | 1, 10, -100 |
| **DECIMAL** | Fixed-point numbers (used for exact precision like currency). | 1.23, 100.00 |
| **FLOAT** | Single-precision floating-point numbers. | 1.234567 |
| **DOUBLE** | Double-precision floating-point numbers. | 1.234567890123 |
| **CHAR** | Fixed-length strings (padded with spaces). | 'A', 'USA' |
| **VARCHAR** | Variable-length strings. | 'Fido', 'Hello World' |
| **TEXT** | Large amounts of variable-length text data. | (A long article) |
| **BLOB** | Binary Large Object (for images or raw files). | (Binary data) |
| **DATE** | Date values (YYYY-MM-DD). | '2023-10-14' |
| **TIME** | Time values (HH:MM:SS). | '15:43:45' |
| **DATETIME** | Combined date and time values. | '2023-10-14 15:43:45' |

## Inserting, Updating, and Deleting Data

Use the `INSERT` statement to add new rows to a table.

```sql
INSERT INTO pet (name, type) VALUES ('Puddles', 'cat');
```

To modify existing data, use the `UPDATE` statement. It is critical to include a `WHERE` clause; if you omit it, **every row** in the table will be updated.

```sql
UPDATE pet SET name = 'Fido' WHERE id = 1;
```

To remove rows, use the `DELETE` statement. Like `UPDATE`, a `WHERE` clause is necessary to avoid deleting all data in the table.

```sql
DELETE FROM pet WHERE type = 'cat';
```

If you want to delete **all** rows from a table while keeping its structure and resetting any auto-increment counters, use `TRUNCATE`.

```sql
TRUNCATE TABLE pet;
```

## Selecting Data

The `SELECT` statement is used to query data. A basic `SELECT` returns specific columns for every row:

```sql
SELECT name, type FROM pet;
```

You can filter results using `WHERE`, sort them with `ORDER BY`, and restrict the number of results with `LIMIT`.

```sql
SELECT name, type 
FROM pet 
WHERE type = 'dog' OR (type = 'cat' AND name = 'Puddles') 
ORDER BY name ASC
LIMIT 2;
```

Use `*` to select all columns and `COUNT()` to return the total number of rows matching your criteria.

```sql
SELECT * FROM pet;
SELECT COUNT(*) FROM pet WHERE type = 'dog';
```

## Joining Tables

In relational databases, data is often normalized—split into multiple tables—to reduce redundancy. To combine data from related tables, use a `JOIN` clause. This links rows based on a common column, typically a foreign key.

```sql
SELECT purchase.id AS purchaseId, purchase.ownerId, pet.id AS petId, pet.name, pet.type
FROM purchase 
JOIN pet ON purchase.petId = pet.id;
```

**Source Tables:**

*Purchase Table*
```text
+----+-------+---------+
| id | petId | ownerId |
+----+-------+---------+
|  1 |   890 |       3 |
|  2 |   891 |       3 |
|  3 |   895 |       4 |
+----+-------+---------+
```

*Pet Table*
```text
+-----+---------+------+
| id  | name    | type |
+-----+---------+------+
| 890 | Puddles | cat  |
| 891 | Fluffy  | cat  |
| 895 | Fido    | dog  |
+-----+---------+------+
```

**Joined Result:**
```text
+------------+---------+-------+---------+------+
| purchaseId | ownerId | petId | name    | type |
+------------+---------+-------+---------+------+
|          1 |       3 |   890 | Puddles | cat  |
|          2 |       3 |   891 | Fluffy  | cat  |
|          3 |       4 |   895 | Fido    | dog  |
+------------+---------+-------+---------+------+
```

## Database Transactions

A **transaction** is a sequence of SQL operations treated as a single unit of work. Transactions ensure data integrity by adhering to **ACID** properties:
- **Atomicity:** All operations in the transaction succeed, or none do.
- **Consistency:** The database moves from one valid state to another, following all defined rules.
- **Isolation:** Concurrent transactions do not interfere with each other's intermediate states.
- **Durability:** Once a transaction is committed, the changes are permanent, even in the event of a system failure.

In SQL, you start a transaction with `START TRANSACTION`, then execute your commands. Use `COMMIT` to save the changes permanently or `ROLLBACK` to undo them if an error occurs.

## Initializing Your Database

When developing applications, it is best practice to ensure the database schema is automatically created upon startup. Using the `IF NOT EXISTS` clause makes scripts **idempotent**, meaning they can be run multiple times without causing errors or duplicate structures.

```sql
CREATE DATABASE IF NOT EXISTS pet_store;
USE pet_store;

CREATE TABLE IF NOT EXISTS pet (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
```

This approach is a form of **Infrastructure as Code** (IaC). By treating your schema configuration as code, you can track changes in version control and automate the setup of development, testing, and production environments.

You can execute SQL scripts from a file using a command-line client:

```sh
mysqlsh -u username -p --sql < initialize.sql
```

## Table of Common SQL Commands

| Command | Purpose | Example |
| :--- | :--- | :--- |
| **CREATE DATABASE** | Creates a new database. | `CREATE DATABASE store;` |
| **DROP DATABASE** | Deletes a database and its data. | `DROP DATABASE store;` |
| **USE** | Sets the active database. | `USE store;` |
| **CREATE TABLE** | Defines a new table. | `CREATE TABLE pet (id INT);` |
| **DESCRIBE** | Displays table structure. | `DESCRIBE pet;` |
| **ALTER TABLE** | Modifies table structure. | `ALTER TABLE pet ADD age INT;` |
| **DROP TABLE** | Deletes a table and its data. | `DROP TABLE pet;` |
| **INSERT INTO** | Adds new rows. | `INSERT INTO pet (name) VALUES ('Fido');` |
| **SELECT** | Retrieves data. | `SELECT * FROM pet;` |
| **UPDATE** | Modifies existing rows. | `UPDATE pet SET name='Rex' WHERE id=1;` |
| **DELETE** | Removes specific rows. | `DELETE FROM pet WHERE id=1;` |
| **TRUNCATE TABLE** | Empties a table but keeps structure. | `TRUNCATE TABLE pet;` |
| **CREATE INDEX** | Optimizes data retrieval. | `CREATE INDEX idx_n ON pet (name);` |
| **CREATE VIEW** | Creates a virtual table from a query. | `CREATE VIEW dogs AS SELECT * FROM pet WHERE type='dog';` |

## ☑ Exercise

````masteryls
{"id":"b99232fb-8952-4891-820d-3d43c212c380","title":"Interpreting a Basic SELECT Statement","type":"multiple-choice"}
Consider the following MySQL query:

```sql
SELECT title, author
FROM books
WHERE pages > 300
ORDER BY title DESC;
```

What will be the result of executing this query?

- [ ] It returns every column for books that have 300 or more pages, sorted alphabetically by title from A to Z.
- [ ] It returns only the `title` and `author` for every book in the table, regardless of page count, sorted by title.
- [x] It returns the `title` and `author` of books with more than 300 pages, sorted by the title in reverse alphabetical order.
- [ ] It updates the `books` table to set the page count to 300 for any book where the title starts with the letter D.
````

```masteryls
{"id":"6752ac49-2730-4b99-bb34-51e62dcf5bc3","title":"Precision and Care","type":"essay"}
How does writing precise, correct SQL queries reflect personal integrity, and how does such careful work serve the people who depend on the answers your queries produce?
```

## Videos

- 🎥 [Structured Query Language (2:14)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=4f5a6bfe-5170-4c3c-97e8-ad660148d05a&start=0) - [[transcript]](https://github.com/user-attachments/files/17780823/CS_240_Structured_Query_Language_.SQL.pdf)
- 🎥 [SQL Data Types (3:48)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=91cb451f-fc88-426d-9656-ad660149a253&start=0) - [[transcript]](https://github.com/user-attachments/files/17737747/CS_240_SQL_Data_Types_Transcript.pdf)
- 🎥 [Creating and Dropping Tables (14:22)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=1f257807-3728-4239-a82b-b1a0011ad706&start=0) - [[transcript]](https://github.com/user-attachments/files/17737753/CS_240_Creating_and_Dropping_Tables_Transcript.pdf)
- 🎥 [Inserting, Updating, and Deleting Rows (6:40)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=203b8c65-d726-4282-8912-b1a0011f18e5&start=0) - [[transcript]](https://github.com/user-attachments/files/17737783/CS_240_Inserting_Updating_and_Deleting_Rows_Transcript.pdf)
- 🎥 [Retrieving Data with SQL Queries (11:15)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=e7083e5f-66a8-425f-be92-ad6601513cbd&start=0) - [[transcript]](https://github.com/user-attachments/files/17780824/CS_240_Retrieving_Data_with_SQL_Queries.pdf)
- 🎥 [Database Transactions (3:46)](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6e75d23e-4075-4a27-85d0-ad6601548134&start=0)- [[transcript]](https://github.com/user-attachments/files/17780825/CS_240_Database_Transactions.pdf)