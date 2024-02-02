import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class Main {
    public static void main(String[] args) {
        Garage garage = new Garage();

        garage.addAutomobile(new Car("Ford", "Mustang", 1965));
        garage.addAutomobile(new Car("Chevrolet", "Corvette", 2023));
        garage.addAutomobile(new Truck("Toyota", "Tundra", 2021, true));

        // Register a deserializer for the automobile interface
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Automobile.class, new AutomobileDeserializer());
        Gson gson = builder.create();

        // Generate a json string
        String jsonString = gson.toJson(garage);
        System.out.println(jsonString);

        // Parse the json string
        Garage parsedGarage = gson.fromJson(jsonString, Garage.class);
        System.out.println(parsedGarage);
    }

    private static class AutomobileDeserializer implements JsonDeserializer<Automobile> {
        @Override
        public Automobile deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String typeString = jsonObject.get("type").getAsString();
            Automobile.AutomobileType automobileType = Automobile.AutomobileType.valueOf(typeString);

            return switch(automobileType) {
                case Car -> context.deserialize(jsonElement, Car.class);
                case Truck -> context.deserialize(jsonElement, Truck.class);
            };
        }
    }
}
