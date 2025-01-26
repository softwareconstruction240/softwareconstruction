package runtimeTypeAdapter;

public abstract class AutomobileImpl implements Automobile {
    private final String make;
    private final String model;
    private final int year;

    public AutomobileImpl(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
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
    public String toString() {
        return "AutomobileImpl{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                '}';
    }
}
