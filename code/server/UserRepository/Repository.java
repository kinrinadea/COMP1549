package server.UserRepository;

import java.util.ArrayList;

import server.Connection;

public class Repository {
    private User coordinator;
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
    public User createUser(String userID, Integer port, Connection clientConnection) {
        boolean isCoordinator = false;
        if (this.users.isEmpty()) {
            isCoordinator = true;
        }
        User newUser = new User(userID, port, isCoordinator, clientConnection);
        this.users.add(newUser);

        if (newUser.isCoordinator) {
            this.coordinator = newUser;
        }

        this.welcome(newUser);

        return newUser;
    }

    public void quitUser(String userID) {
        this.users.remove(findUserByID(userID));
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

    /**
     * Send to message all users
     * 
     * @param message
     */
    public void sendMessageToAllMembers(String message) {
        for (User u : this.users) {
            u.message(message);
        }
    }

    /**
     * Send to specific user
     * 
     */
    public void sendMessageTo(String recipientClientID, String message, String senderClientID) {
        this.findUserByID(recipientClientID).message(senderClientID + " sent to you: " + message);
    }

    /**
     * Welcome message and coordinator info
     */
    public void welcome(User user) {
        user.message("Hello " + user.clientID + ", welcome this this group!");
        if (user.isCoordinator) {
            user.message("You are the responsible coordinator of this group.");
        } else {
            user.message(this.coordinator.clientID + " is the coordinator");
        }
    }

}
