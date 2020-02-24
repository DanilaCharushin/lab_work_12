import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.TimerTask;
import java.util.Timer;

public class Window extends JFrame {

    private boolean TIME_LABEL_VISIBLE = true;
    private Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
    private int WINDOW_WIDTH;
    private int WINDOW_HEIGHT;

    private JLabel timeLabel;
    private JTextArea infoArea;

    private Timer timer;
    private Habitat habitat;


    public Window(int WIDTH, int HEIGHT, Habitat habitat) {
        this.WINDOW_WIDTH = WIDTH;
        this.WINDOW_HEIGHT = HEIGHT;
        this.habitat = habitat;
        this.habitat.setFocusable(false);

        this.setTitle("Крутые птички!!!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(SCREEN.width/2 - WIDTH/2, SCREEN.height/2 - HEIGHT/2, WIDTH, HEIGHT);
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        this.setIconImage(new ImageIcon("res/favicon.png").getImage());
        this.setResizable(true);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridy = 0;
        constraints.gridx = 0;
        timeLabel = new JLabel("Время: 0");
        timeLabel.setFocusable(false);
        timeLabel.setVisible(true);
        this.add(timeLabel, constraints);

        infoArea = new JTextArea();
        infoArea.setBounds(0, 0, 100, 100);
        infoArea.setBackground(new Color(148, 220, 242));
        infoArea.setForeground(new Color(54, 77, 110));
        infoArea.setFont(new Font("Helvetica", Font.ITALIC, 10));
        infoArea.setFocusable(false);
        infoArea.setVisible(false);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridy = 1;
        this.add(infoArea, constraints);

        constraints.ipady = 500;
        constraints.ipadx = 400;
        constraints.weightx = 9;
        constraints.gridheight = 2;
        constraints.gridx = 1;
        constraints.gridy = 0;

        this.add(this.habitat, constraints);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_B:
                        START();
                        break;
                    case KeyEvent.VK_E:
                        STOP();
                        break;
                    case KeyEvent.VK_T:
                        TIME_LABEL_VISIBLE = !TIME_LABEL_VISIBLE;
                        timeLabel.setVisible(TIME_LABEL_VISIBLE);
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                }
            }
        });
        this.setVisible(true);
    }

    private void START() {
        if (habitat.beginSimulation()) {
            infoArea.setVisible(false);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    timeLabel.setVisible(TIME_LABEL_VISIBLE);
                    timeLabel.setText(habitat.getTime());
                }
            }, 0, habitat.getPeriod());
        }
    }
    private void STOP() {
        if (habitat.endSimulation()) {
            infoArea.setText(habitat.getInfo());
            infoArea.setVisible(true);
            timer.cancel();
        }
    }
}
