package service;

import dataaccess.MemoryDataAccess;
import exception.ResponseException;
import model.Pet;
import model.PetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.NoiseMaker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PetServiceTest {
    static final PetService service = new PetService(new MemoryDataAccess(), (pet) -> {
    });

    @BeforeEach
    void clear() throws ResponseException {
        service.deleteAllPets();
    }

    @Test
    void addPet() throws ResponseException {
        var pet = new Pet(0, "joe", PetType.FISH);
        pet = service.addPet(pet);

        Collection<Pet> pets = service.listPets();
        assertEquals(1, pets.size());
        assertTrue(pets.contains(pet));
    }

    @Test
    void listPets() throws ResponseException {
        List<Pet> expected = new ArrayList<>();
        expected.add(service.addPet(new Pet(0, "joe", PetType.FISH)));
        expected.add(service.addPet(new Pet(0, "sally", PetType.CAT)));
        expected.add(service.addPet(new Pet(0, "fido", PetType.DOG)));

        Collection<Pet> actual = service.listPets();
        assertIterableEquals(expected, actual);
    }

    @Test
    void deletePet() throws ResponseException {
        List<Pet> expected = new ArrayList<>();
        Pet pet = service.addPet(new Pet(0, "joe", PetType.FISH));
        expected.add(service.addPet(new Pet(0, "sally", PetType.CAT)));
        expected.add(service.addPet(new Pet(0, "fido", PetType.DOG)));

        service.deletePet(pet);
        Collection<Pet> actual = service.listPets();
        assertIterableEquals(expected, actual);
    }

    @Test
    void deleteAllPets() throws ResponseException {
        service.addPet(new Pet(0, "joe", PetType.FISH));
        service.addPet(new Pet(0, "sally", PetType.CAT));
        service.addPet(new Pet(0, "fido", PetType.DOG));

        service.deleteAllPets();
        assertEquals(0, service.listPets().size());
    }

    @Test
    void noDogsWithFleas() {
        assertThrows(ResponseException.class, () ->
                service.addPet(new Pet(0, "fleas", PetType.DOG)));
    }

    @Test
    void makeNoise() throws ResponseException {
        StringBuilder noises = new StringBuilder();
        NoiseMaker noiseMaker = (pet) -> noises.append(pet.sound());
        var service = new PetService(new MemoryDataAccess(), noiseMaker);
        service.addPet(new Pet(0, "betty", PetType.DOG));

        assertEquals("bark", noises.toString());
    }
}