# Getting Started

📁 [Starter code](starter-code)

The Starter Code has 3 folders, `server` and `websocket`. Do the following:

1. Copy the `passoff/server/WebSocketTests.java` file into your project’s `server/src/test/java/passoff/server` folder. This class contains the pass off test cases that verify your server’s websocket interactions with clients.
1. Copy the `websocket` folder into your project’s `shared/src/main/java` folder. This folder contains the `UserGameCommand` and `ServerMessage` superclasses for the websocket message classes you will create.

This should result in the following additions to your project.

```txt
├── server
│   └── src
│       └── test
│           └── java
│               └── passoff
│                   └── server
│                       └── WebSocketTests.java
└── shared
    └── src
        └── main
            └── java
                └── websocket
                    ├── messages
                    │   └── ServerMessage.java
                    └── commands
                        └── UserGameCommand.java
```

## Dependencies

Add the following dependencies to the `client` and `server` modules:

- **org.glassfish.tyrus.bundles:tyrus-standalone-client:1.15**
  - Scope: Compile
