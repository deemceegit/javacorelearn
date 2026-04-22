package deemcee.Network;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MultiChatServer {
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    // list to save outputStream so Serv can send message to Cli
    private static List<DataOutputStream> clientWriters = new ArrayList<>();

    public static void main(String[] args) {
        setupLogger();
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server run on port " + port);
            logger.info("Server run on port " + port);

            // Constantly waiting to host clients
            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("accept Client socket: " + clientSocket + clientSocket.getInetAddress());
                System.out.println("new Client connected: " + clientSocket.getInetAddress());

                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                // add writer of a Client to the list
                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                // dispatch a Handler to handle the Client
                ClientHandler handler = new ClientHandler(clientSocket, out);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.out.println("Server Error: " + e.getMessage());
        }
    }

    // broadcast to all clients
    public static void broadcastMessage(String message, DataOutputStream excludeOut) {
        synchronized (clientWriters) {
            for (DataOutputStream out : clientWriters) {
                // not send again to client that sent
                if (out != excludeOut) {
                    try {
                        out.writeUTF(message);
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // remove Client if client exit
    public static void removeClient(DataOutputStream out) {
        synchronized (clientWriters) {
            clientWriters.remove(out);
        }
    }

    private static void setupLogger() {
        try {
            FileHandler fh = new FileHandler("server_error.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (IOException e) {
            System.out.println("Không thể thiết lập file log cho Server.");
        }
    }
}

// handler (run on each thread for each client)
class ClientHandler implements Runnable {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(Socket socket, DataOutputStream out) {
        this.socket = socket;
        this.out = out;
        try {
            this.in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Username of client
            String clientName = in.readUTF();
            MultiChatServer.broadcastMessage("--- " + clientName + " joined chat ---", out);

            // constantly listen to client message
            while (true) {
                String message = in.readUTF();
                // broadcast message to everyone
                MultiChatServer.broadcastMessage("[" + clientName + "]: " + message, out);
            }
        } catch (IOException e) {
            System.out.println("a Client disconnected");
        } finally {
            // client exit
            MultiChatServer.removeClient(out);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}