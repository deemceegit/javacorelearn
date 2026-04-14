package deemcee;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomNumberWriter6a implements Runnable {
    // Cờ hiệu (flag) để kiểm soát việc dừng chương trình.
    // Từ khóa 'volatile' rất quan trọng trong đa luồng: Nó đảm bảo khi luồng Main đổi giá trị thành false, luồng này sẽ nhận được thông báo ngay lập tức.
    private volatile boolean isRunning = true;
    private int n;
    private int count = 0;
    public RandomNumberWriter6a(int n) {
        this.n = n;
    }

    // Phương thức để luồng Main gọi khi muốn dừng
    public void stopWriting() {
        this.isRunning = false;
    }

    @Override
    public void run() {
        Random random = new Random();

        // Sử dụng try-with-resources để tự động đóng file (close) khi hoàn thành hoặc có lỗi
        // Tham số 'true' trong FileWriter("output.txt", true) có nghĩa là ghi tiếp (append) vào cuối file nếu file đã tồn tại.
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output.txt", true))) {

            System.out.println("Đang bắt đầu ghi số ra file output.txt...");

            while (isRunning && count <= n) {
                int randomNumber = random.nextInt(1000); // Sinh số ngẫu nhiên từ 0 đến 999

                bufferedWriter.write(String.valueOf(randomNumber));
                bufferedWriter.newLine(); // Xuống dòng
                bufferedWriter.flush();   // Đẩy dữ liệu từ bộ đệm (RAM) xuống thẳng ổ cứng (File) ngay lập tức

                count++;
                // Cho công nhân nghỉ ngơi 1 giây (1000 millisecond) sau mỗi lần ghi
                // Việc này giúp bạn dễ quan sát file lớn lên từ từ, không bị treo máy
                Thread.sleep(1000);
            }
            if (count >= n) {
                System.out.println("🛑 [Công nhân] Đã ghi đủ " + n + " số. Tự động nghỉ ngơi.");
            } else {
                System.out.println("🛑 [Công nhân] Nhận được lệnh hết giờ từ Quản lý. Ngừng ghi.");
            }

        } catch (IOException e) {
            System.out.println("❌ Lỗi khi ghi file: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("❌ Luồng bị ngắt quãng: " + e.getMessage());
        }
    }
}