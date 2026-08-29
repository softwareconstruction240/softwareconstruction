# The Game of Chess

![playingChess.png](playingChess.png)

Chess is a strategy game where 2 players take turns moving pieces and capturing enemy pieces with the goal of trapping the enemy king. Each of the 6 types of pieces has a unique way of moving/capturing. A piece captures an enemy piece when moving to the enemy piece’s location, after which the enemy piece is removed from the game. You win when your opponent has no way to avoid you being able to capture their king.

## A Brief History of Strategy

The origins of chess trace back nearly 1,500 years to 6th-century India, where it was known as *Chaturanga*. This early version represented the four divisions of the Indian military: infantry, cavalry, elephants, and chariots. As the game traveled along the Silk Road to Persia and eventually Europe, the pieces evolved to reflect medieval society—turning chariots into rooks and elephants into bishops.

By the late 15th century, the rules were refined into the modern version we play today. The "Mad Queen" era saw the Queen become the most powerful piece on the board, significantly speeding up the pace of the game. Today, chess is a global phenomenon, governed by FIDE (the International Chess Federation) and played by millions of people and computers alike.

## Rules

Chess is a turn based game. White moves first, then black, and then repeat until the game ends. A game ends whenever a player obtains **Checkmate**. Checkmate means that your opponent's king is being attacked, the king cannot move to a safe square, the attack cannot be blocked, and the attacker cannot be killed.

```mermaid
%%{init: { 'theme': 'neutral', 'themeVariables': { 'mainBkg': '#ffffff', 'lineColor': '#000000', 'primaryTextColor': '#000000', 'actorBorder': '#000000', 'participantBorder': '#000000', 'noteBorderColor': '#000000' } }}%%
stateDiagram-v2
    [*] --> WhiteTurn
    WhiteTurn --> CheckForCheckmate : Move Made
    CheckForCheckmate --> BlackTurn : Game Continues
    CheckForCheckmate --> GameEnd : Checkmate/Stalemate
    BlackTurn --> CheckForCheckmate : Move Made
    GameEnd --> [*]
```


### Board Setup

The game is played on an 8x8 grid of 64 squares, alternating between light and dark colors. When setting up the board, players must ensure that a "light" square is in the bottom-right corner for both sides.

In software engineering terms, we often represent this board as a 2D array or a bitboard. Each square is identified by a coordinate system known as algebraic notation: files are labeled **a** through **h**, and ranks are numbered **1** through **8**.

Each player has 8 pawns *(p)*, 1 King *(k)*, 1 Queen *(q)*, 2 Bishops *(b)*, 2 Knights *(n)*, and 2 Rooks *(r)*. This gives each player a total of 16 pieces.

Below is the setup using the first letter to denote each piece (using `n` for knight). Capital letters represent white pieces and lowercase letters represent black pieces.

```sh
|r|n|b|q|k|b|n|r|
|p|p|p|p|p|p|p|p|
| | | | | | | | |
| | | | | | | | |
| | | | | | | | |
| | | | | | | | |
|P|P|P|P|P|P|P|P|
|R|N|B|Q|K|B|N|R|
```

Here is what the board would look like graphically. Notice that there should be a white square on the right of each player and the queen should be placed on a square that matches her color.

![chessboard](chess-board.png)


### Movement

Each piece has a unique movement profile, dictating its value and strategic utility on the board.

### The King
The King is the most important piece but also one of the most vulnerable. It can move exactly one square in any direction: horizontally, vertically, or diagonally. While it lacks the range of other pieces, its safety is the primary objective of the game.

#### Castling

Castling is a special move in chess that involves the King and one of the Rooks, designed to secure the King’s position and bring the Rook toward the center of the board. During this move, the King slides two squares toward the Rook, and the Rook jumps over the King to land on the square immediately adjacent to it.

Castling is only legal under specific conditions:

1. Neither the King nor the Rook involved can have moved earlier in the game.
1. The path between them must be clear of other pieces.
1. The King is not currently in check and the King does not pass through a square attacked by an opponent's piece.


### The Queen
The Queen is the most powerful piece, combining the movement capabilities of the Rook and the Bishop. She can move any number of squares along a rank, file, or diagonal, provided no other pieces are in her path.

### The Rook
Rooks move any number of squares horizontally or vertically. They are particularly powerful in the "endgame" when the board is clear and they can control open files.

### The Bishop
Bishops move any number of squares diagonally. Because they stay on the same color throughout the game, a player starts with one "light-squared" bishop and one "dark-squared" bishop.

### The Knight
The Knight moves in an "L" shape: two squares in one cardinal direction and then one square perpendicularly. Uniquely, the Knight is the only piece that can "jump" over other pieces, making it a dangerous tool in crowded, closed positions.

### The Pawn
Pawns are the "soul of chess." They move forward one square at a time, but they capture diagonally. On their very first move, a pawn has the option to move forward two squares.

 Pawns also have two special rules:
- **Promotion:** If a pawn reaches the 8th rank, it must be promoted to any other piece (usually a Queen).
- **En Passant:** A complex capture rule that occurs when a pawn moves two squares forward and lands horizontally adjacent to an opponent's pawn.

## Check, Checkmate, and Stalemate

The game of chess revolves around the status of the King. Understanding the distinction between these three states is critical for both players and programmers.

**Check** occurs when the King is under immediate attack by an opponent's piece. A player in check must immediately move their King, block the attack, or capture the attacking piece. You cannot move into check or remain in check.

**Checkmate** is the goal of the game. It occurs when a King is in check and there are no legal moves available to escape the threat. When checkmate is delivered, the game ends immediately.

![checkmate.png](checkmate.png)


**Stalemate** is a unique type of draw. It occurs when the player whose turn it is has no legal moves, but their King is **not** currently in check. In competitive play, this results in a split point, often frustrating the player who had the material advantage.

## Winning the Game

While checkmate is the most common way to win, a game can also end if:
- A player **resigns**, acknowledging that their position is hopeless.
- A player **runs out of time** (in timed matches).
- A **draw** is agreed upon by both players.
- A **draw by repetition** occurs (the same board position appears three times).

## Strategic Foundations

To transition from knowing the rules to playing effectively, consider these three core principles:

1.  **Control the Center:** The squares d4, d5, e4, and e5 are the most important on the board. Pieces placed in the center have the greatest mobility and control.
2.  **Develop Your Pieces:** Do not simply move pawns. Bring your Knights and Bishops out early to active squares where they can influence the game.
3.  **King Safety:** Usually achieved through **castling**, a special move involving the King and a Rook. An uncastled King in the center is an easy target for an organized attack.


![chessStrategy.jpg](chessStrategy.jpg)

## Summary

Chess is a game of perfect information; nothing is hidden, and there is no luck involved. By understanding the movement of the pieces, the conditions of checkmate, and basic strategic positioning, you lay the groundwork for more advanced study. In our next sessions, we will look at how to translate these physical movements into logical constraints within a GitHub repository, preparing you for the architectural challenges of the Programming Exam.

### External Resources
- [Lichess.org - Learn Chess](https://lichess.org/learn): An excellent interactive tool for practicing piece movements.
- [Chess.com - Rules and Basics](https://www.chess.com/learn-how-to-play-chess): Comprehensive guides on opening theory and tactics.
- [FIDE Laws of Chess](https://handbook.fide.com/chapter/E012023): The official handbook for competitive play.