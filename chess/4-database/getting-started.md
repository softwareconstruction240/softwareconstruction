# Getting Started

📁 [Starter code](starter-code)

The Starter Code has two folders, `dataAccess` and `serverTests`. Do the following:

1. Copy the [resources/db.properties](starter-code/resources/db.properties) file into your project’s `server/src/main/resources` folder. This contains your database configuration settings. You will need to replace the values with your database username and password.
1. Copy the [dataAccess/DatabaseManager.java](starter-code/dataAccess/DatabaseManager.java) file from the starter-code into your project's `server/src/main/java/dataAccess` folder. This contains code that will read your database configuration settings and create connections to your database server.
1. Copy the [persistenceTests.java](starter-code/serverTests/PersistenceTests.java) file into your project’s `server/src/test/java/passoffTests/serverTests` folder. This contains a test that makes sure you are persisting information to your database.

This should result in the following additions to your project.

```txt
└── server
    └── src
        ├── main
        │   ├── java
        │   │   └── dataAccess
        │   │       └── DatabaseManager.java
        │   └── resources
        │       └── db.properties
        └── test
            └── java
                └── passoffTests
                    └── serverTests
                        └── PersistenceTests.java
```

## Dependencies

Add the dependency for the MySQL driver and BCrypt. Associate them with your `server` module.

- mysql:mysql-connector-java:8.0.30

  - Scope: Compile

- org.springframework.security:spring-security-core:6.2.1

  - Scope: Compile
