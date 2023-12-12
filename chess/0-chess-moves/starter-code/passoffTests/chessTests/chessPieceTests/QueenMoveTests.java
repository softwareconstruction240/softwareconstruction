package passoffTests.chessTests.chessPieceTests;

import org.junit.jupiter.api.Test;

import static passoffTests.TestFactory.*;

public class QueenMoveTests {
    @Test
    public void queenMoveUntilEdge() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | |q| |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                startPosition(7, 7),
                endPositions(new int[][]{
                        {8, 7},
                        {8, 8},
                        {7, 8},
                        {6, 8},
                        {6, 7}, {5, 7}, {4, 7}, {3, 7}, {2, 7}, {1, 7},
                        {6, 6}, {5, 5}, {4, 4}, {3, 3}, {2, 2}, {1, 1},
                        {7, 6}, {7, 5}, {7, 4}, {7, 3}, {7, 2}, {7, 1},
                        {8, 6},
                })
        );
    }


    @Test
    public void queenCaptureEnemy() {
        validateMoves("""
                        |b| | | | | | | |
                        | | | | | | | | |
                        | | |R| | | | | |
                        | | | | | | | | |
                        |Q| | |p| | | | |
                        | | | | | | | | |
                        |P| |n| | | | | |
                        | | | | | | | | |
                        """,
                startPosition(4, 1),
                endPositions(new int[][]{
                        {5, 1}, {6, 1}, {7, 1}, {8, 1},
                        {5, 2},
                        {4, 2}, {4, 3}, {4, 4},
                        {3, 1}, {3, 2},
                        {2, 3},
                })
        );
    }


    @Test
    public void queenBlocked() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        |P|R| | | | | | |
                        |Q|K| | | | | | |
                        """,
                startPosition(1, 1),
                endPositions(new int[][]{})
        );
    }
}
