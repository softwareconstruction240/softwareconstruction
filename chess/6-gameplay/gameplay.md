# ♕ Phase 6: Chess Gameplay

♟️ [Project Overview](../chess.md)

✅ [Phase 6 Getting Started](getting-started.md)

📁 [Phase 6 Starter code](starter-code)

For the final part of the Chess Project, you will implement gameplay. Gameplay will use WebSocket to communicate between client and server (instead of Web APIs). When a user joins or observes a game, their client should establish a WebSocket connection with the server. This WebSocket will be used to exchange messages between client and server (and vice versa).

## Gameplay Functionality

The gameplay UI should draw the current state of the chess board from the side the user is playing. If playing white, white pieces should be drawn on bottom. If playing black, black pieces should be drawn on bottom. If the user is observing the game, white pieces should be drawn on bottom.

The gameplay UI should support the following user commands:

| Command                   | Description                                                                                                                                                                                                                                         |
| ------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Help**                  | Displays text informing the user what actions they can take.                                                                                                                                                                                        |
| **Redraw Chess Board**    | Redraws the chess board upon the user’s request.                                                                                                                                                                                                    |
| **Leave**                 | Removes the user from the game (whether they are playing or observing the game). The client transitions back to the Post-Login UI.                                                                                                                  |
| **Make Move**             | Allow the user to input what move they want to make. The board is updated to reflect the result of the move, and the board automatically updates on all clients involved in the game.                                                               |
| **Resign**                | Prompts the user to confirm they want to resign. If they do, the user forfeits the game and the game is over. Does not cause the user to leave the game.                                                                                            |
| **Highlight Legal Moves** | Allows the user to input what piece for which they want to highlight legal moves. The selected piece’s current square and all squares it can legally move to are highlighted. This is a local operation and has no effect on remote users’ screens. |

![highlight moves](highlight-moves.png)

_Figure 1: Example Highlight Move_

### Notifications

When the following events occur, notification messages should be displayed on the screen of each player that is involved in the game (player or observer).

1. A user joined the game as a player (black or white). The notification message should include the player’s name and which side they are playing (black or white).
1. A user joined the game as an observer. The notification message should include the observer’s name.
1. A player made a move. The notification message should include the player’s name and a description of the move that was made. (This is in addition to the board being updated on each player’s screen.)
1. A player left the game. The notification message should include the player’s name.
1. A player resigned the game. The notification message should include the player’s name.
1. A player is in check. The notification message should include the player’s name (this notification is generated locally, not by the server).
1. A player is in checkmate. The notification message should include the player’s name (this notification is generated locally, not by the server).

## WebSocket

During gameplay all communication between client and server should be implemented using WebSocket.

When a user joins or observes a game, their client should do the following:

1. Call the server join API to join them to the game (or verify the game exists if they are observing).
1. Open a WebSocket connection with the server (using the `/connect` endpoint) so it can send and receive gameplay messages.
1. Send either a JOIN_PLAYER or JOIN_OBSERVER WebSocket message to the server.
1. Transition to the gameplay UI. The gameplay UI draws the chess board and allows the user perform the gameplay commands described in the previous section.

The following sections describe the messages that will be exchanged between client and server (or vice versa) to implement the gameplay functionality.

## WebSocket Messages

Some WebSocket messages will be sent from client to server. We call these `user game commands`. Other WebSocket messages will be sent from server to client. We call these `server messages`. In code, each of these message types will be represented as a Java class that can be easily serialized and deserialized to/from JSON (similar to the Request and Result classes you created for the server’s Web API). The provided starter code includes a class named `UserGameCommand` which serves as a superclass for all user game command messages, and a class named `ServerMessage` which serves as a superclass for all server messages. These superclasses define fields that are needed by all messages, as follows:

```java
public class UserGameCommand {
    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;

    private String authToken;
}

public class ServerMessage {
    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    ServerMessageType serverMessageType;
}
```

Each user game command class must include the `authToken` field and the `commandType` field and should inherit from the `UserGameCommand` class. The `commandType` field must be set to the corresponding `CommandType`.

All server messages must include a `serverMessageType` field and should inherit from the `ServerMessage` class. This field must be set to the corresponding `ServerMessageType`.

The following sections describe the server messages and user game command messages.

## User Game Commands

| Command           | Required Fields                                 | Description                                                                          |
| ----------------- | ----------------------------------------------- | ------------------------------------------------------------------------------------ |
| **JOIN_PLAYER**   | Integer gameID, ChessGame.TeamColor playerColor | Used for a user to request to join a game.                                           |
| **JOIN_OBSERVER** | Integer gameID                                  | Used to request to start observing a game.                                           |
| **MAKE_MOVE**     | Integer gameID, ChessMove move                  | Used to request to make a move in a game.                                            |
| **LEAVE**         | Integer gameID                                  | Tells the server you are leaving the game so it will stop sending you notifications. |
| **RESIGN**        | Integer gameID                                  | Forfeits the match and ends the game (no more moves can be made).                    |

## Server Messages

| Command          | Required Fields                                        | Description                                                                                                                         |
| ---------------- | ------------------------------------------------------ | ----------------------------------------------------------------------------------------------------------------------------------- |
| **LOAD_GAME**    | game (can be any type, just needs to be called `game`) | Used by the server to send the current game state to a client. When a client receives this message, it will redraw the chess board. |
| **ERROR**        | String errorMessage                                    | This message is sent to a client when it sends an invalid command. The message must include the word `Error`.                       |
| **NOTIFICATION** | String message                                         | This is a message meant to inform a player when another player made an action.                                                      |

## WebSocket Interactions

A client will instigate all interactions by sending a `UserGameCommand` to the server. We refer to the instigating client as the `Root Client`. The server will receive this Command and send appropriate `ServerMessages` to all clients connected to that game.

When sending a `Notification` that refers to one of the clients, the message should use the Clients username. (E.g., `Bob left the game`).

If a `UserGameCommand` is invalid (e.g. invalid authToken or gameID doesn’t exist) the server should only send an `Error` message informing the Root Client what went wrong. No messages should be sent to the other Clients. The Error message must contain the word `error` (case doesn’t matter).

**Root Client sends JOIN_PLAYER**

The server should send a `LOAD_GAME` message back to the root client, and a `Notification` message to all other players and observers in that game informing them what color the root client is joining as.

**Root Client sends JOIN_OBSERVER**

The server should send a `LOAD_GAME` message back to the root client, and a `Notification` message to all other players and observers in that game informing them the root client joined as an observer.

**Root Client sends MAKE_MOVE**

The server should send a `LOAD_GAME` message back to all clients in the game (including the root client) with an updated game. The server should also send a `Notification` message to all players and observers in the game other than the root client informing them what move was made.

**Root Client sends LEAVE**

The server should remove the root client from the game and send all remaining clients a `Notification` message informing them that the root client left. This applies to when a player leaves as well as when an observer leaves.

**Root Client sends RESIGN**

The server should mark the game as over (no more moves can be made) and send all other clients a `Notification` message informing them that the root client resigned. The server should also send a `Notification` message to the root user letting them know they resigned successfully.

Here is a [sequence diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=C4S2BsFMAIHEEMC2MAK54E8BQWB2B7YGfAN0gCdoBlCs8gLmgCFwBXGcEXSAZ2nMgAHAT0i5g0ABIAVaSmjIePeAHNeAOgA6uWALHRO3PgOG8xEgOqQARlXwBjANaQJi5Wp45B8cqHshvcWpaCix4e2B8SjRMCgBGMIio6BiMCgAmRMjKAHlrUXI6BKwAXhKAGXwVLjKAYjTwcHwAdyw8gqKAWlrrNkgAPhpCinpK6txodpDyOOhvJWaogBMsVIzu3vZB6dGqrhT0NPJ0ufgF5dXD+I2+7eGGMf21mdPz8hXSkoRkaHsBeFA+FwdQaTVazziNy2Qzo9AAwv8iHAkJAEhCoQMYSMAFL4fbfVHMcoAQThAGlLrEZt0VHpcHdYdicgBJAByAH0UCSAJoAUQASlgseQaXT+hDRjliQARdmwYkAWV5lKO6QxDJG5RAPGAKvWPVuwvouPxKNmFkkzOkyueatqtMgYg1DCZbM5PIFQumosd9NtkplcsVyuFPqdEtZOWkzIAYsy4cTozlWdAABQAKzxE1t0F64UcAEo2vlppCDdCdlqdcWOtdy5idiaJgSElN7mWHU6ja6OTkmFQBQA1T2h+1ittFAOy+VKr33MN+q4zeiR6NxhNJlMZrOTEvt6D4Pd0IujzuLqnpFdR2PxxPM5NpzP7CfxA9HihFz4EuaHEGQRotHq1Jjr6zr0AqxJkry7IKjkw7QJA6SQAALHOdALuKS5xFOQazqeYr+uUUrTsGaEUBhL7LkRgYziG3ogeGWFXmut6bmmiCkDAiEoSe9FnphF7MTeG73luHFkAhSHIbx84MfSlHYauwl3g+qbiVxUmfmU372EC9hsDwIBAn+AHglhGFGvyvJUMysCsmRIpyQJRyKde64qVuIggCo2ZYTJ6FOf6SnuWxqZeT5BxUnE-nkU5ClCSFolpuFvlRZ+toWZWvLEsODkYRGbmsUlqZQPAEm2jFjn8fFwVFappXlUu6SfjWpaZfcozZblOA4EAA) that demonstrates the player communications.

## Pass Off

The provided tests for this assignment are in the `WebSocketTests` class. These test the `WebSocket` interactions between client and server. To run the tests first start your server, and then run `WebSocketTests`.

Also, meet with a TA and demonstrate that your Chess client and server meet the requirements listed above.
