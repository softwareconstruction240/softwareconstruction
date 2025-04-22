package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.Pet;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public Pet addPet(Pet pet) throws ResponseException {
        var request = buildRequest("POST", "/pet", pet);
        var response = sendRequest(request);
        return handleResponse(response, Pet.class);
    }

    public void deletePet(int id) throws ResponseException {
        var path = String.format("/pet/%s", id);
        var request = buildRequest("DELETE", path, null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void deleteAllPets() throws ResponseException {
        var request = buildRequest("DELETE", "/pet", null);
        sendRequest(request);
    }

    public Pet[] listPets() throws ResponseException {
        record listPetResponse(Pet[] pet) {
        }

        var request = buildRequest("GET", "/pet", null);
        var response = sendRequest(request);
        var petList = handleResponse(response, listPetResponse.class);
        return petList.pet();
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
