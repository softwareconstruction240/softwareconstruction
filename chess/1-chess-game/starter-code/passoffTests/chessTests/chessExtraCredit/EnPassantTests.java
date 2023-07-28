package passoffTests.chessTests.chessExtraCredit;

import chess.*;
import passoffTests.TestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.PAWN;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test's if Pawns can make En Passant move
 * En Passant is a situational move in chess taken directly after your opponent has double moved a pawn
 * If their pawn moves next to one of your pawns, so it passes where your pawn could have captured it, you
 * may capture their pawn with your pawn as if they had only moved a single space. You may only take this move
 * if you do so the turn directly following the pawns double move. This is as if you had caught their
 * pawn "in passing", or translated to French: "En Passant".
 */
public class EnPassantTests {
    private ChessBoard chessBoard;

    @BeforeEach
    public void setup(){
        chessBoard = TestFactory.getNewBoard();
    }

    @Test
    public void enPassantWhiteRight() throws InvalidMoveException {

        /*
        | | | | | | | | |
		| | |p| | | | | |
		| | | | | | | | |
		| |P| | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        //white pawn
        ChessPiece pawn = TestFactory.getNewPiece(WHITE, PAWN);
        ChessPosition position = TestFactory.getNewPosition(5,2);
        chessBoard.addPiece(position, pawn);

        //black pawn that will double move
        chessBoard.addPiece(TestFactory.getNewPosition(7,3), TestFactory.getNewPiece(BLACK, PAWN));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(chessBoard);
        game.setTeamTurn(BLACK);

        //move black piece 2 spaces
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(7,3),
                TestFactory.getNewPosition(5,3), null));
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| |P|p| | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        Collection<ChessMove> moves = game.validMoves(position);
        //make sure pawn has En Passant move
        assertTrue(moves.contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(5,2), TestFactory.getNewPosition(6,3),
                null)));
    }

    @Test
    public void enPassantWhiteLeft() throws InvalidMoveException {

        /*
        | | | | | | | | |
		| | |p| | | | | |
		| | | | | | | | |
		| | | |P| | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        //white pawn
        ChessPiece pawn = TestFactory.getNewPiece(WHITE, PAWN);
        ChessPosition position = TestFactory.getNewPosition(5,4);
        chessBoard.addPiece(position, pawn);

        //black pawn that will double move
        chessBoard.addPiece(TestFactory.getNewPosition(7,3), TestFactory.getNewPiece(BLACK, PAWN));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(chessBoard);
        game.setTeamTurn(BLACK);

        //move black piece 2 spaces
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(7,3),
                TestFactory.getNewPosition(5,3), null));
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | |p|P| | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        //make sure pawn has En Passant move
        assertTrue(game.validMoves(position).contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(5,4), TestFactory.getNewPosition(6,3),
                null)));
    }

    @Test
    public void imPassantBlackRight() throws InvalidMoveException {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | |p| | |
		| | | | | | | | |
		| | | | | | |P| |
		| | | | | | | | |
         */

        //black pawn
        ChessPiece pawn = TestFactory.getNewPiece(BLACK, PAWN);
        ChessPosition position = TestFactory.getNewPosition(4,6);
        chessBoard.addPiece(position, pawn);

        //white pawn that will double move
        chessBoard.addPiece(TestFactory.getNewPosition(2,7), TestFactory.getNewPiece(WHITE, PAWN));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(chessBoard);
        game.setTeamTurn(WHITE);

        //move white piece 2 spaces
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(2,7),
                TestFactory.getNewPosition(4,7), null));
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | |p|P| |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        //make sure pawn has En Passant move
        assertTrue(game.validMoves(position).contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(4,6), TestFactory.getNewPosition(3,7),
                null)));
    }

    @Test
    public void imPassantBlackLeft() throws InvalidMoveException {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | |p|
		| | | | | | | | |
		| | | | | | |P| |
		| | | | | | | | |
         */

        //Black pawn
        ChessPiece pawn = TestFactory.getNewPiece(BLACK, PAWN);
        ChessPosition position = TestFactory.getNewPosition(4,8);
        chessBoard.addPiece(position, pawn);

        //white pawn that will double move
        chessBoard.addPiece(TestFactory.getNewPosition(2,7), TestFactory.getNewPiece(WHITE, PAWN));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(chessBoard);
        game.setTeamTurn(WHITE);

        //move white piece 2 spaces
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(2,7),
                TestFactory.getNewPosition(4,7), null));
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | |P|p|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        //make sure pawn has En Passant move
        assertTrue(game.validMoves(position).contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(4,8), TestFactory.getNewPosition(3,7),
                null)));
    }

    @Test
    public void missedEnPassant() throws InvalidMoveException {

        /*
        | | | | | | | | |
		| | |p| | | | | |
		| | | | | | | |P|
		| |P| | | | | | |
		| | | | | | | | |
		| | | | | | | |p|
		| | | | | | | | |
		| | | | | | | | |
         */

        //white pawn on board
        ChessPiece pawn = TestFactory.getNewPiece(WHITE, PAWN);
        ChessPosition position = TestFactory.getNewPosition(5,2);
        chessBoard.addPiece(position, pawn);

        //black pawn that double moves
        chessBoard.addPiece(TestFactory.getNewPosition(7,3), TestFactory.getNewPiece(BLACK, PAWN));

        //extra pawns
        chessBoard.addPiece(TestFactory.getNewPosition(3,8), TestFactory.getNewPiece(BLACK, PAWN));
        chessBoard.addPiece(TestFactory.getNewPosition(6,8), TestFactory.getNewPiece(WHITE, PAWN));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(chessBoard);
        game.setTeamTurn(BLACK);

        //move black piece 2 spaces
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(7,3),
                TestFactory.getNewPosition(5,3), null));
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | |P|
		| |P|p| | | | | |
		| | | | | | | | |
		| | | | | | | |p|
		| | | | | | | | |
		| | | | | | | | |
         */

        //filler moves
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(6,8),
                TestFactory.getNewPosition(7,8), null));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(3,8),
                TestFactory.getNewPosition(2,8), null));
        /*
        | | | | | | | | |
		| | | | | | | |P|
		| | | | | | | | |
		| |P|p| | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | |p|
		| | | | | | | | |
         */

        //make sure pawn cannot do En Passant move
        assertFalse(game.validMoves(position).contains(TestFactory.getNewMove(
                TestFactory.getNewPosition(5,2), TestFactory.getNewPosition(6,3),
                null)));
    }
}
