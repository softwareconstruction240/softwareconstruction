package passoffTests.chessTests.chessPieceTests;

import chess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoffTests.TestFactory;

import java.util.HashSet;
import java.util.Set;

public class QueenMoveTests {

    private ChessBoard board;
    private ChessPiece queen;
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
		| | | | | | |q| |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        queen = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        position = TestFactory.getNewPosition(7, 7);
        board.addPiece(position, queen);

        //down
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 6), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 5), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 4), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 2), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 1), null));

        //down right
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(8, 6), null));

        //right
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(8, 7), null));

        //up right
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(8, 8), null));

        //up
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 8), null));

        //up left
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 8), null));

        ///left
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(5, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 7), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 7), null));

        //down left
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 6), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(5, 5), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 4), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 2), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 1), null));

        //check
        Set<ChessMove> pieceMoves = new HashSet<>(queen.pieceMoves(board, position));
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Capture Enemy Pieces")
    public void capture() {

        /*
        |b| | | | | | | |
		| | | | | | | | |
		| | |R| | | | | |
		| | | | | | | | |
		|Q| | |p| | | | |
		| | | | | | | | |
		|P| |n| | | | | |
		| | | | | | | | |
         */

        queen = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        position = TestFactory.getNewPosition(4, 1);
        board.addPiece(position, queen);

        //allied pieces
        ChessPosition[] allyPiecePositions = {TestFactory.getNewPosition(2, 1), TestFactory.getNewPosition(6, 3)};
        board.addPiece(allyPiecePositions[0],
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        board.addPiece(allyPiecePositions[1],
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));


        //enemy pieces
        ChessPosition[] enemyPiecePositions =
                {TestFactory.getNewPosition(2, 3), TestFactory.getNewPosition(4, 4), TestFactory.getNewPosition(8, 1)};
        board.addPiece(enemyPiecePositions[0],
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        board.addPiece(enemyPiecePositions[1],
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        board.addPiece(enemyPiecePositions[2],
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));


        //Get moves for queen
        Set<ChessMove> pieceMoves = new HashSet<>(queen.pieceMoves(board, position));

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

        //down
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 1), null));

        //down right
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(3, 2), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 3), null));

        //right
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 2), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 3), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(4, 4), null));

        //up right
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(5, 2), null));

        //up
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(5, 1), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(6, 1), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(7, 1), null));
        validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(8, 1), null));

        //check
        Assertions.assertEquals(validMoves, pieceMoves,
                "ChessPiece pieceMoves did not return the correct moves");
    }


    @Test
    @DisplayName("Queen Completely Blocked")
    public void queenStuck() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P|R| | | | | | |
		|Q|K| | | | | | |
         */

        queen = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        position = TestFactory.getNewPosition(1, 1);
        board.addPiece(position, queen);

        board.addPiece(TestFactory.getNewPosition(1, 2),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        board.addPiece(TestFactory.getNewPosition(2, 1),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        board.addPiece(TestFactory.getNewPosition(2, 2),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        Set<ChessMove> pieceMoves = new HashSet<>(queen.pieceMoves(board, position));
        Assertions.assertTrue(pieceMoves.isEmpty(),
                "ChessPiece pieceMoves returned valid moves for a trapped piece");
    }

}
