package server;

import com.google.gson.Gson;
import exception.ErrorResponse;
import exception.ResponseException;
import model.Pet;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    // TODO: Finish creating sequence diagram
    // https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEXijoMABKKPZIqhZySBBogQDuABZIYGKIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD02ChgADpoAN4AREgcvWVOADQwvWjAALYoQ73jY71glm1z4wC+mMLFMPms7FyUZX0DQ86LkzNrvYvLqyXzvZtsnNywe9uiZcAcHJXtAAp-mAAJSmHRQFDAADWMGiRggjgOMBS+hgADNuPotkVRLsPkUjjAAKJQbylGBoCAI+yqGBJdIpDFYjTbSj4gpZcxlAAsTicXT6M3UwHsszKvVJ5LKVJpdIZYCZmKhqhu4xich8qt5-M26A4pk+Kl2BVk8iUKnU31+wIBbVBmDNimUajpe2MZQUvxg9pgwKdMEgfvadV03Ed2gtro5hREKjKwJxccMOT2L0OFMT6beMbZFJOg2GF2m4setxWpY2SZ2qc5yG5MAATPzBf1C+dxpdK2qlhXrnrOKYvL5-AFsD4oNhuDAADLRZjAukBVLpTL13IFPNlcoAcWJjRaBnU8TQgq1lC7Y1w6iS0A4A4Nnm8fkCkI4MhQ3AyweYAR0CFAaFMC5DdY2KbdpGJGd92JJpWnaZoSntABJA19VMJ8R0CLxgEsD8v0MBQEAQH8lxXDJgPXZgCWoIlykg6C6lglp7Q0dDMCAA

    public Pet addPet(Pet pet) throws ResponseException {
        var path = "/pet";
        return this.makeRequest("POST", path, pet, Pet.class);
    }

    public void deletePet(int id) throws ResponseException {
        var path = String.format("/pet/%s", id);
        this.makeRequest("DELETE", path, null, null);
    }

    public void deleteAllPets() throws ResponseException {
        var path = "/pets";
        this.makeRequest("DELETE", path, null, null);
    }

    public Pet[] listPets() throws ResponseException {
        var path = "/pets";
        record listPetResponse(Pet[] pet) {
        }
        var response = this.makeRequest("GET", path, null, listPetResponse.class);
        return response.pet();
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
