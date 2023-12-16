# ♕ Phase 3: Chess Web API

- [Chess Application Overview](../chess.md)
- [Getting Started](getting-started.md)
- [Starter Code](starter-code)

🖥️ [Slides: Server Implementation Tips](https://docs.google.com/presentation/d/1hORd88ej8W-nqHgEpYU2GmPcrSrHew1V/edit?usp=drive_link&ouid=110961336761942794636&rtpof=true&sd=true)

In this phase, you will create your Chess server and implement seven HTTP endpoints that the chess client will use to communicate with your server. This will include creating your server, service, and data access classes. You will also write unit tests for your service classes.

![Sever class structure](server-class-structure.png)

## Required HTTP Endpoints

An endpoint is a URL that your server exposes so that clients can make Hypertext Transfer Protocol (HTTP) requests to your server. Often the server requires some data when a client calls an endpoint. For an HTTP request this data can be stored in HTTP Headers, in the URL, and/or in the request body. The Server then sends back data to the client, including a value in the HTTP Response Code (indicating if the command was completed successfully), and any needed information in the HTTP Response Body. For your server, you will use JSON strings to encode the objects we include in the Request and Response bodies.

## Endpoint specifications

The following defines the endpoints that your server is required to implement. Your server must accept the URL, HTTP Method, Headers, and body that the endpoint defines. Likewise you must return the specified status codes and body for the endpoint.

### Clear application

| property             | value                                                          |
| -------------------- | -------------------------------------------------------------- |
| **Description**      | Clears the database. Removes all users, games, and authTokens. |
| **URL path**         | `/db`                                                          |
| **HTTP Method**      | `DELETE`                                                       |
| **Success response** | [200]                                                          |
| **Failure response** | [500] `{ "message": "Error: description" }`                    |

### Register

| property             | value                                          |
| -------------------- | ---------------------------------------------- |
| **Description**      | Register a new user.                           |
| **URL path**         | `/user`                                        |
| **HTTP Method**      | `POST`                                         |
| **Body**             | `{ "username":"", "password":"", "email":"" }` |
| **Success response** | [200] `{ "username":"", "authToken":"" }`      |
| **Failure response** | [400] `{ "message": "Error: bad request" }`    |
| **Failure response** | [403] `{ "message": "Error: already taken" }`  |
| **Failure response** | [500] `{ "message": "Error: description" }`    |

### Login

| property             | value                                               |
| -------------------- | --------------------------------------------------- |
| **Description**      | Logs in an existing user (returns a new authToken). |
| **URL path**         | `/session`                                          |
| **HTTP Method**      | `POST`                                              |
| **Body**             | `{ "username":"", "password":"" }`                  |
| **Success response** | [200] `{ "username":"", "authToken":"" }`           |
| **Failure response** | [401] `{ "message": "Error: unauthorized" }`        |
| **Failure response** | [500] `{ "message": "Error: description" }`         |

### Logout

| property             | value                                           |
| -------------------- | ----------------------------------------------- |
| **Description**      | Logs out the user represented by the authToken. |
| **URL path**         | `/session`                                      |
| **HTTP Method**      | `DELETE`                                        |
| **Headers**          | `authorization: <authToken>`                    |
| **Success response** | [200]                                           |
| **Failure response** | [401] `{ "message": "Error: unauthorized" }`    |
| **Failure response** | [500] `{ "message": "Error: description" }`     |

### List Games

Note that `whiteUsername` and `blackUsername` may be `null`.

| property             | value                                                                                         |
| -------------------- | --------------------------------------------------------------------------------------------- |
| **Description**      | Gives a list of all games.                                                                    |
| **URL path**         | `/game`                                                                                       |
| **HTTP Method**      | `GET`                                                                                         |
| **Headers**          | `authorization: <authToken>`                                                                  |
| **Success response** | [200] `{ "games": [{"gameID": 1234, "whiteUsername":"", "blackUsername":"", "gameName:""} ]}` |
| **Failure response** | [401] `{ "message": "Error: unauthorized" }`                                                  |
| **Failure response** | [500] `{ "message": "Error: description" }`                                                   |

### Create Game

| property             | value                                        |
| -------------------- | -------------------------------------------- |
| **Description**      | Creates a new game.                          |
| **URL path**         | `/game`                                      |
| **HTTP Method**      | `POST`                                       |
| **Headers**          | `authorization: <authToken>`                 |
| **Body**             | `{ "gameName":"" }`                          |
| **Success response** | [200] `{ "gameID": 1234 }`                   |
| **Failure response** | [400] `{ "message": "Error: bad request" }`  |
| **Failure response** | [401] `{ "message": "Error: unauthorized" }` |
| **Failure response** | [500] `{ "message": "Error: description" }`  |

### Join Game

| property             | value                                                                                                                                                                                                              |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Description**      | Verifies that the specified game exists, and, if a color is specified, adds the caller as the requested color to the game. If no color is specified the user is joined as an observer. This request is idempotent. |
| **URL path**         | `/game`                                                                                                                                                                                                            |
| **HTTP Method**      | `PUT`                                                                                                                                                                                                              |
| **Headers**          | `authorization: <authToken>`                                                                                                                                                                                       |
| **Body**             | `{ "playerColor":"WHITE/BLACK", "gameID": 1234 }`                                                                                                                                                                  |
| **Success response** | [200]                                                                                                                                                                                                              |
| **Failure response** | [400] `{ "message": "Error: bad request" }`                                                                                                                                                                        |
| **Failure response** | [401] `{ "message": "Error: unauthorized" }`                                                                                                                                                                       |
| **Failure response** | [403] `{ "message": "Error: already taken" }`                                                                                                                                                                      |
| **Failure response** | [500] `{ "message": "Error: description" }`                                                                                                                                                                        |

## Required Classes

The following sections describe the various classes that are depicted in the architecture diagram above.

### Data Model Classes

The Java `chess` package in your project's `shared` module contains classes that represents all of the data and algorithmic functionality that is shared by your chess client and server.

As part of this phase, you need to create [record](../../instruction/records/records.md)  classes and add them to the shared `chess` package that represent the classes used for the chess application's core data objects. This includes the following.

**UserData**

| Field    | Type   |
| -------- | ------ |
| username | String |
| password | String |
| email    | String |

**GameData**

| Field         | Type                       |
| ------------- | -------------------------- |
| gameID        | int                        |
| whiteUsername | String                     |
| blackUsername | String                     |
| gameName      | String                     |
| game          | `ChessGame` implementation |

**AuthData**

| Field     | Type   |
| --------- | ------ |
| authToken | String |
| username  | String |

⚠ You must places these three records classes in a folder named `shared/src/main/java/model`.

### Data Access Classes

Classes that represent the access to your database are often called `Data Access Objects` or DOAs. Create your data access classes in the `server/src/main/java/dataAccess` package. Data access classes are responsible for storing and retrieving the server’s data (users, games, etc.).

For the most part, the methods on your DAO classes will be `CRUD` operations that: 1) Create objects in the data store, 2) Read (or query) objects from the data store, 3) Update objects already in the data store, and 4) Delete objects from the data store. Oftentimes, the parameters and return values of your DAO methods will be the model objects described in the previous section (UserData, GameData, AuthData). For example, your DAO classes will certainly need to provide a method for creating new UserData objects in the data store. This method might have a signature that looks like this:

```java
void insertUser(UserData u) throws DataAccessException
```


Here are some examples of the kinds of methods your DAO classes will need to support. If a method cannot be completed, it should throw a `DataAccessException` (e.g., trying to update a non-existent game). This list is not exhaustive.

- **clear**: A method for clearing all data from the database. This is used during testing.
- **createUser**: Create a new user. 
- **getUser**: Retrieve a user with the given username.
- **createGame**: Create a new game.
- **getGame**: Retrieve a specified game with the given game ID.
- **listGames**: Retrieve all games.
- **updateGame**: Updates a chess game. It should replace the chess game string corresponding to a given gameID. This is used when players join a game or when a move is made.
- **createAuth**: Create a new authorization.
- **getAuth**:  Retrieve an authorization given an authToken.
- **deleteAuth**: Delete an authorization so that it is no longer valid.

### DataAccessException

The starter code includes a `dataAccess.DataAccessException`. This exception should be thrown by DAO methods that could fail. For example, inserting a second user with the same username shouldn’t be allowed, and therefore should throw a `DataAccessException` in that case.

### DataAccess Interface

 In order to abstract from your services where data is actually being stored, you must create a Java interface that hides all of the implementation details for accessing and retrieving data. In this phase you will create an implementation of your data access interface that stores your server’s data in main memory (RAM) using standard data structures (maps, sets, lists). In the next phase you will create an implementation of the data access interface that uses an external SQL database.

![data access classes](data-access-classes.png)

By using an interface you can hide, or encapsulate, how your data access works from the code that does not need to be aware of those details. This creates a flexible architecture that allows you to change how things work without rewriting all of your code. We see the benefits of this pattern in two ways.

1. You can quickly implement our services without having to implement a backing SQL database. This allows us to focus on the HTTP part of our server and then move over to SQL without changing any of our service code.
2. You can write data access tests against the memory implementation of the interface and then reuse those tests when we create the SQL implementation.


⚠ You must place your data access classes in a folder named `server/src/test/java/dataAccess`.


### Service Classes

The Service classes implement the actual functionality of the server. More specifically, the Service classes implement the logic associated with the web endpoints.

A simple implementation of this is to have a separate Service class for each group of related endpoints. For example, a `UserService` class might look like this:

```java
public class UserService {
	public AuthData register(UserData user) {}
	public AuthData login(UserData user) {}
	public void logout(UserData user) {}
}
```

Each service method receives a Request object containing all the information it needs to do its work. After performing its function it returns a corresponding Result object containing the output of the method. To do their work, service classes need to make heavy use of the Model classes and Data Access classes described below.


⚠ You must place your service classes in a folder named `server/src/main/java/service`.


### Request and Result Classes

As described in the previous section, service class methods receive request objects as input, and return result objects as output. The contents of these classes can be derived from the JSON inputs and outputs of the web endpoints documented above. For example, the `login` endpoint accepts the following JSON object as input:

```java
{
    "username":"your_username",
    "password": "your_password"
}
```

From this you can derive the following LoginRequest class:

```java
	record LoginRequest(
		String username,
		String password;
	}
```

Alternatively, you could use the model `UserData` object that you will also use when you call your data access layer. Reusing these objects can create confusion with what the method needs to operate, but it does simplify your architecture by reducing the duplication of model objects.

```java
	record UserData(
		String username,
		String password;
		String email;
	}
```

Similarly, the `login` endpoint returns a JSON object of the following format, depending on whether the login operation succeeded or failed:

**Success**

```json
{
  "authToken": "example_auth",
  "username": "example_username"
}
```

**Error**

```json
{
  "message": "Error: description"
}
```

From this you can derive the following LoginResult class:

```java
	record LoginResult(String username, String authToken) {}
```

and in the case where the service fails, it can throw an exception that the server handles by returning the proper error message and HTTP status code.

### Serialization

You will be using the Gson library for serialization and deserialization. Gson can take a Java Object and convert its contents to a JSON string. In the other direction, Gson can take a JSON string and a class type, and create a new instance of that class with any matching fields being initialized from the JSON string. For this process to work properly, the field names in your Request and Result classes must match exactly the property names in the JSON strings, including capitalization.

Here is an example of using Gson to serialize and deserialize a ChessGame.

```java
var game = new ChessGame();
var serializer = new Gson();
var json = serializer.toJson(game);
game = serializer.fromJson(json, ChessGame.class);
```

We install the third party package already in your project as part of its initial configuration and so you are ready to start using Gson in your code.

### Handler Classes

The server handler classes serve as a translator between HTTP and Java. Your handlers will convert an HTTP request into Java usable objects & data. The handler then calls the appropriate service. When the service responds it converts the response object back to JSON and sends the HTTP response.

You need to create the number of handler classes that are appropriate for your server design. For a simple server this could be a single class with a few handler methods, or for a complex application it could be dozens of classes each representing a different group of cohesive endpoints.

### Server Class

The Server receives network HTTP requests and sends them to the correct handler for processing. The server should also handle all unhandled exceptions that your application generates and return the appropriate HTTP status code. 


⚠ For the pass off tests to work properly, your server class must be named `Server` and provide a `run` method that conforms to the following signature.

```java
public class Server {
	public int run(
		int desiredPort,
		String dbConnectionUrl
	) {
		// Start up the server.
		// Wait for it to finish initializing.
		// Obtain the actual server port.

		return actualPort;
	}
}
```

This allows the tests to start up and shutdown the server as required by the tests.

⚠ You must place your `Server` class in a folder named `server/src/main/java/server`.


## Service Unit Tests

In addition to the tests provided in the starter code, you need to write tests that execute directly against your service classes. These tests skip the HTTP server network communication and should help you in the development of your code for this phase.

Good tests extensively show that we get the expected behavior. This could be asserting that data put into the database is really there, or that a function throws an error when it should. Write a positive and a negative JUNIT test case for each public method on your Service classes, except for Clear which only needs a positive test case. A positive test case is one for which the action happens successfully (e.g., successfully claiming a spot in a game). A negative test case is one for which the operation fails (e.g., trying to claim an already claimed spot).

The service unit tests must directly call the methods on your service classes. They should not use the HTTP server test code that is provided with the starter code.

⚠ You must place your service test cases in a folder named `server/src/test/java/serviceTests`.

## Server Directory Structure

After you have created all the classes necessary for this phase you should have a server directory structure that looks like the following.

```txt
└── src
    ├── main
    │   └── java
    │       ├── dataAccess
    │       ├── server
    │       └── service
    └── test
        └── java
            ├── passoffTests
            │   └── serverTests
            └── serviceTests
```

## Suggested Implementation Order

You can create and test your code in whatever order you would like. However, if you are trying to figure out how to get started, you might consider the following order.

1. Use your sequence diagrams to guide the decision for what classes you need for your server, service, and data access objects.
1. Implement your services
	1. Create the classes you need to implement the `clear` service method.
	1. Write a service test for `clear` to make sure the service and data access parts of your code are working properly.
	1. Repeat writing and implementing service classes and tests until you have built all the required functionality. 
1. Create your server handler for a single endpoint that simply returns a string.
1. Make sure you can hit your endpoint from a browser or Curl.
1. Implement your server handlers
	1. Convert your test server handler to implement the `clear` and `register` endpoints.
	1. Run the pass off test for registration.
	1. Repeat writing and implementing server handlers until you have completed all the pass off tests.


## Relevant Instruction Topics

- [Web API](../../instruction/web-api/web-api.md): Creating an HTTP server.

## Pass Off Tests

The provided tests for this assignment are in the `StandardAPITests` class. These tests make HTTP requests to test your server.


## Code Quality

For this phase the TAs will grade the quality of your project's source code. The rubric used to evaluate code quality can be found here: [Rubric](../code-quality-rubric.md)

## Pass Off and Grading

All of the tests in your project must succeed in order to complete this phase.

To pass off this assignment use the course auto-grading tool. If your code passes then your grade will automatically be entered in Canvas.

After your code has successfully been auto-graded, a TA will review the code in your GitHub repository in order to determine its quality.


### Grading Rubric

| Category       | Criteria                                                                                                                                                                                         | Points |
| -------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | -----: |
| Web API Works  | All pass off test cases in `StandardAPITests.java` succeed                                                                                                                                       |    115 |
| Web Page Loads | Test web page properly loads in browser (including all files linked to by the test page: favicon.ico, index.css, index.js)                                                                       |     10 |
| Code Quality   | [Rubric](../code-quality-rubric.md)                                                                                                                                                              |     30 |
| Unit Tests     | All test cases pass<br/>Each public method on your **Service classes** has two test cases, one positive test and one negative test<br/>Every test case includes an Assert statement of some type |     25 |
|                | Total                                                                                                                                                                                            |    180 |
