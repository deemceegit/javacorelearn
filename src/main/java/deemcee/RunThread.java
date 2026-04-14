package deemcee;

public abstract class RunThread extends Thread {
    private boolean isRunning = true;
    private int count = 0;
    private int n;

    public RunThread(int n) {
        this.n = n;
    }

    @Override
    public void run() {
        while (isRunning && count < n) {
            execute();
            count++;
            try {
                Thread.sleep(500); // nghỉ 0.5 giây cho dễ quan sát
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRunning() {
        isRunning = false;
    }

    abstract void execute();
}
