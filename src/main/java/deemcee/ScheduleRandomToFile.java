package deemcee;

import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleRandomToFile {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter interval between each digit to print (second): ");
        int n = sc.nextInt();

        System.out.println("Enter time to stop program (minute): ");
        int m = sc.nextInt();

        Timer timer = new Timer();
        Random random = new Random();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println(random.nextInt(100));
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, n * 1000L);

        TimerTask stopTask = new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
            }
        };
        timer.schedule(stopTask, m * 60 * 1000L);

        sc.close();
    }
}
