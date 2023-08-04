## Pregame

In this part of the Chess Project, you will create an initial version of your Chess client. Your Chess client will be a terminal-based (i.e., console-based) program that gives the user a simple interface for playing Chess. Your client should implement all user interactions that occur before and after actual gameplay (gameplay interactions will be implemented in the next part of the project). This includes allowing the user to display help text, register, login, list existing games, create a new game, join a game, observe a game, logout, and exit. You will also write the client code that draws the chessboard.

## Required Functionality

### Prelogin UI

| Command      | Description                                                                                                                                                                                               |
| ------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Help**     | Displays text informing the user what actions they can take.                                                                                                                                              |
| **Quit**     | Exits the program.                                                                                                                                                                                        |
| **Login**    | Prompts the user to input login info. Calls the server login API to login the user. When successfully logged in, the client should transition to the Postlogin UI.                                        |
| **Register** | Prompts the user to input registration info. Calls the server register API to register and login the user. If successfully registered, the client should be logged in and transition to the Postlogin UI. |

### Postlogin UI

| Command           | Description                                                                                                                                                                                                                                                                                                                          |
| ----------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Help**          | Displays text informing the user what actions they can take.                                                                                                                                                                                                                                                                         |
| **Logout**        | Logs out the user. Calls the server logout API to logout the user. After logging out with the server, the client should transition to the Prelogin UI.                                                                                                                                                                               |
| **Create Game**   | Allows the user input a name for the new game. Calls the server create API to create the game. This does not join the player to the created game; it only creates the new game in the server.                                                                                                                                        |
| **List Games**    | Lists all the games that currently exist on the server. Calls the server list API to get all the game data, and will displays the games in a numbered list, including the game name and players (not observers) in the game. The numbering for the list should be independent of the game IDs.                                       |
| **Join Game**     | Allows the user to specify which game they want to join and what color they want to play. They should be able to enter the number of the desired game. Your client will need to keep track of which number corresponds to which game from the last time it listed the games. Calls the server join API to join the user to the game. |
| **Join Observer** | Allows the user to specify which game they want to observe. They should be able to enter the number of the desired game. Your client will need to keep track of which number corresponds to which game from the last time it listed the games. Calls the server join API to verify that the specified game exists.                   |

### Gameplay UI

As stated previously, gameplay will not be implemented until later. For now, when a user joins or observes a game, the client should draw the initial state of a Chess game in the terminal, but not actually enter gameplay mode. The chessboard should be drawn twice, once in each orientation (i.e., once with white pieces at the bottom and once with black pieces at the bottom). An example of what the chessboard might look like is given below. You are free to make your chessboard look different as long as the essential information is displayed in an easily readable way.

![chessboard](ChessBoard.png)

### Tips for using Unicode chess characters

Should you choose to use the unicode Chess characters, some machines by default don't render them in the console (this is often the case if you are using a .jar file). To fix this on Windows (it shouldn't be an issue on Mac/Linux), go to Settings > Time and Language > Language and Region > Administrative Language Settings and on the Administrative tab click Change System Locale and then check the box for using UTF-8 which requires system reboot. This should fix the issue.

Additionally, you may notice that the chess characters might render slightly wider than everything else. To balance this out, a wider version of a space called an em-space can be used. It is denoted by \u2003 and is currently being used as the middle space in the `EMPTY` escape sequence, so if you choose not to use the Unicode chess pieces, you will have to replace that with a regular space to get everything to line up vertically.

## Unit Tests

Write positive and negative unit tests for each method on your ServerFacade class (all the methods used to call your server).

## Pass Off

There are no automated pass off test cases for this assignment. To pass off meet with a TA and demonstrate that your Chess client meets the requirements listed above.
