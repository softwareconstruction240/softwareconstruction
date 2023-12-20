# Getting Started

📁 [Starter code](starter-code)

1. Create the folder `client/src/main/java/ui`. This is where you will put your client interface code.
1. Copy the [EscapeSequences.java](starter-code/ui/EscapeSequences.java) file into your project's `client/src/main/java/ui` folder. This file defines values that you can use to control the coloration of your console output.
1. Create the folder `client/src/test/java/clientTests`. Mark the `client/src/test/java` directory as `Test sources root` by right clicking on the folder and selecting the `Mark Directory as` option.
1. Copy the [ServerFacadeTests.java](starter-code/clientTests/ServerFacadeTests.java) into the `client/src/test/java/clientTests` folder.

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
                └── ClientTests
                    └── ServerFacadeTests.java
```