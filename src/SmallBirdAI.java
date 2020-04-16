public class SmallBirdAI extends BaseAI {
    public SmallBirdAI() {
        Thread.currentThread().setName("SmallBirdAI");
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                BirdArray.getBirdArray().moveSmallBirds(dX, dY);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                continue;
            }

            synchronized (this) {
                while (!running) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        continue;
                    }
                }
            }
        }
    }
}
