import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Habitat extends JComponent {
    private long BEGIN_TIME = 0;
    private long END_TIME = 0;
    private long LAST_BIG_BIRD_TIME = 0;
    private long LAST_SMALL_BIRD_TIME = 0;
    private BirdFactory birdFactory;
    private boolean IS_RUNNING = false;

    private Timer timer = null;

    private int N1 = 500;
    private int N2 = 500;
    private double P1 = 1;
    private double K = 0.5;
    private int PERIOD = 100;

    public Habitat() {
        this.N1 = N1;
        this.N2 = N2;
        this.P1 = P1;
        this.K = K;
        this.PERIOD = PERIOD;
        BigBird.SET_IMAGE("res/BigBird.png");
        SmallBird.SET_IMAGE("res/SmallBird.png");
    }

    public void setN1(int n1) {
        N1 = n1;
    }

    public void setN2(int n2) {
        N2 = n2;
    }

    public void setP1(double p1) {
        P1 = p1;
    }

    public void setK(double k) {
        K = k;
    }

    public void setPERIOD(int PERIOD) {
        this.PERIOD = PERIOD;
    }

    public void update(long time) {
        int WIDTH = this.getWidth();
        int HEIGHT = this.getHeight();

        if ((time - LAST_BIG_BIRD_TIME) % N1 == 0) {
            LAST_BIG_BIRD_TIME = time;
            if ((float) Math.random() <= P1 && P1 > 0) {
                birdFactory = createBirdFactory("big");
                int imageWidth = BigBird.GET_IMAGE().getWidth(null);
                int imageHeight = BigBird.GET_IMAGE().getHeight(null);
                int cordX = (int) (Math.random() * (WIDTH + 1 - imageWidth - 10));
                int cordY = (int) (Math.random() * (HEIGHT + 1 - imageHeight - 50));
                BirdArray.getBirdArray().addBird(birdFactory.createBird(cordX, cordY));
            }
        }

        if ((time - LAST_SMALL_BIRD_TIME) % N2 == 0) { // каждые N2 секунд рождается маленькая птичка
            LAST_SMALL_BIRD_TIME = time;
            if (SmallBird.GET_COUNT() < BigBird.GET_COUNT() * K) { // условие рождения маленькой птички
                birdFactory = createBirdFactory("small");
                int imageWidth = SmallBird.GET_IMAGE().getWidth(null);
                int imageHeight = SmallBird.GET_IMAGE().getHeight(null);
                int cordX = (int) (Math.random() * (WIDTH + 1 - imageWidth - 10));
                int cordY = (int) (Math.random() * (HEIGHT + 1 - imageHeight - 50));
                BirdArray.getBirdArray().addBird(birdFactory.createBird(cordX, cordY));
            }
        }
    }

    public boolean beginSimulation() {
        if (IS_RUNNING)
            return false;
        IS_RUNNING = true;

        BEGIN_TIME = System.currentTimeMillis() / PERIOD * PERIOD;
        runTimer();
        return true;
    }

    public boolean endSimulation() {
        if (!IS_RUNNING)
            return false;
        IS_RUNNING = false;
        timer.cancel();
        BirdArray.getBirdArray().removeAllBirds();
        return true;
    }

    public void pauseSimulation() {
        timer.cancel();
    }

    public void continueSimulation() {
        runTimer();
    }

    private void runTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                END_TIME = System.currentTimeMillis() / PERIOD * PERIOD;
                update(END_TIME - BEGIN_TIME);
                repaint();
            }
        }, 0, PERIOD);
    }

    public int getPeriod() {
        return PERIOD;
    }

    public String getInfo() {
        String resStr = "";
        resStr += "Время симуляции: ";
        resStr += (END_TIME - BEGIN_TIME) / 1000.0;
        resStr += "\nВсего птиц: ";
        resStr += (BigBird.GET_COUNT() + SmallBird.GET_COUNT());
        resStr += "\nМаленьких: ";
        resStr += SmallBird.GET_COUNT();
        resStr += "\nБольших: ";
        resStr += BigBird.GET_COUNT();
        BigBird.ZERO_COUNT();
        SmallBird.ZERO_COUNT();
        return resStr;
    }

    public String getTime() {
        return "" + (END_TIME - BEGIN_TIME)/1000.0;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        BirdArray.getBirdArray().paintBirds(g);
    }

    private static BirdFactory createBirdFactory(String bird) {
        if (bird.equalsIgnoreCase("small")) {
            return new SmallBirdFactory();
        } else if (bird.equalsIgnoreCase("big")) {
            return new BigBirdFactory();
        } else {
            throw new RuntimeException(bird + " is unknown bird");
        }
    }
}
