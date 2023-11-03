package service;

import dataAccess.DataAccess;
import model.Pet;

import java.util.Collection;

public class PetService {

    private final DataAccess dataAccess;

    public PetService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public Pet addPet(Pet pet) {
        return dataAccess.addPet(pet);
    }

    public Collection<Pet> listPets() {
        return dataAccess.listPets();
    }


    public void deletePet(Integer id) {
        dataAccess.deletePet(id);
    }

    public void deleteAllPets() {
        dataAccess.deleteAllPets();
    }
}
