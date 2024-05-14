# ♕ Phase 2: Chess Server Design

- [Chess Application Overview](../chess.md)

_This phase has no starter code._

🖥️ [Slides](https://docs.google.com/presentation/d/12zsEJ-at5DsbKNy7a0Eac0D1ZWa4RBIC/edit?usp=sharing&ouid=117271818978464480745&rtpof=true&sd=true)

In this part of the Chess Project, you will create a [sequence diagram](https://en.wikipedia.org/wiki/Sequence_diagram) that represents the design of your chess server. Your chess server exposes seven endpoints. An endpoint is a URL that maps to a method that handles HTTP network requests. Your chess client calls the endpoints in order to play a game of chess. Each of these endpoints convert the HTTP network request into service object method calls, that in turn read and write data from data access objects. The data access objects persistently store data in a database. The service object method uses the information from the request and the data access objects to create a response that is sent back to the chess client through the HTTP server.

## Application Components

The chess application components are demonstrated by the following diagram and description.

![top level](top-level.png)

| Component    | Sub-Component | Description                                                                                                                                                                                                                                                       |
| ------------ | ------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Chess Client |               | A terminal based program that allows a user to play a game of chess. This includes actions to login, create, and play games. The client exchanges messages over the network with the chess server.                                                                |
| Chess Server |               | A command line program that accepts network requests from the chess client to login, create, and play games. Users and games are stored in the database. The server also sends game play commands to the chess clients that are participating in a specific game. |
|              | Server        | Receives network requests and locates correct endpoints.                                                                                                                                                                                                          |
|              | Handlers      | Deserialize information into service objects. Call service methods to satisfy the requests.                                                                                                                                                                       |
|              | Services      | Processes the business logic for the application. This includes registering and logging in users, creating, listing, and playing chess games. Calls the data access methods to retrieve and persist application data data.                                        |
|              | DataAccess    | Provides methods that persistently store and retrieve the application data.                                                                                                                                                                                       |
| Database     |               | Stores data persistently.                                                                                                                                                                                                                                         |


**⚠ Note** that while we have the Handlers as distinct components here and later in the diagram, it is not required to have a specific Handler class. It can be a part of the Server class using lamba functions or methods. 

## Application Programming Interface (API)

As a first step for creating your design diagram, you need to carefully read the [Phase 3: Web API](../3-web-api/web-api.md) requirements so that you can internalize what each of the server endpoints do. This will help you understand the purpose and structure of the classes you are designing in this phase.

The server endpoints are summarized below, but it is critical that you completely understand their purpose, the data they expect, and the data that they return.

| Endpoint    | Description                                                                                                                     |
| ----------- |---------------------------------------------------------------------------------------------------------------------------------|
| Clear       | Clears the database. Removes all users, games, and authTokens.                                                                  |
| Register    | Register a new user.                                                                                                            |
| Login       | Logs in an existing user (returns a new authToken).                                                                             |
| Logout      | Logs out the user represented by the provided authToken.                                                                        |
| List Games  | Verifies the provided authToken and gives a list of all games.                                                                  |
| Create Game | Verifies the provided authToken and creates a new game.                                                                         |
| Join Game   | Verifies the provided authToken. Checks that the specified game exists, and adds the caller as the requested color to the game. |

## Data Model Classes

The different components in your architecture will operate on three data model objects that your application must implement. This includes the following.

| Object   | Description                                                                                                                                   |
| -------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| UserData | A user is registered and authenticated as a player or observer in the application.                                                            |
| AuthData | The association of a username and an authorization token that represents that the user has previously been authorized to use the application. |
| GameData | The information about the state of a game. This includes the players, the board, and the current state of the game.                           |

These objects represent the core of what you are passing between your server, service, and data access components.

## Creating Sequence Diagrams

Based upon your understanding of the requirements provided by [Phase 3](../3-web-api/web-api.md) you now must create a sequence diagram that demonstrates the flow of interactions between your application objects for each of the server endpoints.

### SequenceDiagram.Org

You will create your sequence diagram using a simple web based editing tool found at [sequencediagram.org](https://sequencediagram.org). The [instructions](https://sequencediagram.org/instructions.html) for using the tool document all of the basic elements necessary to create your diagram. It is not necessary for you to fully understand all the details of UML sequence diagrams, but it should be obvious from your diagrams what your application is designed to do.

A basic sequence diagram uses objects names separated by arrows that show the direction of the sequence. This is followed by a colon separated description of the sequence action. The following is a simple diagram for taking a class.

```uml
actor Student

group #green Take class #white
Student -> University:register(className)
Student -> Class:attend(date)
Class --> Student:knowledge
end
```

### Register Example

To get you started on creating your sequence diagrams, we have provided you with a template that already contains a possible solution for the `register` endpoint and place holders for the other six endpoints.

⚠ Here is a link to your [Starter Diagram](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZMAcygQArthgBiNMCoBPGACUUZpKrBQ5SCDR7AHcACyQwMURUUgBaAD5yShooAC4YAG0ABQB5MgAVAF0YAHorAygAHTQAb3LKRwBbFAAaGFx1YOgONpRG4CQEAF9MYRSYBNZ2Lkp0uoqm1vb9VS6oHpg+geHMNk5uWEmx0XSPLx9KDwBHKzUwAAp6qEW2jtXu3v7BgEpR5NEJolZPIlCp1OkzCgwABVCqPBbAZq-YGKZRqVSAow6dJkACiABlcXB8jAnosYAAzSyNUkVTAo0HoiZHf4qdJoKwIBB-EQqTEMtHgmAgKAoOQoWGUeENRFLN5rDZbH707SM9SY4zpACSADk8W4SWTZa8VgrPtsYLr8jlabRjnzJgKwap0iKxZEFFYwKFpc9ZcjVYKMZNNZa9biDba-c02sAvaF8hAANboMPWmBx70qkFB5mJe0odKZhPJ9A8qgA2KTPYzNLuTzeSJQDyqTkPI0xjPxxMptC-GsHPNJaizGDzGWd4s99AjMaUIfRdBgdIAJgADGvquPo0sp6W0CN0BxTBZrLY7NB2JCYPiIF4gnYwhEosgl5i53XsnkiqUDOoAmgW4dnKprdIenAnpYNj2BYKCpreZjWMwj7hJEmCLnE+bJKOGTSASuL5LixQlH+qgAdUe69pgR6QWe9iihwN6NjAADisoYihz7oa+mHDik6QZCxBHEWYsoUd2+7URB5hQee2BWFA2DcPAoriqxsohKhL4xMwLIjp+uQFCJYmOBJvZbqJzQ6rK4HHjJdF2M4KBchAwQwAAUhASBBGxzT2DoCCgEm3E6e+2EGdCP4lJZKDid606AbUSnAE5UBwBACDQG0MVatItm0dBdgWCl8DcHgGbYEphD+IEmlcRhulYfpAl4YShHEcYUnHkAA) on SequenceDiagram.Org.

When you are done editing your diagram make sure you export a link as described in the **Deliverable** section below.

![Register Sequence Diagram](register-sequence-diagram.png)

This example diagram represents the following sequence for registering and authorizing a player.

1. A `client`, acting as a chess player, calls the `register` endpoint. This request is made as an HTTP network request with the `/user` URL path and a body that contains her username, password, and email in a JSON representation.
2. The `server` gets the body with its information from the HTTP request and matches it to the correct handler.
3. The `handler` takes the JSON information and creates an object to hold it and sends it to the correct service class. ⚠ Note: in your code the handlers do not need to be their own distinct classes, they can be contained in your server class using methods or lamba functions. 
4. The `service` calls a data access method in order to determine if there is already a user with that username.
5. The `data access` method checks the database and returns that there is no user with that name (null).
6. The `service` then calls another data access method to create a new user with the given name and password.
7. The `data access` method inserts the user into the database.
8. The `service` then calls another data access method to create and store an authorization token (authToken) for the user. The authToken can be used on subsequent endpoint calls to represent that the user has already been authenticated.
9. The `data access` method stores the username and associated authToken in the database.
10. The `service` returns a result object containing the username and authToken.
11. The `handler` converts the object into JSON text.
12. The `server` returns this to the client.

⚠ Note that the diagram includes simple representations of HTTP and database requests. You will learn how to use these technologies in later phases. You just need to understand that the `server` receives HTTP network requests and the database persistently stores your application data. It is also not important that you use correct UML Sequence diagram syntax for your diagrams. You just need to show that you understand what each of the endpoints are doing inside your code. 

## Classes

Using your sequence diagram, you should be able to envision the Java classes and methods that are necessary for handling the interactions between your server, services, and data access components. You will create and implement these classes in the next phase.

Once such possible class architecture could be the following.

![sever design architecture](server-class-structure.png)

This architecture includes a handler method for each server endpoint that call a corresponding service method. Each service method takes a request object and returns a response object. The service method then interacts with the data access methods to store and retrieve data from the database. The application model objects serve as the primary data representations that are passed between the server, services, and data access components.

You can decompose your handlers, services, and data access components into multiple classes or leave them as a single class as your design requires in order to meet the principles of good software design.

## ☑ Deliverable

### Pass Off, Submission, and Grading

Once you have created your diagram you can create a URL that represents it by selecting the `export diagram` tool found on the toolbar to the left of the application. In the export dialog select `Presentation Mode Link` and copy the URL. Submit the URLs to the `Chess Server Design` Canvas Assignment.

![sequencediagram.org](sequence-diagram-org.gif)

Make sure you save a copy of your sequence diagram URL in your GitHub repository. A good place for this is in the README.md file of your project.

### Grading Rubric

When initially graded, your design will be given one of three scores:

| Criteria                                                                                                                                                                                                                                          | Score |
| :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ----: |
| Your design is mostly correct with only minor adjustments needed. Read TA suggestions for improvement in Canvas.                                                                                                                                  |    50 |
| Your design has significant deficiencies. Meet with a TA to discuss your design, ideally the same TA who originally graded your design. Improve and resubmit your design within one week of initial grading, and receive a maximum score of 100%. |    25 |
| The submitted design was not a serious attempt at doing the assignment correctly. Resubmit your design within one week of initial grading and receive a maximum score of 50%.                                                                     |     0 |

