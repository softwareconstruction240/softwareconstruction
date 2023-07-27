package passoffTests.chessTests.validMovesTests;

import chess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static passoffTests.TestFactory.*;
import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;
import static org.junit.jupiter.api.Assertions.*;

public class PawnMoveTests {
    private ChessBoard chessBoard;
    private ChessGame chessGame;

    private final Set<ChessMove> validMovesFromD4White = new HashSet<>();
    private final Set<ChessMove> validMovesFromD4Black = new HashSet<>();
    private final ChessPosition d4Position = getNewPosition(4, 4);

    @BeforeEach
    public void setup(){
        //empty board
        chessBoard = getNewBoard();
        chessGame = getNewGame();


        //generic starting spot. Can modify from here as needed
        validMovesFromD4White.add(getNewMove(d4Position, getNewPosition(5,4), null));

        validMovesFromD4Black.add(getNewMove(d4Position, getNewPosition(3,4), null));
    }

    @AfterEach
    public void tearDown(){
        validMovesFromD4White.clear();
        validMovesFromD4Black.clear();
    }

    @Test
    public void emptyBoardWhite(){

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |P| | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        //test if all correct positions show up with no obstacles
        ChessPiece pawn = getNewPiece(WHITE, PAWN);
        chessBoard.addPiece(d4Position, pawn);

        Set<ChessMove> pawnMoves = new HashSet<>(pawn.pieceMoves(chessBoard, d4Position));
        assertEquals(validMovesFromD4White, pawnMoves);
    }

    @Test
    public void emptyBoardBlack(){

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |p| | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        ChessPiece pawn = getNewPiece(BLACK, PAWN);
        chessBoard.addPiece(d4Position, pawn);

        Set<ChessMove> pawnMoves = new HashSet<>(pawn.pieceMoves(chessBoard, d4Position));
        assertEquals(validMovesFromD4Black, pawnMoves);
    }

    //A pawn's first move (from the starting position) they can move 2 spaces instead of just 1
    @Test
    public void doubleMoveWhite(){

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | |P| | | |
		| | | | | | | | |
         */

        ChessPiece pawn = getNewPiece(WHITE, PAWN);
        ChessPosition startPosition = getNewPosition(2,5);

        chessBoard.addPiece(startPosition, pawn);
        ChessMove doubleMove = getNewMove(startPosition, getNewPosition(4,5), null);
        chessGame.setBoard(chessBoard);

        assertTrue(chessGame.validMoves(startPosition).contains(doubleMove));
    }

    @Test
    public void doubleMoveBlack(){

        /*
        | | | | | | | | |
		| | |p| | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        ChessPiece pawn = getNewPiece(BLACK, PAWN);
        ChessPosition startPosition = getNewPosition(7,3);

        chessBoard.addPiece(startPosition, pawn);
        ChessMove doubleMove = getNewMove(startPosition, getNewPosition(5,3), null);
        chessGame.setBoard(chessBoard);

        assertTrue(chessGame.validMoves(startPosition).contains(doubleMove));
    }

    @Test
    public void edgePromotionWhite(){

        /*
        | | | | | | | | |
		| | |P| | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        Set<ChessMove> validMoves = new HashSet<>();
        ChessPosition start = getNewPosition(7,3);
        ChessPosition end = getNewPosition(8,3);

        //add all promotions
        validMoves.add(getNewMove(start, end, QUEEN));
        validMoves.add(getNewMove(start, end, BISHOP));
        validMoves.add(getNewMove(start, end, ROOK));
        validMoves.add(getNewMove(start, end, KNIGHT));

        //check
        ChessPiece pawn = getNewPiece(WHITE, PAWN);
        chessBoard.addPiece(start, pawn);
        chessGame.setBoard(chessBoard);
        Set<ChessMove> gameMoves = new HashSet<>(chessGame.validMoves(start));
        assertEquals(validMoves, gameMoves);
    }

    @Test
    public void edgePromotionBlack(){

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | |p| | | | | |
		| | | | | | | | |
         */

        Set<ChessMove> validMoves = new HashSet<>();
        ChessPosition start = getNewPosition(2, 3);
        ChessPosition end = getNewPosition(1,3);

        //add all promotions
        validMoves.add(getNewMove(start, end, QUEEN));
        validMoves.add(getNewMove(start, end, BISHOP));
        validMoves.add(getNewMove(start, end, ROOK));
        validMoves.add(getNewMove(start, end, KNIGHT));

        //check
        ChessPiece pawn = getNewPiece(BLACK, PAWN);
        chessBoard.addPiece(start, pawn);
        chessGame.setBoard(chessBoard);
        Set<ChessMove> gameMoves = new HashSet<>(chessGame.validMoves(start));
        assertEquals(validMoves, gameMoves);
    }

    @Test
    public void piecesInWayWhite(){

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |k| | | | |
		| | | |P| | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        ChessPiece pawn = getNewPiece(WHITE, PAWN);
        chessBoard.addPiece(d4Position, pawn);

        //other team obstacle
        chessBoard.addPiece(getNewPosition(5, 4), getNewPiece(BLACK, KING));
        chessGame.setBoard(chessBoard);
        assertTrue(chessGame.validMoves(d4Position).isEmpty());
    }

    @Test
    public void piecesInWayBlack(){

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |p| | | | |
		| | | |k| | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        ChessPiece pawn = getNewPiece(BLACK, PAWN);

        //same team obstacle
        chessBoard.addPiece(d4Position, pawn);
        chessBoard.addPiece(getNewPosition(3,4), getNewPiece(BLACK, KING));
        chessGame.setBoard(chessBoard);
        assertTrue(chessGame.validMoves(d4Position).isEmpty());

    }

    @Test
    public void doubleMoveBlocked(){

        /*
        | | | | | | | | |
		| | |p| | | | | |
		| | |p| | | | | |
		| | | | | | | | |
		| | | | | | |p| |
		| | | | | | | | |
		| | | | | | |P| |
		| | | | | | | | |
         */

        ChessPiece whitePawn = getNewPiece(WHITE, PAWN);
        ChessPosition whitePosition = getNewPosition(2,7);
        chessBoard.addPiece(whitePosition, whitePawn);

        ChessPiece blackPawn   = getNewPiece(BLACK, PAWN);
        ChessPosition blackPosition = getNewPosition(7,3);
        chessBoard.addPiece(blackPosition, blackPawn);

        //white second space blocked
        chessBoard.addPiece(getNewPosition(4,7), blackPawn);

        //black first space blocked
        chessBoard.addPiece(getNewPosition(6,3), blackPawn);

        chessGame.setBoard(chessBoard);

        //test white
        Set<ChessMove> whiteMoves = new HashSet<>();
        whiteMoves.add(getNewMove(whitePosition, getNewPosition(3,7), null));
        Set<ChessMove> gameMoves = new HashSet<>(chessGame.validMoves(whitePosition));
        assertEquals(whiteMoves, gameMoves);

        //test black
        assertTrue(chessGame.validMoves(blackPosition).isEmpty());
    }

    @Test
    public void capturingByWhite(){

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | |r| |N| | | |
		| | | |P| | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        ChessPiece pawn = getNewPiece(WHITE, PAWN);
        chessBoard.addPiece(d4Position, pawn);

        //same team no capture
        chessBoard.addPiece(getNewPosition(5,5), getNewPiece(WHITE, KNIGHT));

        // enemy can capture left
        chessBoard.addPiece(getNewPosition(5,3), getNewPiece(BLACK, ROOK));

        //expected moves
        validMovesFromD4White.add(getNewMove(d4Position, getNewPosition(5,3), null));

        //check
        chessGame.setBoard(chessBoard);
        Set<ChessMove> gameMoves = new HashSet<>(chessGame.validMoves(d4Position));
        assertEquals(validMovesFromD4White, gameMoves);
    }

    @Test
    public void capturingByBlack(){

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |p| | | | |
		| | | |n|R| | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        ChessPiece pawn = getNewPiece(BLACK, PAWN);
        chessBoard.addPiece(d4Position, pawn);

        //piece blocking forward
        chessBoard.addPiece(getNewPosition(3,4), getNewPiece(BLACK, KNIGHT));

        // can capture right
        chessBoard.addPiece(getNewPosition(3,5), getNewPiece(WHITE, ROOK));

        //expected moves
        Set<ChessMove> moves = new HashSet<>();
        moves.add(getNewMove(d4Position, getNewPosition(3,5), null));

        //check
        chessGame.setBoard(chessBoard);
        Set<ChessMove> gameMoves = new HashSet<>(chessGame.validMoves(d4Position));
        assertEquals(moves, gameMoves);
    }
}
