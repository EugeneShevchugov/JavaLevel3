package task3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    public static final int PORT = 8082;
    private AuthService authService;
    private Set<ClientHandler> clientHandlers;

    public Server() {
        this(PORT);
    }

    public Server(int port) {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            authService = new BasicAuthService();
            System.out.println("Auth is started up");

            clientHandlers = new HashSet<>();

            while (true) {
                System.out.println("Waiting for a connection...");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket);
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public synchronized boolean isOccupied(AuthService.Record record) {
        for (ClientHandler ch : clientHandlers) {
            if (ch.getRecord().equals(record)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void subscribe(ClientHandler ch) {
        clientHandlers.add(ch);
    }

    public synchronized void unsubscribe(ClientHandler ch) {
        clientHandlers.remove(ch);
    }

    public synchronized void broadcastMessage(String message) {
        for (ClientHandler ch : clientHandlers) {
            ch.sendMessage(message);
        }
    }

    public synchronized void sendMessageOnlyOne(String message, String sender) {
        String recipient = message.substring(0, message.indexOf(' '));
        message = message.substring(message.indexOf(' ') + 1);
        for (ClientHandler ch : clientHandlers) {
            if (recipient.equals(ch.getRecipient()))
                ch.sendMessage(String.format("%s (only for you): %s", sender, message));
            if (sender.equals(ch.getRecipient()))
                ch.sendMessage(String.format("You (only %s): %s", recipient, message));
        }
    }
}
