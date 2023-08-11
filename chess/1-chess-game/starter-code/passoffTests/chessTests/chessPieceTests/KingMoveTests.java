package passoffTests.chessTests.chessPieceTests;

import chess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoffTests.TestFactory;

import java.util.HashSet;
import java.util.Set;

public class KingMoveTests {

    private ChessBoard board;
    private ChessPiece king;
    private ChessPosition position;
    private Set<ChessMove> validMoves;

    @BeforeEach
    public void setup() {
        board = TestFactory.getNewBoard();
        validMoves = new HashSet<>();
    }


    @Test
    @DisplayName("Move Unhindered")
    public void kingEmptyBoard() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | |K| | |
		| | | | | | | | |
		| | | | | | | | |
         */

        king = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        position = TestFactory.getNewPosition(3, 6);
        board.addPiece(position, king);


        //all 8 directions
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 6), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 5), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 5), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 5), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 6), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 7), null));


        Set<ChessMove> pieceMoves = new HashSet<>(king.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Capture Enemy Pieces")
    public void kingCapture() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |N|n| | | |
		| | | |k| | | | |
		| | |P|b|p| | | |
		| | | | | | | | |
         */

        king = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        position = TestFactory.getNewPosition(3, 4);
        board.addPiece(position, king);


        //allied pieces
        ChessPosition[] allyPiecePositions =
                {TestFactory.getNewPosition(4, 5), TestFactory.getNewPosition(2, 4), TestFactory.getNewPosition(2, 5)};

        board.addPiece(allyPiecePositions[0],
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        board.addPiece(allyPiecePositions[1],
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        board.addPiece(allyPiecePositions[2],
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));


        //enemy pieces
        ChessPosition[] enemyPiecePositions = {TestFactory.getNewPosition(2, 3), TestFactory.getNewPosition(4, 4)};

        board.addPiece(enemyPiecePositions[0],
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        board.addPiece(enemyPiecePositions[1],
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));

        //Get moves for king
        Set<ChessMove> pieceMoves = new HashSet<>(king.pieceMoves(board, position));

        //Cannot capture friendlies
        for (ChessPosition allyPiece : allyPiecePositions) {
            ChessMove badCapture = TestFactory.getNewMove(position, allyPiece, null);
            Assertions.assertFalse(pieceMoves.contains(badCapture),
                    "Piece moves contained invalid move: " + badCapture + " that would capture a ally piece");
        }

        //Can capture unfriendlies
        for (ChessPosition enemyPiece : enemyPiecePositions) {
            ChessMove capture = TestFactory.getNewMove(position, enemyPiece, null);
            Assertions.assertTrue(pieceMoves.contains(capture),
                    "Piece moves did not contain valid move: " + capture + " that would capture an enemy piece");
            validMoves.add(capture);
        }

        //normal moves
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 5), null));

        //check
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("King Completely Blocked")
    public void kingStuck() {

        /*
        | | | | | | |r|k|
		| | | | | | |p|p|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        king = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        position = TestFactory.getNewPosition(8, 8);
        board.addPiece(position, king);

        board.addPiece(TestFactory.getNewPosition(8, 7),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        board.addPiece(TestFactory.getNewPosition(7, 8),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        board.addPiece(TestFactory.getNewPosition(7, 7),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));

        Set<ChessMove> pieceMoves = new HashSet<>(king.pieceMoves(board, position));
        Assertions.assertTrue(pieceMoves.isEmpty(),
                "ChessPiece pieceMoves returned valid moves for a trapped piece");
    }

}
