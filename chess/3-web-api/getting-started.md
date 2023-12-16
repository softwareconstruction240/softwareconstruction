# Getting Started

📁 [Starter code](starter-code)

The Starter Code should have three folders: `dataAccess`, `serverTests`, and `web`. Do the following:

1. Copy the [dataAccess](starter-code/dataAccess/) folder into the `server/src/main/java` folder. This contains an exception class that you will throw whenever there is a data access error.
1. Create the folder `server/src/test/java`. Right click on the folder and select the option to mark the directory as `test sources root`. This tells IntelliJ where to look for code to run as tests.
    ![mark test root](mark-test-root.png)
1. Copy the [serverTests](starter-code/serverTests/) folder the `server/src/test/java/passoffTests` folder. The `serverTests` folder contains the server test cases.
1. Copy the [web](starter-code/web/) folder to the root of your project. The `web` folder contains the files that implement the server’s test web page. When you create your server class, you will need to specify the `web` folder’s path for static files, as shown below.

   ```java
   Spark.externalStaticFileLocation("web");
   ```

This should result in the following additions to your project.

```txt
├── server
│   └── src
│       ├── main
│       │   └── java
│       │       └── dataAccess
│       │           └── DataAccessException.java
│       └── test
│           └── java
│               └── passoffTests
│                   └── serverTests
│                       └── StandardAPITests.java
└── web
    ├── favicon.ico
    ├── index.css
    ├── index.html
    └── index.js
```

## Dependencies

There is a lot of 3rd party code that you can download and include as a dependency for your project. We have already included packages that run your JUnit tests and process JSON  as part of the starter project you created in the initial phase. We now what to install another third party package to  help us create an HTTP server.

We are going to use a cloud based package repository called Maven as the source for our new dependency. You install a dependency with IntelliJ by opening the project settings and going to the `libraries` tab. Press the `+` button to add a dependency from Maven, and then supply the name of the library you want to add. You then specify which module will use the dependency.

![Install dependency](install-dependency.gif)

Install the following dependencies from Maven and add them with your `server` module. 


- `com.sparkjava:spark-core:2.9.3`

    Handles HTTP requests and registers handlers for endpoints.

- `org.slf4j:slf4j-simple:1.7.36`

    Logger for SparkJava.


