# Getting Started

The Starter Code has three folders, `resources`, `dataaccess`, and `passoff/server`. Complete the following steps to move the starter code into your project for this phase.

1. Open your chess project directory.
1. Copy the `starter-code/4-database/resources/db.properties` file into your project’s `server/src/main/resources` folder. This contains your database configuration settings. You will need to replace the values with your database username and password.
1. Copy the `starter-code/4-database/dataaccess/DatabaseManager.java` file from the starter-code into your project's `server/src/main/java/dataaccess` folder. This contains code that will read your database configuration settings and create connections to your database server.
1. Copy the `starter-code/4-database/passoff/server/DatabaseTests.java` file into your project’s `server/src/test/java/passoff/server` folder. This contains a test that makes sure you are persisting information to your database.

This should result in the following additions to your project.

```txt
└── server
    └── src
        ├── main
        │   ├── java
        │   │   └── dataaccess
        │   │       └── DatabaseManager.java
        │   └── resources
        │       └── db.properties
        └── test
            └── java
                └── passoff
                    └── server
                        └── DatabaseTests.java
```

## Dependencies

Add the dependency for the MySQL driver and BCrypt. Associate them with your `server` module.

- mysql:mysql-connector-java:8.0.30

  - Scope: Compile

- org.mindrot:jbcrypt:0.4

  - Scope: Compile
