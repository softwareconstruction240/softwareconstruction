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

public class KnightMoveTests {
    private ChessBoard board;
    private ChessGame game;
    private final ChessPosition midBoard = getNewPosition(5,5);
    private Set<ChessMove> baseMoves;

    @BeforeEach
    public void setup(){
        board = getNewBoard();
        game = getNewGame();

        baseMoves = new HashSet<>();
        baseMoves.add(getNewMove(midBoard, getNewPosition(3,4), null));
        baseMoves.add(getNewMove(midBoard, getNewPosition(3,6), null));
        baseMoves.add(getNewMove(midBoard, getNewPosition(4,7), null));
        baseMoves.add(getNewMove(midBoard, getNewPosition(6,7), null));
        baseMoves.add(getNewMove(midBoard, getNewPosition(7,6), null));
        baseMoves.add(getNewMove(midBoard, getNewPosition(7,4), null));
        baseMoves.add(getNewMove(midBoard, getNewPosition(6,3), null));
        baseMoves.add(getNewMove(midBoard, getNewPosition(4,3), null));
    }

    @Test
    public void emptyBoard(){
        //white
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | |N| | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */
        ChessPiece knight = getNewPiece(WHITE, KNIGHT);
        board.addPiece(midBoard, knight);
        game.setBoard(board);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(midBoard));
        assertEquals(baseMoves, gameMoves);

        //black
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | |n| | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */
        knight = getNewPiece(BLACK, KNIGHT);
        board.addPiece(midBoard, knight);
        game.setBoard(board);
        gameMoves = new HashSet<>(game.validMoves(midBoard));
        assertEquals(baseMoves, gameMoves);
    }

    @Test
    public void boardEdge(){
        //left
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|n| | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */
        ChessPosition edge = getNewPosition(4,1);
        ChessPiece knight = getNewPiece(BLACK, KNIGHT);
        board.addPiece(edge, knight);
        Set<ChessMove> moves = new HashSet<>();

        //add in moves
        moves.add(getNewMove(edge, getNewPosition(2,2), null));
        moves.add(getNewMove(edge, getNewPosition(6,2), null));
        moves.add(getNewMove(edge, getNewPosition(3,3), null));
        moves.add(getNewMove(edge, getNewPosition(5,3), null));

        game.setBoard(board);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(edge));
        assertEquals(moves, gameMoves);


        //right
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | |n|
		| | | | | | | | |
		| | | | | | | | |
         */
        edge = getNewPosition(3,8);
        knight = getNewPiece(BLACK, KNIGHT);
        board = getNewBoard();
        board.addPiece(edge, knight);
        moves = new HashSet<>();

        //add in moves
        moves.add(getNewMove(edge, getNewPosition(1,7), null));
        moves.add(getNewMove(edge, getNewPosition(5,7), null));
        moves.add(getNewMove(edge, getNewPosition(2,6), null));
        moves.add(getNewMove(edge, getNewPosition(4,6), null));

        game.setBoard(board);
        gameMoves = new HashSet<>(game.validMoves(edge));
        assertEquals(moves, gameMoves);


        //bottom
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | |N| | |
         */
        edge = getNewPosition(1,6);
        knight = getNewPiece(WHITE, KNIGHT);
        board = getNewBoard();
        board.addPiece(edge, knight);
        moves = new HashSet<>();

        //add in moves
        moves.add(getNewMove(edge, getNewPosition(2,8), null));
        moves.add(getNewMove(edge, getNewPosition(2,4), null));
        moves.add(getNewMove(edge, getNewPosition(3,5), null));
        moves.add(getNewMove(edge, getNewPosition(3,7), null));

        game.setBoard(board);
        gameMoves = new HashSet<>(game.validMoves(edge));
        assertEquals(moves, gameMoves);


        //top
        /*
        | | |N| | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */
        edge = getNewPosition(8,3);
        knight = getNewPiece(WHITE, KNIGHT);
        board = getNewBoard();
        board.addPiece(edge, knight);
        moves = new HashSet<>();

        //add in moves
        moves.add(getNewMove(edge, getNewPosition(7,1), null));
        moves.add(getNewMove(edge, getNewPosition(7,5), null));
        moves.add(getNewMove(edge, getNewPosition(6,4), null));
        moves.add(getNewMove(edge, getNewPosition(6,2), null));

        game.setBoard(board);
        gameMoves = new HashSet<>(game.validMoves(edge));
        assertEquals(moves, gameMoves);
    }

    @Test
    public void boardCorner(){
        //bottom right
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | |N|
         */
        ChessPosition edge = getNewPosition(1,8);
        ChessPiece knight = getNewPiece(WHITE, KNIGHT);
        board.addPiece(edge, knight);
        Set<ChessMove> moves = new HashSet<>();

        //add in moves
        moves.add(getNewMove(edge, getNewPosition(2,6), null));
        moves.add(getNewMove(edge, getNewPosition(3,7), null));

        game.setBoard(board);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(edge));
        assertEquals(moves, gameMoves);


        //top right
        /*
        | | | | | | | |N|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */
        edge = getNewPosition(8,8);
        knight = getNewPiece(WHITE, KNIGHT);
        board = getNewBoard();
        board.addPiece(edge, knight);
        moves = new HashSet<>();

        //add in moves
        moves.add(getNewMove(edge, getNewPosition(7,6), null));
        moves.add(getNewMove(edge, getNewPosition(6,7), null));

        game.setBoard(board);
        gameMoves = new HashSet<>(game.validMoves(edge));
        assertEquals(moves, gameMoves);


        //top left
        /*
        |n| | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */
        edge = getNewPosition(8,1);
        knight = getNewPiece(BLACK, KNIGHT);
        board = getNewBoard();
        board.addPiece(edge, knight);
        moves = new HashSet<>();

        //add in moves
        moves.add(getNewMove(edge, getNewPosition(7,3), null));
        moves.add(getNewMove(edge, getNewPosition(6,2), null));

        game.setBoard(board);
        gameMoves = new HashSet<>(game.validMoves(edge));
        assertEquals(moves, gameMoves);


        //bottom left
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|n| | | | | | | |
         */
        edge = getNewPosition(1,1);
        knight = getNewPiece(BLACK, KNIGHT);
        board = getNewBoard();
        board.addPiece(edge, knight);
        moves = new HashSet<>();

        //add in moves
        moves.add(getNewMove(edge, getNewPosition(2,3), null));
        moves.add(getNewMove(edge, getNewPosition(3,2), null));

        game.setBoard(board);
        gameMoves = new HashSet<>(game.validMoves(edge));
        assertEquals(moves, gameMoves);
    }

    @Test
    public void friendlyPieces(){

        /*
        | | | | | | | | |
		| | | |R| | | | |
		| | | | | | |K| |
		| | | | |N| | | |
		| | |N| | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        ChessPiece knight = getNewPiece(WHITE, KNIGHT);

        board.addPiece(midBoard, knight);

        //add pieces
        board.addPiece(getNewPosition(6,7), getNewPiece(WHITE, KING));
        board.addPiece(getNewPosition(7,4), getNewPiece(WHITE, ROOK));
        board.addPiece(getNewPosition(4,3), getNewPiece(WHITE, KNIGHT));

        //can't move to where friendlies are moves
        baseMoves.remove(getNewMove(midBoard, getNewPosition(6,7), null));
        baseMoves.remove(getNewMove(midBoard, getNewPosition(7,4), null));
        baseMoves.remove(getNewMove(midBoard, getNewPosition(4,3), null));

        game.setBoard(board);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(midBoard));
        assertEquals(baseMoves, gameMoves);
    }

    @Test
    public void enemyPieces(){

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | |n| | | |
		| | |N| | | | | |
		| | | |K| |R| | |
		| | | | | | | | |
		| | | | | | | | |
         */

        ChessPiece knight = getNewPiece(BLACK, KNIGHT);

        board.addPiece(midBoard, knight);

        //add pieces
        board.addPiece(getNewPosition(3,4), getNewPiece(WHITE, KING));
        board.addPiece(getNewPosition(3,6), getNewPiece(WHITE, ROOK));
        board.addPiece(getNewPosition(4,3), getNewPiece(WHITE, KNIGHT));


        //make sure enemy pieces didn't stop any moves
        game.setBoard(board);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(midBoard));
        assertEquals(baseMoves, gameMoves);
    }

    @Test
    public void mixedPieces(){

        /*
        | | | | | | | | |
		| | | |n| |N| | |
		| | |K| | | | | |
		| | | | |N|r| | |
		| | |R| | | |q|B|
		| | | | | | | | |
		|k| | | | | | | |
		| | | | | | | | |
         */

        ChessPiece knight = getNewPiece(WHITE, KNIGHT);
        board.addPiece(midBoard, knight);

        //add in friendlies
        board.addPiece(getNewPosition(6,3), getNewPiece(WHITE, KING));
        board.addPiece(getNewPosition(4,3), getNewPiece(WHITE, ROOK));
        board.addPiece(getNewPosition(7,6), getNewPiece(WHITE, KNIGHT));

        //remove friendly spaces
        baseMoves.remove(getNewMove(midBoard, getNewPosition(4,3), null));
        baseMoves.remove(getNewMove(midBoard, getNewPosition(6,3), null));
        baseMoves.remove(getNewMove(midBoard, getNewPosition(7,6), null));

        //add in enemy
        board.addPiece(getNewPosition(4,7), getNewPiece(BLACK, QUEEN));
        board.addPiece(getNewPosition(7,4), getNewPiece(BLACK, KNIGHT));

        //extra filler pieces
        board.addPiece(getNewPosition(2,1), getNewPiece(BLACK, KING));
        board.addPiece(getNewPosition(4,8), getNewPiece(WHITE, BISHOP));
        board.addPiece(getNewPosition(5,6), getNewPiece(BLACK, ROOK));

        game.setBoard(board);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(midBoard));
        assertEquals(baseMoves, gameMoves);
    }
}
