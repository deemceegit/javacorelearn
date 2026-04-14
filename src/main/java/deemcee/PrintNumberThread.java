package deemcee;

import java.io.BufferedReader;
import java.io.FileReader;

public class PrintNumberThread extends RunThread {
    public PrintNumberThread(int n) {
        super(n);
    }

    @Override
    void execute() {
        System.out.println(Thread.currentThread().getName() + " - " + System.currentTimeMillis());
    }
}
