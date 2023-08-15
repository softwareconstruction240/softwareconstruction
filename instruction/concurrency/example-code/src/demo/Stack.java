package demo;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Stack<T> {
    private LinkedList<T> values;

    public Stack() {
        values = new LinkedList<T>();
    }

    public synchronized void push(T value) {
        values.addFirst(value);
    }

    public synchronized T pop() throws NoSuchElementException {
        return values.removeFirst();
    }

    public synchronized int size() {
        return values.size();
    }
}
