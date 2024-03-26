package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

    private Socket client;
    private BufferedReader inputReader;
    private PrintWriter outputWriter;
    private Inputs inputHandler;
    private Thread thread;
    private boolean quit;

    public Client(
        Socket socket,
        PrintWriter output,
        BufferedReader input
    ) {
        this.client = socket;
        this.outputWriter = output;
        this.inputReader = input;
        this.inputHandler = new Inputs();
        this.thread = new Thread(this.inputHandler);
    }

    @Override
    public void run() {
        try {
            this.thread.start();

            String inputMsg;
            while ((inputMsg = this.inputReader.readLine()) != null) {
                System.out.println(inputMsg);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            turnoff();
        }
    }

    class Inputs implements Runnable {

        @Override
        public void run() {
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
                while (!quit) {
                    String msg = inputReader.readLine();
                    if (msg.equals("/quit")) {
                        outputWriter.println(msg);
                        inputReader.close();
                        turnoff();
                    } else {
                        outputWriter.println(msg);
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void turnoff() {
        quit = true;
        try {
            this.inputReader.close();
            this.outputWriter.close();
            if (!client.isClosed()) {
                client.close(); 
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        try {
            // Manufacturing and injecting dependencies
            Socket socket = new Socket("127.0.0.1", 5555);
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            Client client = new Client(socket, outputWriter, inputReader);
            client.run();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
