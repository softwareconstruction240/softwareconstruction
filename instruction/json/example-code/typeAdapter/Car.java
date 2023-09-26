public class Car extends AutomobileImpl {
    public Car(String make, String model, int year) {
        super(make, model, year, AutomobileType.Car);
    }

    @Override
    public String toString() {
        return "Car{} " + super.toString();
    }
}
