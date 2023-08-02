package dataaccess;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class DatabaseManagerTest {

    private static DatabaseManager db;

    @BeforeAll
    public static void createDatabase() throws DatabaseException {
        db = new DatabaseManager();

        // If we were testing an existing application, the tables would already exist so we would not have
        // these three lines of code.
        db.openConnection();
        db.createTables();
        db.closeConnection(true);
    }

    @BeforeEach
    public void setUp() throws DatabaseException {
        db.openConnection();
        db.fillDictionary();
    }

    @AfterEach
    public void tearDown() throws DatabaseException {
        db.closeConnection(false);
    }

    @Test
    public void testLoadDictionaryFromDatabase() throws DatabaseException {
        Set<String> words = db.loadDictionary();

        Assertions.assertEquals(4, words.size());
        Assertions.assertTrue(words.contains("fred"));
        Assertions.assertTrue(words.contains("barney"));
        Assertions.assertTrue(words.contains("betty"));
        Assertions.assertTrue(words.contains("wilma"));
    }
}