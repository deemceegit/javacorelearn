package deemcee;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        PrintNumberThread t1 = new PrintNumberThread(5);
        PrintNumberThread t2 = new PrintNumberThread(5);

        t1.setName("Threadler1");
        t2.setName("Threadler2");

        t1.start();
        t2.start();

//        // 1. Tạo ra một đối tượng tác vụ
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Choose n: ");
//        int n = scanner.nextInt();
//
//        RandomNumberWriter6a task = new RandomNumberWriter6a(n);
//        // 2. Giao tác vụ đó cho một Thread (Luồng) mới và ra lệnh Bắt đầu (start)
//        Thread thread = new Thread(task);
//        thread.start();
//
//        // 3. Luồng Main (Quản lý) bắt đầu làm việc của mình: Lắng nghe bàn phím
//
//        Timer timer = new Timer();
//
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("\n" + n + "minutes over");
//                task.stopWriting();
//                timer.cancel();
//            }
//        }, n * 1000L);
//
////        System.out.println("✅ Chương trình đang chạy. Gõ 'stop' và nhấn Enter để dừng.");
////
////        while (true) {
////            String input = scanner.nextLine(); // Chờ người dùng nhập
////
////            // Nếu người dùng nhập "stop" (không phân biệt hoa thường)
////            if (input.equalsIgnoreCase("stop")) {
////                System.out.println("⚠️ [Quản lý] Đang phát lệnh dừng...");
////                task.stopWriting(); // Đổi cờ hiệu isRunning thành false
////                break; // Thoát khỏi vòng lặp vô hạn của luồng Main
////            } else {
////                System.out.println("Lệnh không hợp lệ. Gõ 'stop' để dừng.");
////            }
////        }
//
//        // Đóng scanner để giải phóng tài nguyên
//        scanner.close();
//        System.out.println("Program fully terminated");
    }
}