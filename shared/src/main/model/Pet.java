package model;

import com.google.gson.*;

import java.lang.reflect.Type;

public record Pet(int id, String name, PetType type, FriendList friends) {
    public String toString() {
        return new Gson().toJson(this);
    }
}
