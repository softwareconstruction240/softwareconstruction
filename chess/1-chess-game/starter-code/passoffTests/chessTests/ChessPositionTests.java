package passoffTests.chessTests;

import chess.*;
import org.junit.jupiter.api.*;
import passoffTests.TestFactory;

import java.util.HashSet;
import java.util.Set;

public class ChessPositionTests {
    private ChessPosition original;
    private ChessPosition equal;
    private ChessPosition different;
    @BeforeEach
    public void setUp() {
        original = TestFactory.getNewPosition(3, 7);
        equal = TestFactory.getNewPosition(3, 7);
        different = TestFactory.getNewPosition(7, 3);
    }

    @Test
    @DisplayName("Equals Testing")
    public void equalsTest() {
        Assertions.assertEquals(original, equal, "equals returned false for equal positions");
        Assertions.assertNotEquals(original, different, "equals returned true for different positions");
    }

    @Test
    @DisplayName("HashCode Testing")
    public void hashTest() {
        Assertions.assertEquals(original.hashCode(), equal.hashCode(),
                "hashCode returned different values for equal positions");
        Assertions.assertNotEquals(original.hashCode(), different.hashCode(),
                "hashCode returned the same value for different positions");
    }

    @Test
    @DisplayName("Combined Testing")
    public void hashSetTest() {
        Set<ChessPosition> set = new HashSet<>();
        set.add(original);

        Assertions.assertTrue(set.contains(original));
        Assertions.assertTrue(set.contains(equal));
        Assertions.assertEquals(1, set.size());
        set.add(equal);
        Assertions.assertEquals(1, set.size());

        Assertions.assertFalse(set.contains(different));
        set.add(different);
        Assertions.assertEquals(2, set.size());


    }

}
