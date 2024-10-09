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

    // Pet Shop is very simple.
    // A more complicated application would do the business logic in this
    // service.

    public Pet addPet(Pet pet) throws ResponseException {
        return dataAccess.addPet(pet);
    }

    public Collection<Pet> listPets() throws ResponseException {
        return dataAccess.listPets();
    }

    public Pet getPet(int id) throws ResponseException {
        return dataAccess.getPet(id);
    }

    public void deletePet(Integer id) throws ResponseException {
        dataAccess.deletePet(id);
    }

    public void deleteAllPets() throws ResponseException {
        dataAccess.deleteAllPets();
    }
}
