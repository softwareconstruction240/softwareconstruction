# Relational Databases - JDBC and MySQL

🖥️ [Slides - JDBC](https://docs.google.com/presentation/d/19nC7v6SDqoEeK75Mb-f6L3QhnbuP6Xfo/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

🖥️ [Slides - MySQL](https://docs.google.com/presentation/d/1w5bcntrExgMnB92uLJL52uuutLLQABSt/edit?usp=sharing&ouid=114081115660452804792&rtpof=true&sd=true)

Now that we have covered what relational databases are and how to use SQL to interact with them, it is time to discuss how to use SQL from a Java program. Java uses a standard interface library called Java Database Connector (JDBC). This library provides you with classes to connect to a database, execute SQL queries, and process the results.

We also discuss using the popular open source relational database software MySQL. You will install a MySQL server in your development environment and use it as the persistent data store for your chess program.

To actually create a connection to your database you must first download the MySQL JDBC driver jar. You can then use the standard JDK JDBC classes as shown in the following example:

```java
import java.sql.DriverManager;

public class DatabaseExample {
    public static void main(String[] args) throws Exception {
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "monkeypie")) {
            conn.setCatalog("petshop");

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

## Things to Understand

- How to execute each of the SQL statements from Java using JDBC
- How to use JDBC connections
- How to generate Java objects from the result of a database query
- How to retrieve auto-increment primary keys that were generate by the database on an insert
- How to get a JDBC driver for MySQL and make it available to your Java project/code

## Videos

- 🎥 [Java Database Access with JDBC](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=cabe9971-3ff7-4579-be2e-ad660156090a&start=0)
- 🎥 [JDBC - Putting it All Together](https://byu.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=e5d62c40-3494-4ff9-9558-ad6b013cdfb6&start=0)
