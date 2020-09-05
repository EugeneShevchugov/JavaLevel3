package task4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler {
    private AuthService.Record record;
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private final String ABOUTNIKNAME = "You changed your nickname.";

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            /*Реализация управления потоком через ExecutorService.*/
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(new Runnable() {
                public void run() {
                    try {
                        out.writeUTF("Please, log in");
                        doAuth();
                        readMessage();
                    } catch (IOException e) {
                        System.out.println("Client disconnected.");
                        closeConnection();
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Client handler was not created");
        }
    }

    public AuthService.Record getRecord() {
        return record;
    }

    public void doAuth() throws IOException {
        while (true) {
            String message = in.readUTF();
            out.writeUTF("Waiting your logging in...");

            if (message.startsWith("/auth")) {
                String[] credentials = message.split("\\s");
                try {
                    AuthService.Record possibleRecord = server.getAuthService().findRecord(credentials[1], credentials[2]);
                    if (possibleRecord != null) {
                        if (!server.isOccupied(possibleRecord)) {
                            record = possibleRecord;
                            out.writeUTF(String.format("You (%s)[%s] logged-in.", record.getName(), record.getLogin()));
                            server.broadcastMessage("Logged-in " + record.getName());
                            server.subscribe(this);
                            break;
                        } else {
                            sendMessage(String.format("Current user [%s] is already occupied", possibleRecord.getName()));
                        }
                    } else if (server.getAuthService().findRecordLogin(credentials[1]) != null) {
                        sendMessage("Password isn't correct.");
                    } else {
                        sendMessage(String.format("User no found"));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    sendMessage(String.format("Incorrect entry, repeat..."));
                }
            }
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Message [" + message + "] didn't send. Socket closed");
        }
    }

    public void readMessage() throws IOException {
        while (true) {
            String message = in.readUTF();
            System.out.println(String.format("Incoming message from %s: %s", record.getName(), message));
            if (message.equals("/end")) {
                closeConnection();
                return;
            }
            if (message.startsWith("/w")) {
                server.sendMessageOnlyOne(message.substring(message.indexOf('w') + 2), record.getName());
            } else if (message.startsWith("/nick")) {
                String[] strWithNewName = message.split("\\s");
                String oldName = record.getName();
                record.setName(strWithNewName[1]);
                sendMessage(ABOUTNIKNAME + '(' + record.getName() + ')');
                if (commitIntoDB(record, strWithNewName[1]))
                    server.broadcastMessage
                            (String.format("System info: %s has changed his nickname. New nickname %s", oldName, strWithNewName[1]));
                else sendMessage("Your nickname isn't changed.");

            } else {
                server.broadcastMessage(String.format("%s: %s", record.getName(), message));
            }
        }
    }

    public void closeConnection() {
        server.unsubscribe(this);
        try {
            server.broadcastMessage(record.getName() + " left chat");
        } catch (NullPointerException e) {
//            throw new RuntimeException("There was a crash server.broadcastMessage()");
            System.out.println("There was a crash in the broadcast messaging.");
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientHandler that = (ClientHandler) o;
        return record.equals(that.record) &&
                server.equals(that.server) &&
                socket.equals(that.socket) &&
                in.equals(that.in) &&
                out.equals(that.out);
    }

    @Override
    public int hashCode() {
        return Objects.hash(record, server, socket, in, out);
    }

    public String getRecipient() {
        return record.getName();
    }

    public boolean commitIntoDB(AuthService.Record record, String newName) {
        ClientRecord bd = new ClientRecord();
        return bd.commit(record, newName);
    }
}