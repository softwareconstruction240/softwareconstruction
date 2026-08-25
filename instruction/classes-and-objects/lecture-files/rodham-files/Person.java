package javaclasses;

public abstract class Person {

    private static int idCounter = 1;
    private static int generateId() {
        return idCounter++;
    }

    private int id;
    private String name;
    private int age;

    public Person(String name, int age) {
        setId(generateId());
        setName(name);
        setAge(age);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("name: %s, age: %d", name, age);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (o.getClass() != this.getClass()) {
            return false;
        }

        Person p = (Person)o;

        return (name.equals(p.name) && (age == p.age));
    }

    @Override
    public int hashCode() {
        return (name.hashCode() * age * id);
    }

    public final int getPriority() {
        return (agePriority() + categoryPriority());
    }

    protected abstract int agePriority();

    protected abstract int categoryPriority();
}
