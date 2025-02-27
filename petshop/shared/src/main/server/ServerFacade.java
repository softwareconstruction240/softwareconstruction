package server;

import com.google.gson.Gson;
import exception.ErrorResponse;
import exception.ResponseException;
import model.ListPetResponse;
import model.Pet;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    // TODO: Finish creating sequence diagram
    // https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEXijo8FAockgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9NgoYAA6aADeAERIHB2FTgA0MB1owAC2KL0dQ4MdYJaNk0MAvpjCeTBZrOxclIWd3b3OMyPjix0zcwv5Ux0rbJzcsJtrooXAHBwlTQAUX2AAlKYdOFgABrGBIVRGCCObYweL6GAAM24+lWuVEG2euV2MAAolBvAUYGgIND7FDYkl4sjURo1pQsdlUuZCgAWJxOVqdcbqYD2CaFDoEomFUnkynU2kRVTnIaqeRgHyy9mclboDimF4qDbZWTyJQqdRvD5-b6NAGYfWKZRqKGbYyFBQfGAWmB-a0wSDupqVXTcK3aQ12pk5EQqQp-dHhwzpTb3HbEqMJx6hhnE-Y9PrHMaCm4XeZ55bR9Zx5nIVkwABMnO5XSzRyGJyLctmhbO6s4pi8vn8AWwPig2G4MAAMpDmH8oQEEkkUhWMtl04UigBxPE1eoW+k4p7ZFO4jodTAHvdhqivGDIBVT76A7WxvVB23Gq8T2+A63B9S6ow6Qqrk0MDAAgCCuk0UJIt4ozgVgX4vva2IxoUcAQKBKDgFEaAADx-JkJaYmWWwPLiqHoZh0S4U0+Gnmmu6FOON5NAASmo2DRAYJaMkRLIYIUNZcu0QzbocRSZocAxNrmZwFlcNxLLUnaap43h+IE4QcDIKDcMkPrMAEOgIKAoKYLxzBIXkK7SHio4bnitQNE0dT5BaACSmoaqYKm9oEXjAJYWk6YYCigXp06zskpkLuZS70cU1m2ZU9lbhBZhdpgQA

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

    public ListPetResponse listPets() throws ResponseException {
        var path = "/pets";
        return this.makeRequest("GET", path, null, ListPetResponse.class);
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
