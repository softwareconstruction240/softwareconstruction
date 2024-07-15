package typeAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Garage garage = new Garage();

        garage.addAutomobile(new Car("Ford", "Mustang", 1965));
        garage.addAutomobile(new Car("Chevrolet", "Corvette", 2023));
        garage.addAutomobile(new Truck("Toyota", "Tundra", 2021, true));

        // Register a type adapter for the automobile interface
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Automobile.class, new AutomobileTypeAdapter());
        Gson gson = builder.create();

        // Generate a json string
        String jsonString = gson.toJson(garage);
        System.out.println(jsonString);

        // Parse the json string
        Garage parsedGarage = gson.fromJson(jsonString, Garage.class);
        System.out.println(parsedGarage);
    }

    private static class AutomobileTypeAdapter extends TypeAdapter<Automobile> {
        @Override
        public void write(JsonWriter jsonWriter, Automobile automobile) throws IOException {
            Gson gson = new Gson();

            switch(automobile.getType()) {
                case Car -> gson.getAdapter(Car.class).write(jsonWriter, (Car) automobile);
                case Truck -> gson.getAdapter(Truck.class).write(jsonWriter, (Truck) automobile);
            }
        }

        @Override
        public Automobile read(JsonReader jsonReader) throws IOException {
            String make = null;
            String model = null;
            int year = 0;
            Automobile.AutomobileType type = null;
            boolean fourWheelDrive = false;

            jsonReader.beginObject();

            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                switch (name) {
                    case "make" -> make = jsonReader.nextString();
                    case "model" -> model = jsonReader.nextString();
                    case "year" -> year = jsonReader.nextInt();
                    case "type" -> type = Automobile.AutomobileType.valueOf(jsonReader.nextString());
                    case "fourWheelDrive" -> fourWheelDrive = jsonReader.nextBoolean();
                }
            }

            jsonReader.endObject();

            if(type == null) {
                return null;
            } else {
                return switch (type) {
                    case Car -> new Car(make, model, year);
                    case Truck -> new Truck(make, model, year, fourWheelDrive);
                };
            }
        }
    }
}
