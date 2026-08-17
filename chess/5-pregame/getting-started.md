# Phase 5: Getting Started

The starter code contains two folders: `client` and `ui`. Complete the following steps to integrate the starter code into your project for this phase.

1.  Open your chess project directory.
2.  Create the folder `client/src/main/java/ui`. This directory will contain your client application's user interface code.
3.  Copy the `starter-code/5-pregame/ui/EscapeSequences.java` file into your project's `client/src/main/java/ui` folder. This file defines ANSI escape sequences used to control the coloration and formatting of your console output.
4.  Create the folder `client/src/test/java/client`. This is where you will implement your client-side unit tests.
5.  In IntelliJ, right-click the `client/src/test/java` directory and select **Mark Directory as** > **Test Sources Root**. This configuration allows IntelliJ to recognize and execute the code in this directory as tests.
6.  Copy the `starter-code/5-pregame/client/ServerFacadeTests.java` file into your project's `client/src/test/java/client` folder. This class provides a foundation for testing your `ServerFacade`. It includes boilerplate code for starting and stopping your HTTP server so that your tests can make live server requests.


````masteryls
{"id":"e286f471-ddec-45b2-9f02-7d50634fa061","title":"Phase 5: Getting started","type":"multiple-select"}
Once finished, your project structure should include the following additions:

```text
└─ client
    └─ src
        ├─ main
        │   └─ java
        │       └─ ui
        │           └─ EscapeSequences.java
        └─ test
            └─ java
                └─ client
                    └─ ServerFacadeTests.java
```

- [x] I completed the getting started steps and my project settings and structure look correct.
````

