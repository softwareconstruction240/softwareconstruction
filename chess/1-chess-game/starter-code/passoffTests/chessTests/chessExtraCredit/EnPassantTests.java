package passoffTests.chessTests.chessExtraCredit;

import chess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoffTests.TestFactory;

/**
 * Tests if the ChessGame implementation can handle En Passant moves
 * En Passant is a situational move in chess taken directly after your opponent has double moved a pawn
 * If their pawn moves next to one of your pawns, so it passes where your pawn could have captured it, you
 * may capture their pawn with your pawn as if they had only moved a single space. You may only take this move
 * if you do so the turn directly following the pawns double move. This is as if you had caught their
 * pawn "in passing", or translated to French: "En Passant".
 */
public class EnPassantTests {

    private ChessBoard chessBoard;

    @BeforeEach
    public void setup() {
        chessBoard = TestFactory.getNewBoard();
    }


    @Test
    @DisplayName("White En Passant Right")
    public void enPassantWhiteRight() throws InvalidMoveException {

        /*
        | | | | | | | | |
		| | |p| | | | | |
		| | | | | | | | |
		| |P| | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        //white pawn
        ChessPiece pawn = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPosition position = TestFactory.getNewPosition(5, 2);
        chessBoard.addPiece(position, pawn);

        //black pawn that will double move
        chessBoard.addPiece(TestFactory.getNewPosition(7, 3),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(chessBoard);
        game.setTeamTurn(ChessGame.TeamColor.BLACK);

        //move black piece 2 spaces
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(7, 3), TestFactory.getNewPosition(5, 3), null));
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| |P|p| | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        //make sure pawn has En Passant move
        ChessMove enPassantMove =
                TestFactory.getNewMove(TestFactory.getNewPosition(5, 2), TestFactory.getNewPosition(6, 3), null);
        Assertions.assertTrue(game.validMoves(position).contains(enPassantMove),
                "ChessGame validMoves did not contain a valid En Passant move");

        //en passant move works correctly
        Assertions.assertDoesNotThrow(() -> game.makeMove(enPassantMove));
        Assertions.assertNull(game.getBoard().getPiece(TestFactory.getNewPosition(5, 2)),
                "After En Passant move, piece still present at original position");
        Assertions.assertNull(game.getBoard().getPiece(TestFactory.getNewPosition(5, 3)),
                "After En Passant move, piece still present at taken pawn position");
        ChessPiece foundPawn = game.getBoard().getPiece(TestFactory.getNewPosition(6, 3));
        Assertions.assertNotNull(foundPawn, "After En Passant move, no piece present at final position");
        Assertions.assertEquals(ChessPiece.PieceType.PAWN, foundPawn.getPieceType(),
                "Found piece at pawn's position is not a pawn");
        Assertions.assertEquals(ChessGame.TeamColor.WHITE, foundPawn.getTeamColor(),
                "Found piece at pawn's position is the wrong team color");
    }


    @Test
    @DisplayName("White En Passant Left")
    public void enPassantWhiteLeft() throws InvalidMoveException {

        /*
        | | | | | | | | |
		| | |p| | | | | |
		| | | | | | | | |
		| | | |P| | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        //white pawn
        ChessPiece pawn = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPosition position = TestFactory.getNewPosition(5, 4);
        chessBoard.addPiece(position, pawn);

        //black pawn that will double move
        chessBoard.addPiece(TestFactory.getNewPosition(7, 3),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(chessBoard);
        game.setTeamTurn(ChessGame.TeamColor.BLACK);

        //move black piece 2 spaces
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(7, 3), TestFactory.getNewPosition(5, 3), null));
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | |p|P| | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        //make sure pawn has En Passant move
        ChessMove enPassantMove =
                TestFactory.getNewMove(TestFactory.getNewPosition(5, 4), TestFactory.getNewPosition(6, 3), null);
        Assertions.assertTrue(game.validMoves(position).contains(enPassantMove),
                "ChessGame validMoves did not contain a valid En Passant move");

        //en passant move works correctly
        Assertions.assertDoesNotThrow(() -> game.makeMove(enPassantMove));
        Assertions.assertNull(game.getBoard().getPiece(TestFactory.getNewPosition(5, 4)),
                "After En Passant move, piece still present at original position");
        Assertions.assertNull(game.getBoard().getPiece(TestFactory.getNewPosition(5, 3)),
                "After En Passant move, piece still present at taken pawn position");
        ChessPiece foundPawn = game.getBoard().getPiece(TestFactory.getNewPosition(6, 3));
        Assertions.assertNotNull(foundPawn, "After En Passant move, no piece present at final position");
        Assertions.assertEquals(ChessPiece.PieceType.PAWN, foundPawn.getPieceType(),
                "Found piece at pawn's position is not a pawn");
        Assertions.assertEquals(ChessGame.TeamColor.WHITE, foundPawn.getTeamColor(),
                "Found piece at pawn's position is the wrong team color");
    }


    @Test
    @DisplayName("Black En Passant Right")
    public void enPassantBlackRight() throws InvalidMoveException {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | |p| | |
		| | | | | | | | |
		| | | | | | |P| |
		| | | | | | | | |
         */

        //black pawn
        ChessPiece pawn = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPosition position = TestFactory.getNewPosition(4, 6);
        chessBoard.addPiece(position, pawn);

        //white pawn that will double move
        chessBoard.addPiece(TestFactory.getNewPosition(2, 7),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(chessBoard);
        game.setTeamTurn(ChessGame.TeamColor.WHITE);

        //move white piece 2 spaces
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(2, 7), TestFactory.getNewPosition(4, 7), null));
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | |p|P| |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        //make sure pawn has En Passant move
        ChessMove enPassantMove =
                TestFactory.getNewMove(TestFactory.getNewPosition(4, 6), TestFactory.getNewPosition(3, 7), null);
        Assertions.assertTrue(game.validMoves(position).contains(enPassantMove),
                "ChessGame validMoves did not contain a valid En Passant move");

        //en passant move works correctly
        Assertions.assertDoesNotThrow(() -> game.makeMove(enPassantMove));
        Assertions.assertNull(game.getBoard().getPiece(TestFactory.getNewPosition(4, 6)),
                "After En Passant move, piece still present at original position");
        Assertions.assertNull(game.getBoard().getPiece(TestFactory.getNewPosition(4, 7)),
                "After En Passant move, piece still present at taken pawn position");
        ChessPiece foundPawn = game.getBoard().getPiece(TestFactory.getNewPosition(3, 7));
        Assertions.assertNotNull(foundPawn, "After En Passant move, no piece present at final position");
        Assertions.assertEquals(ChessPiece.PieceType.PAWN, foundPawn.getPieceType(),
                "Found piece at pawn's position is not a pawn");
        Assertions.assertEquals(ChessGame.TeamColor.BLACK, foundPawn.getTeamColor(),
                "Found piece at pawn's position is the wrong team color");
    }


    @Test
    @DisplayName("Black En Passant Left")
    public void enPassantBlackLeft() throws InvalidMoveException {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | |p|
		| | | | | | | | |
		| | | | | | |P| |
		| | | | | | | | |
         */

        //Black pawn
        ChessPiece pawn = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPosition position = TestFactory.getNewPosition(4, 8);
        chessBoard.addPiece(position, pawn);

        //white pawn that will double move
        chessBoard.addPiece(TestFactory.getNewPosition(2, 7),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(chessBoard);
        game.setTeamTurn(ChessGame.TeamColor.WHITE);

        //move white piece 2 spaces
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(2, 7), TestFactory.getNewPosition(4, 7), null));
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | |P|p|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */

        //make sure pawn has En Passant move
        ChessMove enPassantMove =
                TestFactory.getNewMove(TestFactory.getNewPosition(4, 8), TestFactory.getNewPosition(3, 7), null);
        Assertions.assertTrue(game.validMoves(position).contains(enPassantMove),
                "ChessGame validMoves did not contain a valid En Passant move");

        //en passant move works correctly
        Assertions.assertDoesNotThrow(() -> game.makeMove(enPassantMove));
        Assertions.assertNull(game.getBoard().getPiece(TestFactory.getNewPosition(4, 8)),
                "After En Passant move, piece still present at original position");
        Assertions.assertNull(game.getBoard().getPiece(TestFactory.getNewPosition(4, 7)),
                "After En Passant move, piece still present at taken pawn position");
        ChessPiece foundPawn = game.getBoard().getPiece(TestFactory.getNewPosition(3, 7));
        Assertions.assertNotNull(foundPawn, "After En Passant move, no piece present at final position");
        Assertions.assertEquals(ChessPiece.PieceType.PAWN, foundPawn.getPieceType(),
                "Found piece at pawn's position is not a pawn");
        Assertions.assertEquals(ChessGame.TeamColor.BLACK, foundPawn.getTeamColor(),
                "Found piece at pawn's position is the wrong team color");
    }


    @Test
    @DisplayName("Can Only En Passant on Next Turn")
    public void missedEnPassant() throws InvalidMoveException {

        /*
        | | | | | | | | |
		| | |p| | | | | |
		| | | | | | | |P|
		| |P| | | | | | |
		| | | | | | | | |
		| | | | | | | |p|
		| | | | | | | | |
		| | | | | | | | |
         */

        //white pawn on board
        ChessPiece pawn = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPosition position = TestFactory.getNewPosition(5, 2);
        chessBoard.addPiece(position, pawn);

        //black pawn that double moves
        chessBoard.addPiece(TestFactory.getNewPosition(7, 3),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));

        //extra pawns
        chessBoard.addPiece(TestFactory.getNewPosition(3, 8),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        chessBoard.addPiece(TestFactory.getNewPosition(6, 8),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));

        //set up game
        ChessGame game = TestFactory.getNewGame();
        game.setBoard(chessBoard);
        game.setTeamTurn(ChessGame.TeamColor.BLACK);

        //move black piece 2 spaces
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(7, 3), TestFactory.getNewPosition(5, 3), null));
        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | |P|
		| |P|p| | | | | |
		| | | | | | | | |
		| | | | | | | |p|
		| | | | | | | | |
		| | | | | | | | |
         */

        //filler moves
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(6, 8), TestFactory.getNewPosition(7, 8), null));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(3, 8), TestFactory.getNewPosition(2, 8), null));
        /*
        | | | | | | | | |
		| | | | | | | |P|
		| | | | | | | | |
		| |P|p| | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | |p|
		| | | | | | | | |
         */

        //make sure pawn cannot do En Passant move
        Assertions.assertFalse(game.validMoves(position).contains(
                TestFactory.getNewMove(TestFactory.getNewPosition(5, 2),
                        TestFactory.getNewPosition(6, 3), null)),
                "ChessGame validMoves contained a En Passant move after the move became invalid");
    }

}
