# The Game of Chess

Chess is a strategy game where 2 players take turns moving pieces and capturing enemy pieces with the goal of trapping the enemy king. Each of the 6 types of pieces has a unique way of moving/capturing. A piece captures an enemy piece when moving to the enemy piece’s location, after which the enemy piece is removed from the game. You win when your opponent has no way to avoid you being able to capture their king.

### Movement

#### King

Kings may move 1 square in any direction (including diagonal) to either a position occupied by an enemy piece (capturing the enemy piece), or to an unoccupied position. A player is not allowed to make any move that would allow the opponent to capture their King. If your King is in danger of being captured on your turn, you must make a move that removes your King from immediate danger.

#### Pawn

Pawns normally may move forward one square if that square is unoccupied, though if it is the first time that pawn is being moved, it may be moved forward 2 squares (provided both squares are unoccupied).
Pawns cannot capture forward, but instead capture forward diagonally (1 square forward and 1 square sideways). They may only move diagonally like this if capturing an enemy piece.
When a pawn moves to the end of the board (row 8 for white and row 1 for black), they get promoted and are replaced with the player's choice of Rook, Knight, Bishop, or Queen (they cannot stay a Pawn or become King).

#### Rook

Rooks may move in straight lines as far as there is open space. If there is an enemy piece at the end of the line, rooks may move to that position and capture the enemy piece.

#### Knight

Knights move in an `L` shape, moving 2 squares in one direction and 1 square in the other direction. Knights are the only piece that can ignore pieces in the in-between squares (they can "jump" over other pieces). They can move to squares occupied by an enemy piece and capture the enemy piece, or to unoccupied squares.

#### Bishop

Bishops move in diagonal lines as far as there is open space. If there is an enemy piece at the end of the diagonal, the bishop may move to that position and capture the enemy piece.

#### Queen

Queens are the most powerful piece and may move in straight lines and diagonals as far as there is open space. If there is an enemy piece at the end of the line, they may move to that position and capture the enemy piece. (In simpler terms, Queens can take all moves a Rook or Bishop could take from the Queen's position).

### Board Setup

Each player starts with 8 pawns, 1 King, 1 Queen, 2 Bishops, 2 Knight, and 2 Rooks. This gives you a total of 16 pieces per player. White’s pawns span row 2, while Black’s pawns span row 7. The rest of the pieces are lined up across row 1 for white and row 8 for black in the following columns: 1: Rook, 2: Knight, 3: Bishop, 4: Queen, 5: King, 6: Bishop, 7: Knight, 8: Rook. A black square should always be the square in the bottom left side of the board.

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

Here are some guides that may help you learn the rules of chess:

- [https://www.chess.com/
  learn-how-to-play-chess](https://www.chess.com/learn-how-to-play-chess)
- [https://en.wikipedia.org/wiki/Rules_of_chess](https://en.wikipedia.org/wiki/Rules_of_chess)
