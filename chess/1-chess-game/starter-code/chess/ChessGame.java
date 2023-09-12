package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * 
 * Note: You can add to this interface, but you should not alter the existing
 * methods.
 */
public interface ChessGame {

    /**
     * @return Which team's turn it is
     */
    TeamColor getTeamTurn();

    /**
     * Set's which teams turn it is
     * 
     * @param team the team whose turn it is
     */
    void setTeamTurn(TeamColor team);

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     * 
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     *         startPosition
     */
    Collection<ChessMove> validMoves(ChessPosition startPosition);

    /**
     * Makes a move in a chess game
     * 
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    void makeMove(ChessMove move) throws InvalidMoveException;

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    boolean isInCheck(TeamColor teamColor);

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    boolean isInCheckmate(TeamColor teamColor);

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    boolean isInStalemate(TeamColor teamColor);

    /**
     * Sets this game's chessboard with a given board
     * 
     * @param board the new board to use
     */
    void setBoard(ChessBoard board);

    /**
     * Gets the current chessboard
     * 
     * @return the chessboard
     */
    ChessBoard getBoard();
}
