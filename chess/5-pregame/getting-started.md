# Getting Started

♟️ [Project Overview](../chess.md)

📋 [Phase 5 Requirements](pregame.md)

📁 [Phase 5 Starter code](starter-code)

## Create Client Module

Until now, your Chess IntelliJ project has contained only server code. In this part of the project, you will create code for your Chess client. The server and client code can be in the same IntelliJ project, but the server code should be kept separate from client code. For this purpose we will use IntelliJ’s `module` mechanism. IntelliJ allows large projects to be divided into `modules`, each of which represents a major subsystem of the project. By default, a project has one module that has the same name as the project itself. Your server code is already in the default module. Now, you should create a new module named `client` where you can put your client code. Follow these steps to create your `client` module:

1. In the `File` menu, select `Project Structure…` This will open a dialog that lets you modify the structure of your project.
1. On the left side, select `Modules`.
1. Client the `+` button above the module list to create a new module.
1. Select `New Module`.
1. Give the new module the name `client` and click the `Create` button. This will add the new module to the module list.
1. Click the `OK` button to close the project structure dialog.

Next, you will create the folder structure for the `client` module. Do the following:

1. In the Project navigator on the left side, expand the new module. It should already have a `src` folder.
1. Right Click on `src` and select `Mark Directory as`->`Unmark as Sources Root`
1. In the `src` folder, create a subfolder named `main`. Right-click on `main` and select `Mark Directory as`->`Mark as Sources Root`. This folder will contain your client source code.
1. In the `src` folder, create a subfolder named `starter`. Right-click on `starter` and select `Mark Directory as`->`Mark as Sources Root`. This folder will contain the starter Java code that we provide to you.
1. In the `src` folder, create a subfolder named `test`. Right-click on `test` and select `Mark Directory as`->`Mark as Test Sources Root`. This folder will contain your Junit test cases.
1. In the `src/main` folder, create a Java package named `ui`.
1. The starter code contains a file named `EscapeSequences.java`. This class contains pre-defined string constants for the terminal control code sequences you will need to implement your Chess client user interface. Copy `EscapeSequences.java` into the `src/main/ui` package (i.e. folder) you just created.

Add the following dependencies to the `client` module:

- **org.junit.jupiter:junit-jupiter:5.9.2**
  - Scope: Test
- **com.google.code.gson:gson:2.10.1**
  - Scope: Compile
- **org.glassfish.tyrus.bundles:tyrus-standalone-client:1.15**
  - Scope: Compile
  - Add this to the server module as well
- **org.slf4j:slf4j-simple:1.7.36**
  - Because you already added this to the server, it should already be an option to add and you shouldn't need to search for it again.

## Create Shared Module

There are many classes in your project that will be needed by both your server and your client programs. Rather than make two copies of these classes (one in the server module and one in the client module), we prefer to have only one copy that can be shared by both programs. To achieve this code sharing between programs, we will create a third module in our project named `shared`, which will contain all classes needed by both client and server. Then, we will make the server and client modules depend on the shared module so they can access the shared classes. Add a `shared` module to your Chess project as follows:

1. In the `File` menu, select `Project Structure…` This will open a dialog that lets you modify the structure of your project.
1. On the left side, select `Modules`.
1. Client the `+` button above the module list to create a new module.
1. Select `New Module`.
1. Give the new module the name `shared` and click the `Create` button. This will add the new module to the module list.
1. Click the `OK` button to close the project structure dialog.

Next, you will create the folder structure for the `shared` module. Do the following:

1. In the Project navigator on the left side, expand the new module. It should already have a `src` folder.
1. Right Click on `src` and select `Mark Directory as`->`Unmark as Sources Root`
1. In the `src` folder, create a subfolder named `main`. Right-click on `main` and select `Mark Directory as`->`Mark as Sources Root`. This folder will contain your client source code.
1. In the `src` folder, create a subfolder named `starter`. Right-click on `starter` and select `Mark Directory as`->`Mark as Sources Root`. This folder will contain the starter Java code that we provide to you.
1. In the `src` folder, create a subfolder named `test`. Right-click on `test` and select `Mark Directory as`->`Mark as Test Sources Root`. This folder will contain your Junit test cases.

Add the following dependencies to the `shared` module:

- **com.google.code.gson:gson:2.10.1**
  - Scope: Compile

## Move Shared Files into Shared Module

Next, move all files needed by both server and client from the default server module into the `shared` module. Since these files have previously been added to your Git repository, you need to move the files using `git mv` rather than just moving the files in a file explorer. This can be accomplished by either using the Git command-line, or by dragging and dropping files within the IntelliJ Project navigator (i.e., IntelliJ will do a `git mv` for you).

Here are the files you should move into the `shared` module:

1. Move the `src/starter/chess` package to `shared/src/starter`. ⚠ Do not move `src/starter/dataAccess` since that code is only needed by the server
1. Move the `src/main/chess` package to `shared/src/main`.
1. Move the package containing your model classes from `src/main` to `shared/src/main`.
1. Move the package containing your Web API request classes from `src/main` to `shared/src/main`.
1. Move the package containing your Web API result classes from `src/main` to `shared/src/main`.

Now that you’ve moved the shared classes into the `shared` module, your server code will contain lots of compile errors. These errors will be fixed in the next section.

## Create Dependencies on Shared Module

Next, modify the default (server) module and the client module so that they have a dependency on the shared module. This is done as follows:

1. In the `File` menu, select `Project Structure…` This will open a dialog that lets you modify the structure of your project.
1. On the left side, select `Modules`.
1. In the module list, select the default (server) module in the module list. It should have the same name as your project.
1. Select the `Dependencies` tab.
1. Click the `+` button at the top of the Dependencies tab to add a new dependency.
1. Select `Module Dependency…` from the drop-down list.
1. Select the `shared` module and click `OK`. Your default (server) module now has a dependency on the shared code module, and should compile successfully again.

Now, follow the previous steps again to add the same dependency to the `client` module.

## Create Client Executable JAR File

At times you might want to run your Chess client outside of IntelliJ. For example, when testing you will need to run multiple instances of your client to test interaction between multiple users. Or, you might want to share your client with someone so they can play chess with you. To make it possible to run the Chess client outside of IntelliJ, we will create an `executable JAR file` for your client. A `JAR file` has a name with the .jar extension and is essentially a zip file containing a collection of compiled Java classes (i.e., .class files). An `executable JAR file` is a JAR file that contains at least one class with a `main` method, and thus can be executed to run the program within the JAR file. For example, if you have an executable JAR file named `client.jar` containing your Chess client, it can be executed using the following command line (Java must be installed on the computer where the JAR file is being executed):

```sh
java –jar client.jar
```

To create an executable JAR file for your Chess client, do the following in IntelliJ:

1. In the `File` menu, select `Project Structure…` This will open a dialog that lets you modify the structure of your project.
1. On the left side, select `Artifacts`.
1. From the drop-down list select `JAR` and then `From modules with dependencies…`
1. In the `Module` field select the client module.
1. In the `Main Class` field select the class that contains your client’s `main` method (add such a class if you do not already have one).
1. In the `JAR files from libraries` section, select `extract to the target JAR`.
1. Click the OK button. This will create an `artifact` named `client:jar` and add it to the list of artifacts in your project.
1. After clicking `OK`, select the `client:jar` artifact in the artifact list, and check the `Include in project build` option. This will cause the client executable JAR file to be re-created each time you rebuild your project so it is always up to date.
1. Click the `OK` button to close the project structure dialog.

After rebuilding your project, you should find the client.jar file in the folder `out/artifacts/client_jar/`.
