package deemcee.ProducerConsumer;

class Producer implements Runnable {
    private MessageQueue queue;
    public Producer(MessageQueue queue) {
        this.queue = queue;
    }

    //producing task
    @Override
    public void run() {
        int i = 1;
        try {
            while (true) {
                Message msg = new Message("Message " + String.valueOf(i++));
                queue.put(msg);     //.add()
                Thread.sleep(2000L);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Producer interrupted");
        }
    }
}
