package passoffTests.chessTests.chessPieceTests;

import chess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoffTests.TestFactory;

import java.util.HashSet;
import java.util.Set;

public class RookMoveTests {

    private ChessBoard board;
    private ChessPiece rook;
    private ChessPosition position;
    private Set<ChessMove> validMoves;

    @BeforeEach
    public void setup() {
        board = TestFactory.getNewBoard();
        validMoves = new HashSet<>();
    }


    @Test
    @DisplayName("Move Until Edge")
    public void emptyBoard() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | |R| | | | | |
		| | | | | | | | |
         */

        rook = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        position = TestFactory.getNewPosition(2, 3);
        board.addPiece(position, rook);

        //up
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 4), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 5), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 6), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 8), null));

        //down
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 2), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 1), null));

        //left
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 3), null));

        //right
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(5, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(8, 3), null));

        //check
        Set<ChessMove> pieceMoves = new HashSet<>(rook.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Capture Enemy Pieces")
    public void capture() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|N| | | | | | | |
		|r| | | | |B| | |
		| | | | | | | | |
		|q| | | | | | | |
		| | | | | | | | |
         */

        rook = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        position = TestFactory.getNewPosition(4, 1);
        board.addPiece(position, rook);

        //allied pieces
        ChessPosition[] allyPiecePositions = {TestFactory.getNewPosition(2, 1)};
        board.addPiece(allyPiecePositions[0],
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));


        //enemy pieces
        ChessPosition[] enemyPiecePositions = {TestFactory.getNewPosition(4, 6), TestFactory.getNewPosition(5, 1)};
        board.addPiece(enemyPiecePositions[0],
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        board.addPiece(enemyPiecePositions[1],
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));


        //Get moves for bishop
        Set<ChessMove> pieceMoves = new HashSet<>(rook.pieceMoves(board, position));

        //Cannot capture friendlies
        for (ChessPosition allyPosition : allyPiecePositions) {
            ChessMove badCapture = TestFactory.getNewMove(position, allyPosition, null);
            Assertions.assertFalse(pieceMoves.contains(badCapture),
                    "Piece moves contained move: " + badCapture + " that would capture a ally piece");
        }

        //Can capture unfriendlies
        for (ChessPosition enemyPosition : enemyPiecePositions) {
            ChessMove capture = TestFactory.getNewMove(position, enemyPosition, null);
            Assertions.assertTrue(pieceMoves.contains(capture),
                    "Piece moves did not contain valid move: " + capture + " that would capture an enemy piece");
        }

        //left none

        //up
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(5, 1), null));

        //down
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 1), null));

        //right
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 2), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 4), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 5), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 6), null));


        //check
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Rook Completely Blocked")
    public void rookStuck() {

        /*
        | | | | | | |n|r|
		| | | | | | | |p|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        position = TestFactory.getNewPosition(8, 8);
        rook = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        board.addPiece(position, rook);

        //pieces in way
        board.addPiece(TestFactory.getNewPosition(7, 8),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        board.addPiece(TestFactory.getNewPosition(8, 7),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));

        //make sure move list is empty
        Assertions.assertTrue(rook.pieceMoves(board, position).isEmpty(),
                "ChessPiece pieceMoves returned valid moves for a trapped piece");
    }

}
