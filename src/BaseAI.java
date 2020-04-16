abstract public class BaseAI extends Thread {
    static int dX;
    static int dY;
    boolean running = true;
    abstract public void run();

    public static void SET_DIRECTION(int dX, int dY) {
        BaseAI.dX = dX;
        BaseAI.dY = dY;
    }

    public synchronized void changeState() {
        running = !running;
        notifyAll();
    }

    public synchronized void pause() {
        running = false;
        notifyAll();
    }

    public synchronized void continue_() {
        running = true;
        notifyAll();
    }

    public synchronized boolean isRunning() {
        return running;
    }
}
