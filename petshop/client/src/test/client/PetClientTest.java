package client;

import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.*;
import server.PetServer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class PetClientTest {

    static private PetServer petServer;
    static PetClient client;

    @BeforeAll
    static void startServer() {
        petServer = new PetServer(new MemoryDataAccess());
        petServer.run(0);
        var url = "http://localhost:" + petServer.port();
        client = new PetClient(url);
    }

    @AfterAll
    static void stopServer() {
        petServer.stop();
    }

    @BeforeEach
    void clear() throws Exception {
        client.adoptAllPets();
    }

    @Test
    void addPet() {
        var result = assertDoesNotThrow(() -> client.rescuePet("joe", "fish"));
        assertMatches("\\{'id':\\d+,'name':'joe','type':'FISH','friends':\\{'list':\\[]}}", result);

        result = assertDoesNotThrow(() -> client.rescuePet("sally", "cat"));
        assertMatches("\\{'id':\\d+,'name':'sally','type':'CAT','friends':\\{'list':\\[]}}", result);
    }


    @Test
    void addPetWithFriends() {
        var result = assertDoesNotThrow(() -> client.rescuePet("joe", "fish", "a", "b"));
        assertMatches("\\{'id':\\d+,'name':'joe','type':'FISH','friends':\\{'list':\\['a','b']}}", result);
    }

    @Test
    void deletePet() throws Exception {
        var id = getId(client.rescuePet("joe", "frog"));
        client.rescuePet("sally", "cat");

        var result = assertDoesNotThrow(() -> client.adoptPet(id));
        assertEquals("Deleted " + id, result);
    }


    @Test
    void deleteAllPets() throws Exception {
        client.rescuePet("joe", "rock");
        var result = assertDoesNotThrow(() -> client.adoptAllPets());
        assertEquals("Deleted all", result);
        assertEquals(q(""), client.listPets());
    }

    @Test
    void deleteNonexistentPet() {
        var result = assertDoesNotThrow(() -> client.adoptPet("933432"));
        assertEquals(q("Deleted 933432"), result);
    }


    @Test
    void listPet() {
        assertDoesNotThrow(() -> client.rescuePet("joe", "fish"));
        assertDoesNotThrow(() -> client.rescuePet("sally", "fish"));

        var result = assertDoesNotThrow(() -> client.listPets());
        assertMatches("""
                \\{'id':\\d+,'name':'joe','type':'FISH','friends':\\{'list':\\[]}}
                \\{'id':\\d+,'name':'sally','type':'FISH','friends':\\{'list':\\[]}}
                """, result);
    }

    private void assertMatches(String expected, String actual) {
        actual = actual.replace('"', '\'');

        assertTrue(actual.matches(expected), actual);
    }

    private static String q(String in) {
        return in.replace('\'', '"');
    }

    private static String getId(String text) {
        Pattern p = Pattern.compile("'id':(\\d+),");
        text = text.replace('"', '\'');
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(1);
        }
        fail("ID not found");
        return "";
    }
}