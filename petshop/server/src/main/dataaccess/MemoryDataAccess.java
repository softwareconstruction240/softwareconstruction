package dataaccess;

import model.*;

import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {
    private int nextId = 1;
    final private HashMap<Integer, Pet> pets = new HashMap<>();

    public Pet addPet(Pet pet) {
        pet = new Pet(nextId++, pet.name(), pet.type());

        pets.put(pet.id(), pet);
        return pet;
    }

    public PetList listPets() {
        return new PetList(pets.values());
    }


    public Pet getPet(int id) {
        return pets.get(id);
    }

    public void deletePet(Integer id) {
        pets.remove(id);
    }

    public void deleteAllPets() {
        pets.clear();
    }
}
