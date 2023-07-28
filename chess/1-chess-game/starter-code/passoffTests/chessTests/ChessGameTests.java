package passoffTests.chessTests;

import passoffTests.TestFactory;
import chess.ChessBoard;
import chess.ChessGame;
import chess.InvalidMoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static passoffTests.TestFactory.*;
import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;
import static org.junit.jupiter.api.Assertions.*;

public class ChessGameTests {
    ChessGame game;

    @BeforeEach
    public void setup(){
        //start with a new board each time
        game = getNewGame();
        ChessBoard board = getNewBoard();
        board.resetBoard();
        game.setBoard(board);
    }

    @Test
    public void defaultGameBoard(){

        /*
        |r|n|b|q|k|b|n|r|
		|p|p|p|p|p|p|p|p|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P|P|P|P|P|P|P|P|
		|R|N|B|Q|K|B|N|R|
         */

        ChessBoard board = TestFactory.getNewBoard();

        //set board to default
        board.resetBoard();

        //back row pieces
        //white
        assertEquals(ROOK, board.getPiece(TestFactory.getNewPosition(1,1)).getPieceType());
        assertEquals(KNIGHT, board.getPiece(TestFactory.getNewPosition(1,2)).getPieceType());
        assertEquals(BISHOP, board.getPiece(TestFactory.getNewPosition(1,3)).getPieceType());
        assertEquals(QUEEN, board.getPiece(TestFactory.getNewPosition(1,4)).getPieceType());
        assertEquals(KING, board.getPiece(TestFactory.getNewPosition(1,5)).getPieceType());
        assertEquals(BISHOP, board.getPiece(TestFactory.getNewPosition(1,6)).getPieceType());
        assertEquals(KNIGHT, board.getPiece(TestFactory.getNewPosition(1,7)).getPieceType());
        assertEquals(ROOK, board.getPiece(TestFactory.getNewPosition(1,8)).getPieceType());

        //black
        assertEquals(ROOK, board.getPiece(TestFactory.getNewPosition(8,1)).getPieceType());
        assertEquals(KNIGHT, board.getPiece(TestFactory.getNewPosition(8,2)).getPieceType());
        assertEquals(BISHOP, board.getPiece(TestFactory.getNewPosition(8,3)).getPieceType());
        assertEquals(QUEEN, board.getPiece(TestFactory.getNewPosition(8,4)).getPieceType());
        assertEquals(KING, board.getPiece(TestFactory.getNewPosition(8,5)).getPieceType());
        assertEquals(BISHOP, board.getPiece(TestFactory.getNewPosition(8,6)).getPieceType());
        assertEquals(KNIGHT, board.getPiece(TestFactory.getNewPosition(8,7)).getPieceType());
        assertEquals(ROOK, board.getPiece(TestFactory.getNewPosition(8,8)).getPieceType());

        //pawns
        for (int column = 1; column <= 8; ++column){
            assertEquals(PAWN, board.getPiece(TestFactory.getNewPosition(2, column)).getPieceType());
            assertEquals(PAWN, board.getPiece(TestFactory.getNewPosition(7, column)).getPieceType());
        }

        //check color
        for (int column = 1; column <=8; ++column){
            //white team
            assertEquals(WHITE, board.getPiece(TestFactory.getNewPosition(1, column)).getTeamColor());
            assertEquals(WHITE,board.getPiece(TestFactory.getNewPosition(2, column)).getTeamColor());

            //black team
            assertEquals(BLACK, board.getPiece(TestFactory.getNewPosition(7, column)).getTeamColor());
            assertEquals(BLACK, board.getPiece(TestFactory.getNewPosition(8, column)).getTeamColor());
        }
    }

    @Test
    public void whiteCheck(){

        /*
        | | | | | | | |k|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| |K| | | |r| | |
		| | | | | | | | |
		| | | | | | | | |
         */

        ChessBoard board = TestFactory.getNewBoard();

        //white king
        board.addPiece(TestFactory.getNewPosition(3,2), TestFactory.getNewPiece(WHITE, KING));

        //black king
        board.addPiece(TestFactory.getNewPosition(8,8), TestFactory.getNewPiece(BLACK, KING));

        //threatening piece
        board.addPiece(TestFactory.getNewPosition(3,6), TestFactory.getNewPiece(BLACK, ROOK));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(board);
        game.setTeamTurn(WHITE);

        assertTrue(game.isInCheck(WHITE));
        assertFalse(game.isInCheck(BLACK));
    }

    @Test
    public void blackCheck(){

        /*
        | | | |K| | | | |
		| | | | | | | | |
		| | | |k| | | | |
		| | | | | | | | |
		| | | | | | | | |
		|B| | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        ChessBoard board = TestFactory.getNewBoard();

        //black king
        board.addPiece(TestFactory.getNewPosition(6,4), TestFactory.getNewPiece(BLACK, KING));

        //white king
        board.addPiece(TestFactory.getNewPosition(8,4), TestFactory.getNewPiece(WHITE, KING));

        //threatening piece
        board.addPiece(TestFactory.getNewPosition(3,1), TestFactory.getNewPiece(WHITE, BISHOP));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(board);

        assertTrue(game.isInCheck(BLACK));
        assertFalse(game.isInCheck(WHITE));
    }

    @Test
    public void whiteTeamCheckmate(){

        /*
        | | | | | | | | |
		| | |b|q| | | | |
		| | | | | | | | |
		| | | |p| | | |k|
		| | | | | |K| | |
		| | |r| | | | | |
		| | | | |n| | | |
		| | | | | | | | |
         */

        ChessBoard board = TestFactory.getNewBoard();

        //white king
        board.addPiece(TestFactory.getNewPosition(4,6), TestFactory.getNewPiece(WHITE, KING));

        //black pieces
        board.addPiece(TestFactory.getNewPosition(3,3), TestFactory.getNewPiece(BLACK, ROOK));
        board.addPiece(TestFactory.getNewPosition(7,3), TestFactory.getNewPiece(BLACK, BISHOP));
        board.addPiece(TestFactory.getNewPosition(5,4), TestFactory.getNewPiece(BLACK, PAWN));
        board.addPiece(TestFactory.getNewPosition(7,4), TestFactory.getNewPiece(BLACK, QUEEN));
        board.addPiece(TestFactory.getNewPosition(2,5), TestFactory.getNewPiece(BLACK, KNIGHT));
        board.addPiece(TestFactory.getNewPosition(5,8), TestFactory.getNewPiece(BLACK, KING));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(board);

        assertTrue(game.isInCheckmate(WHITE));
        assertFalse(game.isInCheckmate(BLACK));
    }

    @Test
    public void pawnCheckmate(){

        /*
        | | | |k| | | | |
		| | | |P|P| | | |
		| |P| | |P|P| | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |K| | | | |
         */

        ChessBoard board = TestFactory.getNewBoard();
        //black king
        board.addPiece(TestFactory.getNewPosition(8,4), TestFactory.getNewPiece(BLACK, KING));

        //white king
        board.addPiece(TestFactory.getNewPosition(1,4), TestFactory.getNewPiece(WHITE, KING));

        //pawns
        board.addPiece(TestFactory.getNewPosition(6,2), TestFactory.getNewPiece(WHITE, PAWN));
        board.addPiece(TestFactory.getNewPosition(7,4), TestFactory.getNewPiece(WHITE, PAWN));
        board.addPiece(TestFactory.getNewPosition(6,5), TestFactory.getNewPiece(WHITE, PAWN));
        board.addPiece(TestFactory.getNewPosition(7,5), TestFactory.getNewPiece(WHITE, PAWN));
        board.addPiece(TestFactory.getNewPosition(6,6), TestFactory.getNewPiece(WHITE, PAWN));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(board);

        assertTrue(game.isInCheckmate(BLACK));
        assertFalse(game.isInCheckmate(WHITE));

    }

    @Test
    public void stalemate() {

        /*
        |k| | | | | | | |
		| | | | | | | |r|
		| | | | | | | | |
		| | | | |q| | | |
		| | | |n| | |K| |
		| | | | | | | | |
		| | | | | | | | |
		| | | | |b| | | |
         */

        ChessBoard board = TestFactory.getNewBoard();

        //white king
        board.addPiece(TestFactory.getNewPosition(4,7), TestFactory.getNewPiece(WHITE, KING));

        //black king
        board.addPiece(TestFactory.getNewPosition(8,1), TestFactory.getNewPiece(BLACK, KING));

        //pieces pinning king
        board.addPiece(TestFactory.getNewPosition(4,4), TestFactory.getNewPiece(BLACK, KNIGHT));
        board.addPiece(TestFactory.getNewPosition(1,5), TestFactory.getNewPiece(BLACK, BISHOP));
        board.addPiece(TestFactory.getNewPosition(5,5), TestFactory.getNewPiece(BLACK, QUEEN));
        board.addPiece(TestFactory.getNewPosition(7,8), TestFactory.getNewPiece(BLACK, ROOK));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(board);
        game.setTeamTurn(WHITE);

        assertTrue(game.isInStalemate(WHITE));
        assertFalse(game.isInStalemate(BLACK));
    }

    @Test
    public void scholarsMate() throws InvalidMoveException {
        assertFalse(game.isInCheckmate(ChessGame.TeamColor.BLACK));

        game.setTeamTurn(WHITE);
        /*
        |r|n|b|q|k|b|n|r|
		|p|p|p|p|p|p|p|p|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P|P|P|P|P|P|P|P|
		|R|N|B|Q|K|B|N|R|
         */

        game.makeMove(getNewMove(getNewPosition(2,5), getNewPosition(4,5), null));
        /*
        |r|n|b|q|k|b|n|r|
		|p|p|p|p|p|p|p|p|
		| | | | | | | | |
		| | | | | | | | |
		| | | | |P| | | |
		| | | | | | | | |
		|P|P|P|P| |P|P|P|
		|R|N|B|Q|K|B|N|R|
         */

        game.makeMove(getNewMove(getNewPosition(7,5), getNewPosition(5,5), null));
        /*
        |r|n|b|q|k|b|n|r|
		|p|p|p|p| |p|p|p|
		| | | | | | | | |
		| | | | |p| | | |
		| | | | |P| | | |
		| | | | | | | | |
		|P|P|P|P| |P|P|P|
		|R|N|B|Q|K|B|N|R|
         */

        game.makeMove(getNewMove(getNewPosition(1,6), getNewPosition(4,3), null));
        /*
        |r|n|b|q|k|b|n|r|
		|p|p|p|p| |p|p|p|
		| | | | | | | | |
		| | | | |p| | | |
		| | |B| |P| | | |
		| | | | | | | | |
		|P|P|P|P| |P|P|P|
		|R|N|B|Q|K| |N|R|
         */

        game.makeMove(getNewMove(getNewPosition(8,7), getNewPosition(6,6), null));
        /*
        |r|n|b|q|k|b| |r|
		|p|p|p|p| |p|p|p|
		| | | | | |n| | |
		| | | | |p| | | |
		| | |B| |P| | | |
		| | | | | | | | |
		|P|P|P|P| |P|P|P|
		|R|N|B|Q|K| |N|R|
         */

        game.makeMove(getNewMove(getNewPosition(1,4), getNewPosition(5,8), null));
        /*
        |r|n|b|q|k|b| |r|
		|p|p|p|p| |p|p|p|
		| | | | | |n| | |
		| | | | |p| | |Q|
		| | |B| |P| | | |
		| | | | | | | | |
		|P|P|P|P| |P|P|P|
		|R|N|B| |K| |N|R|
         */

        game.makeMove(getNewMove(getNewPosition(8,2), getNewPosition(6,3), null));
        /*
        |r| |b|q|k|b| |r|
		|p|p|p|p| |p|p|p|
		| | |n| | |n| | |
		| | | | |p| | |Q|
		| | |B| |P| | | |
		| | | | | | | | |
		|P|P|P|P| |P|P|P|
		|R|N|B| |K| |N|R|
         */

        game.makeMove(getNewMove(getNewPosition(5,8), getNewPosition(7,6), null));
        /*
        |r| |b|q|k|b| |r|
		|p|p|p|p| |Q|p|p|
		| | |n| | |n| | |
		| | | | |p| | | |
		| | |B| |P| | | |
		| | | | | | | | |
		|P|P|P|P| |P|P|P|
		|R|N|B| |K| |N|R|
         */

        assertTrue(game.isInCheckmate(ChessGame.TeamColor.BLACK));
    }

    @Test
    public void invalidMoves() throws InvalidMoveException {
        game.setTeamTurn(WHITE);
        /*
        |r|n|b|q|k|b|n|r|
		|p|p|p|p|p|p|p|p|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P|P|P|P|P|P|P|P|
		|R|N|B|Q|K|B|N|R|
         */

        //move further than can go
        assertThrows(InvalidMoveException.class, ()->game.makeMove(getNewMove(
                getNewPosition(2, 1), getNewPosition(5, 1), null)));
        game.makeMove(getNewMove(getNewPosition(2,1), getNewPosition(3,1), null));
        /*
        |r|n|b|q|k|b|n|r|
		|p|p|p|p|p|p|p|p|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P| | | | | | | |
		| |P|P|P|P|P|P|P|
		|R|N|B|Q|K|B|N|R|
         */

        //pawn diagonal when no capture
        assertThrows(InvalidMoveException.class, ()->game.makeMove(getNewMove(
                getNewPosition(7,2), getNewPosition(6,3), null)));
        game.makeMove(getNewMove(getNewPosition(7,2), getNewPosition(6,2), null));
        /*
        |r|n|b|q|k|b|n|r|
		|p| |p|p|p|p|p|p|
		| |p| | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P| | | | | | | |
		| |P|P|P|P|P|P|P|
		|R|N|B|Q|K|B|N|R|
         */

        //make move out of turn
        assertThrows(InvalidMoveException.class, ()->game.makeMove(
                getNewMove(getNewPosition(6,2), getNewPosition(5,2), null)));

        //pawn in way
        assertThrows(InvalidMoveException.class, ()->game.makeMove(getNewMove(
                getNewPosition(1,1), getNewPosition(4,1), null)));
        game.makeMove(getNewMove(getNewPosition(1,1), getNewPosition(2,1), null));
        /*
        |r|n|b|q|k|b|n|r|
		|p| |p|p|p|p|p|p|
		| |p| | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P| | | | | | | |
		|R|P|P|P|P|P|P|P|
		| |N|B|Q|K|B|N|R|
         */

        //not a move the piece can ever take
        assertThrows(InvalidMoveException.class, ()->game.makeMove(getNewMove(
                getNewPosition(8,7), getNewPosition(5,5), null)));
        game.makeMove(getNewMove(getNewPosition(8,7), getNewPosition(6,6), null));
        /*
        |r|n|b|q|k|b| |r|
		|p| |p|p|p|p|p|p|
		| |p| | | |n| | |
		| | | | | | | | |
		| | | | | | | | |
		|P| | | | | | | |
		|R|P|P|P|P|P|P|P|
		| |N|B|Q|K|B|N|R|
         */

        //team at destination
        assertThrows(InvalidMoveException.class, ()->game.makeMove(getNewMove(
                getNewPosition(1, 6), getNewPosition(2, 5), null)));
        game.makeMove(getNewMove(getNewPosition(2, 5), getNewPosition(4, 5), null));
        /*
        |r|n|b|q|k|b| |r|
		|p| |p|p|p|p|p|p|
		| |p| | | |n| | |
		| | | | | | | | |
		| | | | |P| | | |
		|P| | | | | | | |
		|R|P|P|P| |P|P|P|
		| |N|B|Q|K|B|N|R|
         */

        //team blocking path
        assertThrows(InvalidMoveException.class, ()->game.makeMove(getNewMove(
                getNewPosition(8,4), getNewPosition(6,4), null)));
        game.makeMove(getNewMove(getNewPosition(6,6), getNewPosition(4,5), null));
        /*
        |r|n|b|q|k|b| |r|
		|p| |p|p|p|p|p|p|
		| |p| | | | | | |
		| | | | | | | | |
		| | | | |n| | | |
		|P| | | | | | | |
		|R|P|P|P| |P|P|P|
		| |N|B|Q|K|B|N|R|
         */

        //try moving captured piece
        assertThrows(InvalidMoveException.class, ()->game.makeMove(
                getNewMove(getNewPosition(4,5), getNewPosition(5,5), null)));
        game.makeMove(getNewMove(getNewPosition(1,6), getNewPosition(3,4), null));
        game.makeMove(getNewMove(getNewPosition(8,3), getNewPosition(6,1), null));
        /*
        |r|n| |q|k|b| |r|
		|p| |p|p|p|p|p|p|
		|b|p| | | | | | |
		| | | | | | | | |
		| | | | |n| | | |
		|P| | |B| | | | |
		|R|P|P|P| |P|P|P|
		| |N|B|Q|K| |N|R|
         */

        //try moving through enemy piece
        assertThrows(InvalidMoveException.class, ()->game.makeMove(getNewMove(
                getNewPosition(3,4), getNewPosition(5,6), null)));
        game.makeMove(getNewMove(getNewPosition(1,7), getNewPosition(3,6), null));
        game.makeMove(getNewMove(getNewPosition(4, 5), getNewPosition(2,4), null));
        assertFalse(game.isInCheck(ChessGame.TeamColor.WHITE));
        /*
        |r|n| |q|k|b| |r|
		|p| |p|p|p|p|p|p|
		|b|p| | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P| | |B| |N| | |
		|R|P|P|n| |P|P|P|
		| |N|B|Q|K| | |R|
         */

        game.makeMove(getNewMove(getNewPosition(1,8), getNewPosition(1,7), null));
        game.makeMove(getNewMove(getNewPosition(2,4), getNewPosition(3,6), null));
        assertTrue(game.isInCheck(ChessGame.TeamColor.WHITE));
        /*
        |r|n| |q|k|b| |r|
		|p| |p|p|p|p|p|p|
		|b|p| | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P| | |B| |n| | |
		|R|P|P| | |P|P|P|
		| |N|B|Q|K| |R| |
         */

        //try not getting out of check
        assertThrows(InvalidMoveException.class, ()-> game.makeMove(
                getNewMove(getNewPosition(1,7), getNewPosition(1,8), null)));
        game.makeMove(getNewMove(getNewPosition(2,7), getNewPosition(3,6), null));
        assertFalse(game.isInCheck(ChessGame.TeamColor.WHITE));
        /*
        |r|n| |q|k|b| |r|
		|p| |p|p|p|p|p|p|
		|b|p| | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P| | |B| |P| | |
		|R|P|P| | |P| |P|
		| |N|B|Q|K| |R| |
         */

        //try double-moving moved pawn
        assertThrows(InvalidMoveException.class, ()->game.makeMove(getNewMove(
                getNewPosition(6,2), getNewPosition(4,2), null)));

        //few more moves to try
        game.makeMove(getNewMove(getNewPosition(6,2), getNewPosition(5,2), null));
        game.makeMove(getNewMove(getNewPosition(1,7), getNewPosition(1,8), null));
        game.makeMove(getNewMove(getNewPosition(5,2), getNewPosition(4,2), null));
        /*
        |r|n| |q|k|b| |r|
		|p| |p|p|p|p|p|p|
		|b| | | | | | | |
		| | | | | | | | |
		| |p| | | | | | |
		|P| | |B| |P| | |
		|R|P|P| | |P| |P|
		| |N|B|Q|K| | |R|
         */
    }
}

