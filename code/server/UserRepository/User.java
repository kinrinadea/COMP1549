package server.UserRepository;

public class User {
    public String clientID;
    public Integer port;
    public boolean isCoordinator;

    public User(String clientID, Integer port, boolean isCoordinator) {
        this.clientID = clientID;
        this.port = port;
        this.isCoordinator = isCoordinator;
    }

}
