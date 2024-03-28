package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import cron.Cron;
import server.UserRepository.Repository;
import server.UserRepository.User;

public class Connection implements Runnable {
    
    private Socket client;
    private Repository usersRepo;
    private User user;
    private Cron cron;

    public BufferedReader inputReader;
    public PrintWriter outputWriter;
    
    public String clientID;
    public String currentUserId;
    public Integer port;


    public Connection(Socket client, Repository usersRepo) {
        try {
            this.client = client;
            this.usersRepo = usersRepo;
            this.user = null; // Assigned after inputs

            this.outputWriter = new PrintWriter(client.getOutputStream(), true);
            this.inputReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            this.clientID = "";
            this.port = null;

            this.cron = new Cron(usersRepo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void turnoff() {
        try {
            if (!this.client.isClosed()) {
                this.client.close();
            }
            this.inputReader.close();
            this.outputWriter.close();
        } catch (IOException e) {
            // Logs error
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String inputId = "";
            boolean inputIdOk = false;
            this.outputWriter.println("Choose your user ID:");
            while (!inputIdOk) {
                inputId  = this.inputReader.readLine().trim();
                if (inputId.isEmpty() || this.usersRepo.findUserByID(inputId) != null) {
                    this.outputWriter.println("Invalid input, blanks and duplicated IDs not allowed, try again.");
                } else {
                    inputIdOk = true;
                }
            }

            String inputPort = "";
            boolean inputPortOk = false;
            this.outputWriter.println("Choose your port:");
            while (!inputPortOk) {
                inputPort  = this.inputReader.readLine().trim();
                if (!this.validatePortInput(inputPort)) {
                    this.outputWriter.println("Invalid input, port must be integer or it mighe be in use, try again.");
                } else {
                    inputPortOk = true;
                }
            }

            this.user = this.usersRepo.createUser(inputId, Integer.valueOf(inputPort), this);
            this.usersRepo.sendMessageToAllMembers(this.user.clientID + " has just connected to this chat group!");

            String input;
            while((input = this.inputReader.readLine()) != null ) {
                try {
                    if (input.substring(0, Math.min(input.length(), 3)).equals("@pv")) {
                        String[] arrayInput = input.trim().split("\\s+");
                        String message = input.replace("@pv " + arrayInput[1], "");
                        this.usersRepo.sendMessageTo(arrayInput[1], message, this.user.clientID);
                        this.outputWriter.println("Sent");
                        continue;
                    }
                    if (input.equals("@bye")) {
                        this.outputWriter.print("Goodbye!");
                        this.usersRepo.quitUser(this.user.clientID);
                        this.usersRepo.sendMessageToAllMembers(this.user.clientID + " has left!");
                        continue;
                    }
                    if (input.substring(0, Math.min(input.length(), 5)).equals("@info")) {
                        String[] arrayInput = input.trim().split("\\s+");
                        this.outputWriter.println(this.usersRepo.findUserByID(arrayInput[1]).requestData());
                        continue;
                    }

                    this.usersRepo.sendMessageToAllMembers(this.user.clientID + ": " + input);
                } catch (Exception e) {
                    this.outputWriter.println("Error sending message, please try again. Details: " + e.getMessage());
                    this.outputWriter.println(this.getHelpCommands());
                }
            }
            this.usersRepo.sendMessageToAllMembers(this.user.clientID + " left :)");
            this.usersRepo.quitUser(this.user.clientID);
        } catch (IOException e) {
            e.printStackTrace();
            this.turnoff();
        }
    }

    public Boolean validatePortInput(String inputPort) {
        if (inputPort.isBlank()) {
            return false;
        }

        // Checks if its numeric
        if (inputPort != null) {
            for (char c : inputPort.toCharArray()) {
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
        }

        // Transform to int and check if port is free
        Integer intPort = Integer.valueOf(inputPort);
        if (this.usersRepo.findUserByPort(intPort) != null) {
            return false;
        }
        
        return true;
    }

    private String getHelpCommands() {
        String guide = "Please check the commands guidelines: \n@pv {friendID} {message} - for private messages \n@info {friendID} - to request friend info\n @bye - to leave";
        return guide;
    }

    public static Boolean testValidatePortInput(String inputPort) {
        if (inputPort.isBlank()) {
            return false;
        }

        // Checks if its numeric
        if (inputPort != null) {
            for (char c : inputPort.toCharArray()) {
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
        }

        // Transform to int and check if port is free
        Integer intPort = Integer.valueOf(inputPort);
        // if (this.usersRepo.findUserByPort(intPort) != null) {
        //     return false;
        // }
        
        return true;
    }

}
