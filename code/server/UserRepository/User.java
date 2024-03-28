package server.UserRepository;

import server.Connection;

public class User {

    public String clientID;
    public Integer port;
    public boolean isCoordinator;
    public String info;
    private Connection clientConnection;

    public User(String clientID, Integer port, boolean isCoordinator, Connection clientConnection) {
        this.clientID = clientID;
        this.port = port;
        this.isCoordinator = isCoordinator;
        this.clientConnection = clientConnection;
        this.info = "Info: " + this.clientID + " - PORT: " + this.port;
    }

    public void message(String message) {
        this.clientConnection.outputWriter.println(message);
    }

    public String requestData() {
        return this.info;
    }

}
