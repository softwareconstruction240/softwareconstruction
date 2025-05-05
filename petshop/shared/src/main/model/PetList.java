package model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;

public class PetList extends ArrayList<Pet> {
    public PetList() {

    }

    public PetList(Collection<Pet> pets) {
        super(pets);
    }

    public String toString() {
        return new Gson().toJson(this.toArray());
    }
}