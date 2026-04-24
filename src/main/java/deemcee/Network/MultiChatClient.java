package deemcee.Network;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MultiChatClient {
    private static final Logger logger = Logger.getLogger(MultiChatClient.class.getName());

    public static void main(String[] args) {
        setupLogger();
        String ip = "127.0.0.1";
        int port = 5000;
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String myName = scanner.nextLine();

        try (Socket socket = new Socket(ip, port)) {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // send name to Server first
            out.writeUTF(myName);
            out.flush();
            System.out.println("start chatting");

            logger.info("CONNECTED: '" + myName + "' connected successfully to Server " + ip + ":" + port);

            // thread to listen
            Thread readThread = new Thread(() -> {
                try {
                    while (true) {
                        String incomingMsg = in.readUTF();
                        System.out.println("\r" + incomingMsg);
                        System.out.print("[" + myName + "(Me)]: ");
                    }
                } catch (IOException e) {
                    System.out.println("\nDisconnected from server.");
                    logger.warning("DISCONNECTED: " + e.getMessage());
                }
            });
            readThread.start();

            // Main thread: read message from keyboard
            while (true) {
                System.out.print("[" + myName + "(Me)]: ");
                String outgoingMsg = scanner.nextLine();

                if (outgoingMsg.equalsIgnoreCase("quit")) {
                    System.out.println("Exiting...");
                    logger.info("QUIT: '" + myName + "' quit the chatroom");
                    break;
                }

                out.writeUTF(outgoingMsg);
                out.flush();
            }

            socket.close();
            System.exit(0);

        } catch (IOException e) {
            System.out.println("Cannot connect to Server");
            logger.log(Level.SEVERE, "CONNECTION FAILED: Unable to connect to Server " + ip + ":" + port, e);
        }
    }

    private static void setupLogger() {
        try {
            FileHandler fh = new FileHandler("client.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (IOException e) {
            System.out.println("can not initiate log for Client.");
        }
    }
}

//read username from keyboard
//connect 'socket' to Server (IP, port)
//get IO streams
//send username to Server first
//run thread 'readThread' (to listen to Server)
//      loop continuously:
//          read incoming message from Server stream -> print to screen
//[Main Thread] loop continuously (to send message)
//      read message from keyboard
//      if message == "quit" -> break loop
//      write message to Server stream
//close socket & exit