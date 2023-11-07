package client;

import dataaccess.MemoryDataAccess;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.PetServer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class PetClientTest {

    static private PetServer petServer;
    static PetClient client;

    @BeforeAll
    static void startServer() throws Exception {
        petServer = new PetServer(new MemoryDataAccess());
        petServer.run(0);
        var url = "http://localhost:" + petServer.port();
        client = new PetClient(url, null);
        client.signin("tester");
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
    void rescuePet() {
        var result = assertDoesNotThrow(() -> client.rescuePet("joe", "fish"));
        assertMatches("You rescued joe. Assigned ID: \\d+", result);

        result = assertDoesNotThrow(() -> client.rescuePet("sally", "cat"));
        assertMatches("You rescued sally. Assigned ID: \\d+", result);
    }


    @Test
    void rescuePetWithFriends() {
        assertDoesNotThrow(() -> client.rescuePet("joe", "fish", "a", "b"));
        var result = assertDoesNotThrow(() -> client.listPets());
        assertMatches("""
                \\{'id':\\d+,'name':'joe','type':'FISH','friends':\\{'list':\\['a','b']}}
                """, result);
    }

    @Test
    void adoptPet() throws Exception {
        var id = getId(client.rescuePet("joe", "frog"));
        client.rescuePet("sally", "cat");

        var result = assertDoesNotThrow(() -> client.adoptPet(id));
        assertEquals("joe says ribbit", result);
    }


    @Test
    void adoptAllPets() throws Exception {
        client.rescuePet("joe", "rock");
        client.rescuePet("pat", "cat");
        var result = assertDoesNotThrow(() -> client.adoptAllPets());
        assertEquals("joe says roll\npat says meow\n", result);
        assertEquals("", client.listPets());
    }

    @Test
    void deleteNonexistentPet() {
        assertThrows(ResponseException.class, () -> client.adoptPet("933432"));
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

    private static String getId(String text) {
        Pattern p = Pattern.compile("ID: (\\d+)");
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(1);
        }
        fail("ID not found");
        return "";
    }
}