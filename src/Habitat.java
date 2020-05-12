import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

enum STATE {
    RUNNING,
    STOPPED,
    PAUSED,
}

public class Habitat extends JPanel {
    private long PAUSE_TIME = 0;
    private long LAST_PAUSE = 0;
    private long BEGIN_TIME = 0;
    private long END_TIME = 0;
    private long LAST_BIG_BIRD_TIME = 0;
    private long LAST_SMALL_BIRD_TIME = 0;
    private long LAST_T = 0;
    private BirdFactory birdFactory;
    private BigBirdAI bigBirdAI;
    private SmallBirdAI smallBirdAI;
    private STATE state = STATE.STOPPED;

    private Timer timer = null;
    private Thread pauseThread;

    private int N1 = 1000;
    private int N2 = 1000;
    private double P1 = 1;
    private double K = 1;
    private int PERIOD = 100;
    private int timeLife1 = 1000;
    private int timeLife2 = 1000;
    private int T = 1000;

    private String info = "";

    public Habitat(int N1, int N2, double P1, double K) {
        this.N1 = N1;
        this.N2 = N2;
        this.P1 = P1;
        this.K = K;
        BigBird.SET_IMAGE("res/BigBird.png");
        SmallBird.SET_IMAGE("res/SmallBird.png");
        bigBirdAI = new BigBirdAI();
        smallBirdAI = new SmallBirdAI();
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

    public void setTimeLife1(int timeLife1) {
        this.timeLife1 = timeLife1;
    }

    public void setTimeLife2(int timeLife2) {
        this.timeLife2 = timeLife2;
    }

    public int getTimeLife1()  {
        return timeLife1;
    }

    public int getTimeLife2() {
        return timeLife2;
    }

    public int getN1() {
        return N1;
    }

    public int getN2() {
        return N2;
    }

    public double getP1() {
        return P1;
    }

    public double getK() {
        return K;
    }

    public void setPeriod(int PERIOD) {
        this.PERIOD = PERIOD;
    }

    public void setT(int T) {
        this.T = T;
    }

    public int getT() {
        return T;
    }

    public BigBirdAI getBigBirdAI() {
        return bigBirdAI;
    }

    public SmallBirdAI getSmallBirdAI() {
        return smallBirdAI;
    }

    public void update(long time) {
        int WIDTH = this.getWidth();
        int HEIGHT = this.getHeight();
        BirdArray.getBirdArray().checkBirds(timeLife1 / 1000.0, timeLife2 / 1000.0, time / 1000.0);

        if ((time - LAST_BIG_BIRD_TIME) % N1 == 0) {
            LAST_BIG_BIRD_TIME = time;
            if ((float) Math.random() <= P1 && P1 > 0) {
                birdFactory = createBirdFactory("big");
                int imageWidth = BigBird.GET_IMAGE().getWidth(null);
                int imageHeight = BigBird.GET_IMAGE().getHeight(null);
                int cordX = (int) (Math.random() * (WIDTH + 1 - imageWidth - 10));
                int cordY = (int) (Math.random() * (HEIGHT + 1 - imageHeight - 50));
                BirdArray.getBirdArray().addBird(birdFactory.createBird(cordX, cordY), "" + time / 1000.0);
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
                BirdArray.getBirdArray().addBird(birdFactory.createBird(cordX, cordY), "" + time / 1000.0);
            }
        }

        if ((time - LAST_T) % T == 0) {
            LAST_T = time;
            int dX = (int) (Math.random() * (1+50)) - 25;
            int dY = (int) (Math.random() * (1+50)) - 25;
            BaseAI.SET_DIRECTION(dX, dY);
        }
    }

    public void beginSimulation() {
        state = STATE.RUNNING;
        BEGIN_TIME = System.currentTimeMillis() / PERIOD * PERIOD;
        LAST_PAUSE = 0;
        runTimer();
    }

    public void endSimulation() {
        state = STATE.STOPPED;
        timer.cancel();
        bigBirdAI.interrupt();
        smallBirdAI.interrupt();
        info = calculateInfo();
        BirdArray.getBirdArray().removeAllBirds();
        BigBird.ZERO_COUNT();
        SmallBird.ZERO_COUNT();
    }

    public void pauseSimulation() {
        LAST_PAUSE = PAUSE_TIME;
        state = STATE.PAUSED;
        bigBirdAI.pause();
        smallBirdAI.pause();
        timer.cancel();
        info = calculateInfo();
        long START_PAUSE = System.currentTimeMillis() / PERIOD * PERIOD;
        pauseThread = new Thread(() -> {
            while (state == STATE.PAUSED) {
                try {
                    Thread.sleep(PERIOD);
                    PAUSE_TIME = LAST_PAUSE + System.currentTimeMillis() / PERIOD * PERIOD - START_PAUSE;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        pauseThread.start();
    }

    public void continueSimulation() {
        state = STATE.RUNNING;
        runTimer();
    }

    private void runTimer() {
        bigBirdAI = new BigBirdAI();
        bigBirdAI.continue_();
        bigBirdAI.start();
        smallBirdAI = new SmallBirdAI();
        smallBirdAI.continue_();
        smallBirdAI.start();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                END_TIME = System.currentTimeMillis() / PERIOD * PERIOD - PAUSE_TIME;
                update(END_TIME - BEGIN_TIME);
                repaint();
            }
        }, 0, PERIOD);
    }

    public int getPeriod() {
        return PERIOD;
    }

    public STATE getState() {
        return state;
    }

    private String calculateInfo() {
        String resStr = "";
        resStr += "Время симуляции: ";
        resStr += (END_TIME - BEGIN_TIME) / 1000.0;
        resStr += "\nВсего птиц: ";
        resStr += (BigBird.GET_COUNT() + SmallBird.GET_COUNT());
        resStr += "\nМаленьких: ";
        resStr += SmallBird.GET_COUNT();
        resStr += "\nБольших: ";
        resStr += BigBird.GET_COUNT();
        return resStr;
    }

    public String getInfo() {
        return info;
    }

    public String getTime() {
        return "" + (END_TIME - BEGIN_TIME)/1000.0;
    }

    public double getDoubleTime() {
        return (END_TIME - BEGIN_TIME)/1000.0;
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
