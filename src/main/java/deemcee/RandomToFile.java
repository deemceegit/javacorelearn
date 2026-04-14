package deemcee;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;
import java.util.Scanner;

public class RandomToFile {
    private static volatile boolean isRunning = true;
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("RandomToFileNumber.txt"))) {
                Random random = new Random();
                System.out.println("Printing nummbers");
                while (isRunning) {
                    int number =  random.nextInt(100);
                    bw.write(String.valueOf(number));
                    bw.newLine();
                    bw.flush();
                    Thread.sleep(1000);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("RandomToFileCharacter.txt"))) {
                Random random = new Random();
                System.out.println("Printing character");
                while (isRunning) {
                    int c =  (char) (random.nextInt(26) + 'a');
                    bw.write(c);
                    bw.newLine();
                    bw.flush();
                    Thread.sleep(1000);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
        t2.start();

        Scanner sc = new Scanner(System.in);
        System.out.println("Type stop to stop");
        while(true) {
            String input = sc.nextLine();
            if (input.equals("stop")) {
                isRunning = false;
                break;
            }else{
                System.out.println("wrong command");
            }
        }
        sc.close();
        
    }
}
