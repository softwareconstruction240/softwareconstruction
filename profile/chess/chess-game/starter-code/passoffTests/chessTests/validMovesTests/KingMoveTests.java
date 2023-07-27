package passoffTests.chessTests.validMovesTests;

import chess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static passoffTests.TestFactory.*;
import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;
import static org.junit.jupiter.api.Assertions.*;

public class KingMoveTests {
    ChessBoard board;
    ChessGame game;
    ChessPosition position;
    ChessPiece king;
    Set<ChessMove> moves;

    @BeforeEach
    public void setup(){
        board = getNewBoard();
        game = getNewGame();
        moves = new HashSet<>();
    }

    @Test
    public void emptyBoard(){

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

        king = getNewPiece(WHITE, KING);
        position = getNewPosition(3,6);
        board.addPiece(position, king);

        //all 8 directions
        moves.add(getNewMove(position, getNewPosition(3,7), null));
        moves.add(getNewMove(position, getNewPosition(4,7), null));
        moves.add(getNewMove(position, getNewPosition(4,6), null));
        moves.add(getNewMove(position, getNewPosition(4,5), null));
        moves.add(getNewMove(position, getNewPosition(3,5), null));
        moves.add(getNewMove(position, getNewPosition(2,5), null));
        moves.add(getNewMove(position, getNewPosition(2,6), null));
        moves.add(getNewMove(position, getNewPosition(2,7), null));

        game.setBoard(board);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(position));
        assertEquals(moves, gameMoves);
    }

    @Test
    public void piecesInWay(){

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P| | | | | | | |
		| | | |P|n| | | |
		| | | |k| |q| | |
		| | |P|b|p| | | |
		| | | | | | | | |
         */

        king = getNewPiece(BLACK, KING);
        position = getNewPosition(3,4);
        board.addPiece(position, king);

        //enemy pieces
        board.addPiece(getNewPosition(2,3), getNewPiece(WHITE, PAWN));
        board.addPiece(getNewPosition(4,4), getNewPiece(WHITE, PAWN));

        //friendly pieces
        board.addPiece(getNewPosition(4,5), getNewPiece(BLACK, KNIGHT));
        board.addPiece(getNewPosition(2,4), getNewPiece(BLACK, BISHOP));
        board.addPiece(getNewPosition(2,5), getNewPiece(BLACK, PAWN));

        //extra decoy pieces
        board.addPiece(getNewPosition(3,6), getNewPiece(BLACK, QUEEN));
        board.addPiece(getNewPosition(5,1), getNewPiece(WHITE, PAWN));

        //normal moves
        moves.add(getNewMove(position, getNewPosition(3,3), null));
        moves.add(getNewMove(position, getNewPosition(4,3), null));
        moves.add(getNewMove(position, getNewPosition(3,5), null));

        //capture moves
        moves.add(getNewMove(position, getNewPosition(2,3), null));
        moves.add(getNewMove(position, getNewPosition(4,4), null));

        game.setBoard(board);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(position));
        assertEquals(moves, gameMoves);
    }

    @Test
    public void noPutSelfInDanger(){

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | |k| | |
		| | | | | | | | |
		| | | | | |K| | |
		| | | | | | | | |
         */

        king = getNewPiece(WHITE, KING);
        position = getNewPosition(2,6);
        board.addPiece(position, king);

        board.addPiece(getNewPosition(4,6), getNewPiece(BLACK, KING));


        //can't move towards enemy king
        moves.add(getNewMove(position, getNewPosition(1,5), null));
        moves.add(getNewMove(position, getNewPosition(1,6), null));
        moves.add(getNewMove(position, getNewPosition(1,7), null));
        moves.add(getNewMove(position, getNewPosition(2,5), null));
        moves.add(getNewMove(position, getNewPosition(2,7), null));

        game.setBoard(board);

        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(position));
        assertEquals(moves, gameMoves);
    }

    @Test
    public void boardCorner(){

        /*
        | | | | | | | |k|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        king = getNewPiece(BLACK, KING);
        position = getNewPosition(8,8);
        board.addPiece(position, king);

        //can only move away from corner
        moves.add(getNewMove(position, getNewPosition(8,7), null));
        moves.add(getNewMove(position, getNewPosition(7,7), null));
        moves.add(getNewMove(position, getNewPosition(7,8), null));

        game.setBoard(board);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(position));
        assertEquals(moves, gameMoves);
    }

}
