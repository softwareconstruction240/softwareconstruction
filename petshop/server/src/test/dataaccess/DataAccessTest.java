package dataaccess;

import exception.ResponseException;
import model.Pet;
import model.PetType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessTest {

    private DataAccess getDataAccess(Class<? extends DataAccess> databaseClass) throws ResponseException {
        DataAccess db;
        if (databaseClass.equals(MySqlDataAccess.class)) {
            db = new MySqlDataAccess();
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

        var pet = new Pet(0, "joe", PetType.FISH);
        assertDoesNotThrow(() -> dataAccess.addPet(pet));
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, MemoryDataAccess.class})
    void listPets(Class<? extends DataAccess> dbClass) throws ResponseException {
        DataAccess dataAccess = getDataAccess(dbClass);

        List<Pet> expected = new ArrayList<>();
        expected.add(dataAccess.addPet(new Pet(0, "joe", PetType.FISH)));
        expected.add(dataAccess.addPet(new Pet(0, "sally", PetType.CAT)));
        expected.add(dataAccess.addPet(new Pet(0, "fido", PetType.DOG)));

        var actual = dataAccess.listPets();
        assertPetCollectionEqual(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, MemoryDataAccess.class})
    void deletePet(Class<? extends DataAccess> dbClass) throws ResponseException {
        DataAccess dataAccess = getDataAccess(dbClass);

        List<Pet> expected = new ArrayList<>();
        var deletePet = dataAccess.addPet(new Pet(0, "joe", PetType.FISH));
        expected.add(dataAccess.addPet(new Pet(0, "sally", PetType.CAT)));
        expected.add(dataAccess.addPet(new Pet(0, "fido", PetType.DOG)));

        dataAccess.deletePet(deletePet.id());

        var actual = dataAccess.listPets();
        assertPetCollectionEqual(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(classes = {MySqlDataAccess.class, MemoryDataAccess.class})
    void deleteAllPets(Class<? extends DataAccess> dbClass) throws Exception {
        DataAccess dataAccess = getDataAccess(dbClass);

        dataAccess.addPet(new Pet(0, "joe", PetType.FISH));
        dataAccess.addPet(new Pet(0, "sally", PetType.CAT));

        dataAccess.deleteAllPets();

        var actual = dataAccess.listPets();
        assertEquals(0, actual.size());
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