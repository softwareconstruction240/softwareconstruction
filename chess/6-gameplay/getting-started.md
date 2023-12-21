# Getting Started

📁 [Starter code](starter-code)

The Starter Code has 3 folders, `serverTests` and `webSocketMessages`. Do the following:

1. Copy the `serverTests/webSocketTests.java` file into your project’s `server/src/test/java/passoffTests/serverTests` folder. This class contains the pass off test cases that verify your server’s websocket interactions with clients.
1. Copy the `webSocketMessages` folder into your project’s `shared/src/main/java` folder. This folder contains the `UserGameCommand` and `ServerMessage` superclasses for the websocket message classes you will create.

This should result in the following additions to your project.

```txt
├── server
│   └── src
│       └── test
│           └── java
│               └── passoffTests
│                   └── serverTests
│                       └── WebSocketTests.java
└── shared
    └── src
        └── main
            └── java
                └── webSocketMessages
                    ├── serverMessages
                    │   └── ServerMessage.java
                    └── userCommands
                        └── UserGameCommand.java
```

## Dependencies

Add the following dependencies to the `client` module:

- **org.glassfish.tyrus.bundles:tyrus-standalone-client:1.15**
  - Scope: Compile

Add the following dependencies to the `server` module:

- **org.glassfish.tyrus.bundles:tyrus-standalone-client:1.15**
  - Scope: Test
