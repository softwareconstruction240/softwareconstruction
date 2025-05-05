package dataaccess;

import exception.ResponseException;
import model.*;

public interface DataAccess {
    Pet addPet(Pet pet) throws ResponseException;

    PetList listPets() throws ResponseException;

    Pet getPet(int id) throws ResponseException;

    void deletePet(Integer id) throws ResponseException;

    void deleteAllPets() throws ResponseException;
}
