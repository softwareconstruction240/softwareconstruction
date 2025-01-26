# Getting Started

The Starter Code has 2 folders, `client` and `ui`. Complete the following steps to move the starter code into your project for this phase.

1. Open your chess project directory.
1. Create the folder `client/src/main/java/ui`. This is where you will put your client application code.
1. Copy the `starter-code/5-pregame/ui/EscapeSequences.java` file into your project's `client/src/main/java/ui` folder. This file defines values that you can use to control the coloration of your console output.
1. Create the folder `client/src/test/java/client`. This is where you will put your client unit tests.
1. Mark the `client/src/test/java` directory as `Test sources root` by right clicking on the folder and selecting the `Mark Directory as` option. This enables IntelliJ to run this code as tests.
1. Copy the `starter-code/5-pregame/client/ServerFacadeTests.java` file into the `client/src/test/java/client` folder. This test class is meant to get you started on your server facade tests. It includes code for starting and stopping your HTTP server so that your tests can make server requests.

This should result in the following additions to your project.

```txt
└── client
    └── src
        ├── main
        │   └── java
        │       └── ui
        │           └── EscapeSequences.java
        └── test
            └── java
                └── client
                    └── ServerFacadeTests.java
```
