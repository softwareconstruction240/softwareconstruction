package passoffTests.chessTests.chessPieceTests;

import chess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoffTests.TestFactory;

import java.util.HashSet;
import java.util.Set;

public class BishopMoveTests {

    private ChessBoard board;
    private ChessPiece bishop;
    private ChessPosition position;
    private Set<ChessMove> validMoves;

    @BeforeEach
    public void setup() {
        board = TestFactory.getNewBoard();
        validMoves = new HashSet<>();
    }


    @Test
    @DisplayName("Move Until Edge")
    public void bishopEmptyBoard() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |B| | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        position = TestFactory.getNewPosition(5, 4);
        bishop = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board.addPiece(position, bishop);

        //- -
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 2), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 1), null));

        // + -
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 2), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(8, 1), null));

        // - +
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 5), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 6), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 8), null));

        // + +
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 5), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 6), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(8, 7), null));


        //check
        Set<ChessMove> pieceMoves = new HashSet<>(bishop.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Capture Enemy Pieces")
    public void bishopCapture() {

        /*
        | | | | | | | | |
		| | | |Q| | | | |
		| | | | | | | | |
		| |b| | | | | | |
		|r| | | | | | | |
		| | | | | | | | |
		| | | | |P| | | |
		| | | | | | | | |
         */

        position = TestFactory.getNewPosition(5, 2);
        bishop = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board.addPiece(position, bishop);

        //allied pieces
        ChessPosition[] allyPiecePositions = {TestFactory.getNewPosition(4, 1)};
        board.addPiece(allyPiecePositions[0],
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));


        //enemy pieces
        ChessPosition[] enemyPiecePositions = {TestFactory.getNewPosition(2, 5), TestFactory.getNewPosition(7, 4)};
        board.addPiece(enemyPiecePositions[0],
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        board.addPiece(enemyPiecePositions[1],
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));


        //Get moves for bishop
        Set<ChessMove> pieceMoves = new HashSet<>(bishop.pieceMoves(board, position));

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

        // - - none

        // + -
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 1), null));


        // - +
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 4), null));
        validMoves.add(TestFactory.getNewMove(position, enemyPiecePositions[0], null));


        // + +
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 3), null));
        validMoves.add(TestFactory.getNewMove(position, enemyPiecePositions[1], null));


        //check
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Bishop Completely Blocked")
    public void stuck() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | |R| |P| |
		| | | | | |B| | |
         */

        position = TestFactory.getNewPosition(1, 6);
        bishop = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board.addPiece(position, bishop);

        //pieces in way
        board.addPiece(TestFactory.getNewPosition(2, 7),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        board.addPiece(TestFactory.getNewPosition(2, 5),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        //make sure move list is empty
        Assertions.assertTrue(bishop.pieceMoves(board, position).isEmpty(),
                "ChessPiece pieceMoves returned valid moves for a trapped piece");
    }

}
