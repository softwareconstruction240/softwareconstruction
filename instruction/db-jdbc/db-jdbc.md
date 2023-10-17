# Relational Databases - JDBC

🖥️ [Slides - JDBC](https://docs.google.com/presentation/d/19nC7v6SDqoEeK75Mb-f6L3QhnbuP6Xfo)

Now that we have covered what relational databases are and how to use SQL to interact with them, it is time to discuss how to use SQL from a Java program. Java uses a standard interface library called Java Database Connector (JDBC). This library provides you with classes to connect to a database, execute SQL queries, and process the results.

We also discuss using the popular open source relational database software MySQL. You will install a MySQL server in your development environment and use it as the persistent data store for your chess program.

To actually create a connection to your database you must first download the MySQL JDBC driver jar. You can then use the standard JDK JDBC classes as shown in the following example:

```java
import java.sql.DriverManager;

public class DatabaseExample {
    public static void main(String[] args) throws Exception {
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "monkeypie")) {
            conn.setCatalog("pet_store");

            try (var preparedStatement = conn.prepareStatement("SELECT id, name, type from pet")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var id = rs.getInt("id");
                        var name = rs.getString("name");
                        var type = rs.getString("type");

                        System.out.printf("id: %d, name: %s, type: %s%n", id, name, type);
                    }
                }
            }
        }
    }
}
```

## Database Connectors

The JDK `java.sql` package contains interfaces and abstract classes for connecting to a RDBMS. However, you need to download a specific database connector that implements the interfaces for the RDBMS that you are using. For this course, we use the `mysql.connector`. Here is an example of using IntelliJ to import the MySQL connection into a project.

![MySQL connector](mysql-connector.png)

Most modern connector packages don't require you to initialize the connector. The act of including it in your classpath will do that automatically. Once you have installed the package for you project you are good to create connections with it.

## Obtaining a Connection

With the database specific connector installed, you are ready to use the JDK `DriverManager` class to get a connection to the database specified by a given URL. The DriverManager inspects the URL and passes the connection request over appropriate database connector. In the example below, the DriverManager will pass the request to the MySQL connector that registered itself when the connector package was loaded.

If the connector exists, and the RDBMS specified by the URL is available then you will get back a connection object.

Note that we use the Java `try-with-resource` syntax so that the connection will be released as soon as the try block exits. This is important, because the number of available connections is limited by the database server. If you don't release connections then any request for new connections will fail and you will no longer be able to use the database.

```java

Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "monkeypie");
}

void makeSQLCalls() throws SQLException {
    try (var conn = getConnection() {
        // Execute SQL statements on the connection here
    }
}
```

## Creating Databases and Tables

Once you have a connection you can use it to create databases and tables. It is a good idea to create Java code that does this either when your application starts up, or with an explicit initialization operation. This allows you to define all of the infrastructure that your code depends upon in the code that actually uses the infrastructure. That way you can always assume that the required databases and tables exist instead of managing and initializing that infrastructure as some sort of external manual executed process.

We can fully configure a theoretical pet store application by doing the following.

1. Get a connection to the RDBMS.
1. Create the the pet store database if it doesn't exist.
1. Create the pet table if it doesn't exist.

```java
    void configureDatabase() throws SQLException {
        try (var conn = getConnection()) {
            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS pet_store");
            createDbStatement.executeUpdate();

            conn.setCatalog("pet_store");

            var createPetTable = """
                CREATE TABLE  IF NOT EXISTS pet (
                    id INT NOT NULL AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    type VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id)
                )""";


            try (var createTableStatement = conn.prepareStatement(createPetTable)) {
                createTableStatement.executeUpdate();
            }
        }
    }
```

We execute SQL statements by first creating a `PreparedStatement`. You can think of a prepared statement as a SQL statement template. In the above code we create two prepared statements. One to create the database and one to create the table. Both of the statements are created with hard coded strings that represent the desired SQL to execute.

In the same way that you had to use a try-with-resource block to close our connection, you also need to make sure you close the prepared statement when we are done with it.

Make sure you note the use of the `setCatalog` call. We call this after we have made sure that the pet store database is created. Setting the catalog tells the connection that all subsequent calls should be done in the context of the given database. In the example, we are creating the `pet` table in the context of the `pet_store` database.

## Inserting Data

Once you have created your database and tables, you can now start inserting data. The following is an example of how to insert a new row in the `pet` table.

```java
int insertPet(Connection conn, String name, String type) throws SQLException {
    try (var preparedStatement = conn.prepareStatement("INSERT INTO pet (name, type) VALUES(?, ?)", RETURN_GENERATED_KEYS)) {
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, type);

        preparedStatement.executeUpdate();

        var resultSet = preparedStatement.getGeneratedKeys();
        var ID = 0;
        if (resultSet.next()) {
            ID = resultSet.getInt(1);
        }

        return ID;
    }
}
```

This code starts by creating a new prepared statement that contains the `INSERT` command. Note however that instead of concatenating the pet name and type directly into the command, we parameterize the prepared statement by putting question marks (`?`) into the statement syntax. We then supply the values of the parameter with the `setString` method on the prepared statement.

The first parameter to setString tells the position of the parameter to set and the second parameter is the value to set. This corresponds to the position of the question marks in the prepared statement.

```java
preparedStatement.setString(1, name);
```

There are `set` methods for all the SQL types (e.g. `setInt`, `setDate`, `setBoolean`, ...). Make sure you use the one that conforms with the schema of the table field you are populating.

Once you have created the statement and populated the parameters you then execute the statement by calling `executeUpdate`. If this doesn't throw an exception then the data was successfully inserted.

### Generating Primary Keys

Our insertPet function does one more thing. If you look at the table schema that we defined when we created the pet table, you will see that we set the `id` column to `AUTO_INCREMENT` the value.

```sql
CREATE TABLE  IF NOT EXISTS pet (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
)
```

We use this to let the database handle the assignment of the primary key for the pet record. That means that we don't have to provide that as a field when when insert a new pet, but we do potentially want to know what ID was generated by the database. To get the generated value we specify the parameter `RETURN_GENERATED_KEYS` when we create the prepared statement. We then call `getGeneratedKeys` after we called `executeUpdate`. This returns an iterator, but we are only interested in the first value and so we advance the iterator with `next` and the call `getInt` to get the new ID

```java
var resultSet = preparedStatement.getGeneratedKeys();
var ID = 0;
if (resultSet.next()) {
    ID = resultSet.getInt(1);
}
```

## Protecting Against SQL Injections

The reason why you want to use the set functions on a prepared statement is to prevent against what is know as a SQL injection. Consider the case where we simplified our insertion function to the following.

```java
void sqlInjection(Connection conn, String name, String type) throws SQLException {
    try (var preparedStatement = conn.prepareStatement("INSERT INTO pet (name, type) VALUES(" + name + "," + type + ")")) {
        preparedStatement.executeUpdate();
    }
}
```

This would actually work fine in the normal case and simplify our code somewhat.

The problem occurs when someone supplies the name following name:

```text
'joe','cat');INSERT INTO pet (name, type) VALUES('die','frog'
```

## Updates

## Deleting Data

## Queries

### Reading Complex Data

## Type Adapters

## Things to Understand

- How to execute each of the SQL statements from Java using JDBC
- How to use JDBC connections
- How to generate Java objects from the result of a database query
- How to retrieve auto-increment primary keys that were generate by the database on an insert
- How to get a JDBC driver for MySQL and make it available to your Java project/code

## Videos

- 🎥 [Java Database Access with JDBC](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=cabe9971-3ff7-4579-be2e-ad660156090a&start=0)
- 🎥 [JDBC - Putting it All Together](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=e5d62c40-3494-4ff9-9558-ad6b013cdfb6&start=0)
