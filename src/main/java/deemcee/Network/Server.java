package deemcee.Network;

import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.*;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public Server() {
        setupLogger();
        Properties config = loadConfig();

        int port = Integer.parseInt(config.getProperty("port", "5000"));
        int receiveTimeout = Integer.parseInt(config.getProperty("receive_timeout", "10000"));

        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Server đã khởi động trên port: " + port);
            System.out.println("Đang chờ client kết nối...");

            // Lắng nghe kết nối
            try (Socket s = ss.accept()) {
                System.out.println("Client đã kết nối: " + s.getInetAddress());

                // Thiết lập timeout khi nhận dữ liệu
                s.setSoTimeout(receiveTimeout);

                DataInputStream in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                DataOutputStream out = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
                Scanner scanner = new Scanner(System.in);

                Thread thread = new Thread(() -> {
                    while (true) {
                        try {
                            String receivedMsg = in.readUTF();
                            System.out.println("\n[Client]: " + receivedMsg);

                            if (receivedMsg.equalsIgnoreCase("Over")) {
                                break;
                            }
                        } catch (SocketTimeoutException ste) {
                            System.out.println("Quá thời gian nhận dữ liệu (Timeout). Đang chờ tiếp...");
                        } catch (EOFException eof) {
                            System.out.println("Client đã ngắt kết nối.");
                            break;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                thread.start();

                while (true) {
                    System.out.print("[Me]: ");
                    String sentMsg = scanner.nextLine(); // Cấu trúc tự tạo: chuỗi ngẫu nhiên dài 10 ký tự
//                    System.out.println("Đang gửi: " + sentMsg);

                    out.writeUTF(sentMsg);
                    out.flush();

                    if (sentMsg.equalsIgnoreCase("Over")) {
                        break;
                    }
                }
                s.close();
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println("Lỗi Server: " + e.getMessage());
            logger.log(Level.SEVERE, "Lỗi Server", e);
        }
        System.out.println("Đóng Server.");
    }

    private Properties loadConfig() {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
        } catch (IOException ex) {
            System.out.println("Không tìm thấy file config.properties, dùng cấu hình mặc định.");
            logger.log(Level.WARNING, "Không thể tải config", ex);
        }
        return prop;
    }

    private void setupLogger() {
        try {
            FileHandler fh = new FileHandler("server_error.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (IOException e) {
            System.out.println("Không thể thiết lập file log cho Server.");
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}

/**
 * Server connect ServerSocket: wait client requests + use [s] to comm w/ client
 * [in] take input d client socket
 * [in] readUTF until "Over"
 * close
 */

