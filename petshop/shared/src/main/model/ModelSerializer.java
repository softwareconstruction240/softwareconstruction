package model;

import com.google.gson.*;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;

public class ModelSerializer {
    public static <T> T deserialize(String json, Class<T> responseClass) {
        return deserialize(new StringReader(json), responseClass);
    }

    public static <T> T deserialize(Reader reader, Class<T> responseClass) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Deserialize FriendList to ArrayFriendList if used.
        gsonBuilder.registerTypeAdapter(FriendList.class, new FriendListAdapter());
        return gsonBuilder.create().fromJson(reader, responseClass);
    }

    private static class FriendListAdapter implements JsonDeserializer<FriendList> {
        public FriendList deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            return ctx.deserialize(el, ArrayFriendList.class);
        }
    }
}
