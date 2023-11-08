package dataaccess;

import exception.ResponseException;
import model.ArrayFriendList;
import model.Pet;
import model.PetType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessTest {
    public static MySqlDataAccessConfig testMySqlConfig = new MySqlDataAccessConfig("jdbc:mysql://localhost:3306", "root", "monkeypie", "testpetstore");

    private DataAccess getDataAccess(Class<? extends DataAccess> databaseClass) throws ResponseException {
        DataAccess db;
        if (databaseClass.equals(MySqlDataAccess.class)) {
            db = new MySqlDataAccess(testMySqlConfig);
        } else {
            db = new MemoryDataAccess();
        }
        db.deleteAllPets();
        return db;
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, MemoryDataAccess.class})
    void addPet(Class<? extends DataAccess> dbClass) throws ResponseException {
        DataAccess dataAccess = getDataAccess(dbClass);

        var pet = new Pet(0, "joe", PetType.FISH, new ArrayFriendList("a", "b"));
        assertDoesNotThrow(() -> dataAccess.addPet(pet));
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, MemoryDataAccess.class})
    void listPets(Class<? extends DataAccess> dbClass) throws ResponseException {
        DataAccess dataAccess = getDataAccess(dbClass);

        List<Pet> expected = new ArrayList<>();
        expected.add(dataAccess.addPet(new Pet(0, "joe", PetType.FISH, null)));
        expected.add(dataAccess.addPet(new Pet(0, "sally", PetType.CAT, null)));
        expected.add(dataAccess.addPet(new Pet(0, "fido", PetType.DOG, new ArrayFriendList("a", "b"))));

        var actual = dataAccess.listPets();
        assertIterableEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, MemoryDataAccess.class})
    void deletePet(Class<? extends DataAccess> dbClass) throws ResponseException {
        DataAccess dataAccess = getDataAccess(dbClass);

        List<Pet> expected = new ArrayList<>();
        var deletePet = dataAccess.addPet(new Pet(0, "joe", PetType.FISH, null));
        expected.add(dataAccess.addPet(new Pet(0, "sally", PetType.CAT, null)));
        expected.add(dataAccess.addPet(new Pet(0, "fido", PetType.DOG, new ArrayFriendList("a", "b"))));

        dataAccess.deletePet(deletePet.id());

        var actual = dataAccess.listPets();
        assertIterableEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, MemoryDataAccess.class})
    void deleteAllPets(Class<? extends DataAccess> dbClass) throws Exception {
        DataAccess dataAccess = getDataAccess(dbClass);

        dataAccess.addPet(new Pet(0, "joe", PetType.FISH, null));
        dataAccess.addPet(new Pet(0, "sally", PetType.CAT, null));

        dataAccess.deleteAllPets();

        var actual = dataAccess.listPets();
        assertEquals(0, actual.size());
    }
}