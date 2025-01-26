package deserializer;

public class Truck extends AutomobileImpl {
    private final boolean fourWheelDrive;

    public Truck(String make, String model, int year, boolean fourWheelDrive) {
        super(make, model, year, AutomobileType.Truck);
        this.fourWheelDrive = fourWheelDrive;
    }

    public boolean isFourWheelDrive() {
        return fourWheelDrive;
    }

    @Override
    public String toString() {
        return "Truck{" +
                "fourWheelDrive=" + fourWheelDrive +
                "} " + super.toString();
    }
}
