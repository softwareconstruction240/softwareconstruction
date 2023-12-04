package passoffTests.chessTests;

import chess.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static passoffTests.TestFactory.*;

public class ChessGameTests {
    @Test
    public void makeValidMoves() throws InvalidMoveException {

        var game = getNewGame();
        game.setBoard(loadBoard("""
                | | | | | | | | |
                | | | | | | | |q|
                | | |n| | | |p| |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | |B| | | | | |
                | |K| | | | | |R|
                """));
        game.setTeamTurn(ChessGame.TeamColor.WHITE);

        //king
        var kingStartPosition = getNewPosition(1, 2);
        var kingEndPosition = getNewPosition(1, 1);
        game.makeMove(getNewMove(kingStartPosition, kingEndPosition, null));

        Assertions.assertEquals(game.getBoard(), loadBoard("""
                | | | | | | | | |
                | | | | | | | |q|
                | | |n| | | |p| |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | |B| | | | | |
                |K| | | | | | |R|
                """));

        //queen
        var queenStartPosition = getNewPosition(7, 8);
        var queenEndPosition = getNewPosition(8, 7);
        game.makeMove(getNewMove(queenStartPosition, queenEndPosition, null));

        Assertions.assertEquals(game.getBoard(), loadBoard("""
                | | | | | | |q| |
                | | | | | | | | |
                | | |n| | | |p| |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | |B| | | | | |
                |K| | | | | | |R|
                """));

        //rook
        var rookStartPosition = getNewPosition(1, 8);
        ChessPosition rookEndPosition = getNewPosition(3, 8);
        game.makeMove(getNewMove(rookStartPosition, rookEndPosition, null));

        Assertions.assertEquals(game.getBoard(), loadBoard("""
                | | | | | | |q| |
                | | | | | | | | |
                | | |n| | | |p| |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | |R|
                | | |B| | | | | |
                |K| | | | | | | |
                """));

        //knight
        var knightStartPosition = getNewPosition(6, 3);
        ChessPosition knightEndPosition = getNewPosition(7, 5);
        game.makeMove(getNewMove(knightStartPosition, knightEndPosition, null));

        Assertions.assertEquals(game.getBoard(), loadBoard("""
                | | | | | | |q| |
                | | | | |n| | | |
                | | | | | | |p| |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | |R|
                | | |B| | | | | |
                |K| | | | | | | |
                """));


        //bishop
        var bishopStartPosition = getNewPosition(2, 3);
        ChessPosition bishopEndPosition = getNewPosition(1, 2);
        game.makeMove(getNewMove(bishopStartPosition, bishopEndPosition, null));

        Assertions.assertEquals(game.getBoard(), loadBoard("""
                | | | | | | |q| |
                | | | | |n| | | |
                | | | | | | |p| |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | |R|
                | | | | | | | | |
                |K|B| | | | | | |
                """));

        //pawn
        var pawnStartPosition = getNewPosition(6, 7);
        var pawnEndPosition = getNewPosition(5, 7);
        game.makeMove(getNewMove(pawnStartPosition, pawnEndPosition, null));

        Assertions.assertEquals(game.getBoard(), loadBoard("""
                | | | | | | |q| |
                | | | | |n| | | |
                | | | | | | | | |
                | | | | | | |p| |
                | | | | | | | | |
                | | | | | | | |R|
                | | | | | | | | |
                |K|B| | | | | | |
                """));
    }


    @Test
    @DisplayName("Invalid Make Move")
    public void invalidMoves() throws InvalidMoveException {
        var board = getNewBoard();
        board.resetBoard();
        var game = getNewGame();
        game.setBoard(board);
        game.setTeamTurn(ChessGame.TeamColor.WHITE);

        Assertions.assertEquals(game.getBoard(),
                loadBoard("""
                        |r|n|b|q|k|b|n|r|
                        |p|p|p|p|p|p|p|p|
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        |P|P|P|P|P|P|P|P|
                        |R|N|B|Q|K|B|N|R|
                        """));

        //move further than can go
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                getNewMove(getNewPosition(2, 1), getNewPosition(5, 1), null)));

        game.makeMove(getNewMove(getNewPosition(2, 1), getNewPosition(3, 1), null));
        Assertions.assertEquals(game.getBoard(),
                loadBoard("""
                        |r|n|b|q|k|b|n|r|
                        |p|p|p|p|p|p|p|p|
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        |P| | | | | | | |
                        | |P|P|P|P|P|P|P|
                        |R|N|B|Q|K|B|N|R|
                        """));

        //pawn diagonal when no capture
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                getNewMove(getNewPosition(7, 2), getNewPosition(6, 3), null)));

        game.makeMove(getNewMove(getNewPosition(7, 2), getNewPosition(6, 2), null));
        Assertions.assertEquals(game.getBoard(),
                loadBoard("""
                        |r|n|b|q|k|b|n|r|
                        |p| |p|p|p|p|p|p|
                        | |p| | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        |P| | | | | | | |
                        | |P|P|P|P|P|P|P|
                        |R|N|B|Q|K|B|N|R|
                        """));

        //make move out of turn
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                getNewMove(getNewPosition(6, 2), getNewPosition(5, 2), null)));

        //pawn in way
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                getNewMove(getNewPosition(1, 1), getNewPosition(4, 1), null)));


        game.makeMove(getNewMove(getNewPosition(1, 1), getNewPosition(2, 1), null));
        Assertions.assertEquals(game.getBoard(),
                loadBoard("""
                        |r|n|b|q|k|b|n|r|
                        |p| |p|p|p|p|p|p|
                        | |p| | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        |P| | | | | | | |
                        |R|P|P|P|P|P|P|P|
                        | |N|B|Q|K|B|N|R|
                        """));

        //not a move the piece can ever take
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                getNewMove(getNewPosition(8, 7), getNewPosition(5, 5), null)));


        game.makeMove(getNewMove(getNewPosition(8, 7), getNewPosition(6, 6), null));
        Assertions.assertEquals(game.getBoard(),
                loadBoard("""
                        |r|n|b|q|k|b| |r|
                        |p| |p|p|p|p|p|p|
                        | |p| | | |n| | |
                        | | | | | | | | |
                        | | | | | | | | |
                        |P| | | | | | | |
                        |R|P|P|P|P|P|P|P|
                        | |N|B|Q|K|B|N|R|
                        """));

        //same team at destination
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                getNewMove(getNewPosition(1, 6), getNewPosition(2, 5), null)));


        game.makeMove(getNewMove(getNewPosition(2, 5), getNewPosition(4, 5), null));
        Assertions.assertEquals(game.getBoard(),
                loadBoard("""
                        |r|n|b|q|k|b| |r|
                        |p| |p|p|p|p|p|p|
                        | |p| | | |n| | |
                        | | | | | | | | |
                        | | | | |P| | | |
                        |P| | | | | | | |
                        |R|P|P|P| |P|P|P|
                        | |N|B|Q|K|B|N|R|
                        """));

        //same team blocking path
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                getNewMove(getNewPosition(8, 4), getNewPosition(6, 4), null)));


        game.makeMove(getNewMove(getNewPosition(6, 6), getNewPosition(4, 5), null));
        Assertions.assertEquals(game.getBoard(),
                loadBoard("""
                        |r|n|b|q|k|b| |r|
                        |p| |p|p|p|p|p|p|
                        | |p| | | | | | |
                        | | | | | | | | |
                        | | | | |n| | | |
                        |P| | | | | | | |
                        |R|P|P|P| |P|P|P|
                        | |N|B|Q|K|B|N|R|
                        """));

        //try moving captured piece
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                getNewMove(getNewPosition(4, 5), getNewPosition(5, 5), null)));

        game.makeMove(getNewMove(getNewPosition(1, 6), getNewPosition(3, 4), null));
        game.makeMove(getNewMove(getNewPosition(8, 3), getNewPosition(6, 1), null));
        Assertions.assertEquals(game.getBoard(),
                loadBoard("""
                        |r|n| |q|k|b| |r|
                        |p| |p|p|p|p|p|p|
                        |b|p| | | | | | |
                        | | | | | | | | |
                        | | | | |n| | | |
                        |P| | |B| | | | |
                        |R|P|P|P| |P|P|P|
                        | |N|B|Q|K| |N|R|
                        """));

        //try moving through enemy piece
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                getNewMove(getNewPosition(3, 4), getNewPosition(5, 6), null)));

        game.makeMove(getNewMove(getNewPosition(1, 7), getNewPosition(3, 6), null));
        game.makeMove(getNewMove(getNewPosition(4, 5), getNewPosition(2, 4), null));
        Assertions.assertFalse(game.isInCheck(ChessGame.TeamColor.WHITE),
                "White is not in check but isInCheck returned true");
        Assertions.assertEquals(game.getBoard(),
                loadBoard("""
                        |r|n| |q|k|b| |r|
                        |p| |p|p|p|p|p|p|
                        |b|p| | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        |P| | |B| |N| | |
                        |R|P|P|n| |P|P|P|
                        | |N|B|Q|K| | |R|
                        """));

        game.makeMove(getNewMove(getNewPosition(1, 8), getNewPosition(1, 7), null));
        game.makeMove(getNewMove(getNewPosition(2, 4), getNewPosition(3, 6), null));
        Assertions.assertTrue(game.isInCheck(ChessGame.TeamColor.WHITE),
                "White is in check but isInCheck returned false");
        Assertions.assertEquals(game.getBoard(),
                loadBoard("""
                        |r|n| |q|k|b| |r|
                        |p| |p|p|p|p|p|p|
                        |b|p| | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        |P| | |B| |n| | |
                        |R|P|P| | |P|P|P|
                        | |N|B|Q|K| |R| |
                        """));

        //try not getting out of check
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                getNewMove(getNewPosition(1, 7), getNewPosition(1, 8), null)));

        game.makeMove(getNewMove(getNewPosition(2, 7), getNewPosition(3, 6), null));
        Assertions.assertFalse(game.isInCheck(ChessGame.TeamColor.WHITE),
                "White is not in check but isInCheck returned true");
        Assertions.assertEquals(game.getBoard(),
                loadBoard("""
                        |r|n| |q|k|b| |r|
                        |p| |p|p|p|p|p|p|
                        |b|p| | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        |P| | |B| |P| | |
                        |R|P|P| | |P| |P|
                        | |N|B|Q|K| |R| |
                        """));

        //try double-moving moved pawn
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                getNewMove(getNewPosition(6, 2), getNewPosition(4, 2), null)));

        //few more moves to try
        game.makeMove(getNewMove(getNewPosition(6, 2), getNewPosition(5, 2), null));
        game.makeMove(getNewMove(getNewPosition(1, 7), getNewPosition(1, 8), null));
        game.makeMove(getNewMove(getNewPosition(5, 2), getNewPosition(4, 2), null));

        Assertions.assertEquals(game.getBoard(),
                loadBoard("""
                        |r|n| |q|k|b| |r|
                        |p| |p|p|p|p|p|p|
                        |b| | | | | | | |
                        | | | | | | | | |
                        | |p| | | | | | |
                        |P| | |B| |P| | |
                        |R|P|P| | |P| |P|
                        | |N|B|Q|K| | |R|
                        """));
    }


    @ParameterizedTest
    @EnumSource(value = ChessPiece.PieceType.class, names = {"QUEEN", "ROOK", "KNIGHT", "BISHOP"})
    @DisplayName("Pawn Promotion")
    public void promotionMoves(ChessPiece.PieceType promotionType) throws InvalidMoveException {
        var game = getNewGame();
        game.setBoard(loadBoard("""
                | | | | | | | | |
                | | |P| | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | |p| | | |
                | | | | | |Q| | |
                """));
        game.setTeamTurn(ChessGame.TeamColor.WHITE);

        //White promotion
        ChessMove whitePromotion = getNewMove(getNewPosition(7, 3),
                getNewPosition(8, 3), promotionType);
        game.makeMove(whitePromotion);

        Assertions.assertNull(game.getBoard().getPiece(whitePromotion.getStartPosition()),
                "After move, a piece is still present in the start position");
        ChessPiece whiteEndPiece = game.getBoard().getPiece(whitePromotion.getEndPosition());
        Assertions.assertNotNull(whiteEndPiece, "After move, no piece found at the end position");
        Assertions.assertEquals(promotionType, whiteEndPiece.getPieceType(),
                "Found piece at end position is not the correct piece type");
        Assertions.assertEquals(ChessGame.TeamColor.WHITE, whiteEndPiece.getTeamColor(),
                "Found piece at end position is the wrong team color");


        //Black take + promotion
        ChessMove blackPromotion = getNewMove(getNewPosition(2, 5),
                getNewPosition(1, 6), promotionType);
        game.makeMove(blackPromotion);

        Assertions.assertNull(game.getBoard().getPiece(blackPromotion.getStartPosition()),
                "After move, a piece is still present in the start position");
        ChessPiece blackEndPiece = game.getBoard().getPiece(blackPromotion.getEndPosition());
        Assertions.assertNotNull(blackEndPiece, "After move, no piece found at the end position");
        Assertions.assertEquals(promotionType, blackEndPiece.getPieceType(),
                "Found piece at end position is not the correct piece type");
        Assertions.assertEquals(ChessGame.TeamColor.BLACK, blackEndPiece.getTeamColor(),
                "Found piece at end position is the wrong team color");
    }


    @Test
    @DisplayName("White in Check")
    public void whiteCheck() {
        var game = getNewGame();
        game.setBoard(loadBoard("""
                | | | | | | | |k|
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | |K| | | |r| | |
                | | | | | | | | |
                | | | | | | | | |
                """));

        Assertions.assertTrue(game.isInCheck(ChessGame.TeamColor.WHITE),
                "White is in check but isInCheck returned false");
        Assertions.assertFalse(game.isInCheck(ChessGame.TeamColor.BLACK),
                "Black is not in check but isInCheck returned true");
    }


    @Test
    @DisplayName("Black in Check")
    public void blackCheck() {
        var game = getNewGame();
        game.setBoard(loadBoard("""
                | | | |K| | | | |
                | | | | | | | | |
                | | | |k| | | | |
                | | | | | | | | |
                | | | | | | | | |
                |B| | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                """));

        Assertions.assertTrue(game.isInCheck(ChessGame.TeamColor.BLACK),
                "Black is in check but isInCheck returned false");
        Assertions.assertFalse(game.isInCheck(ChessGame.TeamColor.WHITE),
                "White is not in check but isInCheck returned true");
    }


    @Test
    @DisplayName("White in Checkmate")
    public void whiteTeamCheckmate() {

        var game = getNewGame();
        game.setBoard(loadBoard("""
                | | | | | | | | |
                | | |b|q| | | | |
                | | | | | | | | |
                | | | |p| | | |k|
                | | | | | |K| | |
                | | |r| | | | | |
                | | | | |n| | | |
                | | | | | | | | |
                """));
        game.setTeamTurn(ChessGame.TeamColor.WHITE);

        Assertions.assertTrue(game.isInCheckmate(ChessGame.TeamColor.WHITE),
                "White is in checkmate but isInCheckmate returned false");
        Assertions.assertFalse(game.isInCheckmate(ChessGame.TeamColor.BLACK),
                "Black is not in checkmate but isInCheckmate returned true");
    }


    @Test
    @DisplayName("Black in Checkmate by Pawns")
    public void blackTeamPawnCheckmate() {
        var game = getNewGame();
        game.setBoard(loadBoard("""
                | | | |k| | | | |
                | | | |P|P| | | |
                | |P| | |P|P| | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | |K| | | | |
                """));
        game.setTeamTurn(ChessGame.TeamColor.BLACK);

        Assertions.assertTrue(game.isInCheckmate(ChessGame.TeamColor.BLACK),
                "Black is in checkmate but isInCheckmate returned false");
        Assertions.assertFalse(game.isInCheckmate(ChessGame.TeamColor.WHITE),
                "White is not in checkmate but isInCheckmate returned true");

    }


    @Test
    @DisplayName("Pinned King Causes Stalemate")
    public void stalemate() {
        var game = getNewGame();
        game.setBoard(loadBoard("""
                |k| | | | | | | |
                | | | | | | | |r|
                | | | | | | | | |
                | | | | |q| | | |
                | | | |n| | |K| |
                | | | | | | | | |
                | | | | | | | | |
                | | | | |b| | | |
                """));
        game.setTeamTurn(ChessGame.TeamColor.WHITE);

        Assertions.assertTrue(game.isInStalemate(ChessGame.TeamColor.WHITE),
                "White is in a stalemate but isInStalemate returned false");
        Assertions.assertFalse(game.isInStalemate(ChessGame.TeamColor.BLACK),
                "Black is not in a stalemate but isInStalemate returned true");
    }


    @Test
    @DisplayName("Full Game Checkmate")
    public void scholarsMate() throws InvalidMoveException {
        var board = getNewBoard();
        board.resetBoard();
        var game = getNewGame();
        game.setBoard(board);
        game.setTeamTurn(ChessGame.TeamColor.WHITE);

        Assertions.assertFalse(game.isInCheckmate(ChessGame.TeamColor.BLACK),
                "Black is not in checkmate but isInCheckmate returned true");

        /*
        |r|n|b|q|k|b|n|r|
		|p|p|p|p|p|p|p|p|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P|P|P|P|P|P|P|P|
		|R|N|B|Q|K|B|N|R|
         */

        game.makeMove(getNewMove(getNewPosition(2, 5), getNewPosition(4, 5), null));
        /*
        |r|n|b|q|k|b|n|r|
		|p|p|p|p|p|p|p|p|
		| | | | | | | | |
		| | | | | | | | |
		| | | | |P| | | |
		| | | | | | | | |
		|P|P|P|P| |P|P|P|
		|R|N|B|Q|K|B|N|R|
         */

        game.makeMove(getNewMove(getNewPosition(7, 5), getNewPosition(5, 5), null));
        /*
        |r|n|b|q|k|b|n|r|
		|p|p|p|p| |p|p|p|
		| | | | | | | | |
		| | | | |p| | | |
		| | | | |P| | | |
		| | | | | | | | |
		|P|P|P|P| |P|P|P|
		|R|N|B|Q|K|B|N|R|
         */

        game.makeMove(getNewMove(getNewPosition(1, 6), getNewPosition(4, 3), null));
        /*
        |r|n|b|q|k|b|n|r|
		|p|p|p|p| |p|p|p|
		| | | | | | | | |
		| | | | |p| | | |
		| | |B| |P| | | |
		| | | | | | | | |
		|P|P|P|P| |P|P|P|
		|R|N|B|Q|K| |N|R|
         */

        game.makeMove(getNewMove(getNewPosition(8, 7), getNewPosition(6, 6), null));
        /*
        |r|n|b|q|k|b| |r|
		|p|p|p|p| |p|p|p|
		| | | | | |n| | |
		| | | | |p| | | |
		| | |B| |P| | | |
		| | | | | | | | |
		|P|P|P|P| |P|P|P|
		|R|N|B|Q|K| |N|R|
         */

        game.makeMove(getNewMove(getNewPosition(1, 4), getNewPosition(5, 8), null));
        /*
        |r|n|b|q|k|b| |r|
		|p|p|p|p| |p|p|p|
		| | | | | |n| | |
		| | | | |p| | |Q|
		| | |B| |P| | | |
		| | | | | | | | |
		|P|P|P|P| |P|P|P|
		|R|N|B| |K| |N|R|
         */

        game.makeMove(getNewMove(getNewPosition(8, 2), getNewPosition(6, 3), null));
        /*
        |r| |b|q|k|b| |r|
		|p|p|p|p| |p|p|p|
		| | |n| | |n| | |
		| | | | |p| | |Q|
		| | |B| |P| | | |
		| | | | | | | | |
		|P|P|P|P| |P|P|P|
		|R|N|B| |K| |N|R|
         */

        game.makeMove(getNewMove(getNewPosition(5, 8), getNewPosition(7, 6), null));
        /*
        |r| |b|q|k|b| |r|
		|p|p|p|p| |Q|p|p|
		| | |n| | |n| | |
		| | | | |p| | | |
		| | |B| |P| | | |
		| | | | | | | | |
		|P|P|P|P| |P|P|P|
		|R|N|B| |K| |N|R|
         */

        Assertions.assertTrue(game.isInCheckmate(ChessGame.TeamColor.BLACK),
                "Black is in checkmate but isInCheckmate returned false");
    }


    @Nested
    @DisplayName("Valid Move Tests")
    public class ValidMoveTests {
        @Test
        @DisplayName("Check Forces Movement")
        public void forcedMove() {

            var game = getNewGame();
            game.setBoard(loadBoard("""
                    | | | | | | | | |
                    | | | | | | | | |
                    | |B| | | | | | |
                    | | | | | |K| | |
                    | | |n| | | | | |
                    | | | | | | | | |
                    | | | |q| |k| | |
                    | | | | | | | | |
                    """));

            // Knight moves
            ChessPosition knightPosition = getNewPosition(4, 3);
            var validMoves = loadMoves(knightPosition, new int[][]{{3, 5}, {6, 2}});
            Assertions.assertEquals(validMoves, game.validMoves(knightPosition),
                    "ChessGame validMoves did not return the correct moves");

            // Queen Moves
            ChessPosition queenPosition = getNewPosition(2, 4);
            validMoves = loadMoves(queenPosition, new int[][]{{3, 5}, {4, 4}});
            Assertions.assertEquals(validMoves, game.validMoves(queenPosition),
                    "ChessGame validMoves did not return the correct moves");

        }


        @Test
        @DisplayName("Piece Partially Trapped")
        public void moveIntoCheck() {

            var game = getNewGame();
            game.setBoard(loadBoard("""
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | |r| | | |R| |K|
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    """));

            ChessPosition rookPosition = getNewPosition(5, 6);
            var validMoves = loadMoves(rookPosition, new int[][]{
                    {5, 7}, {5, 5}, {5, 4}, {5, 3}, {5, 2}
            });

            Assertions.assertEquals(validMoves, game.validMoves(rookPosition),
                    "ChessGame validMoves did not return the correct moves");
        }


        @Test
        @DisplayName("Piece Completely Trapped")
        public void rookPinnedToKing() {

            var game = getNewGame();
            game.setBoard(loadBoard("""
                    | | | | | | | |Q|
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | |r| | | | |
                    | | | | | | | | |
                    | |k| | | | | | |
                    | | | | | | | | |
                    """));

            ChessPosition position = getNewPosition(4, 4);
            Assertions.assertTrue(game.validMoves(position).isEmpty(),
                    "ChessGame validMoves returned valid moves for a trapped piece");
        }


        @Test
        @DisplayName("Pieces Cannot Eliminate Check")
        public void kingInDanger() {

            var game = getNewGame();
            game.setBoard(loadBoard("""
                    |R| | | | | | | |
                    | | | |k| | | |b|
                    | | | | |P| | | |
                    | | |Q|n| | | | |
                    | | | | | | | | |
                    | | | | | | | |r|
                    | | | | | |p| | |
                    | |q| | | | | | |
                    """));

            //get positions
            ChessPosition kingPosition = getNewPosition(7, 4);
            ChessPosition pawnPosition = getNewPosition(2, 6);
            ChessPosition bishopPosition = getNewPosition(7, 8);
            ChessPosition queenPosition = getNewPosition(1, 2);
            ChessPosition knightPosition = getNewPosition(5, 4);
            ChessPosition rookPosition = getNewPosition(3, 8);


            var validMoves = loadMoves(kingPosition, new int[][]{{6, 5}});

            Assertions.assertEquals(validMoves, game.validMoves(kingPosition),
                    "ChessGame validMoves did not return the correct moves");

            //make sure teams other pieces are not allowed to move
            Assertions.assertTrue(game.validMoves(pawnPosition).isEmpty(),
                    "ChessGame validMoves returned valid moves for a trapped piece");
            Assertions.assertTrue(game.validMoves(bishopPosition).isEmpty(),
                    "ChessGame validMoves returned valid moves for a trapped piece");
            Assertions.assertTrue(game.validMoves(queenPosition).isEmpty(),
                    "ChessGame validMoves returned valid moves for a trapped piece");
            Assertions.assertTrue(game.validMoves(knightPosition).isEmpty(),
                    "ChessGame validMoves returned valid moves for a trapped piece");
            Assertions.assertTrue(game.validMoves(rookPosition).isEmpty(),
                    "ChessGame validMoves returned valid moves for a trapped piece");
        }


        @Test
        @DisplayName("King Cannot Move Into Check")
        public void noPutSelfInDanger() {

            var game = getNewGame();
            game.setBoard(loadBoard("""
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | |k| | |
                    | | | | | | | | |
                    | | | | | |K| | |
                    | | | | | | | | |
                    """));

            ChessPosition position = getNewPosition(2, 6);
            var validMoves = loadMoves(position, new int[][]{
                    {1, 5}, {1, 6}, {1, 7}, {2, 5}, {2, 7},
            });

            Assertions.assertEquals(validMoves, game.validMoves(position),
                    "ChessGame validMoves did not return the correct moves");
        }

    }

}

