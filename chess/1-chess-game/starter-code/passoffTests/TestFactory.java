package passoffTests;

import chess.*;
import org.junit.jupiter.api.Assertions;

import java.util.*;

/**
 * Used for testing your code
 * Add in code using your classes for each method for each FIXME
 */
public class TestFactory {

    //Chess Functions
    //------------------------------------------------------------------------------------------------------------------
    public static ChessBoard getNewBoard(){
        // FIXME
		return null;
    }

    public static ChessGame getNewGame(){
        // FIXME
		return null;
    }

    public static ChessPiece getNewPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type){
        // FIXME
		return null;
    }

    public static ChessPosition getNewPosition(Integer row, Integer col){
        // FIXME
		return null;
    }

    public static ChessMove getNewMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece){
        // FIXME
		return null;
    }
    //------------------------------------------------------------------------------------------------------------------


    //Server APIs
    //------------------------------------------------------------------------------------------------------------------
    public static String getServerPort(){
        return "8080";
    }
    //------------------------------------------------------------------------------------------------------------------


    //Websocket Tests
    //------------------------------------------------------------------------------------------------------------------
    public static Long getMessageTime(){
        /*
        Changing this will change how long tests will wait for the server to send messages.
        3000 Milliseconds (3 seconds) will be enough for most computers. Feel free to change as you see fit,
        just know increasing it can make tests take longer to run.
        (On the flip side, if you've got a good computer feel free to decrease it)
         */
        return 3000L;
    }
    //------------------------------------------------------------------------------------------------------------------


    static public void validateMoves(String boardText, ChessPosition startPosition, int[][] moves) {
        var board = loadBoard(boardText);
        var testPiece = board.getPiece(startPosition);
        var validMoves = loadMoves(startPosition, moves);

        Assertions.assertEquals(validMoves, testPiece.pieceMoves(board, startPosition), "Wrong moves");
    }

    final static Map<Character, ChessPiece.PieceType> charToTypeMap = Map.of(
            'p', ChessPiece.PieceType.PAWN,
            'n', ChessPiece.PieceType.KNIGHT,
            'r', ChessPiece.PieceType.ROOK,
            'q', ChessPiece.PieceType.QUEEN,
            'k', ChessPiece.PieceType.KING,
            'b', ChessPiece.PieceType.BISHOP
    );

    public static ChessBoard loadBoard(String boardText) {
        var board = getNewBoard();
        int row = 8;
        int column = 1;
        for (var c : boardText.toCharArray()) {
            switch (c) {
                case '\n' -> {
                    column = 1;
                    row--;
                }
                case ' ' -> column++;
                case '|' -> {
                }
                default -> {
                    ChessGame.TeamColor color = Character.isLowerCase(c) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
                    var type = charToTypeMap.get(Character.toLowerCase(c));
                    var position = TestFactory.getNewPosition(row, column);
                    var piece = TestFactory.getNewPiece(color, type);
                    board.addPiece(position, piece);
                    column++;
                }
            }
        }
        return board;
    }

    public static Set<ChessMove> loadMoves(ChessPosition startPosition, int[][] endPositions) {
        var validMoves = new HashSet<ChessMove>();
        for (var endPosition : endPositions) {
            validMoves.add(TestFactory.getNewMove(startPosition, TestFactory.getNewPosition(endPosition[0], endPosition[1]), null));
        }
        return validMoves;
    }
}
