package deserializer;

public interface Automobile {
    enum AutomobileType {
        Car, Truck
    }

    String getMake();
    String getModel();
    int getYear();
    AutomobileType getType();
}
