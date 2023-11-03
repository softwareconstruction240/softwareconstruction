package model;

import com.google.gson.*;

public record Pet(int id, String name, PetType type, FriendList friends) {
    public String toString() {
        return new Gson().toJson(this);
    }
}
