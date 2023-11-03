import dataAccess.MemoryDataAccess;
import model.ArrayFriendList;
import model.Pet;
import model.PetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.PetService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PetServiceTest {
    static final PetService service = new PetService(new MemoryDataAccess());

    @BeforeEach
    void clear() {
        service.deleteAllPets();
    }

    @Test
    void addPet() {
        var pet = new Pet(0, "joe", PetType.FISH, null);
        pet = service.addPet(pet);

        var pets = service.listPets();
        assertEquals(1, pets.size());
        assertTrue(pets.contains(pet));
    }

    @Test
    void listPets() {
        List<Pet> expected = new ArrayList<>();
        expected.add(service.addPet(new Pet(0, "joe", PetType.FISH, null)));
        expected.add(service.addPet(new Pet(0, "sally", PetType.CAT, null)));
        expected.add(service.addPet(new Pet(0, "fido", PetType.DOG, new ArrayFriendList("a", "b"))));

        var actual = service.listPets();
        assertIterableEquals(expected, actual);
    }

    @Test
    void deletePet() {
        List<Pet> expected = new ArrayList<>();
        var pet = service.addPet(new Pet(0, "joe", PetType.FISH, null));
        expected.add(service.addPet(new Pet(0, "sally", PetType.CAT, null)));
        expected.add(service.addPet(new Pet(0, "fido", PetType.DOG, new ArrayFriendList("a", "b"))));

        service.deletePet(pet.id());
        var actual = service.listPets();
        assertIterableEquals(expected, actual);
    }

    @Test
    void deleteAllPets() {
        service.addPet(new Pet(0, "joe", PetType.FISH, null));
        service.addPet(new Pet(0, "sally", PetType.CAT, null));
        service.addPet(new Pet(0, "fido", PetType.DOG, new ArrayFriendList("a", "b")));

        service.deleteAllPets();
        assertEquals(0, service.listPets().size());
    }
}