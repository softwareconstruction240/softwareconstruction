package deserializer;

public abstract class AutomobileImpl implements Automobile {
    private final String make;
    private final String model;
    private final int year;
    private final AutomobileType type;

    public AutomobileImpl(String make, String model, int year, AutomobileType type) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.type = type;
    }

    @Override
    public String getMake() {
        return make;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public AutomobileType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "AutomobileImpl{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", type=" + type +
                '}';
    }
}
