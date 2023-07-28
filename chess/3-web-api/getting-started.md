# Getting Started

📁 [Starter code](starter-code)

The Starter Code should have three folders: `libs`, `serverTests`, and `web`. Do the following:

1. Copy the `libs` folder into your project’s root folder. The `libs` folder contains some Java libraries (i.e., JAR files) that the pass off test cases depend on.
1. Copy the `serverTests` folder into your project’s `src/test/passoffTests` folder so it’s a sibling of `chessTests` and `TestFactory`. The `serverTests` folder contains the server test cases.
1. Copy the `web` folder into your project’s root folder. The `web` folder contains the files that implement the server’s test web page. When you create your server class, you will need to specify the `web` folder’s path for static files, as shown below.

   ```java
   Spark.externalStaticFileLocation("path/to/web/folder");
   ```

## Dependencies

Add the following dependencies to your project. HINT: Use the File->Project Structure menu option.

- `org.slf4j:slf4j-simple:1.7.36`

  - Scope: Compile

    Logger for the server. The server needs a logger to work. This is a simple one that tells you about things like when an API is accessed.

- `com.sparkjava:spark-core:2.9.3`

  - Scope: Compile

    This library gives us the framework for our server.

- `com.google.code.gson:gson:2.10.1`

  - Scope: Compile

    Library for serializing and deserializing objects to/from JSON

- The `libs` folder in the root folder of your project.

  - Scope: Compile

    This dependency is a `directory` dependency instead of a `library` dependency. When you add a `directory` dependency to a project, it creates a dependency on all the JAR files in the selected directory. When creating this dependency, instead of selecting `Library`, select `JARs or Directories` and select your project’s `libs` folder.
