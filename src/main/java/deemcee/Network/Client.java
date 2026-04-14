package deemcee.Network;

import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.*;

public class Client {
    private static final Logger logger = Logger.getLogger(Client.class.getName());

    public Client() {
        setupLogger();
        Properties config = loadConfig();

        String ip = config.getProperty("ip", "127.0.0.1");
        int port = Integer.parseInt(config.getProperty("port", "5000"));
        int connectTimeout = Integer.parseInt(config.getProperty("connection_timeout", "5000"));
        int sendTimeout = Integer.parseInt(config.getProperty("send_timeout", "5000"));

        // Sử dụng try-with-resources để tự động đóng socket
        try (Socket s = new Socket(ip, port)) {
            System.out.println("Đang kết nối tới Server...");
            // Thiết lập connection timeout
//            s.connect(new InetSocketAddress(ip, port), connectTimeout);
            // Thiết lập timeout cho socket (áp dụng chủ yếu cho Read, nhưng cấu hình SO_TIMEOUT giúp giới hạn block)
//            s.setSoTimeout(sendTimeout);
            System.out.println("Đã kết nối tới Server thành công!");

            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            Scanner scanner = new Scanner(System.in);

            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        String receivedMsg = in.readUTF();
                        System.out.println("\n[Server]: " + receivedMsg);
                    }
                } catch (IOException e) {
                    System.out.println("\nMáy A đã ngắt kết nối.");
                }
            });
            thread.start();

            // Gửi dữ liệu liên tục
            while (true) {
                System.out.print("[Me]: ");
                String sentMsg = scanner.nextLine(); // // Cấu trúc tự tạo: chuỗi ngẫu nhiên dài 10 ký tự
//                System.out.println("Đang gửi: " + sentMsg);
                out.writeUTF(sentMsg);
                out.flush();

                // Dừng 1 giây giữa các lần gửi để bạn có thời gian "rút dây mạng" test thử
                if (sentMsg.equalsIgnoreCase("Over")) break;

            }
            s.close();
            System.exit(0);

        } catch (SocketTimeoutException ste) {
            System.out.println("Lỗi: Quá thời gian kết nối hoặc gửi dữ liệu (Timeout).");
            logger.log(Level.SEVERE, "Lỗi Timeout", ste);
        } catch (IOException e) {
            System.out.println("Lỗi mạng: Mất kết nối hoặc cáp bị rút (" + e.getMessage() + ")");
            logger.log(Level.SEVERE, "Lỗi Mất kết nối", e);
        }
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
            FileHandler fh = new FileHandler("client_error.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (IOException e) {
            System.out.println("Không thể thiết lập file log cho Client.");
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}

/**
 * Client connect socket + input from terminal ([in]) + output to socket ([out])
 * [in] read until "Over"
 * [out] utf [in]
 * close
 */
