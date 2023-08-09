package chess;

/**
 * Represents a single square position on a chessboard
 */
public interface ChessPosition {
    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    Integer getRow();

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    Integer getColumn();
}
