package server.websocket;

import dataaccess.DataAccess;
import model.Pet;
import webSocketMessages.Notification;

import java.util.Random;
import java.util.TimerTask;

public class NoiseTimer extends TimerTask {
    private final ConnectionManager connections;
    private final DataAccess dataAccess;
    private final Random rand = new Random();

    public NoiseTimer(ConnectionManager connections, DataAccess dataAccess) {
        this.connections = connections;
        this.dataAccess = dataAccess;
    }

    public void run() {
        try {
            var pets = dataAccess.listPets();
            if (!pets.isEmpty()) {
                var pet = pets.stream().toList().get(rand.nextInt() % pets.size());
                var message = String.format("%s said %s", pet.name(), pet.sound());
                var notification = new Notification(Notification.Type.NOISE, message);
                connections.broadcast(null, notification);
            }
        } catch (Exception ignore) {
        }
    }
}

