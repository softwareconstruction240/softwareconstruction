package service;

import dataaccess.DataAccess;
import model.Pet;
import exception.ResponseException;

import java.util.Collection;

public class PetService {

    private final DataAccess dataAccess;

    public PetService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public Pet addPet(Pet pet) throws ResponseException {
        return dataAccess.addPet(pet);
    }

    public Collection<Pet> listPets() throws ResponseException {
        return dataAccess.listPets();
    }


    public void deletePet(Integer id) throws ResponseException {
        dataAccess.deletePet(id);
    }

    public void deleteAllPets() throws ResponseException {
        dataAccess.deleteAllPets();
    }
}
