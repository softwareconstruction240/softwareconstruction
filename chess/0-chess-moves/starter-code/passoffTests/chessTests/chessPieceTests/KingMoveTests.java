package passoffTests.chessTests.chessPieceTests;

import org.junit.jupiter.api.Test;

import static passoffTests.TestFactory.*;


public class KingMoveTests {

    @Test
    public void kingMoveUntilEdge() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | |K| | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                startPosition(3, 6),
                endPositions(new int[][]{{4, 6}, {4, 7}, {3, 7}, {2, 7}, {2, 6}, {2, 5}, {3, 5}, {4, 5}})
        );
    }


    @Test
    public void kingCaptureEnemy() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | |N|n| | | |
                        | | | |k| | | | |
                        | | |P|b|p| | | |
                        | | | | | | | | |
                        """,
                startPosition(3, 4),
                endPositions(new int[][]{{4, 4}, {3, 5}, {2, 3}, {3, 3}, {4, 3}})
        );
    }


    @Test
    public void kingBlocked() {
        validateMoves("""
                        | | | | | | |r|k|
                        | | | | | | |p|p|
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                startPosition(8, 8),
                endPositions(new int[][]{})
        );
    }

}
