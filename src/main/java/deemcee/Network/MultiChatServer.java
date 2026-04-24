package deemcee.Network;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MultiChatServer {
    // list to save outputStream so Serv can send message to Cli
    private static List<DataOutputStream> clientWriters = new ArrayList<>();
    public static final Logger logger = Logger.getLogger(MultiChatServer.class.getName());

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
                System.out.println("new Client connected: " + clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort());

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
            logger.log(Level.SEVERE, "Severe crash server", e);
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
                        logger.log(Level.WARNING, "Error broadcast messages", e);
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
            FileHandler fh = new FileHandler("server.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (IOException e) {
            System.out.println("can not initiate log for Server");
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
            MultiChatServer.logger.log(Level.SEVERE, "Error create IO Stream for Client " + socket.getInetAddress().toString() + ":" + socket.getPort(), e);
        }
    }

    @Override
    public void run() {
        String clientName = "";
        try {
            // Username of client
            clientName = in.readUTF();
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
            MultiChatServer.logger.info("EXITED: User '" + clientName + "' (" + socket.getInetAddress().toString() + ":" + socket.getPort() + ") quit room.");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}