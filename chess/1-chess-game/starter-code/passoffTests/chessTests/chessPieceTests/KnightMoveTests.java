package passoffTests.chessTests.chessPieceTests;

import org.junit.jupiter.api.Test;

import static passoffTests.TestFactory.*;

public class KnightMoveTests {

    @Test
    public void knightMiddleOfBoardWhite() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | |N| | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                getNewPosition(5, 5),
                new int[][]{
                        {7, 6}, {6, 7}, {4, 7}, {3, 6}, {3, 4}, {4, 3}, {6, 3}, {7, 4},
                }
        );
    }

    @Test
    public void knightMiddleOfBoardBlack() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | |n| | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                getNewPosition(5, 5),
                new int[][]{
                        {7, 6}, {6, 7}, {4, 7}, {3, 6}, {3, 4}, {4, 3}, {6, 3}, {7, 4},
                }
        );
    }


    @Test
    public void knightEdgeOfBoardLeft() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        |n| | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                getNewPosition(4, 1),
                new int[][]{{6, 2}, {5, 3}, {3, 3}, {2, 2}}
        );
    }

    @Test
    public void knightEdgeOfBoardRight() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | |n|
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                getNewPosition(3, 8),
                new int[][]{{1, 7}, {2, 6}, {4, 6}, {5, 7}}
        );
    }

    @Test
    public void knightEdgeOfBoardBottom() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | |N| | |
                        """,
                getNewPosition(1, 6),
                new int[][]{{2, 4}, {3, 5}, {3, 7}, {2, 8}}
        );
    }

    @Test
    public void knightEdgeOfBoardTop() {
        validateMoves("""
                        | | |N| | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                getNewPosition(8, 3),
                new int[][]{{7, 5}, {6, 4}, {6, 2}, {7, 1}}
        );
    }


    @Test
    public void knightCornerOfBoardBottomRight() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | |N|
                        """,
                getNewPosition(1, 8),
                new int[][]{{2, 6}, {3, 7}}
        );
    }

    @Test
    public void knightCornerOfBoardTopRight() {
        validateMoves("""
                        | | | | | | | |N|
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                getNewPosition(8, 8),
                new int[][]{{6, 7}, {7, 6}}
        );
    }

    @Test
    public void knightCornerOfBoardTopLeft() {
        validateMoves("""
                        |n| | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                getNewPosition(8, 1),
                new int[][]{{7, 3}, {6, 2}}
        );
    }

    @Test
    public void knightCornerOfBoardBottomLeft() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        |n| | | | | | | |
                        """,
                getNewPosition(1, 1),
                new int[][]{{2, 3}, {3, 2}}
        );
    }


    @Test
    public void knightBlocked() {
        validateMoves("""
                        | | | | | | | | |
                        | | | |R| | | | |
                        | | | | | | |P| |
                        | | | | |N| | | |
                        | | |N| | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                getNewPosition(5, 5),
                new int[][]{{3, 4}, {3, 6}, {4, 7}, {7, 6}, {6, 3}}
        );
    }


    @Test
    public void knightCaptureEnemy() {
        validateMoves("""
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | |n| | | |
                        | | |N| | | | | |
                        | | | |P| |R| | |
                        | | | | | | | | |
                        | | | | | | | | |
                        """,
                getNewPosition(5, 5),
                new int[][]{{7, 6}, {6, 7}, {4, 7}, {3, 6}, {3, 4}, {4, 3}, {6, 3}, {7, 4}}
        );
    }
}
