package passoffTests;

import chess.*;
import org.junit.jupiter.api.Assertions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Used for testing your code
 */
public class TestFactory {

    // Chess Functions
    // ------------------------------------------------------------------------------------------------------------------
    public static ChessBoard getNewBoard() {
        return new ChessBoard();
    }

    public static ChessGame getNewGame() {
        return new ChessGame();
    }

    public static ChessPiece getNewPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        return new ChessPiece(pieceColor, type);
    }

    public static ChessPosition getNewPosition(int row, int col) {
        return new ChessPosition(row, col);
    }

    public static ChessMove getNewMove(ChessPosition startPosition, ChessPosition endPosition,
                                       ChessPiece.PieceType promotionPiece) {
        return new ChessMove(startPosition, endPosition, promotionPiece);
    }
    // ------------------------------------------------------------------------------------------------------------------

    // Server APIs
    // ------------------------------------------------------------------------------------------------------------------
    public static int getServerPort() {
        return 8080;
    }
    // ------------------------------------------------------------------------------------------------------------------

    // Websocket Tests
    // ------------------------------------------------------------------------------------------------------------------
    public static Long getMessageTime() {
        /*
         * Changing this will change how long tests will wait for the server to send
         * messages.
         * 3000 Milliseconds (3 seconds) will be enough for most computers. Feel free to
         * change as you see fit,
         * just know increasing it can make tests take longer to run.
         * (On the flip side, if you've got a good computer feel free to decrease it)
         */
        return 3000L;
    }
    // ------------------------------------------------------------------------------------------------------------------

    static public ChessPosition startPosition(int row, int col) {
        return getNewPosition(row, col);
    }

    static public int[][] endPositions(int[][] endPos) {
        return endPos;
    }

    static public void validateMoves(String boardText, ChessPosition startPosition, int[][] endPositions) {
        var board = loadBoard(boardText);
        var testPiece = board.getPiece(startPosition);
        var validMoves = loadMoves(startPosition, endPositions);

        Assertions.assertEquals(validMoves, testPiece.pieceMoves(board, startPosition), "Wrong moves");
    }

    final static Map<Character, ChessPiece.PieceType> charToTypeMap = Map.of(
            'p', ChessPiece.PieceType.PAWN,
            'n', ChessPiece.PieceType.KNIGHT,
            'r', ChessPiece.PieceType.ROOK,
            'q', ChessPiece.PieceType.QUEEN,
            'k', ChessPiece.PieceType.KING,
            'b', ChessPiece.PieceType.BISHOP);

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
                    ChessGame.TeamColor color = Character.isLowerCase(c) ? ChessGame.TeamColor.BLACK
                            : ChessGame.TeamColor.WHITE;
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
            validMoves.add(TestFactory.getNewMove(startPosition,
                    TestFactory.getNewPosition(endPosition[0], endPosition[1]), null));
        }
        return validMoves;
    }
}
