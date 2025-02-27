import dataaccess.MemoryDataAccess;
import exception.ResponseException;
import model.Pet;
import model.PetType;
import org.junit.jupiter.api.*;
import server.PetServer;
import server.ServerFacade;
import service.PetService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PetServerTest {
    static private PetServer petServer;
    static ServerFacade server;

    @BeforeAll
    static void startServer() {
        var service = new PetService(new MemoryDataAccess());
        petServer = new PetServer(service);
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
        assertDoesNotThrow(() -> server.deleteAllPets());
    }

    @Test
    void addPet() {
        var joe = new Pet(0, "joe", PetType.CAT);
        var result = assertDoesNotThrow(() -> server.addPet(joe));
        assertPetEqual(joe, result);
    }

    @Test
    void deletePet() throws Exception {
        var expected = new ArrayList<Pet>();
        expected.add(server.addPet(new Pet(0, "sally", PetType.CAT)));

        var joe = server.addPet(new Pet(0, "joe", PetType.CAT));
        server.deletePet(joe.id());

        var result = assertDoesNotThrow(() -> server.listPets());
        assertPetCollectionEqual(expected, List.of(result.pets()));
    }

    @Test
    void listPets() throws Exception {
        var expected = new ArrayList<Pet>();
        expected.add(server.addPet(new Pet(0, "joe", PetType.CAT)));
        expected.add(server.addPet(new Pet(0, "sally", PetType.CAT)));

        var result = assertDoesNotThrow(() -> server.listPets());
        assertPetCollectionEqual(expected, List.of(result.pets()));
    }

    @Test
    void invalidPetAddition() throws Exception {
        // Dogs with fleas are not allowed
        ResponseException error = Assertions.assertThrows(ResponseException.class,
                () -> server.addPet(new Pet(-1, "fleas", PetType.DOG)),
                "Inserting an invalid pet should throw an error");

        Assertions.assertEquals(400, error.StatusCode());
        Assertions.assertEquals("Error: no dogs with fleas", error.getMessage());
    }

    public static void assertPetEqual(Pet expected, Pet actual) {
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.type(), actual.type());
    }

    public static void assertPetCollectionEqual(Collection<Pet> expected, Collection<Pet> actual) {
        Pet[] actualList = actual.toArray(new Pet[]{});
        Pet[] expectedList = expected.toArray(new Pet[]{});
        assertEquals(expectedList.length, actualList.length);
        for (var i = 0; i < actualList.length; i++) {
            assertPetEqual(expectedList[i], actualList[i]);
        }
    }
}
