package deserializer;

import java.util.ArrayList;
import java.util.List;

public class Garage {
    private final List<Automobile> automobiles = new ArrayList<>();

    public void addAutomobile(Automobile automobile) {
        automobiles.add(automobile);
    }

    @Override
    public String toString() {
        return "Garage{" +
                "automobiles=" + automobiles +
                '}';
    }
}
