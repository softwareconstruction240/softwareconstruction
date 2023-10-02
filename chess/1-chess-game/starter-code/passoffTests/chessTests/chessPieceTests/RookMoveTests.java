package passoffTests.chessTests.chessPieceTests;

import org.junit.jupiter.api.Test;

import static passoffTests.TestFactory.*;

public class RookMoveTests {

    @Test
    public void rookMoveUntilEdge() {

        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | |R| | | | | |
                        | | | | | | | | |
                        """,
                getNewPosition(2, 3),
                new int[][]{
                        {2, 4}, {2, 5}, {2, 6}, {2, 7}, {2, 8},
                        {2, 2}, {2, 1},
                        {1, 3},
                        {3, 3}, {4, 3}, {5, 3}, {6, 3}, {7, 3}, {8, 3},
                }
        );
    }


    @Test
    public void bishopCaptureEnemy() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        |N| | | | | | | |
                        |r| | | | |B| | |
                        | | | | | | | | |
                        |q| | | | | | | |
                        | | | | | | | | |
                        """,
                getNewPosition(4, 1),
                new int[][]{
                        {5, 1},
                        {3, 1},
                        {4, 2}, {4, 3}, {4, 4}, {4, 5}, {4, 6},
                }
        );
    }


    @Test
    public void rookBlocked() {
        validateMoves("""
                        | | | | | | |n|r|
                        | | | | | | | |p|
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                getNewPosition(8, 8),
                new int[][]{}
        );
    }

}
