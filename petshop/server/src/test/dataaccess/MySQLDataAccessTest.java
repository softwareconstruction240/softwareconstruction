package dataaccess;

import exception.ResponseException;
import model.ArrayFriendList;
import model.Pet;
import model.PetType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MySQLDataAccessTest {
    private static MySQLDataAccess dataAccess;

    @BeforeAll
    static void create() throws ResponseException {
        var config = DataAccessConfig.testDefault;
        dataAccess = new MySQLDataAccess(config);
    }

    @AfterEach
    void deleteAll() throws ResponseException {
        dataAccess.deleteAllPets();
    }

    @Test
    void addPet() {
        var pet = new Pet(0, "joe", PetType.FISH, new ArrayFriendList("a", "b"));
        assertDoesNotThrow(() -> dataAccess.addPet(pet));
    }

    @Test
    void listPets() throws ResponseException {
        List<Pet> expected = new ArrayList<>();
        expected.add(dataAccess.addPet(new Pet(0, "joe", PetType.FISH, null)));
        expected.add(dataAccess.addPet(new Pet(0, "sally", PetType.CAT, null)));
        expected.add(dataAccess.addPet(new Pet(0, "fido", PetType.DOG, new ArrayFriendList("a", "b"))));

        var actual = dataAccess.listPets();
        assertIterableEquals(expected, actual);
    }

    @Test
    void deletePet() throws ResponseException {
        List<Pet> expected = new ArrayList<>();
        var deletePet = dataAccess.addPet(new Pet(0, "joe", PetType.FISH, null));
        expected.add(dataAccess.addPet(new Pet(0, "sally", PetType.CAT, null)));
        expected.add(dataAccess.addPet(new Pet(0, "fido", PetType.DOG, new ArrayFriendList("a", "b"))));

        dataAccess.deletePet(deletePet.id());

        var actual = dataAccess.listPets();
        assertIterableEquals(expected, actual);
    }

    @Test
    void deleteAllPets() throws Exception {
        dataAccess.addPet(new Pet(0, "joe", PetType.FISH, null));
        dataAccess.addPet(new Pet(0, "sally", PetType.CAT, null));

        dataAccess.deleteAllPets();

        var actual = dataAccess.listPets();
        assertEquals(0, actual.size());
    }
}