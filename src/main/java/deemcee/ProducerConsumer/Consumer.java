package deemcee.ProducerConsumer;

class Consumer implements Runnable {
    private MessageQueue queue;
    public Consumer(MessageQueue queue) {
        this.queue = queue;
    }

    //consuming task
    @Override
    public void run() {
        try {
            Thread.sleep(10000L);
            while (true) {
                Message msg = queue.take();     //.poll() + return msg
                Thread.sleep(10000L);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Consumer interrupted");
        }
    }
}
