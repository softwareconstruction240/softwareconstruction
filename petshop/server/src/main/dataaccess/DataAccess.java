package dataaccess;

import exception.ResponseException;
import model.Pet;

import java.util.Collection;

public interface DataAccess {
    Pet addPet(Pet pet) throws ResponseException;

    Collection<Pet> listPets() throws ResponseException;

    Pet getPet(int id) throws ResponseException;
    
    void deletePet(Integer id) throws ResponseException;

    void deleteAllPets() throws ResponseException;
}
