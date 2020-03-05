import javafx.scene.control.ComboBox;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
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
    private int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    private int WINDOW_WIDTH;
    private int WINDOW_HEIGHT;

    private InfoDialog infoDialog;
    private ErrorDialog errorDialog;
    private ControlPanel controlPanel;

    private Timer timer;
    private Habitat habitat;

    public Window(int WIDTH, int HEIGHT) {
        this.WINDOW_WIDTH = WIDTH;
        this.WINDOW_HEIGHT = HEIGHT;
        setTitle("Крутые птички!!!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(SCREEN_WIDTH / 2 - WINDOW_WIDTH / 2, SCREEN_HEIGHT / 2 - WINDOW_HEIGHT / 2, WINDOW_WIDTH, WINDOW_HEIGHT);
        setLayout(new BorderLayout());
        setIconImage(new ImageIcon("res/favicon.png").getImage());
        setResizable(true);

        add((controlPanel = new ControlPanel()), BorderLayout.WEST);

        habitat = new Habitat(1000, 1000, 1, 1);
        habitat.setFocusable(false);
        add(habitat);

        addKeyListener(new KeyAdapter() {
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
                        /*controlPanel.getTimeLabel().setVisible(TIME_LABEL_VISIBLE);
                        controlPanel.resetTimeVisibleGroup();
                        if (TIME_LABEL_VISIBLE) {
                            timeVisibleTrue.setSelected(true);
                            timeVisibleFalse.setSelected(false);
                        } else {
                            timeVisibleTrue.setSelected(false);
                            timeVisibleFalse.setSelected(true);
                        }*/

                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                }
            }
        });

        setVisible(true);
        infoDialog = new InfoDialog("Информация о симуляции");
    }


    private void START() {
        if (habitat.beginSimulation()) {
            //controlPanel.getButtonStart().setEnabled(false);
            //controlPanel.getButtonStop().setEnabled(true);
            runTimer();
        }
    }

    private void PAUSE() {
        timer.cancel();
        habitat.pauseSimulation();
        //infoDialog.setText(habitat.getInfo());
        //infoDialog.setVisible(INFO_DIALOG_VISIBLE.isSelected());
    }

    private void CONTINUE() {
        runTimer();
        habitat.continueSimulation();
        //infoDialog.setVisible(false);
    }

    private void STOP() {
        if (habitat.endSimulation()) {
            //controlPanel.getButtonStart().setEnabled(true);
            //controlPanel.getButtonStop().setEnabled(false);
            //infoDialog.setVisible(false);
        }
    }

    private void runTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                /*controlPanel.getTimeLabel().setVisible(TIME_LABEL_VISIBLE);
                controlPanel.getTimeLabel().setText(habitat.getTime());
                //menuPanel.setFocusable(true);
                habitat.setN1(controlPanel.getN1());
                habitat.setN2(controlPanel.getN2());*/
            }
        }, 0, habitat.getPeriod());
    }


    //TODO
    private class ControlPanel extends JPanel {
        private ButtonPanel buttonPanel;
        private CheckBoxPanel checkBoxPanel;
        private RadioButtonPanel radioButtonPanel;
        private TextFieldPanel textFieldPanel;
        private ComboBoxListPanel comboBoxListPanel;
        private SliderPanel sliderPanel;

        public ControlPanel() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.weighty = 0.33;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(4, 4, 4, 4);

            add((buttonPanel = new ButtonPanel()), gbc);
            gbc.gridy++;
            add((checkBoxPanel = new CheckBoxPanel()), gbc);
            gbc.gridy++;
            add((radioButtonPanel = new RadioButtonPanel()), gbc);
            gbc.gridy++;
            add((textFieldPanel = new TextFieldPanel()), gbc);
            gbc.gridy++;
            add((comboBoxListPanel = new ComboBoxListPanel()), gbc);
            gbc.gridy++;
            add((sliderPanel = new SliderPanel()), gbc);
        }

        private class ButtonPanel extends JPanel {
            private JButton buttonStart;
            private JButton buttonStop;
            private JButton buttonPauseContinue;
            private JLabel label;

            public ButtonPanel() {
                setLayout(new GridBagLayout());
                setBorder(new CompoundBorder(new TitledBorder("RUN"), new EmptyBorder(0, 0, 0, 0)));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(0, 2, 0, 2);

                /*JPanel panel = new JPanel(new GridBagLayout());
                panel.add(new JLabel("RUN: "), gbc);
                gbc.gridx++;
                gbc.weightx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(0, 0, 0, 0);
                panel.add((label = new JLabel()), gbc);

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 1;
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(4, 4, 4, 4);
                add(panel, gbc);*/

                gbc.gridwidth = 1;
                gbc.weightx = 0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                add((buttonStart = new JButton("Start")), gbc);
                gbc.gridx++;
                add((buttonStop = new JButton("Stop")), gbc);
                gbc.gridx++;
                add((buttonPauseContinue = new JButton("Pause/Continue")), gbc);
            }
        }

        private class CheckBoxPanel extends JPanel {
            private JLabel timeLabel;
            private JCheckBox showInfo;

            public CheckBoxPanel() {
                setLayout(new GridBagLayout());
                setBorder(new CompoundBorder(new TitledBorder("INFO"), new EmptyBorder(12, 0, 0, 0)));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(0, 0, 0, 4);

                JPanel panel = new JPanel(new GridBagLayout());
                panel.add(new JLabel("INFO: "), gbc);
                gbc.gridx++;
                gbc.weightx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(0, 0, 0, 0);
                panel.add((timeLabel = new JLabel("Time: ")), gbc);

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 1;
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(4, 4, 4, 4);
                add(panel, gbc);

                gbc.gridwidth = 1;
                gbc.weightx = 0.25;
                gbc.gridy++;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                add((showInfo = new JCheckBox("Start")), gbc);
            }
        }

        private class RadioButtonPanel extends JPanel {
            private ButtonGroup timeGroup;
            private JRadioButton showTimeLabel;
            private JRadioButton hideTimeLabel;
            private JLabel label;

            public RadioButtonPanel() {
                setLayout(new GridBagLayout());
                setBorder(new CompoundBorder(new TitledBorder("TIME VISIBLE"), new EmptyBorder(8, 0, 0, 0)));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new Insets(0, 0, 0, 4);
                gbc.anchor = GridBagConstraints.WEST;

                JPanel panel = new JPanel(new GridBagLayout());
                panel.add((showTimeLabel = new JRadioButton("Show")), gbc);
                gbc.gridy++;
                panel.add((hideTimeLabel = new JRadioButton("Hide: ")), gbc);

                gbc.gridx++;
                gbc.weightx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                panel.add((label = new JLabel("Some text")), gbc);

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 1;
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(4, 4, 4, 4);
                add(panel, gbc);
            }
        }

        private class TextFieldPanel extends JPanel {
            private JTextField textFieldN1;
            private JTextField textFieldN2;

            public TextFieldPanel() {
                setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;

                add(new JLabel("BigBird period (N1): "), gbc);
                gbc.gridy++;
                add(new JLabel("SmallBird period (N2): "), gbc);

                gbc.gridx++;
                gbc.gridy = 0;
                gbc.weightx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;

                add((textFieldN1 = new JTextField(10)), gbc);
                gbc.gridy++;
                add((textFieldN2 = new JTextField(10)), gbc);
            }
        }

        private class ComboBoxListPanel extends JPanel {
            private JComboBox comboBox;
            private JList list;

            public ComboBoxListPanel() {
                setLayout(new GridBagLayout());
                setBorder(new CompoundBorder(new TitledBorder("PARAMETERS P1, K"),
                        new EmptyBorder(0, 0, 0, 0)));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(0, 0, 0, 4);

                JPanel panel = new JPanel(new GridBagLayout());
                panel.add(new JLabel("P1: "), gbc);
                gbc.gridx++;
                gbc.weightx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(0, 0, 0, 0);
                panel.add((new JLabel("K: ")), gbc);

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 1;
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(4, 4, 4, 4);
                add(panel, gbc);

                gbc.gridwidth = 1;
                gbc.weightx = 0.25;
                gbc.gridy++;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                add((comboBox = new JComboBox()), gbc);
                gbc.gridwidth = 1;
                gbc.weightx = 0.25;
                gbc.gridx++;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                add((list = new JList()), gbc);
            }
        }

        private class SliderPanel extends JPanel {
            private JSlider sliderP1;
            private JSlider sliderK;

            public SliderPanel() {
                setLayout(new GridBagLayout());
                setBorder(new CompoundBorder(new TitledBorder("PARAMETERS P1, K"),
                        new EmptyBorder(0, 0, 0, 0)));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(0, 0, 0, 4);

                JPanel panel = new JPanel(new GridBagLayout());
                panel.add(new JLabel("P1: "), gbc);
                gbc.gridx++;
                gbc.weightx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(0, 0, 0, 0);
                panel.add((new JLabel("K: ")), gbc);

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 1;
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(4, 4, 4, 4);
                add(panel, gbc);

                gbc.gridwidth = 1;
                gbc.weightx = 0.25;
                gbc.gridy++;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                add((sliderP1 = new JSlider()), gbc);
                gbc.gridwidth = 1;
                gbc.weightx = 0.25;
                gbc.gridx++;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                add((sliderK = new JSlider()), gbc);
            }
        }

    }

    //TODO
    private class InfoDialog extends JDialog {

        private JTextArea infoArea;
        private JButton buttonOK;
        private JButton buttonCancel;
        private int DIALOG_WIDTH = 200;
        private int DIALOG_HEIGHT = 200;

        public InfoDialog(String title) {
            this.setTitle(title);
            this.setBounds(SCREEN_WIDTH / 2 + WINDOW_WIDTH / 2, SCREEN_HEIGHT / 2 - WINDOW_HEIGHT / 2, DIALOG_WIDTH, DIALOG_HEIGHT);
            this.setLayout(new GridLayout(2, 1));

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

    //TODO
    private class ErrorDialog extends JDialog {

        private JTextArea infoArea;
        private JButton buttonOK;
        private JButton buttonCancel;
        private int DIALOG_WIDTH = 200;
        private int DIALOG_HEIGHT = 200;

        public ErrorDialog() {

        }
    }
}