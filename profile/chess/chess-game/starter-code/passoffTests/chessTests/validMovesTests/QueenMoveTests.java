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

public class QueenMoveTests {
    private ChessPiece queen;
    private ChessBoard board;
    private ChessGame game;
    private Set<ChessMove> moves;
    private ChessPosition position;

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
		| | | | | | |q| |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        queen = getNewPiece(BLACK, QUEEN);
        position = getNewPosition(7,7);
        board.addPiece(position, queen);

        //down
        moves.add(getNewMove(position, getNewPosition(7,6), null));
        moves.add(getNewMove(position, getNewPosition(7,5), null));
        moves.add(getNewMove(position, getNewPosition(7,4), null));
        moves.add(getNewMove(position, getNewPosition(7,3), null));
        moves.add(getNewMove(position, getNewPosition(7,2), null));
        moves.add(getNewMove(position, getNewPosition(7,1), null));

        //down right
        moves.add(getNewMove(position, getNewPosition(8,6), null));

        //right
        moves.add(getNewMove(position, getNewPosition(8,7), null));

        //up right
        moves.add(getNewMove(position, getNewPosition(8,8), null));

        //up
        moves.add(getNewMove(position, getNewPosition(7,8), null));

        //up left
        moves.add(getNewMove(position, getNewPosition(6,8), null));

        ///left
        moves.add(getNewMove(position, getNewPosition(6,7), null));
        moves.add(getNewMove(position, getNewPosition(5,7), null));
        moves.add(getNewMove(position, getNewPosition(4,7), null));
        moves.add(getNewMove(position, getNewPosition(3,7), null));
        moves.add(getNewMove(position, getNewPosition(2,7), null));
        moves.add(getNewMove(position, getNewPosition(1,7), null));

        //down left
        moves.add(getNewMove(position, getNewPosition(6,6), null));
        moves.add(getNewMove(position, getNewPosition(5,5), null));
        moves.add(getNewMove(position, getNewPosition(4,4), null));
        moves.add(getNewMove(position, getNewPosition(3,3), null));
        moves.add(getNewMove(position, getNewPosition(2,2), null));
        moves.add(getNewMove(position, getNewPosition(1,1), null));

        //check
        game.setBoard(board);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(position));
        assertEquals(moves, gameMoves);
    }

    @Test
    public void piecesInWay(){

        /*
        |b| | | | | | | |
		| | | | | | | | |
		| | |R| | | | | |
		| | |N| | | | | |
		|Q| | |p| | | | |
		| | | | | |r| | |
		|K| | | | | | | |
		| | | | | | | | |
         */

        queen = getNewPiece(WHITE, QUEEN);
        position = getNewPosition(4,1);
        board.addPiece(position, queen);

        //pieces in way
        board.addPiece(getNewPosition(2,1), getNewPiece(WHITE, KING));
        board.addPiece(getNewPosition(4,4), getNewPiece(BLACK, PAWN));
        board.addPiece(getNewPosition(6,3), getNewPiece(WHITE, ROOK));
        board.addPiece(getNewPosition(8,1), getNewPiece(BLACK, BISHOP));

        //extra decoy pieces
        board.addPiece(getNewPosition(3,6), getNewPiece(BLACK, ROOK));
        board.addPiece(getNewPosition(5,3), getNewPiece(WHITE, KNIGHT));

        //left
        moves.add(getNewMove(position, getNewPosition(3,1), null));

        //up left
        moves.add(getNewMove(position, getNewPosition(3,2), null));
        moves.add(getNewMove(position, getNewPosition(2,3), null));
        moves.add(getNewMove(position, getNewPosition(1,4), null));

        //up
        moves.add(getNewMove(position, getNewPosition(4,2), null));
        moves.add(getNewMove(position, getNewPosition(4,3), null));
        moves.add(getNewMove(position, getNewPosition(4,4), null));

        //up right
        moves.add(getNewMove(position, getNewPosition(5,2), null));

        //right
        moves.add(getNewMove(position, getNewPosition(5,1), null));
        moves.add(getNewMove(position, getNewPosition(6,1), null));
        moves.add(getNewMove(position, getNewPosition(7,1), null));
        moves.add(getNewMove(position, getNewPosition(8,1), null));

        //check
        game.setBoard(board);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(position));
        assertEquals(moves, gameMoves);
    }

    @Test
    public void forcedMove(){

        /*
        | | |q| | | | | |
		| | | | | | | | |
		| |k| | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | |Q| | |
		| | | | | | | | |
         */

        queen = getNewPiece(BLACK, QUEEN);
        position = getNewPosition(8,3);
        board.addPiece(position, queen);

        //queen threatening team king
        board.addPiece(getNewPosition(2,6), getNewPiece(WHITE, QUEEN));
        board.addPiece(getNewPosition(6,2), getNewPiece(BLACK, KING));

        //can only defend king
        moves.add(getNewMove(position, getNewPosition(5,3), null));

        //check
        game.setBoard(board);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(position));
        assertEquals(moves, gameMoves);
    }
}
