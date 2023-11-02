package client;

import org.junit.jupiter.api.*;
import server.PetServer;
import util.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;

class ServerFacadeTest {

    static private PetServer petServer;
    static ServerFacade server;

    @BeforeAll
    static void startServer() {
        petServer = new PetServer();
        petServer.run(0);
        var url = "http://localhost:" + petServer.port();
        server = new ServerFacade(url);
    }

    @AfterAll
    static void stopServer() {
        petServer.stop();
    }

    @BeforeEach
    void clear() {
        var result = assertDoesNotThrow(() -> server.deletePet("*"));
        assertEquals(q("{'pet':[]}"), result);
    }

    @Test
    void addPet() {
        var result = assertDoesNotThrow(() -> server.addPet("joe"));
        assertEquals(q("{'pet':['joe']}"), result);

        result = assertDoesNotThrow(() -> server.addPet("sally"));
        assertEquals(q("{'pet':['joe','sally']}"), result);
    }


    @Test
    void deletePet() {
        assertDoesNotThrow(() -> server.addPet("joe"));
        assertDoesNotThrow(() -> server.addPet("sally"));

        var result = assertDoesNotThrow(() -> server.deletePet("joe"));
        assertEquals(q("{'pet':['sally']}"), result);
    }

    @Test
    void deleteNonexistentPet() {
        assertDoesNotThrow(() -> server.addPet("joe"));
        assertDoesNotThrow(() -> server.addPet("sally"));

        var result = assertDoesNotThrow(() -> server.deletePet("buddy"));
        assertEquals(q("{'pet':['joe','sally']}"), result);
    }


    @Test
    void listPet() {
        assertDoesNotThrow(() -> server.addPet("joe"));
        assertDoesNotThrow(() -> server.addPet("sally"));

        var result = assertDoesNotThrow(() -> server.listPets());
        assertEquals(q("{'pet':['joe','sally']}"), result);
    }

    private String q(String in) {
        return in.replace('\'', '"');
    }
}