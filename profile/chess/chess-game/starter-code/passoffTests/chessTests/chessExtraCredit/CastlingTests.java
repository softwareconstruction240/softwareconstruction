package passoffTests.chessTests.chessExtraCredit;

import chess.*;
import passoffTests.TestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests if the King can take the Castling move
 * Castling is a situational move the king can make as it's first move. If one of the rooks has not yet moved
 * and there are no pieces between the rook and the king, and the path is "safe", the king can castle. Castling is
 * performed by moving the king 2 spaces towards the qualifying rook, and the rook "jumping" the king to sit next
 * to the king on the opposite side it was previously. A path is considered "safe" if 1: the king is not in check
 * and 2: neither the space the king moves past nor the space the king ends up at can be reached by an opponents piece.
 */
public class CastlingTests {

    ChessBoard board;
    ChessPosition position;
    ChessPiece king;
    Set<ChessMove> moves;

    @BeforeEach
    public void setup(){
        board = TestFactory.getNewBoard();
        moves = new HashSet<>();
    }

    @Test
    public void canCastleWhite(){

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|R| | | |K| | |R|
         */

        //set up king
        king = TestFactory.getNewPiece(WHITE, ChessPiece.PieceType.KING);
        position = TestFactory.getNewPosition(1,5);
        board.addPiece(position, king);

        //add both rooks
        board.addPiece(TestFactory.getNewPosition(1,1), TestFactory.getNewPiece(WHITE, ROOK));
        board.addPiece(TestFactory.getNewPosition(1,8), TestFactory.getNewPiece(WHITE, ROOK));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(board);

        //check that with nothing in way, king can castle
        assertTrue(game.validMoves(position).contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(1,5), TestFactory.getNewPosition(1,3), null)));
        assertTrue(game.validMoves(position).contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(1,5), TestFactory.getNewPosition(1,7), null)));
    }

    @Test
    public void canCastleBlack(){

        /*
        |r| | | |k| | |r|
		| |p| | | | | |q|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|R| | | | | | | |
         */

        //set up king
        king = TestFactory.getNewPiece(BLACK, ChessPiece.PieceType.KING);
        position = TestFactory.getNewPosition(8,5);
        board.addPiece(position, king);

        //set up rooks
        board.addPiece(TestFactory.getNewPosition(8,1), TestFactory.getNewPiece(BLACK, ROOK));
        board.addPiece(TestFactory.getNewPosition(8,8), TestFactory.getNewPiece(BLACK, ROOK));

        //extra decoy pieces
        board.addPiece(TestFactory.getNewPosition(7,2), TestFactory.getNewPiece(BLACK, PAWN));
        board.addPiece(TestFactory.getNewPosition(1,1), TestFactory.getNewPiece(WHITE, ROOK));
        board.addPiece(TestFactory.getNewPosition(7,8), TestFactory.getNewPiece(BLACK, QUEEN));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(board);

        //check that with nothing in way, king can castle
        assertTrue(game.validMoves(position).contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(8,5), TestFactory.getNewPosition(8,3), null)));
        assertTrue(game.validMoves(position).contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(8,5), TestFactory.getNewPosition(8,7), null)));
    }

    @Test
    public void castlingBlockedByTeam(){
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|R| |B| |K| |Q|R|
         */

        //set up king
        king = TestFactory.getNewPiece(WHITE, KING);
        position = TestFactory.getNewPosition(1,5);
        board.addPiece(position, king);

        //set up rooks
        board.addPiece(TestFactory.getNewPosition(1,1), TestFactory.getNewPiece(WHITE, ROOK));
        board.addPiece(TestFactory.getNewPosition(1,8), TestFactory.getNewPiece(WHITE, ROOK));

        //own team pieces in way
        board.addPiece(TestFactory.getNewPosition(1,3), TestFactory.getNewPiece(WHITE, BISHOP));
        board.addPiece(TestFactory.getNewPosition(1,7), TestFactory.getNewPiece(WHITE, QUEEN));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(board);

        //make sure king cannot castle
        assertFalse(game.validMoves(position).contains(TestFactory.getNewMove(
            TestFactory.getNewPosition(1,5),  TestFactory.getNewPosition(1,3), null)));
        assertFalse(game.validMoves(position).contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(1,5), TestFactory.getNewPosition(1,7), null)));
    }

    @Test
    public void castlingBlockedByEnemy(){

        /*
        |r| | |B|k| | |r|
		| | | | | | | | |
		| | | | | |R| | |
		| | | | | | | | |
		| | | | | | | | |
		| |K| | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        //set up king
        king = TestFactory.getNewPiece(BLACK, ChessPiece.PieceType.KING);
        position = TestFactory.getNewPosition(8,5);
        board.addPiece(position, king);

        //set up rooks
        board.addPiece(TestFactory.getNewPosition(8,1), TestFactory.getNewPiece(BLACK, ROOK));
        board.addPiece(TestFactory.getNewPosition(8,8), TestFactory.getNewPiece(BLACK, ROOK));

        //enemy piece in way
        board.addPiece(TestFactory.getNewPosition(8,4), TestFactory.getNewPiece(WHITE, BISHOP));

        //enemy threatening in between position
        board.addPiece(TestFactory.getNewPosition(6,6), TestFactory.getNewPiece(WHITE, ROOK));

        //decoy enemy king
        board.addPiece(TestFactory.getNewPosition(3,2), TestFactory.getNewPiece(WHITE, KING));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(board);

        //make sure king cannot castle on either side
        assertFalse(game.validMoves(position).contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(8,5), TestFactory.getNewPosition(8,3), null)));
        assertFalse(game.validMoves(position).contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(8,5), TestFactory.getNewPosition(8,7), null)));
    }

    @Test
    public void noCastleAfterMove() throws InvalidMoveException {

        /*
        | | | | | | | | |
		|p| | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|R| | | |K| | |R|
         */

        //set up king
        king = TestFactory.getNewPiece(WHITE, ChessPiece.PieceType.KING);
        position = TestFactory.getNewPosition(1,5);
        board.addPiece(position, king);

        //add both rooks
        board.addPiece(TestFactory.getNewPosition(1,1), TestFactory.getNewPiece(WHITE, ROOK));
        board.addPiece(TestFactory.getNewPosition(1,8), TestFactory.getNewPiece(WHITE, ROOK));

        //enemy pawn for filler moves
        board.addPiece(TestFactory.getNewPosition(7,1), TestFactory.getNewPiece(BLACK, PAWN));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(board);
        game.setTeamTurn(WHITE);

        //move left rook
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(1,1),
                TestFactory.getNewPosition(1,4), null));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(7,1),
                TestFactory.getNewPosition(6,1), null));
        /*
        | | | | | | | | |
		| | | | | | | | |
		|p| | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |R|K| | |R|
         */

        //move rook back to starting spot
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(1,4),
                TestFactory.getNewPosition(1,1), null));
        /*
        | | | | | | | | |
		| | | | | | | | |
		|p| | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|R| | | |K| | |R|
         */

        //make sure king can't castle towards moved rook, but still can to unmoved rook
        assertFalse(game.validMoves(position).contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(1,5), TestFactory.getNewPosition(1,3),
                null)));
        assertTrue(game.validMoves(position).contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(1,5), TestFactory.getNewPosition(1,7),
                null)));

        //move king
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(6,1),
                TestFactory.getNewPosition(5,1), null));
        game.makeMove(TestFactory.getNewMove(position, TestFactory.getNewPosition(1,6),
                null));
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|p| | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|R| | | | |K| |R|
         */

        //move king back to starting position
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(5,1),
                TestFactory.getNewPosition(4,1), null));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(1,6),
                position, null));
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|p| | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|R| | | |K| | |R|
         */

        //make sure king can't castle anymore
        assertFalse(game.validMoves(position).contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(1,5), TestFactory.getNewPosition(1,3),
                null)));
        assertFalse(game.validMoves(position).contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(1,5), TestFactory.getNewPosition(1,7),
                null)));
    }
}
