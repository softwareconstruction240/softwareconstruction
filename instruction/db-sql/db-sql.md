# Relational Databases - SQL

🖥️ [Slides](https://docs.google.com/presentation/d/19nC7v6SDqoEeK75Mb-f6L3QhnbuP6Xfo/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

`Structure Query Language` (SQL) is a programming language that is specifically designed to interact with relational databases. It contains instructions for inserting, updating, reading, and deleting data. It also provides instructions for administrating the database and the users that have access to the database.

Here is an example of SQL statements for creating a database, creating at table, and inserting some sample data.

```sql
> CREATE DATABASE petshop;
> USE petshop;
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
| 93     | Fido	    | dog           |
| 14     | Puddles  | cat           |
| 77     | Chip	    | bird          |
+--------+----------+---------------+
```

## Table of Common SQL Commands

## Working with Databases

## Creating a Table

### Types

## Inserting Data

## Querying a Table

## Joining Tables

## Dropping a Table

## Things to Understand

- How to create and drop tables
- Why drop table if exists is useful
- How to insert, update, delete and retrieve data from a database table
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
