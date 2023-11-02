package client;

import org.junit.jupiter.api.*;
import server.PetServer;

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
        assertEquals(q("{'id':1,'name':'joe','type':'FISH','friends':{'list':[]}}"), result);

        result = assertDoesNotThrow(() -> client.addPet("sally"));
        assertEquals(q("{'pet':['joe','sally']}"), result);
    }


    @Test
    void deletePet() throws Exception {
        client.addPet("joe");
        client.addPet("sally");

        var result = assertDoesNotThrow(() -> client.deletePet("joe"));
        assertEquals(q("{'pet':['sally']}"), result);
    }


    @Test
    void deleteAllPets() throws Exception {
        client.addPet("joe", "rock");
        var result = assertDoesNotThrow(() -> client.deleteAllPets());
        assertEquals("Deleted all", result);

        var listResult = client.listPets();
        assertEquals(q(""), listResult);
    }

    @Test
    void deleteNonexistentPet() {
        assertDoesNotThrow(() -> client.addPet("joe"));
        assertDoesNotThrow(() -> client.addPet("sally"));

        var result = assertDoesNotThrow(() -> client.deletePet("buddy"));
        assertEquals(q("{'pet':['joe','sally']}"), result);
    }


    @Test
    void listPet() {
        assertDoesNotThrow(() -> client.addPet("joe"));
        assertDoesNotThrow(() -> client.addPet("sally"));

        var result = assertDoesNotThrow(() -> client.listPets());
        assertEquals(q("{'pet':['joe','sally']}"), result);
    }

    private String q(String in) {
        return in.replace('\'', '"');
    }
}