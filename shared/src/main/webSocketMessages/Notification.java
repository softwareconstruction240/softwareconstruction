package webSocketMessages;

import com.google.gson.Gson;

public record Notification(Type type, String message) {
    public enum Type {
        ARRIVAL,
        NOISE,
        DEPARTURE
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
