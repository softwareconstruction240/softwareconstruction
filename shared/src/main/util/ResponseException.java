package util;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.HashMap;

public class ResponseException extends Exception {
    final private int statusCode;

    public ResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int StatusCode() {
        return statusCode;
    }
}
