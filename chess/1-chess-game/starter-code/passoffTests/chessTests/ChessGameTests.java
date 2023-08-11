package passoffTests.chessTests;

import chess.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import passoffTests.TestFactory;

import java.util.HashSet;
import java.util.Set;

public class ChessGameTests {

    private ChessGame game;
    private ChessBoard board;

    @BeforeEach
    public void setup() {
        //start with a new board each time
        game = TestFactory.getNewGame();
        board = TestFactory.getNewBoard();
    }

    @Test
    @DisplayName("Normal Make Move")
    public void validMoves() throws InvalidMoveException {
        /*
        | | | | | | | | |
		| | | | | | | |q|
		| | |n| | | |p| |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | |B| | | | | |
		| |K| | | | | |R|
         */
        
        ChessPosition kingStartPosition = TestFactory.getNewPosition(1, 2);
        board.addPiece(kingStartPosition, 
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));

        ChessPosition queenStartPosition = TestFactory.getNewPosition(7, 8);
        board.addPiece(queenStartPosition, 
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));

        ChessPosition rookStartPosition = TestFactory.getNewPosition(1, 8);
        board.addPiece(rookStartPosition, 
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        ChessPosition knightStartPosition = TestFactory.getNewPosition(6, 3);
        board.addPiece(knightStartPosition, 
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        
        ChessPosition bishopStartPosition = TestFactory.getNewPosition(2, 3);
        board.addPiece(bishopStartPosition, 
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));

        ChessPosition pawnStartPosition = TestFactory.getNewPosition(6, 7);
        board.addPiece(pawnStartPosition, 
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));

        game.setBoard(board);
        game.setTeamTurn(ChessGame.TeamColor.WHITE);
        
        //king
        ChessPosition kingEndPosition = TestFactory.getNewPosition(1, 1);
        game.makeMove(TestFactory.getNewMove(kingStartPosition, kingEndPosition, null));
        
        Assertions.assertNull(game.getBoard().getPiece(kingStartPosition), 
                "After move, a piece is still present in the start position");
        ChessPiece king = game.getBoard().getPiece(kingEndPosition);
        Assertions.assertNotNull(king, "After move, no piece found at the end position");
        Assertions.assertEquals(ChessPiece.PieceType.KING, king.getPieceType(),
                "Found piece at end position is not the correct piece type");
        Assertions.assertEquals(ChessGame.TeamColor.WHITE, king.getTeamColor(),
                "Found piece at end position is the wrong team color");

        /*
        | | | | | | | | |
		| | | | | | | |q|
		| | |n| | | |p| |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | |B| | | | | |
		|K| | | | | | |R|
         */

        //queen
        ChessPosition queenEndPosition = TestFactory.getNewPosition(8, 7);
        game.makeMove(TestFactory.getNewMove(queenStartPosition, queenEndPosition, null));

        Assertions.assertNull(game.getBoard().getPiece(queenStartPosition),
                "After move, a piece is still present in the start position");
        ChessPiece queen = game.getBoard().getPiece(queenEndPosition);
        Assertions.assertNotNull(queen, "After move, no piece found at the end position");
        Assertions.assertEquals(ChessPiece.PieceType.QUEEN, queen.getPieceType(),
                "Found piece at end position is not the correct piece type");
        Assertions.assertEquals(ChessGame.TeamColor.BLACK, queen.getTeamColor(),
                "Found piece at end position is the wrong team color");

        /*
        | | | | | | |q| |
		| | | | | | | | |
		| | |n| | | |p| |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | |B| | | | | |
		|K| | | | | | |R|
         */

        //rook
        ChessPosition rookEndPosition = TestFactory.getNewPosition(3, 8);
        game.makeMove(TestFactory.getNewMove(rookStartPosition, rookEndPosition, null));

        Assertions.assertNull(game.getBoard().getPiece(rookStartPosition),
                "After move, a piece is still present in the start position");
        ChessPiece rook = game.getBoard().getPiece(rookEndPosition);
        Assertions.assertNotNull(rook, "After move, no piece found at the end position");
        Assertions.assertEquals(ChessPiece.PieceType.ROOK, rook.getPieceType(),
                "Found piece at end position is not the correct piece type");
        Assertions.assertEquals(ChessGame.TeamColor.WHITE, rook.getTeamColor(),
                "Found piece at end position is the wrong team color");

        /*
        | | | | | | |q| |
		| | | | | | | | |
		| | |n| | | |p| |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | |R|
		| | |B| | | | | |
		|K| | | | | | | |
         */

        //knight
        ChessPosition knightEndPosition = TestFactory.getNewPosition(7, 5);
        game.makeMove(TestFactory.getNewMove(knightStartPosition, knightEndPosition, null));

        Assertions.assertNull(game.getBoard().getPiece(knightStartPosition),
                "After move, a piece is still present in the start position");
        ChessPiece knight = game.getBoard().getPiece(knightEndPosition);
        Assertions.assertNotNull(knight, "After move, no piece found at the end position");
        Assertions.assertEquals(ChessPiece.PieceType.KNIGHT, knight.getPieceType(),
                "Found piece at end position is not the correct piece type");
        Assertions.assertEquals(ChessGame.TeamColor.BLACK, knight.getTeamColor(),
                "Found piece at end position is the wrong team color");

        /*
        | | | | | | |q| |
		| | | | |n| | | |
		| | | | | | |p| |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | |R|
		| | |B| | | | | |
		|K| | | | | | | |
         */

        //bishop
        ChessPosition bishopEndPosition = TestFactory.getNewPosition(1, 2);
        game.makeMove(TestFactory.getNewMove(bishopStartPosition, bishopEndPosition, null));

        Assertions.assertNull(game.getBoard().getPiece(bishopStartPosition),
                "After move, a piece is still present in the start position");
        ChessPiece bishop = game.getBoard().getPiece(bishopEndPosition);
        Assertions.assertNotNull(bishop, "After move, no piece found at the end position");
        Assertions.assertEquals(ChessPiece.PieceType.BISHOP, bishop.getPieceType(),
                "Found piece at end position is not the correct piece type");
        Assertions.assertEquals(ChessGame.TeamColor.WHITE, bishop.getTeamColor(),
                "Found piece at end position is the wrong team color");

        /*
        | | | | | | |q| |
		| | | | |n| | | |
		| | | | | | |p| |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | |R|
		| | | | | | | | |
		|K|B| | | | | | |
         */

        //pawn
        ChessPosition pawnEndPosition = TestFactory.getNewPosition(5, 7);
        game.makeMove(TestFactory.getNewMove(pawnStartPosition, pawnEndPosition, null));

        Assertions.assertNull(game.getBoard().getPiece(pawnStartPosition),
                "After move, a piece is still present in the start position");
        ChessPiece pawn = game.getBoard().getPiece(pawnEndPosition);
        Assertions.assertNotNull(pawn, "After move, no piece found at the end position");
        Assertions.assertEquals(ChessPiece.PieceType.PAWN, pawn.getPieceType(),
                "Found piece at end position is not the correct piece type");
        Assertions.assertEquals(ChessGame.TeamColor.BLACK, pawn.getTeamColor(),
                "Found piece at end position is the wrong team color");

        /*
        | | | | | | |q| |
		| | | | |n| | | |
		| | | | | | | | |
		| | | | | | |p| |
		| | | | | | | | |
		| | | | | | | |R|
		| | | | | | | | |
		|K|B| | | | | | |
         */
    }

    
    @Test
    @DisplayName("Invalid Make Move")
    public void invalidMoves() throws InvalidMoveException {
        board.resetBoard();
        game.setBoard(board);
        game.setTeamTurn(ChessGame.TeamColor.WHITE);
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

        //move further than can go
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                TestFactory.getNewMove(TestFactory.getNewPosition(2, 1), TestFactory.getNewPosition(5, 1), null)));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(2, 1), TestFactory.getNewPosition(3, 1), null));
        /*
        |r|n|b|q|k|b|n|r|
		|p|p|p|p|p|p|p|p|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P| | | | | | | |
		| |P|P|P|P|P|P|P|
		|R|N|B|Q|K|B|N|R|
         */

        //pawn diagonal when no capture
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                TestFactory.getNewMove(TestFactory.getNewPosition(7, 2), TestFactory.getNewPosition(6, 3), null)));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(7, 2), TestFactory.getNewPosition(6, 2), null));
        /*
        |r|n|b|q|k|b|n|r|
		|p| |p|p|p|p|p|p|
		| |p| | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P| | | | | | | |
		| |P|P|P|P|P|P|P|
		|R|N|B|Q|K|B|N|R|
         */

        //make move out of turn
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                TestFactory.getNewMove(TestFactory.getNewPosition(6, 2), TestFactory.getNewPosition(5, 2), null)));

        //pawn in way
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                TestFactory.getNewMove(TestFactory.getNewPosition(1, 1), TestFactory.getNewPosition(4, 1), null)));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(1, 1), TestFactory.getNewPosition(2, 1), null));
        /*
        |r|n|b|q|k|b|n|r|
		|p| |p|p|p|p|p|p|
		| |p| | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P| | | | | | | |
		|R|P|P|P|P|P|P|P|
		| |N|B|Q|K|B|N|R|
         */

        //not a move the piece can ever take
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                TestFactory.getNewMove(TestFactory.getNewPosition(8, 7), TestFactory.getNewPosition(5, 5), null)));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(8, 7), TestFactory.getNewPosition(6, 6), null));
        /*
        |r|n|b|q|k|b| |r|
		|p| |p|p|p|p|p|p|
		| |p| | | |n| | |
		| | | | | | | | |
		| | | | | | | | |
		|P| | | | | | | |
		|R|P|P|P|P|P|P|P|
		| |N|B|Q|K|B|N|R|
         */

        //team at destination
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                TestFactory.getNewMove(TestFactory.getNewPosition(1, 6), TestFactory.getNewPosition(2, 5), null)));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(2, 5), TestFactory.getNewPosition(4, 5), null));
        /*
        |r|n|b|q|k|b| |r|
		|p| |p|p|p|p|p|p|
		| |p| | | |n| | |
		| | | | | | | | |
		| | | | |P| | | |
		|P| | | | | | | |
		|R|P|P|P| |P|P|P|
		| |N|B|Q|K|B|N|R|
         */

        //team blocking path
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                TestFactory.getNewMove(TestFactory.getNewPosition(8, 4), TestFactory.getNewPosition(6, 4), null)));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(6, 6), TestFactory.getNewPosition(4, 5), null));
        /*
        |r|n|b|q|k|b| |r|
		|p| |p|p|p|p|p|p|
		| |p| | | | | | |
		| | | | | | | | |
		| | | | |n| | | |
		|P| | | | | | | |
		|R|P|P|P| |P|P|P|
		| |N|B|Q|K|B|N|R|
         */

        //try moving captured piece
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                TestFactory.getNewMove(TestFactory.getNewPosition(4, 5), TestFactory.getNewPosition(5, 5), null)));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(1, 6), TestFactory.getNewPosition(3, 4), null));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(8, 3), TestFactory.getNewPosition(6, 1), null));
        /*
        |r|n| |q|k|b| |r|
		|p| |p|p|p|p|p|p|
		|b|p| | | | | | |
		| | | | | | | | |
		| | | | |n| | | |
		|P| | |B| | | | |
		|R|P|P|P| |P|P|P|
		| |N|B|Q|K| |N|R|
         */

        //try moving through enemy piece
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                TestFactory.getNewMove(TestFactory.getNewPosition(3, 4), TestFactory.getNewPosition(5, 6), null)));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(1, 7), TestFactory.getNewPosition(3, 6), null));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(4, 5), TestFactory.getNewPosition(2, 4), null));
        Assertions.assertFalse(game.isInCheck(ChessGame.TeamColor.WHITE),
                "White is not in check but isInCheck returned true");
        /*
        |r|n| |q|k|b| |r|
		|p| |p|p|p|p|p|p|
		|b|p| | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P| | |B| |N| | |
		|R|P|P|n| |P|P|P|
		| |N|B|Q|K| | |R|
         */

        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(1, 8), TestFactory.getNewPosition(1, 7), null));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(2, 4), TestFactory.getNewPosition(3, 6), null));
        Assertions.assertTrue(game.isInCheck(ChessGame.TeamColor.WHITE),
                "White is in check but isInCheck returned false");
        /*
        |r|n| |q|k|b| |r|
		|p| |p|p|p|p|p|p|
		|b|p| | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P| | |B| |n| | |
		|R|P|P| | |P|P|P|
		| |N|B|Q|K| |R| |
         */

        //try not getting out of check
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                TestFactory.getNewMove(TestFactory.getNewPosition(1, 7), TestFactory.getNewPosition(1, 8), null)));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(2, 7), TestFactory.getNewPosition(3, 6), null));
        Assertions.assertFalse(game.isInCheck(ChessGame.TeamColor.WHITE),
                "White is not in check but isInCheck returned true");
        /*
        |r|n| |q|k|b| |r|
		|p| |p|p|p|p|p|p|
		|b|p| | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		|P| | |B| |P| | |
		|R|P|P| | |P| |P|
		| |N|B|Q|K| |R| |
         */

        //try double-moving moved pawn
        Assertions.assertThrows(InvalidMoveException.class, () -> game.makeMove(
                TestFactory.getNewMove(TestFactory.getNewPosition(6, 2), TestFactory.getNewPosition(4, 2), null)));

        //few more moves to try
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(6, 2), TestFactory.getNewPosition(5, 2), null));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(1, 7), TestFactory.getNewPosition(1, 8), null));
        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(5, 2), TestFactory.getNewPosition(4, 2), null));
        /*
        |r|n| |q|k|b| |r|
		|p| |p|p|p|p|p|p|
		|b| | | | | | | |
		| | | | | | | | |
		| |p| | | | | | |
		|P| | |B| |P| | |
		|R|P|P| | |P| |P|
		| |N|B|Q|K| | |R|
         */
    }


    @ParameterizedTest
    @EnumSource(value = ChessPiece.PieceType.class, names = {"QUEEN", "ROOK", "KNIGHT", "BISHOP"})
    @DisplayName("Pawn Promotion")
    public void promotionMoves(ChessPiece.PieceType promotionType) throws InvalidMoveException {

        board.addPiece(TestFactory.getNewPosition(7, 3),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        board.addPiece(TestFactory.getNewPosition(2, 5),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));

        board.addPiece(TestFactory.getNewPosition(1, 6),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));

        game.setBoard(board);
        game.setTeamTurn(ChessGame.TeamColor.WHITE);
        /*
        | | | | | | | | |
		| | |P| | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | |p| | | |
		| | | | | |Q| | |
         */

        //White promotion
        ChessMove whitePromotion = TestFactory.getNewMove(TestFactory.getNewPosition(7, 3),
                TestFactory.getNewPosition(8, 3), promotionType);
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
        ChessMove blackPromotion = TestFactory.getNewMove(TestFactory.getNewPosition(2, 5),
                TestFactory.getNewPosition(1, 6), promotionType);
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

        /*
        | | | | | | | |k|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| |K| | | |r| | |
		| | | | | | | | |
		| | | | | | | | |
         */


        //white king
        board.addPiece(TestFactory.getNewPosition(3, 2),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));

        //black king
        board.addPiece(TestFactory.getNewPosition(8, 8),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));

        //threatening piece
        board.addPiece(TestFactory.getNewPosition(3, 6),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        //set up game
        game = TestFactory.getNewGame();
        game.setBoard(board);
        game.setTeamTurn(ChessGame.TeamColor.WHITE);

        Assertions.assertTrue(game.isInCheck(ChessGame.TeamColor.WHITE),
                "White is in check but isInCheck returned false");
        Assertions.assertFalse(game.isInCheck(ChessGame.TeamColor.BLACK),
                "Black is not in check but isInCheck returned true");
    }


    @Test
    @DisplayName("Black in Check")
    public void blackCheck() {

        /*
        | | | |K| | | | |
		| | | | | | | | |
		| | | |k| | | | |
		| | | | | | | | |
		| | | | | | | | |
		|B| | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */


        //black king
        board.addPiece(TestFactory.getNewPosition(6, 4),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));

        //white king
        board.addPiece(TestFactory.getNewPosition(8, 4),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));

        //threatening piece
        board.addPiece(TestFactory.getNewPosition(3, 1),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));

        //set up game
        game = TestFactory.getNewGame();
        game.setBoard(board);

        Assertions.assertTrue(game.isInCheck(ChessGame.TeamColor.BLACK),
                "Black is in check but isInCheck returned false");
        Assertions.assertFalse(game.isInCheck(ChessGame.TeamColor.WHITE),
                "White is not in check but isInCheck returned true");
    }


    @Test
    @DisplayName("White in Checkmate")
    public void whiteTeamCheckmate() {

        /*
        | | | | | | | | |
		| | |b|q| | | | |
		| | | | | | | | |
		| | | |p| | | |k|
		| | | | | |K| | |
		| | |r| | | | | |
		| | | | |n| | | |
		| | | | | | | | |
         */


        //white king
        board.addPiece(TestFactory.getNewPosition(4, 6),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));

        //black pieces
        board.addPiece(TestFactory.getNewPosition(3, 3),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        board.addPiece(TestFactory.getNewPosition(7, 3),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        board.addPiece(TestFactory.getNewPosition(5, 4),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        board.addPiece(TestFactory.getNewPosition(7, 4),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        board.addPiece(TestFactory.getNewPosition(2, 5),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        board.addPiece(TestFactory.getNewPosition(5, 8),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));

        //set up game
        game = TestFactory.getNewGame();
        game.setBoard(board);

        Assertions.assertTrue(game.isInCheckmate(ChessGame.TeamColor.WHITE),
                "White is in checkmate but isInCheckmate returned false");
        Assertions.assertFalse(game.isInCheckmate(ChessGame.TeamColor.BLACK),
                "Black is not in checkmate but isInCheckmate returned true");
    }


    @Test
    @DisplayName("Black in Checkmate by Pawns")
    public void blackTeamPawnCheckmate() {

        /*
        | | | |k| | | | |
		| | | |P|P| | | |
		| |P| | |P|P| | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | |K| | | | |
         */

        //black king
        board.addPiece(TestFactory.getNewPosition(8, 4),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));

        //white king
        board.addPiece(TestFactory.getNewPosition(1, 4),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));

        //pawns
        board.addPiece(TestFactory.getNewPosition(6, 2),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        board.addPiece(TestFactory.getNewPosition(7, 4),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        board.addPiece(TestFactory.getNewPosition(6, 5),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        board.addPiece(TestFactory.getNewPosition(7, 5),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        board.addPiece(TestFactory.getNewPosition(6, 6),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));

        //set up game
        game = TestFactory.getNewGame();
        game.setBoard(board);

        Assertions.assertTrue(game.isInCheckmate(ChessGame.TeamColor.BLACK),
                "Black is in checkmate but isInCheckmate returned false");
        Assertions.assertFalse(game.isInCheckmate(ChessGame.TeamColor.WHITE),
                "White is not in checkmate but isInCheckmate returned true");

    }


    @Test
    @DisplayName("Pinned King Causes Stalemate")
    public void stalemate() {

        /*
        |k| | | | | | | |
		| | | | | | | |r|
		| | | | | | | | |
		| | | | |q| | | |
		| | | |n| | |K| |
		| | | | | | | | |
		| | | | | | | | |
		| | | | |b| | | |
         */


        //white king
        board.addPiece(TestFactory.getNewPosition(4, 7),
                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));

        //black king
        board.addPiece(TestFactory.getNewPosition(8, 1),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));

        //pieces pinning king
        board.addPiece(TestFactory.getNewPosition(4, 4),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        board.addPiece(TestFactory.getNewPosition(1, 5),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        board.addPiece(TestFactory.getNewPosition(5, 5),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        board.addPiece(TestFactory.getNewPosition(7, 8),
                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        //set up game
        game = TestFactory.getNewGame();
        game.setBoard(board);
        game.setTeamTurn(ChessGame.TeamColor.WHITE);

        Assertions.assertTrue(game.isInStalemate(ChessGame.TeamColor.WHITE),
                "White is in a stalemate but isInStalemate returned false");
        Assertions.assertFalse(game.isInStalemate(ChessGame.TeamColor.BLACK),
                "Black is not in a stalemate but isInStalemate returned true");
    }


    @Test
    @DisplayName("Full Game Checkmate")
    public void scholarsMate() throws InvalidMoveException {
        board.resetBoard();
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

        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(2, 5), TestFactory.getNewPosition(4, 5), null));
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

        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(7, 5), TestFactory.getNewPosition(5, 5), null));
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

        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(1, 6), TestFactory.getNewPosition(4, 3), null));
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

        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(8, 7), TestFactory.getNewPosition(6, 6), null));
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

        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(1, 4), TestFactory.getNewPosition(5, 8), null));
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

        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(8, 2), TestFactory.getNewPosition(6, 3), null));
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

        game.makeMove(TestFactory.getNewMove(TestFactory.getNewPosition(5, 8), TestFactory.getNewPosition(7, 6), null));
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

        private Set<ChessMove> validMoves;

        @BeforeEach
        public void setup() {
            board = TestFactory.getNewBoard();
            game = TestFactory.getNewGame();
            validMoves = new HashSet<>();
        }


        @Test
        @DisplayName("Check Forces Movement")
        public void forcedMove() {

        /*
        | | | | | | | | |
        | | | | | | | | |
        | |B| | | | | | |
        | | | | | |K| | |
        | | |n| | | | | |
        | | | | | | | | |
        | | | |q| |k| | |
        | | | | | | | | |
         */

            ChessPiece knight = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
            ChessPosition knightPosition = TestFactory.getNewPosition(4, 3);
            board.addPiece(knightPosition, knight);

            ChessPiece queen = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
            ChessPosition queenPosition = TestFactory.getNewPosition(2, 4);
            board.addPiece(queenPosition, queen);

            //vulnerable team king
            board.addPiece(TestFactory.getNewPosition(2, 6),
                    TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));

            //bishop threatening king
            board.addPiece(TestFactory.getNewPosition(6, 2),
                    TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));

            //enemy king
            board.addPiece(TestFactory.getNewPosition(5, 6),
                    TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));

            game.setBoard(board);

            //knight valid moves
            validMoves.add(TestFactory.getNewMove(knightPosition,
                    TestFactory.getNewPosition(3, 5), null));  //defend king
            validMoves.add(TestFactory.getNewMove(knightPosition, 
                    TestFactory.getNewPosition(6, 2), null)); //capture bishop

            Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(knightPosition));
            Assertions.assertEquals(validMoves, gameMoves,
                    "ChessGame validMoves did not return the correct moves");

            //queen valid moves
            validMoves.clear();
            validMoves.add(TestFactory.getNewMove(queenPosition, 
                    TestFactory.getNewPosition(3, 5), null));
            validMoves.add(TestFactory.getNewMove(queenPosition, 
                    TestFactory.getNewPosition(4, 4), null));

            gameMoves = new HashSet<>(game.validMoves(queenPosition));
            Assertions.assertEquals(validMoves, gameMoves,
                    "ChessGame validMoves did not return the correct moves");

        }


        @Test
        @DisplayName("Piece Partially Trapped")
        public void moveIntoCheck() {

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| |r| | | |R| |K|
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
         */


            ChessPiece rook = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
            ChessPosition rookPosition = TestFactory.getNewPosition(5, 6);
            board.addPiece(rookPosition, rook);

            //Enemy Rook causing -xray check (white rook can't move out of line)
            board.addPiece(TestFactory.getNewPosition(5, 2),
                    TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
            board.addPiece(TestFactory.getNewPosition(5, 8),
                    TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));

            
            validMoves.add(TestFactory.getNewMove(rookPosition, TestFactory.getNewPosition(5, 7), null));
            validMoves.add(TestFactory.getNewMove(rookPosition, TestFactory.getNewPosition(5, 5), null));
            validMoves.add(TestFactory.getNewMove(rookPosition, TestFactory.getNewPosition(5, 4), null));
            validMoves.add(TestFactory.getNewMove(rookPosition, TestFactory.getNewPosition(5, 3), null));
            validMoves.add(TestFactory.getNewMove(rookPosition, TestFactory.getNewPosition(5, 2), null));


            //check
            game.setBoard(board);
            Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(rookPosition));
            Assertions.assertEquals(validMoves, gameMoves,
                    "ChessGame validMoves did not return the correct moves");
        }


        @Test
        @DisplayName("Piece Completely Trapped")
        public void rookPinnedToKing() {

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

            ChessPosition position = TestFactory.getNewPosition(4, 4);
            ChessPiece rook = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

            board.addPiece(position, rook);

            //queen pinning rook to king
            board.addPiece(TestFactory.getNewPosition(8, 8),
                    TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
            board.addPiece(TestFactory.getNewPosition(2, 2),
                    TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));

            game.setBoard(board);
            Assertions.assertTrue(game.validMoves(position).isEmpty(),
                    "ChessGame validMoves returned valid moves for a trapped piece");
        }


        @Test
        @DisplayName("Pieces Cannot Eliminate Check")
        public void kingInDanger() {

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

            ChessPiece king = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
            ChessPosition kingPosition = TestFactory.getNewPosition(7, 4);
            board.addPiece(kingPosition, king);


            //enemy pieces
            //queen blocking movement
            board.addPiece(TestFactory.getNewPosition(5, 3),
                    TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));

            //pawn threatening king
            board.addPiece(TestFactory.getNewPosition(6, 5),
                    TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));

            //rook blocking top row
            board.addPiece(TestFactory.getNewPosition(8, 1),
                    TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));


            //team pieces
            //get pieces
            ChessPiece pawn = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            ChessPiece bishop = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
            ChessPiece queen = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
            ChessPiece knight = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
            ChessPiece rook = TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

            //get positions
            ChessPosition pawnPosition = TestFactory.getNewPosition(2, 6);
            ChessPosition bishopPosition = TestFactory.getNewPosition(7, 8);
            ChessPosition queenPosition = TestFactory.getNewPosition(1, 2);
            ChessPosition knightPosition = TestFactory.getNewPosition(5, 4);
            ChessPosition rookPosition = TestFactory.getNewPosition(3, 8);

            //add to board
            board.addPiece(pawnPosition, pawn);
            board.addPiece(bishopPosition, bishop);
            board.addPiece(queenPosition, queen);
            board.addPiece(knightPosition, knight);
            board.addPiece(rookPosition, rook);

            //king expected moves
            validMoves.add(TestFactory.getNewMove(kingPosition, TestFactory.getNewPosition(6, 5), null));

            //check king moves
            game.setBoard(board);
            Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(kingPosition));
            Assertions.assertEquals(validMoves, gameMoves,
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

        /*
        | | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | | | | |
		| | | | | |k| | |
		| | | | | | | | |
		| | | | | |K| | |
		| | | | | | | | |
         */

            ChessPiece king = TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
            ChessPosition position = TestFactory.getNewPosition(2, 6);

            board.addPiece(position, king);

            board.addPiece(TestFactory.getNewPosition(4, 6),
                    TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));


            //can't move towards enemy king
            validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 5), null));
            validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 6), null));
            validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(1, 7), null));
            validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 5), null));
            validMoves.add(TestFactory.getNewMove(position, TestFactory.getNewPosition(2, 7), null));

            game.setBoard(board);
            Set<ChessMove> gameMoves = new HashSet<>(game.validMoves(position));
            Assertions.assertEquals(validMoves, gameMoves,
                    "ChessGame validMoves did not return the correct moves");
        }

    }

}

