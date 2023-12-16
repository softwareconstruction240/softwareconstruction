# ♕ Phase 2: Chess Server Design

- [Chess Application Overview](../chess.md)
- [Getting Started](getting-started.md)
- [Starter Code](starter-code)

🖥️ [Slides](https://docs.google.com/presentation/d/12zsEJ-at5DsbKNy7a0Eac0D1ZWa4RBIC/edit?usp=sharing&ouid=117271818978464480745&rtpof=true&sd=true)

In this part of the Chess Project, you will create create a [sequence diagram](https://en.wikipedia.org/wiki/Sequence_diagram) that represents the design of your chess server. Your chess server exposes seven endpoints. An endpoint is URL that maps to a method that handle HTTP network requests. Your chess client calls the endpoints in order to play a game of chess. Each of these endpoints convert the HTTP network request into service object method calls, that in turn read and write data from data access objects. The data access objects persistently store data in a database. The service object method uses the information from the request and the data access objects to create a response that is sent back to the chess client through the HTTP server.

## Application Components

The chess application components are demonstrated by the following diagram and description.

![top level](top-level.png)

| Component    | Sub-Component | Description                                                                                                                                                                                                                           |
| ------------ | ------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Chess Client |               | Provides the interactive interface that allows a user to play a game of chess. This includes actions to login, create, and play games. The client exchanges messages over the network with the chess server.                                                             |
| Chess Server |               | Accepts network requests from the chess client to login, create, and play games. Users and games are stored in the database. The server also sends game play commands to the chess clients that are participating in a specific game. |
|              | Server        | Receives network requests and deserializes them into service function requests.                                                                                                                                                       |
|              | Services      | Processes the business logic for the application. This includes registering and logging in users, creating, listing, and playing chess games.                                                               |
|              | DataAccess    | Provides methods that persistently store the application data in a database.                                                                                                      |
|Database||Stores data persistently.|

## Application Programming Interface (API)

As a first step for creating your design diagram, you need to carefully read the [Web API](../3-web-api/web-api.md) document for phase three of the project, so you can internalize what each of the server endpoints do. This will help you understand the purpose and structure of the classes you are designing in this phase.

The server endpoints are summarized below, but it is critical that you completely understand their purpose, the data they expect, and the data that they return.

| Endpoint    | Description                                                                                                                                                                                                                                      |
| ----------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Clear       | Clears the database. Removes all users, games, and authTokens.                                                                                                                                                                                   |
| Register    | Register a new user.                                                                                                                                                                                                                             |
| Login       | Logs in an existing user (returns a new authToken).                                                                                                                                                                                              |
| Logout      | Logs out the user represented by the provided authToken.                                                                                                                                                                                         |
| List Games  | Verifies the provided authToken and gives a list of all games.                                                                                                                                                                                   |
| Create Game | Verifies the provided authToken and creates a new game.                                                                                                                                                                                          |
| Join Game   | Verifies the provided authToken. Checks that the specified game exists, and if a color is specified, adds the caller as the requested color to the game. If no color is specified the user is joined as an observer. This request is idempotent. |

## Data Model Classes

The different components in your architecture will operate on three data model objects that your application must implement. This includes the following.

| Object   | Description                                                                                                                                 |
| -------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| UserData | A user is registered and authenticated as a player or observer in the application.                                                          |
| AuthData | The association of a username and an authorization token that represents that the user has previous been authorized to use the application. |
| GameData | The information about the state of a game. This includes the players, the board, and the outcome of the game.                               |

## Creating Sequence Diagrams

Based upon your understanding of the requirements provided by [Phase 3](../3-web-api/web-api.md) you now must create a sequence diagram that demonstrates the flow of interactions between your application objects for each of the server endpoints.

### Register Example

To get you started on creating your sequence diagram we have provided you with template that already contains a possible solution for the `register` endpoint, and place holders for the other six endpoints. You can view this diagram on [SequenceDiagram.Org](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFHXyFiwUgBF+wAIIgQKLl0wATeQCNgXFDA3bMmdlAgBXbDADEaYFQCerDt178kg2wHcAFkjAxRFRSAFoAPnJKGigALhgAbQAFAHkyABUAXRgAegt9KAAdNABvfMp7AFsUABoYXDVvaA06lErgJAQAX0xhGJgIl04ePgEhaNF4qFceSgAKcqgq2vq9LiaoFpg2joQASkw2YfcxvtEByLkwRWVVLnj2FDAAVQKFguWDq5uVNQvDbTxMgAUQAMsC4OkYItljAAGbmSrQgqYb5KX5cAaDI5uUaecYiFTxNAWBAIQ4zE74s4qf5o25qeIgab8FCveYw4DVOoNdbNL7ydF3f5GeIASQAciCWFDOdzVo1mq12p0YJL0ilkbQcSMPIIaQZBvSMUyWYEFBYwL53hUuSgBdchX9BqK1VLgTKtUs7XVgJbfOkIABrdBujUwP1W1GChmY0LYyl4-UTIkR-2BkNoCnHJMEqjneORPqUeKRgPB9C9aKULGRYLoMDxABMAAYW8USmWM+geugNCYzJZrDZoNJHjBQRBOGgfP5Aph62Ei9W4olUhlsjl9Gp8R25SteRsNL20P3TOYrLYzChQ5P2JZmDY-AEgsgG-9i6uEjIwcD0sDN23Lhd3sdMKyzPsBwvYdpg0Cc3BgABxO1MSfOdXxCZhBk-eIEkQv9N3YO1ii7cDMEg88h1sbALCgbAEAMOAzQMZDqlnF8FzfJcomoEs1zSLJciI6oSLAzMO2ElAJTtE8z0HS8bEcFAyQgbwYAAKQgJAZ1YgwbG0BBQCDTjMI-FdcKSZ5BJySTRKtbs0A7RcwDgCAEGgOpJLFGRZKgqibDMYBnEQFA8AjbB6MIPUZzQjjnLM3ivx-cF-03IxyNPTAgA).

![Register Sequence Diagram](register-sequence-diagram.png)

This diagram represent the following sequence for registering and authorizing a player.

1. A `client`, acting as a chess player, calls the `register` endpoint. This request is made as an HTTP network request with the `/user` URL path and a body that contains her username, password, and email in a JSON representation.
1. The `server` converts the HTTP request to an object representation that it passes to a registration service method.
1. The `service` calls a data access method in order to determine if there is already a user with that username.
1. The `data access` method checks the database and returns that there is no user with that name (null).
1. The `service` then calls another data access method to create a new user with the given name and password.
1. The `data access` method inserts the user into the database.
1. The `service` then calls another data access method to create and store an authorization token (authToken) for the user. The authToken can be used on subsequent endpoint calls to represent that the user has already been authenticated.
1. The `data access` method stores the username and associated authToken in the database.
1. The `service` returns the authToken to the server.
1. The `server` returns the authToken to the client.

⚠ Note that the diagram includes simple representations of HTTP and database requests. It is not important that you get the syntax correct in your diagrams. You will learn how to use these technologies in later phases. You just need to convey the idea of what they are doing.


## Classes

Using your sequence diagram, you should be able to envision the Java classes and methods that are necessary for handling the interactions between your server, services, and data access components. You will create and implement these classes in the next phase.

Once such possible class architecture could be the following.

![sever design architecture](server-class-structure.png)

This architecture includes a handler method for each server endpoint that calls a corresponding service method. Each service message takes a request object and returns a response object. The service method then interacts with the data access methods to store and retrieve data from the database. The application model objects serve as the primary data representations that are passed between the server, services, and data access components.

You can decompose your handlers, services, and data access components into multiple classes or leave them as a single class as your design requires in order to meet the principles of good software design.

## SequenceDiagram.Org

You will create your sequence diagram using a simple web based editing tool found at [sequencediagram.org](https://sequencediagram.org). The [instructions](https://sequencediagram.org/instructions.html) for using the tool document all of the basic elements necessary to create your diagram. It is not necessary for you to fully understand all the details of UML sequence diagrams, but it should be obvious from your diagrams what your application is designed to do.

A basic sequence diagram uses objects names seperated by arrows that show the direction of the sequence. This is followed by a colon separated description of the sequence action. The following is a simple diagram for taking a class.

```uml
actor Student

group #green Take class #white
Student -> University:register(className)
Student -> Class:attend(date)
Class --> Student:knowledge
end
```


## Submission and Grading

Once you have created your diagram you can create a URL that represents it by selecting the `export diagram` tool found on the toolbar to the left of the application. In the export dialog select `Presentation Mode Link` and copy the URL. Submit the URLs to the `Chess Server Design` Canvas Assignment.


![sequencediagram.org](sequence-diagram-org.gif)

### Grading Rubric

When initially graded, your design will be given one of three scores:

| Score | Criteria                                                                                                                                                                                                                                          |
| ----: | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
|  100% | Your design is mostly correct with only minor adjustments needed. Read TA suggestions for improvement in Canvas.                                                                                                                                  |
|   50% | Your design has significant deficiencies. Meet with a TA to discuss your design, ideally the same TA who originally graded your design. Improve and resubmit your design within one week of initial grading, and receive a maximum score of 100%. |
|    0% | The submitted design was not a serious attempt at doing the assignment correctly. Resubmit your design within one week of initial grading and receive a maximum score of 50%.                                                                     |
