package dataAccess;

import model.Pet;

import java.util.Collection;

public interface DataAccess {
    Pet addPet(Pet pet);

    Collection<Pet> listPets();

    void deletePet(Integer id);

    void deleteAllPets();
}
