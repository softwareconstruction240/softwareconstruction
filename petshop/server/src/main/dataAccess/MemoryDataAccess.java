package dataAccess;

import model.Pet;

import java.util.Collection;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {
    private int nextId = 1;
    final private HashMap<Integer, Pet> pets = new HashMap<>();

    public Pet addPet(Pet pet) {
        pet = new Pet(nextId++, pet.name(), pet.type(), pet.friends());

        pets.put(pet.id(), pet);
        return pet;
    }

    public Collection<Pet> listPets() {
        return pets.values();
    }


    public void deletePet(Integer id) {
        pets.remove(id);
    }

    public void deleteAllPets() {
        pets.clear();
    }
}
