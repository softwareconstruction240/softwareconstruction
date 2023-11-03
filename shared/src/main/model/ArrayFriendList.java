package model;

import java.util.*;

public class ArrayFriendList implements FriendList {
    final private ArrayList<String> list = new ArrayList<>();

    public ArrayFriendList(String... names) {
        list.addAll(List.of(names));
    }

    public void add(String name) {
        list.add(name);
    }

    public Collection<String> collection() {
        return list;
    }
}
