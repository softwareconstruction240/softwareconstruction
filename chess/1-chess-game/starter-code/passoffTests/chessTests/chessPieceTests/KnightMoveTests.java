package passoffTests.chessTests.chessPieceTests;

import chess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoffTests.TestFactory;

import java.util.HashSet;
import java.util.Set;

public class KnightMoveTests {

    private ChessBoard board;
    private ChessPiece knight;
    private ChessPosition position;
    private Set<ChessMove> validMoves;

    @BeforeEach
    public void setup() {
        board = TestFactory.getNewBoard();
        validMoves = new HashSet<>();
    }


    @Test
    @DisplayName("Middle of Board")
    public void emptyBoard() {
        //white
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | |N| | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */
        knight = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        position = TestFactory.getNewPosition(5, 5);
        board.addPiece(position, knight);

        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 4), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 6), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 6), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 4), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 3), null));


        Set<ChessMove> pieceMoves = new HashSet<>(knight.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");

        //black
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | |n| | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */
        board = TestFactory.getNewBoard();
        knight = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board.addPiece(position, knight);

        pieceMoves = new HashSet<>(knight.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Board Edge")
    public void boardEdge() {
        //left
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|n| | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */
        position = TestFactory.getNewPosition(4, 1);
        knight = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board.addPiece(position, knight);

        //add in moves
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 2), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 2), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(5, 3), null));

        Set<ChessMove> pieceMoves = new HashSet<>(knight.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");


        //right
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | |n|
		| | | | | | | | |
		| | | | | | | | |
         */
        position = TestFactory.getNewPosition(3, 8);
        knight = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board = TestFactory.getNewBoard();
        board.addPiece(position, knight);
        validMoves.clear();

        //add in moves
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(5, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 6), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 6), null));

        pieceMoves = new HashSet<>(knight.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");


        //bottom
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | |N| | |
         */
        position = TestFactory.getNewPosition(1, 6);
        knight = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board = TestFactory.getNewBoard();
        board.addPiece(position, knight);
        validMoves.clear();

        //add in moves
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 8), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 4), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 5), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 7), null));

        pieceMoves = new HashSet<>(knight.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");


        //top
        /*
        | | |N| | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */
        position = TestFactory.getNewPosition(8, 3);
        knight = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board = TestFactory.getNewBoard();
        board.addPiece(position, knight);
        validMoves.clear();

        //add in moves
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 1), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 5), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 4), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 2), null));

        pieceMoves = new HashSet<>(knight.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Board Corner")
    public void boardCorner() {
        //bottom right
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | |N|
         */
        position = TestFactory.getNewPosition(1, 8);
        knight = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board.addPiece(position, knight);

        //add in moves
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 6), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 7), null));

        Set<ChessMove> pieceMoves = new HashSet<>(knight.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");


        //top right
        /*
        | | | | | | | |N|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */
        position = TestFactory.getNewPosition(8, 8);
        knight = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board = TestFactory.getNewBoard();
        board.addPiece(position, knight);
        validMoves.clear();

        //add in moves
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 6), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 7), null));

        pieceMoves = new HashSet<>(knight.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");


        //top left
        /*
        |n| | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */
        position = TestFactory.getNewPosition(8, 1);
        knight = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board = TestFactory.getNewBoard();
        board.addPiece(position, knight);
        validMoves.clear();

        //add in moves
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 2), null));

        pieceMoves = new HashSet<>(knight.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");


        //bottom left
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|n| | | | | | | |
         */
        position = TestFactory.getNewPosition(1, 1);
        knight = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board = TestFactory.getNewBoard();
        board.addPiece(position, knight);
        validMoves.clear();

        //add in moves
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 2), null));

        pieceMoves = new HashSet<>(knight.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Blocked by Friendly Pieces")
    public void allyPieces() {

        /*
        | | | | | | | | |
		| | | |R| | | | |
		| | | | | | |P| |
		| | | | |N| | | |
		| | |N| | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        knight = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        position = TestFactory.getNewPosition(5, 5);
        board.addPiece(position, knight);

        //add pieces
        ChessPosition[] allyPiecePositions =
                {TestFactory.getNewPosition(6, 7), TestFactory.getNewPosition(7, 4), TestFactory.getNewPosition(4, 3)};

        board.addPiece(allyPiecePositions[0],
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        board.addPiece(allyPiecePositions[1],
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        board.addPiece(allyPiecePositions[2],
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));


        Set<ChessMove> pieceMoves = new HashSet<>(knight.pieceMoves(board, position));

        //Cannot capture friendlies
        for (ChessPosition allyPiece : allyPiecePositions) {
            ChessMove badCapture = TestFactory.getNewMove(position, allyPiece, null);
            Assertions.assertFalse(pieceMoves.contains(badCapture),
                    "Piece moves contained invalid move: " + badCapture + " that would capture a ally piece");
        }

        //still available moves
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 4), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 6), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 6), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 3), null));

        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Capture Enemy Pieces")
    public void enemyPieces() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | |n| | | |
		| | |N| | | | | |
		| | | |P| |R| | |
		| | | | | | | | |
		| | | | | | | | |
         */

        knight = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        position = TestFactory.getNewPosition(5, 5);
        board.addPiece(position, knight);

        //add pieces
        ChessPosition[] enemyPiecePositions =
                {TestFactory.getNewPosition(3, 4), TestFactory.getNewPosition(3, 6), TestFactory.getNewPosition(4, 3)};
        board.addPiece(enemyPiecePositions[0],
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        board.addPiece(enemyPiecePositions[1],
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        board.addPiece(enemyPiecePositions[2],
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));

        //Get moves for knight
        Set<ChessMove> pieceMoves = new HashSet<>(knight.pieceMoves(board, position));

        //Can capture unfriendlies
        for (ChessPosition enemyPiece : enemyPiecePositions) {
            ChessMove capture = TestFactory.getNewMove(position, enemyPiece, null);
            Assertions.assertTrue(pieceMoves.contains(capture),
                    "Piece moves did not contain valid move: " + capture + " that would capture an enemy piece");
            validMoves.add(capture);
        }

        //capture moves
        for (ChessPosition enemyPiecePosition : enemyPiecePositions) {
            validMoves.add(TestFactory.getNewMove(position, enemyPiecePosition, null));
        }

        //moves to empty squares
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 6), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 4), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 3), null));

        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }

}
