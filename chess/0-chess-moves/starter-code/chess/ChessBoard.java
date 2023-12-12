package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * 
 * Note: You can add to this interface, but you should not alter the existing
 * methods.
 */
public interface ChessBoard {

    /**
     * Adds a chess piece to the chessboard
     * 
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    void addPiece(ChessPosition position, ChessPiece piece);

    /**
     * Gets a chess piece on the chessboard
     * 
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     *         position
     */
    ChessPiece getPiece(ChessPosition position);

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    void resetBoard();
}
