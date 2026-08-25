package EnumExamples;

public enum Gender {
    Male, Female;

    @Override
    public String toString() {
        return this == Male ? "m" : "f";
    }
}
