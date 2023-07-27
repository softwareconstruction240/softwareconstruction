package chess;

/**
 * Indicates an invalid move was made in a game
 */
public class InvalidMoveException extends Exception {

    public InvalidMoveException() {}

    public InvalidMoveException(String message) {
        super(message);
    }
}
