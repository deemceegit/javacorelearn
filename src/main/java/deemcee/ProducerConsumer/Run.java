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

        pool.execute(new Producer(messageQueue));
        pool.execute(new Producer(messageQueue));

        pool.execute(new Consumer(messageQueue));
        pool.execute(new Consumer(messageQueue));
        pool.execute(new Consumer(messageQueue));

        pool.shutdown();
    }
}
