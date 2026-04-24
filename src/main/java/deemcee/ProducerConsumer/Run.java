package deemcee.ProducerConsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Run {
    public static void main(String[] args) {
        MessageQueue messageQueue = new MessageQueue(10);

//        Thread producerThread = new Thread(new Producer(messageQueue));
//        Thread consumerThread = new Thread(new Consumer(messageQueue));
//
//        producerThread.start();
//        consumerThread.start();

        ExecutorService pool = Executors.newFixedThreadPool(5);

//        ExecutorService producerPool = Executors.newFixedThreadPool(2);
//        ExecutorService consumerPool = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 2; i++) {
            pool.execute(new Producer(messageQueue));
        }

        for (int i = 0; i < 3; i++) {
            pool.execute(new Consumer(messageQueue));
        }

        pool.shutdown();
    }
}
