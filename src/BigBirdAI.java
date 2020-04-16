public class BigBirdAI extends BaseAI {
    public BigBirdAI() {
        Thread.currentThread().setName("BigBirdAI");
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                BirdArray.getBirdArray().moveBigBirds(dX, dY);
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
