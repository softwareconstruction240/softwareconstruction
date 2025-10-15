package server;

import dataaccess.MemoryDataAccess;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import service.PetService;

import java.util.Collection;

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
        Pet result = assertDoesNotThrow(() -> server.addPet(joe));
        assertPetEqual(joe, result);
    }

    @Test
    void deletePet() throws Exception {
        var expected = new PetList();
        expected.add(server.addPet(new Pet(0, "sally", PetType.CAT)));

        Pet joe = server.addPet(new Pet(0, "joe", PetType.CAT));
        server.deletePet(joe.id());

        PetList result = assertDoesNotThrow(() -> server.listPets());
        assertPetCollectionEqual(expected, result);
    }

    @Test
    void listPets() throws Exception {
        var expected = new PetList();
        expected.add(server.addPet(new Pet(0, "joe", PetType.CAT)));
        expected.add(server.addPet(new Pet(0, "sally", PetType.CAT)));

        var result = assertDoesNotThrow(() -> server.listPets());
        assertPetCollectionEqual(expected, result);
    }

    @Test
    void invalidPetAddition() {
        // Dogs with fleas are not allowed
        ResponseException error = Assertions.assertThrows(ResponseException.class,
                () -> server.addPet(new Pet(-1, "fleas", PetType.DOG)),
                "Inserting an invalid pet should throw an error");

        Assertions.assertEquals(ResponseException.Code.ClientError, error.code());
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
