package chess;

/**
 * Represents a single square position on a chess board
 */
public interface ChessPosition {
    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    int getRow();

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    int getColumn();

    //TODO (1,1) coding for bottom left matches a1, matching chess notation better.
    // Should we have it code for top left as that would be more straight forward to iterate over?
}
