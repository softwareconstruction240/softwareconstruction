package client;

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
        petServer = new PetServer();
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
        client.deleteAllPets();
    }

    @Test
    void addPet() {
        var result = assertDoesNotThrow(() -> client.addPet("joe", "fish"));
        assertMatches("\\{'id':\\d+,'name':'joe','type':'FISH','friends':\\{'list':\\[]}}", result);

        result = assertDoesNotThrow(() -> client.addPet("sally", "cat"));
        assertMatches("\\{'id':\\d+,'name':'sally','type':'CAT','friends':\\{'list':\\[]}}", result);
    }


    @Test
    void addPetWithFriends() {
        var result = assertDoesNotThrow(() -> client.addPet("joe", "fish", "a", "b"));
        assertMatches("\\{'id':\\d+,'name':'joe','type':'FISH','friends':\\{'list':\\['a','b']}}", result);
    }

    @Test
    void deletePet() throws Exception {
        var id = getId(client.addPet("joe", "frog"));
        client.addPet("sally", "cat");

        var result = assertDoesNotThrow(() -> client.deletePet(id));
        assertEquals("Deleted " + id, result);
    }


    @Test
    void deleteAllPets() throws Exception {
        client.addPet("joe", "rock");
        var result = assertDoesNotThrow(() -> client.deleteAllPets());
        assertEquals("Deleted all", result);
        assertEquals(q(""), client.listPets());
    }

    @Test
    void deleteNonexistentPet() {
        var result = assertDoesNotThrow(() -> client.deletePet("933432"));
        assertEquals(q("Deleted 933432"), result);
    }


    @Test
    void listPet() {
        assertDoesNotThrow(() -> client.addPet("joe", "fish"));
        assertDoesNotThrow(() -> client.addPet("sally", "fish"));

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