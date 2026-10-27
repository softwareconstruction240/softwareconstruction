package service;

import dataaccess.DataAccess;
import model.*;
import exception.ResponseException;
import model.PetType;
import server.NoiseMaker;

import java.util.Collection;

public class PetService {

    private final DataAccess dataAccess;
    private final NoiseMaker noiseMaker;

    public PetService(DataAccess dataAccess, NoiseMaker noiseMaker) {
        this.dataAccess = dataAccess;
        this.noiseMaker = noiseMaker;
    }

    // Pet Shop is very simple.
    // A more complicated application would do the business logic in the service.

    public Pet addPet(Pet pet) throws ResponseException {
        if (pet.type() == PetType.DOG && pet.name().equals("fleas")) {
            throw new ResponseException(ResponseException.Code.ClientError, "Error: no dogs with fleas");
        }

        noiseMaker.makeNoise(pet);
        return dataAccess.addPet(pet);
    }

    public PetList listPets() throws ResponseException {
        return dataAccess.listPets();
    }

    public Pet getPet(int id) throws ResponseException {
        validateId(id);
        return dataAccess.getPet(id);
    }

    public void deletePet(Pet pet) throws ResponseException {
        validateId(pet.id());

        noiseMaker.makeNoise(pet);
        dataAccess.deletePet(pet.id());
    }

    public void deleteAllPets() throws ResponseException {
        Collection<Pet> pets = dataAccess.listPets();
        if (!pets.isEmpty()) {
            dataAccess.deleteAllPets();
        }
    }

    private void validateId(int id) throws ResponseException {
        if (id <= 0) {
            throw new ResponseException(ResponseException.Code.ClientError, "Error: invalid pet ID");
        }
    }
}
