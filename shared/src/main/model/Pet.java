package model;

import com.google.gson.*;

import java.util.Objects;

public record Pet(int id, String name, PetType type, FriendList friends) {
    public String toString() {
        return new Gson().toJson(this);
    }

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

    // Override because we need to deep compare friends.
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pet pet = (Pet) o;

        if (id != pet.id) return false;
        if (!Objects.equals(name, pet.name)) return false;
        if (type != pet.type) return false;
        if (friends == null && pet.friends == null) return true;
        if (friends == null || pet.friends == null) return false;
        var compareFriends = pet.friends.collection();
        for (var friend : friends.collection()) {
            if (!compareFriends.contains(friend)) return false;
        }
        return true;
    }
}
