import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Garage garage = new Garage();

        garage.addAutomobile(new Car("Ford", "Mustang", 1965));
        garage.addAutomobile(new Car("Chevrolet", "Corvette", 2023));
        garage.addAutomobile(new Truck("Toyota", "Tundra", 2021, true));

        final RuntimeTypeAdapterFactory<Automobile> typeFactory = RuntimeTypeAdapterFactory
                .of(Automobile.class, "type")
                .registerSubtype(Car.class)
                .registerSubtype(Truck.class);

        // Register a type adapter for the automobile interface
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(typeFactory);
        Gson gson = builder.create();

        // Generate a json string
        String jsonString = gson.toJson(garage);
        System.out.println(jsonString);

        // Parse the json string
        Garage parsedGarage = gson.fromJson(jsonString, Garage.class);
        System.out.println(parsedGarage);
    }
}
