package server.UserRepository;

import server.Connection;

public class User {

    public String clientID;
    public Integer port;
    public boolean isCoordinator;
    private String info;
    private Connection clientConnection;

    public User(String clientID, Integer port, boolean isCoordinator, Connection clientConnection) {
        this.clientID = clientID;
        this.port = port;
        this.isCoordinator = isCoordinator;
        this.clientConnection = clientConnection;
        this.info = "My info: " + this.clientID + " - PORT: " + this.port;
    }

    public void message(String message) {
        this.clientConnection.outputWriter.println(message);
    }

    public String requestData() {
        return this.info;
    }

}
