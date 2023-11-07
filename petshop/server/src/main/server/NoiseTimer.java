package server;

import dataaccess.DataAccess;
import webSocketMessages.Notification;

import java.util.TimerTask;

public class NoiseTimer extends TimerTask {
    private final PetStoreConnectionManager connections;
    private final DataAccess dataAccess;

    public NoiseTimer(PetStoreConnectionManager connections, DataAccess dataAccess) {
        this.connections = connections;
        this.dataAccess = dataAccess;
    }

    public void run() {
        try {
            for (var pet : dataAccess.listPets()) {
                var message = String.format("%s said %s", pet.name(), pet.sound());
                var notification = new Notification(Notification.Type.NOISE, message);
                connections.broadcast(null, notification);
            }
        } catch (Exception ignore) {
        }
    }
}

