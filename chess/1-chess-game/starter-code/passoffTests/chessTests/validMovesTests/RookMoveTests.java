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

public class RookMoveTests {
    private ChessBoard board;
    private ChessGame game;
    private ChessPiece rook;
    private ChessPosition rookPosition;
    private Set<ChessMove> moves;

    @BeforeEach
    public void setup(){
        board = getNewBoard();
        game = getNewGame();
        moves = new HashSet<>();
    }

    @Test
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

        rook = getNewPiece(WHITE, ROOK);
        rookPosition = getNewPosition(2,3);
        board.addPiece(rookPosition, rook);

        //up
        moves.add(getNewMove(rookPosition, getNewPosition(2,4), null));
        moves.add(getNewMove(rookPosition, getNewPosition(2,5), null));
        moves.add(getNewMove(rookPosition, getNewPosition(2,6), null));
        moves.add(getNewMove(rookPosition, getNewPosition(2,7), null));
        moves.add(getNewMove(rookPosition, getNewPosition(2,8), null));

        //down
        moves.add(getNewMove(rookPosition, getNewPosition(2,2), null));
        moves.add(getNewMove(rookPosition, getNewPosition(2,1), null));

        //left
        moves.add(getNewMove(rookPosition, getNewPosition(1,3), null));

        //right
        moves.add(getNewMove(rookPosition, getNewPosition(3,3), null));
        moves.add(getNewMove(rookPosition, getNewPosition(4,3), null));
        moves.add(getNewMove(rookPosition, getNewPosition(5,3), null));
        moves.add(getNewMove(rookPosition, getNewPosition(6,3), null));
        moves.add(getNewMove(rookPosition, getNewPosition(7,3), null));
        moves.add(getNewMove(rookPosition, getNewPosition(8,3), null));

        //check
        game.setBoard(board);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(rookPosition));
        assertEquals(moves, gameMoves);
    }

    @Test
    public void piecesInWay(){

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|n|Q| | | | | | |
		|r| | | | |B| | |
		| | | | | | | | |
		|q| |k| | | | | |
		| | | | | | | | |
         */

        rook = getNewPiece(BLACK, ROOK);
        rookPosition = getNewPosition(4,1);
        board.addPiece(rookPosition, rook);

        //pieces in way
        board.addPiece(getNewPosition(2,1), getNewPiece(BLACK, QUEEN));
        board.addPiece(getNewPosition(4,6), getNewPiece(WHITE, BISHOP));
        board.addPiece(getNewPosition(5,1), getNewPiece(BLACK, KNIGHT));

        //extra decoy pieces
        board.addPiece(getNewPosition(5,2), getNewPiece(WHITE, QUEEN));
        board.addPiece(getNewPosition(2,3), getNewPiece(BLACK, KING));

        //left
        moves.add(getNewMove(rookPosition, getNewPosition(3,1), null));

        //up
        moves.add(getNewMove(rookPosition, getNewPosition(4,2), null));
        moves.add(getNewMove(rookPosition, getNewPosition(4,3), null));
        moves.add(getNewMove(rookPosition, getNewPosition(4,4), null));
        moves.add(getNewMove(rookPosition, getNewPosition(4,5), null));
        moves.add(getNewMove(rookPosition, getNewPosition(4,6), null));


        //check
        game.setBoard(board);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(rookPosition));
        assertEquals(moves, gameMoves);
    }

    @Test
    public void xrayCheck(){

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| |r| | | |R| |K|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        rook = getNewPiece(WHITE, ROOK);
        rookPosition = getNewPosition(5,6);
        board.addPiece(rookPosition, rook);

        //Enemy Rook causing -xray check (white rook can't move out of line)
        board.addPiece(getNewPosition(5,2), getNewPiece(BLACK, ROOK));
        board.addPiece( getNewPosition(5,8), getNewPiece(WHITE, KING));

        moves.add(getNewMove(rookPosition, getNewPosition(5,7), null));
        moves.add(getNewMove(rookPosition, getNewPosition(5,5), null));
        moves.add(getNewMove(rookPosition, getNewPosition(5,4), null));
        moves.add(getNewMove(rookPosition, getNewPosition(5,3), null));
        moves.add(getNewMove(rookPosition, getNewPosition(5,2), null));


        //check
        game.setBoard(board);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(rookPosition));
        assertEquals(moves, gameMoves);
    }
}
