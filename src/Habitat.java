import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class Habitat extends JFrame
{
    private int WINDOW_WIDTH;
    private int WINDOW_HEIGHT;
    private long BEGIN_TIME = 0;
    private long END_TIME = 0;
    private long LAST_BIG_BIRD_TIME = 0;
    private long LAST_SMALL_BIRD_TIME = 0;
    private int PERIOD = 10;
    private boolean IS_RUNNING = false;
    private boolean LABEL_VISIBLE = true;
    private JLabel label = null;
    private JTextArea area = null;

    private Timer timer = null;
    private Vector<Bird> birds = null;

    private int N1, N2;
    private double P1, K;

    public Habitat(int WINDOW_WIDTH, int WINDOW_HEIGHT, int N1, int N2, double P1, double K) {
        super("Birds simulation. LABA 1");
        this.WINDOW_WIDTH = WINDOW_WIDTH;
        this.WINDOW_HEIGHT = WINDOW_HEIGHT;
        this.N1 = N1;
        this.N2 = N2;
        this.P1 = P1;
        this.K = K;
        this.setBounds(0,0, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BigBird.setImage("res/BigBird.png");
        SmallBird.setImage("res/SmallBird.png");
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 66 && !IS_RUNNING) { // B
                    IS_RUNNING = true;
                    System.out.println("Begin simulation");
                    beginSimulation();
                } else if (keyEvent.getKeyCode() == 69 && IS_RUNNING) { // E
                    IS_RUNNING = false;
                    System.out.println("End simulation");
                    endSimulation();
                } else if (keyEvent.getKeyCode() == 84) {
                    LABEL_VISIBLE = !LABEL_VISIBLE;
                    label.setVisible(LABEL_VISIBLE);
                }
            }
        });
        label = new JLabel("Время: ");
        area = new JTextArea();
        area.setBounds(0, 0,120, 100);
        area.setBackground(new Color(0,200,200));
        area.setFocusable(false);
        area.setVisible(false);
        this.add(area);
        this.add(label);
        this.setVisible(true);
    }

    private void update(long time) {
        time /= 10;
        time *= 10;
        BirdFactory birdFactory = null;
        int imageWidth;
        int imageHeight;
        int cordX;
        int cordY;
        if ((time - LAST_BIG_BIRD_TIME) % N1 == 0) {
            LAST_BIG_BIRD_TIME = time;
            if ((float)Math.random() <= P1 && P1 > 0) {
                birdFactory = createBirdFactory("big");
                imageWidth = BigBird.GET_IMAGE().getWidth(null);
                imageHeight = BigBird.GET_IMAGE().getHeight(null);
                cordX = (int) (Math.random()*(WINDOW_WIDTH + 1 - imageWidth - 10));
                cordY = (int) (Math.random()*(WINDOW_HEIGHT + 1 - imageHeight - 50));
                birds.addElement(birdFactory.createBird(cordX, cordY));
            }
        }
        if ((time - LAST_SMALL_BIRD_TIME) % N2 == 0) {
            LAST_SMALL_BIRD_TIME = time;
            if (SmallBird.GET_COUNT() < BigBird.GET_COUNT() * K) {
                birdFactory = createBirdFactory("small");
                imageWidth = SmallBird.GET_IMAGE().getWidth(null);
                imageHeight = SmallBird.GET_IMAGE().getHeight(null);
                cordX = (int) (Math.random()*(WINDOW_WIDTH + 1 - imageWidth - 10));
                cordY = (int) (Math.random()*(WINDOW_HEIGHT + 1 - imageHeight - 50));
                birds.addElement(birdFactory.createBird(cordX, cordY));
            }
        }
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        if (birds != null) {
            for (Bird bird : birds) {
                this.add(bird);
                bird.setVisible(true); // TODO
                this.setVisible(true);
            }
        }
    }

    private void beginSimulation() {
        birds = new Vector<Bird>();
        timer = new Timer();
        area.setVisible(false);
        BEGIN_TIME = System.currentTimeMillis();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                update(END_TIME - BEGIN_TIME);
            }
        }, 0, PERIOD);

        new Thread(() -> {
            while (IS_RUNNING) {
                try {
                    Thread.sleep(PERIOD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                END_TIME = System.currentTimeMillis();
                label.setText("Время: " + (END_TIME - BEGIN_TIME) / 1000.0);
            }
        }).start();
    }

    private void endSimulation() {
        timer.cancel();
        String resStr = "";
        resStr += "Simulation time: ";
        resStr += (END_TIME - BEGIN_TIME)/1000.0;
        resStr += "\nCount of Birds: ";
        resStr += (BigBird.GET_COUNT() + SmallBird.GET_COUNT());
        resStr += "\nCount of SmallBirds: ";
        resStr += SmallBird.GET_COUNT();
        resStr += "\nCount of BigBirds: ";
        resStr += BigBird.GET_COUNT();
        BigBird.ZERO_COUNT();
        SmallBird.ZERO_COUNT();
        for (Bird bird : birds) {
            bird.setVisible(false);
            this.remove(bird);
        }
        birds.clear();
        area.setFont(new Font("Times Roman", Font.BOLD,10));
        area.setText(resStr);
        area.setVisible(true);
    }

    private BirdFactory createBirdFactory(String bird) {
        if (bird.equalsIgnoreCase("small")) {
            return new SmallBirdFactory();
        } else if (bird.equalsIgnoreCase("big")) {
            return  new BigBirdFactory();
        } else {
            throw new RuntimeException(bird + " is unknown bird");
        }
    }
}
