import javafx.scene.control.ComboBox;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.Flow;

public class Window extends JFrame {

    private boolean TIME_LABEL_VISIBLE = true;
    private int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    private int WINDOW_WIDTH;
    private int WINDOW_HEIGHT;

    private ControlPanel controlPanel;

    private JLabel timeLabel;
    private Timer timer;
    private Habitat habitat;

    public Window(int WIDTH, int HEIGHT) {
        super("Крутые птички!!!");
        this.WINDOW_WIDTH = WIDTH;
        this.WINDOW_HEIGHT = HEIGHT;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(
                SCREEN_WIDTH / 2 - WINDOW_WIDTH / 2,
                SCREEN_HEIGHT / 2 - WINDOW_HEIGHT / 2,
                WINDOW_WIDTH,
                WINDOW_HEIGHT
        );
        setLayout(new BorderLayout());
        setIconImage(new ImageIcon("res/favicon.png").getImage());
        setResizable(true);

        setJMenuBar(new MenuBar());

        timeLabel = new JLabel("Time: ");
        habitat = new Habitat(1000, 1000, 1, 1);
        controlPanel = new ControlPanel();
        controlPanel.getTextFieldPanel().getTextFieldN1().setText("1000");
        controlPanel.getTextFieldPanel().getTextFieldN2().setText("1000");
        controlPanel.getComboBoxListPanel().getComboBoxP1().setSelectedIndex(10);
        controlPanel.getComboBoxListPanel().getListK().setSelectedIndex(10);
        controlPanel.getSliderPanel().getSliderP1().setValue(100);
        controlPanel.getSliderPanel().getSliderK().setValue(100);

        add(controlPanel, BorderLayout.WEST);
        add(habitat, BorderLayout.CENTER);
        add(timeLabel, BorderLayout.SOUTH);

        addKeyListener(new KeyAdapter() {
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
                        if (TIME_LABEL_VISIBLE) {
                            controlPanel.getRadioButtonPanel().getShowTimeLabel().setSelected(true);
                            controlPanel.getRadioButtonPanel().getHideTimeLabel().setSelected(false);

                        } else {
                            controlPanel.getRadioButtonPanel().getShowTimeLabel().setSelected(false);
                            controlPanel.getRadioButtonPanel().getHideTimeLabel().setSelected(true);
                        }
                        break;
                    case KeyEvent.VK_P:
                        if (habitat.getState() == STATE.RUNNING)
                            PAUSE();
                        else
                            CONTINUE();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                }
            }
        });


        controlPanel.getTextFieldPanel().getTextFieldN1().addActionListener(actionEvent -> {
            try {
                habitat.setN1(Integer.parseInt(controlPanel.getTextFieldPanel().getTextFieldN1().getText()));
            } catch (NumberFormatException ex) {
                new ErrorDialog("Некорректный формат");
            }
            requestFocusInWindow();
        });

        controlPanel.getTextFieldPanel().getTextFieldN2().addActionListener(actionEvent -> {
            try {
                habitat.setN2(Integer.parseInt(controlPanel.getTextFieldPanel().getTextFieldN2().getText()));
            } catch (NumberFormatException ex) {
                new ErrorDialog("Некорректный формат");
            }
            requestFocusInWindow();
        });

        controlPanel.getComboBoxListPanel().getComboBoxP1().addActionListener(actionEvent -> {
            double value = Double.parseDouble((String) controlPanel.getComboBoxListPanel().getComboBoxP1().getSelectedItem());
            habitat.setP1(value);
            controlPanel.getSliderPanel().getSliderP1().setValue((int) (value * 100));
        });

        controlPanel.getComboBoxListPanel().getListK().addListSelectionListener(listSelectionEvent -> {
            double value = Double.parseDouble((String) controlPanel.getComboBoxListPanel().getListK().getSelectedValue());
            habitat.setK(value);
            controlPanel.getSliderPanel().getSliderK().setValue((int) (value * 100));
        });

        controlPanel.getSliderPanel().getSliderP1().addChangeListener(stateChanged -> {
            double value = controlPanel.getSliderPanel().getSliderP1().getValue() / 100.0;
            habitat.setP1(value);
            controlPanel.getComboBoxListPanel().getComboBoxP1().setSelectedIndex((int) (value * 10));
        });

        controlPanel.getSliderPanel().getSliderK().addChangeListener(stateChanged -> {
            double value = controlPanel.getSliderPanel().getSliderK().getValue() / 100.0;
            habitat.setK(value);
            controlPanel.getComboBoxListPanel().getListK().setSelectedIndex((int) (value * 10));
        });

        controlPanel.getRadioButtonPanel().getShowTimeLabel().addActionListener(actionEvent -> {
            TIME_LABEL_VISIBLE = true;
            timeLabel.setVisible(TIME_LABEL_VISIBLE);
        });

        controlPanel.getRadioButtonPanel().getHideTimeLabel().addActionListener(actionEvent -> {
            TIME_LABEL_VISIBLE = false;
            timeLabel.setVisible(TIME_LABEL_VISIBLE);
        });

        controlPanel.getButtonPanel().getButtonStart().addActionListener(actionEvent -> START());
        controlPanel.getButtonPanel().getButtonStop().addActionListener(actionEvent -> STOP());
        controlPanel.getButtonPanel().getButtonPauseContinue().addActionListener(actionEvent -> {
            if (habitat.getState() == STATE.RUNNING)
                PAUSE();
            else
                CONTINUE();
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                requestFocusInWindow();
            }
        });

        requestFocusInWindow();
        setVisible(true);
    }


    private void START() {
        if (habitat.getState() == STATE.STOPPED) {
            habitat.beginSimulation();
            controlPanel.getButtonPanel().getButtonStart().setEnabled(false);
            controlPanel.getButtonPanel().getButtonStop().setEnabled(true);
            controlPanel.getButtonPanel().getButtonPauseContinue().setEnabled(true);
            controlPanel.getButtonPanel().getButtonPauseContinue().setText("Pause");
            runTimer();
        }
    }

    private void PAUSE() {
        timer.cancel();
        habitat.pauseSimulation();
        controlPanel.getButtonPanel().getButtonPauseContinue().setText("Continue");
    }

    private void CONTINUE() {
        runTimer();
        habitat.continueSimulation();
        controlPanel.getButtonPanel().getButtonStart().setEnabled(false);
        controlPanel.getButtonPanel().getButtonStop().setEnabled(true);
        controlPanel.getButtonPanel().getButtonPauseContinue().setEnabled(true);
        controlPanel.getButtonPanel().getButtonPauseContinue().setText("Pause");
    }

    private void STOP() {
        if (habitat.getState() != STATE.STOPPED) {
            if (controlPanel.getCheckBoxPanel().getCheckBox().isSelected()) {
                habitat.pauseSimulation();
                controlPanel.getButtonPanel().getButtonStart().setEnabled(false);
                controlPanel.getButtonPanel().getButtonStop().setEnabled(false);
                controlPanel.getButtonPanel().getButtonPauseContinue().setEnabled(false);
                controlPanel.getButtonPanel().getButtonPauseContinue().setText("Pause/Continue");
                new InfoDialog();
            }
            else
                PAUSE();
        }
    }

    private void runTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeLabel.setText(habitat.getTime() );
            }
        }, 0, habitat.getPeriod());
    }


    private class MenuBar extends JMenuBar {
        public MenuBar() {
            JMenu menuRun = new JMenu("Run");
            menuRun.setToolTipText("Run settings. start, stop, pause");
            menuRun.add("Start").addActionListener(actionEvent -> START());
            menuRun.add("Stop").addActionListener(actionEvent -> STOP());
            menuRun.add("Pause").addActionListener(actionEvent -> PAUSE());
            menuRun.add("Continue").addActionListener(actionEvent -> CONTINUE());

            JMenu menuAbout = new JMenu("About");
            menuAbout.setToolTipText("Info about laboratory work");
            menuAbout.add("Author").addActionListener(actionEvent -> System.out.println("Danil Charushin"));
            menuAbout.add("Version").addActionListener(actionEvent -> System.out.println("v1.0"));

            add(menuRun);
            add(menuAbout);
        }
    }


    private class ControlPanel extends JPanel {
        private ButtonPanel buttonPanel;
        private CheckBoxPanel checkBoxPanel;
        private RadioButtonPanel radioButtonPanel;
        private TextFieldPanel textFieldPanel;
        private ComboBoxListPanel comboBoxListPanel;
        private SliderPanel sliderPanel;

        public ControlPanel() {
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 10));
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

            public ButtonPanel() {
                setLayout(new GridBagLayout());
                setBorder(BorderFactory.createLineBorder(Color.GREEN, 10));

                buttonStart = new JButton("Start");
                buttonStart.setFocusable(false);

                buttonStop = new JButton("Stop");
                buttonStop.setFocusable(false);
                buttonStop.setEnabled(false);

                buttonPauseContinue = new JButton("Pause/Continue");
                buttonPauseContinue.setFocusable(false);
                buttonPauseContinue.setEnabled(false);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(0, 2, 0, 2);

                gbc.gridwidth = 1;
                gbc.weightx = 0;
                gbc.fill = GridBagConstraints.HORIZONTAL;

                add(buttonStart, gbc);
                gbc.gridx++;
                add(buttonStop, gbc);
                gbc.gridx++;
                add(buttonPauseContinue, gbc);
            }

            public JButton getButtonStart() {
                return buttonStart;
            }
            public JButton getButtonStop() {
                return buttonStop;
            }
            public JButton getButtonPauseContinue() {
                return buttonPauseContinue;
            }
        }

        private class CheckBoxPanel extends JPanel {
            private JCheckBox checkBox;

            public CheckBoxPanel() {
                setLayout(new GridBagLayout());
                setBorder(BorderFactory.createLineBorder(Color.YELLOW, 10));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(0, 0, 0, 4);

                gbc.gridwidth = 1;
                gbc.weightx = 0.25;
                gbc.gridy++;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                checkBox = new JCheckBox("Show info");
                checkBox.setFocusable(false);
                add(checkBox, gbc);
            }

            public JCheckBox getCheckBox() {
                return checkBox;
            }

        }

        private class RadioButtonPanel extends JPanel {
            private ButtonGroup timeGroup;
            private JRadioButton showTimeLabel;
            private JRadioButton hideTimeLabel;

            public RadioButtonPanel() {
                setLayout(new GridBagLayout());
                setBorder(BorderFactory.createLineBorder(Color.PINK, 10));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;

                showTimeLabel = new JRadioButton("Show");
                showTimeLabel.setSelected(true);
                showTimeLabel.setFocusable(false);

                hideTimeLabel = new JRadioButton("Hide");
                hideTimeLabel.setFocusable(false);

                timeGroup = new ButtonGroup();
                timeGroup.add(showTimeLabel);
                timeGroup.add(hideTimeLabel);

                add(showTimeLabel, gbc);
                gbc.gridx++;
                add(hideTimeLabel, gbc);
            }

            public JRadioButton getShowTimeLabel() {
                return showTimeLabel;
            }

            public JRadioButton getHideTimeLabel() {
                return hideTimeLabel;
            }
        }

        private class TextFieldPanel extends JPanel {
            private JTextField textFieldN1;
            private JTextField textFieldN2;

            public TextFieldPanel() {
                setLayout(new GridBagLayout());
                setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
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
                textFieldN1 = new JTextField(10);
                add(textFieldN1, gbc);
                gbc.gridy++;
                textFieldN2 = new JTextField(10);
                add(textFieldN2, gbc);
            }

            public JTextField getTextFieldN1() {
                return textFieldN1;
            }

            public JTextField getTextFieldN2() {
                return textFieldN2;
            }
        }

        private class ComboBoxListPanel extends JPanel {
            private JComboBox comboBoxP1;
            private JList listK;

            public ComboBoxListPanel() {
                setLayout(new BorderLayout());
                setBorder(BorderFactory.createLineBorder(Color.RED, 5));
                JPanel panelP1 = new JPanel(new BorderLayout());
                panelP1.add(new JLabel("P1: "), BorderLayout.NORTH);
                comboBoxP1 = new JComboBox();
                for (int i = 0; i < 11; i++) {
                    if (i == 10)
                        comboBoxP1.addItem("1");
                    else
                        comboBoxP1.addItem("0." + i);
                }
                comboBoxP1.setFocusable(false);
                panelP1.add(comboBoxP1, BorderLayout.SOUTH);

                JPanel panelK = new JPanel(new BorderLayout());
                panelK.add(new JLabel("K: "), BorderLayout.NORTH);
                listK = new JList();
                listK.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                Vector<String> vector = new Vector<String>();
                for (int i = 0; i < 11; i++) {
                    if (i == 10)
                        vector.add("1");
                    else
                        vector.add("0." + i);
                }
                listK.setListData(vector);
                listK.setFocusable(false);
                JScrollPane scroll = new JScrollPane(listK);
                scroll.setPreferredSize(new Dimension(50, 100));
                panelK.add(scroll, BorderLayout.CENTER);
                add(panelP1, BorderLayout.WEST);
                add(panelK, BorderLayout.EAST);
            }

            public JComboBox getComboBoxP1() {
                return comboBoxP1;
            }

            public JList getListK() {
                return listK;
            }
        }

        private class SliderPanel extends JPanel {
            private JSlider sliderP1;
            private JSlider sliderK;

            public SliderPanel() {
                setLayout(new GridBagLayout());
                setBorder(BorderFactory.createLineBorder(Color.WHITE, 10));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weighty = 0.25;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.VERTICAL;
                gbc.insets = new Insets(0, 0, 0, 0);

                JPanel panel = new JPanel(new GridBagLayout());

                panel.add(new JLabel("P1: "), gbc);
                gbc.gridy++;

                sliderP1 = new JSlider();
                sliderP1.setFocusable(false);
                panel.add(sliderP1, gbc);
                gbc.gridy++;

                panel.add((new JLabel("K: ")), gbc);
                gbc.gridy++;

                sliderK = new JSlider();
                sliderK.setFocusable(false);
                panel.add(sliderK, gbc);

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 1;
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(4, 4, 4, 4);
                add(panel, gbc);
            }

            public JSlider getSliderP1() {
                return sliderP1;
            }

            public JSlider getSliderK() {
                return sliderK;
            }
        }

        public ButtonPanel getButtonPanel() {
            return buttonPanel;
        }

        public CheckBoxPanel getCheckBoxPanel() {
            return checkBoxPanel;
        }

        public TextFieldPanel getTextFieldPanel() {
            return textFieldPanel;
        }

        public RadioButtonPanel getRadioButtonPanel() {
            return radioButtonPanel;
        }

        public ComboBoxListPanel getComboBoxListPanel() {
            return comboBoxListPanel;
        }

        public SliderPanel getSliderPanel() {
            return sliderPanel;
        }

    }


    private class InfoDialog extends JDialog {

        private JTextArea infoArea;
        private JButton buttonOK;
        private JButton buttonCancel;
        private int DIALOG_WIDTH = 200;
        private int DIALOG_HEIGHT = 200;

        public InfoDialog() {
            setTitle("INFO");
            setBounds(
                    SCREEN_WIDTH / 2 - DIALOG_WIDTH / 2,
                    SCREEN_HEIGHT / 2 - DIALOG_HEIGHT / 2,
                    DIALOG_WIDTH,
                    DIALOG_HEIGHT
            );
            setLayout(new GridLayout(2, 1));
            setBackground(new Color(222,222,222));
            infoArea = new JTextArea();
            infoArea.setBounds(0, 0, 100, 100);
            infoArea.setBackground(new Color(222,222,222));
            infoArea.setFont(new Font("Helvetica", Font.ITALIC, 12));
            infoArea.setFocusable(false);
            infoArea.setText(habitat.getInfo());

            buttonOK = new JButton("OK");
            buttonCancel = new JButton("Отмена");

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(buttonOK);
            buttonPanel.add(buttonCancel);

            buttonOK.addActionListener(actionEvent -> {
                CONTINUE();
                dispose();
            });

            buttonCancel.addActionListener(actionEvent -> {
                habitat.endSimulation();
                controlPanel.getButtonPanel().getButtonStart().setEnabled(true);
                controlPanel.getButtonPanel().getButtonStop().setEnabled(false);
                controlPanel.getButtonPanel().getButtonPauseContinue().setEnabled(false);
                controlPanel.getButtonPanel().getButtonPauseContinue().setText("Pause/Continue");
                dispose();
            });

            add(infoArea);
            add(buttonPanel);
            setVisible(true);
        }
    }


    private class ErrorDialog extends JDialog {

        private JButton buttonOK;
        private int DIALOG_WIDTH = 200;
        private int DIALOG_HEIGHT = 200;

        public ErrorDialog(String message) {
            setTitle("ERROR");
            setBounds(
                    SCREEN_WIDTH / 2 - DIALOG_WIDTH / 2,
                    SCREEN_HEIGHT / 2 - DIALOG_HEIGHT / 2,
                    DIALOG_WIDTH,
                    DIALOG_HEIGHT
            );
            setLayout(new BorderLayout());
            add(new JLabel(message), BorderLayout.CENTER);

            buttonOK = new JButton("Ok");
            add(buttonOK, BorderLayout.SOUTH);
            buttonOK.addActionListener(actionEvent -> {
                setVisible(false);
            });
            setVisible(true);
        }
    }
}