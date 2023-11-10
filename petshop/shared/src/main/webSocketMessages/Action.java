package webSocketMessages;

import com.google.gson.Gson;

public record Action(Type type, String visitorName) {
    public enum Type {
        ENTER,
        EXIT
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
