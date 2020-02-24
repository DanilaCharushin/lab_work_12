import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class Habitat extends JComponent {
    private long BEGIN_TIME = 0;
    private long END_TIME = 0;
    private long LAST_BIG_BIRD_TIME = 0;
    private long LAST_SMALL_BIRD_TIME = 0;
    private int PERIOD;
    private BirdFactory birdFactory;
    private boolean IS_RUNNING = false;
    private Vector<Bird> birds;

    private Timer timer = null;

    private int N1, N2;
    private double P1, K;

    public Habitat(int N1, int N2, double P1, double K, int PERIOD) {
        this.N1 = N1;
        this.N2 = N2;
        this.P1 = P1;
        this.K = K;
        this.PERIOD = PERIOD;
        BigBird.SET_IMAGE("res/BigBird.png");
        SmallBird.SET_IMAGE("res/SmallBird.png");
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
                birds.addElement(birdFactory.createBird(cordX, cordY));
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
                birds.addElement(birdFactory.createBird(cordX, cordY));
            }
        }
    }

    public boolean beginSimulation() {
        if (IS_RUNNING)
            return false;
        IS_RUNNING = true;
        birds = new Vector<Bird>();
        BEGIN_TIME = System.currentTimeMillis() / PERIOD * PERIOD;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                END_TIME = System.currentTimeMillis() / PERIOD * PERIOD;
                update(END_TIME - BEGIN_TIME);
                repaint();
            }
        }, 0, PERIOD);
        return true;
    }

    public boolean endSimulation() {
        if (!IS_RUNNING)
            return false;
        IS_RUNNING = false;
        timer.cancel();
        //birds.removeAllElements();
        return true;
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
        if (birds != null) {
            for (Bird bird : birds) {
                bird.drawBird(g);
            }
        }
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
