package server;

import exception.ResponseException;
import model.Pet;

public interface NoiseMaker {
    void makeNoise(Pet pet) throws ResponseException;
}
