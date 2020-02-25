import javafx.scene.control.ComboBox;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Vector;

public class Window extends JFrame {

    private boolean TIME_LABEL_VISIBLE = true;
    private Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
    private int WINDOW_WIDTH;
    private int WINDOW_HEIGHT;

    private InfoDialog infoDialog;
    private JPanel menuPanel;
    private JButton buttonStart;
    private JButton buttonStop;
    private JLabel timeLabel;

    private JTextField textFieldN1;
    private JTextField textFieldN2;

    private JComboBox comboBoxP1;
    private JList listK;

    private JCheckBox INFO_DIALOG_VISIBLE;
    private ButtonGroup timeGroup;
    private JRadioButton timeVisibleTrue;
    private JRadioButton timeVisibleFalse;

    private Timer timer;
    private Habitat habitat;



    public Window(int WIDTH, int HEIGHT) {
        this.WINDOW_WIDTH = WIDTH;
        this.WINDOW_HEIGHT = HEIGHT;
        this.habitat = new Habitat();
        this.habitat.setFocusable(false);

        this.setTitle("Крутые птички!!!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(SCREEN.width/2 - WIDTH/2, SCREEN.height/2 - HEIGHT/2, WIDTH, HEIGHT);
        this.setLayout(new BorderLayout());
        this.setIconImage(new ImageIcon("res/favicon.png").getImage());
        this.setResizable(true);

        menuPanel = new JPanel(new GridLayout(5, 2));
        timeLabel = new JLabel("Время: 0");

        buttonStart = new JButton("START");
        buttonStop = new JButton("STOP");
        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);
        buttonStart.setFocusable(false);
        buttonStop.setFocusable(false);
        buttonStart.addActionListener(actionEvent -> START());
        buttonStop.addActionListener(actionEvent -> PAUSE());

        INFO_DIALOG_VISIBLE = new JCheckBox("Отобразить информационное окно");
        INFO_DIALOG_VISIBLE.setSelected(true);
        INFO_DIALOG_VISIBLE.setFocusable(false);

        INFO_DIALOG_VISIBLE.addItemListener(itemEvent -> {
            infoDialog.setVisible(((JCheckBox)itemEvent.getItem()).isSelected());
        });

        timeGroup = new ButtonGroup();
        timeVisibleTrue = new JRadioButton("Отображать время симуляции");
        timeVisibleTrue.setSelected(true);
        timeVisibleTrue.setFocusable(false);
        timeVisibleFalse = new JRadioButton("Не отображать время симуляции");
        timeVisibleFalse.setFocusable(false);
        timeGroup.add(timeVisibleTrue);
        timeGroup.add(timeVisibleFalse);

        textFieldN1 = new JTextField("N1");
        textFieldN2 = new JTextField("N2");
        //textFieldN1.setFocusable(false);
        //textFieldN2.setFocusable(false);

        comboBoxP1 = new JComboBox();
        comboBoxP1.setFocusable(false);
        for (int i = 0; i <= 100; i += 10) {
            comboBoxP1.addItem("" + i + "%");
        }
        comboBoxP1.addItemListener(itemEvent -> {

        });

        listK = new JList();
        //listK.setFocusable(false);
        Vector<String> listData = new Vector<String>();
        for (int i = 0; i <= 100; i += 10) {
            listData.add("" + i + "%");
        }
        listK.setListData(listData);
        listK.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listK.addListSelectionListener(listSelectionEvent -> {

        });


        menuPanel.add(buttonStart);
        menuPanel.add(buttonStop);
        menuPanel.add(timeLabel);
        menuPanel.add(INFO_DIALOG_VISIBLE);
        menuPanel.add(timeVisibleTrue);
        menuPanel.add(timeVisibleFalse);
        menuPanel.add(textFieldN1);
        menuPanel.add(textFieldN2);
        menuPanel.add(comboBoxP1);
        menuPanel.add(listK);
        menuPanel.setFocusable(false);

        this.add(habitat);
        this.add(menuPanel, BorderLayout.WEST);
        menuPanel.setFocusable(true);
        menuPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_B:
                        START();
                        break;
                    case KeyEvent.VK_E:
                        PAUSE();
                        break;
                    case KeyEvent.VK_T:
                        TIME_LABEL_VISIBLE = !TIME_LABEL_VISIBLE;
                        timeLabel.setVisible(TIME_LABEL_VISIBLE);
                        if (TIME_LABEL_VISIBLE) {
                            timeVisibleTrue.setSelected(true);
                            timeVisibleFalse.setSelected(false);
                        } else {
                            timeVisibleTrue.setSelected(false);
                            timeVisibleFalse.setSelected(true);
                        }

                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                }
            }
        });
        this.setVisible(true);

        infoDialog = new InfoDialog("Информация о симуляции");
    }

    private void START() {
        if (habitat.beginSimulation()) {
            buttonStart.setEnabled(false);
            buttonStop.setEnabled(true);
            runTimer();
        }
    }
    private void PAUSE() {
        timer.cancel();
        habitat.pauseSimulation();
        infoDialog.setText(habitat.getInfo());
        infoDialog.setVisible(INFO_DIALOG_VISIBLE.isSelected());
    }
    private void CONTINUE() {
        runTimer();
        habitat.continueSimulation();
        infoDialog.setVisible(false);
    }
    private void STOP() {
        if (habitat.endSimulation()) {
            buttonStart.setEnabled(true);
            buttonStop.setEnabled(false);
            infoDialog.setVisible(false);
        }
    }

    private void runTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeLabel.setVisible(TIME_LABEL_VISIBLE);
                timeLabel.setText(habitat.getTime());
                menuPanel.setFocusable(true);
                habitat.setN1(Integer.parseInt(textFieldN1.getText()));
                habitat.setN2(Integer.parseInt(textFieldN2.getText()));
                habitat.setP1(comboBoxP1.getSelectedIndex()*0.1);
                habitat.setK(listK.getSelectedIndex()*0.1);
            }
        }, 0, habitat.getPeriod());
    }

    private class InfoDialog extends JDialog {

        private JTextArea infoArea;
        private JButton buttonOK;
        private JButton buttonCancel;
        private int DIALOG_WIDTH = 200;
        private int DIALOG_HEIGHT = 200;

        public InfoDialog (String title) {
            this.setTitle(title);
            this.setBounds(SCREEN.width/2 + WINDOW_WIDTH/2, SCREEN.height/2 - WINDOW_HEIGHT/2, DIALOG_WIDTH, DIALOG_HEIGHT);
            this.setLayout(new GridLayout(2,1));

            infoArea = new JTextArea();
            infoArea.setBounds(0, 0, 100, 100);
            infoArea.setBackground(new Color(148, 220, 242));
            infoArea.setForeground(new Color(54, 77, 110));
            infoArea.setFont(new Font("Helvetica", Font.ITALIC, 10));
            infoArea.setFocusable(false);

            buttonOK = new JButton("OK");
            buttonCancel = new JButton("Отмена");

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(buttonOK);
            buttonPanel.add(buttonCancel);

            buttonOK.addActionListener(actionEvent -> CONTINUE());
            buttonCancel.addActionListener(actionEvent -> STOP());

            this.add(infoArea);
            this.add(buttonPanel);
        }

        public void setText(String text) {
            infoArea.setText(text);
        }
    }
}


