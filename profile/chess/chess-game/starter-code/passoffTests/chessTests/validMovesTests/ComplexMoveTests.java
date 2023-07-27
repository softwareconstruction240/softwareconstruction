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

public class ComplexMoveTests {
    ChessBoard chessBoard;
    ChessGame game;

    @BeforeEach
    public void setup(){
        chessBoard = getNewBoard();
        game = getNewGame();
    }

    @Test
    public void pawnPromotionCapture(){

        /*
        | | | | | | | | |
        | | | | |k| | | |
        | |r| | | | | | |
        | | | | | | | | |
        | | | | | | | | |
        | | |Q| | | | | |
        | |p|K| | | | | |
        |N| | | | |n| | |
         */

        ChessPiece pawn = getNewPiece(BLACK, PAWN);
        ChessPosition edge = getNewPosition(2,2);
        chessBoard.addPiece(edge, pawn);

        chessBoard.addPiece(getNewPosition(2,3), getNewPiece(WHITE, KING));

        //can capture
        chessBoard.addPiece(getNewPosition(1,1), getNewPiece(WHITE, KNIGHT));

        //Blocking king escape
        chessBoard.addPiece(getNewPosition(1,6), getNewPiece(BLACK, KNIGHT));
        chessBoard.addPiece(getNewPosition(6, 2), getNewPiece(BLACK, ROOK));
        chessBoard.addPiece(getNewPosition(3,3), getNewPiece(WHITE, QUEEN));

        //extra pieces
        chessBoard.addPiece(getNewPosition(7,5), getNewPiece(BLACK, KING));

        //valid moves
        Set<ChessMove> validMoves = new HashSet<>();

        //capturing moves
        validMoves.add(getNewMove(edge, getNewPosition(1,1), QUEEN));
        validMoves.add(getNewMove(edge, getNewPosition(1,1), BISHOP));
        validMoves.add(getNewMove(edge, getNewPosition(1,1), ROOK));
        validMoves.add(getNewMove(edge, getNewPosition(1,1), KNIGHT));

        //straight to promotion moves
        validMoves.add(getNewMove(edge, getNewPosition(1,2), KNIGHT));
        validMoves.add(getNewMove(edge, getNewPosition(1,2), ROOK));
        validMoves.add(getNewMove(edge, getNewPosition(1,2), BISHOP));

        //checkmate move
        validMoves.add(getNewMove(edge, getNewPosition(1,2), QUEEN));

        game.setBoard(chessBoard);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(edge));
        assertEquals(validMoves, gameMoves);
    }

    @Test
    public void knightForcedMove(){

        /*
        | | | | | | | | |
        | | | | | | | | |
        | |B| | | | | | |
        | | | | | |K| | |
        | | |n| | | | | |
        | | | | | | | | |
        | | | | | |k| | |
        | | | | | | | | |
         */

        Set<ChessMove> validMoves = new HashSet<>();
        ChessPiece knight = getNewPiece(BLACK, KNIGHT);
        ChessPosition knightPosition = getNewPosition(4,3);
        chessBoard.addPiece(knightPosition, knight);

        //vulnerable team king
        chessBoard.addPiece(getNewPosition(2,6), getNewPiece(BLACK, KING));

        //bishop threatening king
        chessBoard.addPiece(getNewPosition(6,2), getNewPiece(WHITE, BISHOP));
        //enemy king
        chessBoard.addPiece(getNewPosition(5,6), getNewPiece(WHITE, KING));

        validMoves.add(getNewMove(knightPosition, getNewPosition(3,5), null));  //defend king
        validMoves.add(getNewMove(knightPosition, getNewPosition(6,2), null)); //capture bishop

        game.setBoard(chessBoard);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(knightPosition));
        assertEquals(validMoves, gameMoves);
    }

    @Test
    public void bishopStuck(){

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

        ChessPosition position = getNewPosition(1,6);
        ChessPiece bishop = getNewPiece(WHITE, BISHOP);
        chessBoard.addPiece(position, bishop);

        //pieces in way
        chessBoard.addPiece(getNewPosition(2,7), getNewPiece(WHITE, PAWN));
        chessBoard.addPiece(getNewPosition(2,5), getNewPiece(WHITE, ROOK));

        //make sure move list is empty
        game.setBoard(chessBoard);
        assertEquals(0, game.validMoves(position).size());
    }

    @Test
    public void rookPinnedToKing(){

        /*
        | | | | | | | |Q|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |r| | | | |
		| | | | | | | | |
		| |k| | | | | | |
		| | | | | | | | |
         */

        ChessPosition position = getNewPosition(4,4);
        ChessPiece rook = getNewPiece(BLACK, ROOK);

        chessBoard.addPiece(position, rook);

        //queen pinning rook to king
        chessBoard.addPiece(getNewPosition(8,8), getNewPiece(WHITE, QUEEN));
        chessBoard.addPiece(getNewPosition(2,2), getNewPiece(BLACK, KING));

        game.setBoard(chessBoard);
        assertEquals(0, game.validMoves(position).size());
    }

    @Test
    public void queenAlmostCheckmate(){

        /*
        | | |b| |P|P|R| |
		| | | | |P|Q| |r|
		| | | | | |P| | |
		| | | | | | | |P|
		| | | | | | | | |
		| |p| | | | | |k|
		| | | |n| | | | |
		| | | | |B| |P| |
         */

        ChessPiece queen = getNewPiece(WHITE, QUEEN);
        ChessPosition position = getNewPosition(7,6);
        chessBoard.addPiece(position, queen);

        //black pieces
        chessBoard.addPiece(getNewPosition(3,8), getNewPiece(BLACK, KING));
        chessBoard.addPiece(getNewPosition(3,2), getNewPiece(BLACK, PAWN)); //pawn that can be killed for near checkmate
        chessBoard.addPiece(getNewPosition(7,8), getNewPiece(BLACK, ROOK));
        chessBoard.addPiece(getNewPosition(2,4), getNewPiece(BLACK, KNIGHT)); //knight that can capture queen after move
        chessBoard.addPiece(getNewPosition(8,3), getNewPiece(BLACK, BISHOP)); //bishop stopping other checkmate

        //white pieces
        //pieces blocking king escape
        chessBoard.addPiece(getNewPosition(1,5), getNewPiece(WHITE, BISHOP));
        chessBoard.addPiece(getNewPosition(1,7), getNewPiece(WHITE, PAWN));
        chessBoard.addPiece(getNewPosition(8,7), getNewPiece(WHITE, ROOK));

        //pieces limiting queen movement
        chessBoard.addPiece(getNewPosition(5,8), getNewPiece(WHITE, PAWN));
        chessBoard.addPiece(getNewPosition(6,6), getNewPiece(WHITE, PAWN));
        chessBoard.addPiece(getNewPosition(7,5), getNewPiece(WHITE, PAWN));
        chessBoard.addPiece(getNewPosition(8,5), getNewPiece(WHITE, PAWN));
        chessBoard.addPiece(getNewPosition(8,6), getNewPiece(WHITE, PAWN));

        Set<ChessMove> validMoves = new HashSet<>();

        //up left
        validMoves.add(getNewMove(position, getNewPosition(6,7), null));

        //up
        validMoves.add(getNewMove(position, getNewPosition(7,7), null));
        validMoves.add(getNewMove(position, getNewPosition(7,8), null));

        //down left
        validMoves.add(getNewMove(position, getNewPosition(6,5), null));
        validMoves.add(getNewMove(position, getNewPosition(5,4), null));
        validMoves.add(getNewMove(position, getNewPosition(4,3), null));
        validMoves.add(getNewMove(position, getNewPosition(3,2), null));

        game.setBoard(chessBoard);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(position));
        assertEquals(validMoves, gameMoves);
    }

    @Test
    public void kingInDanger(){

        /*
        |R| | | | | | | |
		| | | |k| | | |b|
		| | | | |P| | | |
		| | |Q|n| | | | |
		| | | | | | | | |
		| | | | | | | |r|
		| | | | | |p| | |
		| |q| | | | | | |
         */

        ChessPiece king = getNewPiece(BLACK, KING);
        ChessPosition kingPosition = getNewPosition(7,4);
        chessBoard.addPiece(kingPosition, king);


        //enemy pieces
        //queen blocking movement
        chessBoard.addPiece(getNewPosition(5,3), getNewPiece(WHITE, QUEEN));

        //pawn threatening king
        chessBoard.addPiece(getNewPosition(6,5), getNewPiece(WHITE, PAWN));

        //rook blocking top row
        chessBoard.addPiece(getNewPosition(8,1), getNewPiece(WHITE, ROOK));


        //team pieces
        //get pieces
        ChessPiece pawn = getNewPiece(BLACK, PAWN);
        ChessPiece bishop = getNewPiece(BLACK, BISHOP);
        ChessPiece queen = getNewPiece(BLACK, QUEEN);
        ChessPiece knight = getNewPiece(BLACK, KNIGHT);
        ChessPiece rook = getNewPiece(BLACK, ROOK);

        //get positions
        ChessPosition pawnPosition = getNewPosition(2,6);
        ChessPosition bishopPosition = getNewPosition(7,8);
        ChessPosition queenPosition = getNewPosition(1,2);
        ChessPosition knightPosition = getNewPosition(5,4);
        ChessPosition rookPosition = getNewPosition(3,8);

        //add to board
        chessBoard.addPiece(pawnPosition, pawn);
        chessBoard.addPiece(bishopPosition, bishop);
        chessBoard.addPiece(queenPosition, queen);
        chessBoard.addPiece(knightPosition, knight);
        chessBoard.addPiece(rookPosition, rook);

        //king expected moves
        Set<ChessMove> kingMoves = new HashSet<>();
        kingMoves.add(getNewMove(kingPosition, getNewPosition(6,5), null));

        //check king moves
        game.setBoard(chessBoard);
        Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(kingPosition));
        assertEquals(kingMoves, gameMoves);

        //make sure teams other pieces are not allowed to move
        assertEquals(0, game.validMoves(pawnPosition).size());
        assertEquals(0, game.validMoves(bishopPosition).size());
        assertEquals(0, game.validMoves(queenPosition).size());
        assertEquals(0, game.validMoves(knightPosition).size());
        assertEquals(0, game.validMoves(rookPosition).size());
    }
}
