package client;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.Map;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public String addPet(String name) throws ResponseException {
        var path = String.format("/pet/%s", name);
        var result = this.makeRequest("POST", path, null, null, Map.class);
        return new Gson().toJson(result);
    }

    public String deletePet(String name) throws ResponseException {
        var path = String.format("/pet/%s", name);
        var result = this.makeRequest("DELETE", path, null, null, Map.class);
        return new Gson().toJson(result);
    }

    public String listPets() throws ResponseException {
        var path = "/pet";
        var result = this.makeRequest("GET", path, null, null, Map.class);
        return new Gson().toJson(result);
    }

    private <T> T makeRequest(String method, String path, Object request, String authToken, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.addRequestProperty("Authorization", authToken);
            }

            if (responseClass != null) {
                http.addRequestProperty("Accept", "application/json");
            }

            if (request != null) {
                http.addRequestProperty("Content-Type", "application/json");
                String reqData = new Gson().toJson(request);
                try (OutputStream reqBody = http.getOutputStream()) {
                    reqBody.write(reqData.getBytes());
                }
            }
            http.connect();

            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (http.getResponseCode() == 200) {
                    if (responseClass != null) {
                        return new Gson().fromJson(reader, responseClass);
                    }
                    return null;
                }

                throw new ResponseException(http.getResponseCode(), reader);
            }
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
