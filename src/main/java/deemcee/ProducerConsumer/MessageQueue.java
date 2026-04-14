package deemcee.ProducerConsumer;

import java.util.LinkedList;
import java.util.Queue;

class MessageQueue {
    private int maxSize;
    private Queue<Message> queue = new LinkedList<>();

    public MessageQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    public synchronized void put(Message msg) throws InterruptedException {
        while (queue.size() == maxSize) {
            System.out.println("QUEUE FULL...Waiting for Consumer");
            wait();   // Chờ và nhả khóa
        }

        queue.add(msg);
        System.out.println("A message ADDED: " + msg.getContent() + " | Total: " + queue.size());

        // wake Consumer to consume (currently sleeping)
        notifyAll();
    }

    public synchronized Message take() throws InterruptedException {
        while (queue.isEmpty()) {
            System.out.println("QUEUE Empty...Waiting for Producer");
            wait();
        }

        Message msg = queue.poll();
        System.out.println("A message TAKEN: " + msg.getContent() + " | Total: " + queue.size());

        notifyAll();
        return msg;
    }
}
