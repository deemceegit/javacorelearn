package deemcee.Network;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MultiChatClient {
    public static void main(String[] args) {
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
                }
            });
            readThread.start();

            // Main thread: read message from keyboard
            while (true) {
                System.out.print("[" + myName + "(Me)]: ");
                String outgoingMsg = scanner.nextLine();

                if (outgoingMsg.equalsIgnoreCase("quit")) {
                    System.out.println("Exiting...");
                    break;
                }

                out.writeUTF(outgoingMsg);
                out.flush();
            }

            socket.close();
            System.exit(0);

        } catch (IOException e) {
            System.out.println("Cannot connect to Server");
        }
    }
}