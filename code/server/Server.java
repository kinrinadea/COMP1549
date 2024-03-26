package server;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.UserRepository.Repository;


public class Server implements Runnable {

    private ArrayList<Connection> connections;
    private Repository usersRepo;
    private ExecutorService threads;
    private ServerSocket server;
    private boolean run;

    public Server(Repository usersRepo) {
        this.usersRepo = usersRepo;
        this.connections = new ArrayList<>();
        this.run = true;
    }

    public void turnoff() {

    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(5555);
            
            threads = Executors.newCachedThreadPool();

            while (this.run) {
                // When client connects, add to the connection list 
                Socket newClient = server.accept();
                Connection newConnection = new Connection(newClient, this.usersRepo);
                this.connections.add(newConnection);
                threads.execute(newConnection);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   

    public static void main(String[] args) {
        Repository usersRepo = new Repository();
        Server srv = new Server(usersRepo);
        srv.run();
    }

}