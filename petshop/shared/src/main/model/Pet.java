package model;

import com.google.gson.*;

public record Pet(int id, String name, PetType type) {

    public String sound() {
        return switch (type) {
            case DOG -> "bark";
            case CAT -> "meow";
            case FISH -> "bubbles";
            case FROG -> "ribbit";
            case BIRD -> "tweet";
            case RAT -> "squeak";
            case ROCK -> "roll";
        };
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
