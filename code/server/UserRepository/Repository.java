package server.UserRepository;

import java.util.ArrayList;

public class Repository {
    private ArrayList<User> users;

    public Repository() {
        this.users = new ArrayList<>();
    }

    /**
     * Creates a new user and add to this repo
     * 
     * @param userID
     * @param port
     */
    public User createUser(String userID, Integer port) {
        boolean isCoordinator = false;
        if (this.users.isEmpty()) {
            isCoordinator = true;
        }
        User newUser = new User(userID, port, isCoordinator);
        this.users.add(newUser);

        return newUser;
    }

    /**
     * Tries to find a user by his ID
     * 
     * @param userID
     * @return User || null
     */
    public User findUserByID(String userID) {
        for (User u : this.users) {
            if (u.clientID.equals(userID)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Tries to find a user by his port
     * 
     * @param port
     * @return User || null
     */
    public User findUserByPort(Integer port) {
        for (User u : this.users) {
            if (u.port == port) {
                return u;
            }
        }
        return null;
    }
}
