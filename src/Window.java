import org.w3c.dom.ranges.DocumentRange;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.Timer;

public class Window extends JFrame {

    final private String confFile = "conf/conf.txt";
    final private String datFile = "conf/conf.dat";

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
        try {
            readConf(confFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                    case  KeyEvent.VK_C:
                        if (habitat.getState() != STATE.STOPPED) {
                            new CurrentBirdsDialog(BirdArray.getBirdArray().getMap(), BirdArray.getBirdArray().getList());
                        }
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
                new ErrorDialog("N1 field must be an integer!");
                controlPanel.getTextFieldPanel().getTextFieldN1().setText(controlPanel.getTextFieldPanel().getDefaultText());
            }
            requestFocusInWindow();
        });
        controlPanel.getTextFieldPanel().getTextFieldN2().addActionListener(actionEvent -> {
            try {
                habitat.setN2(Integer.parseInt(controlPanel.getTextFieldPanel().getTextFieldN2().getText()));
            } catch (NumberFormatException ex) {
                new ErrorDialog("N2 field must be an integer!");
                controlPanel.getTextFieldPanel().getTextFieldN2().setText(controlPanel.getTextFieldPanel().getDefaultText());
            }
            requestFocusInWindow();
        });

        controlPanel.getTextFieldPanel().getTextFieldN1().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {}

            @Override
            public void focusLost(FocusEvent focusEvent) {
                try {
                    habitat.setN1(Integer.parseInt(controlPanel.getTextFieldPanel().getTextFieldN1().getText()));
                } catch (NumberFormatException ex) {
                    new ErrorDialog("N1 field must be an integer!");
                    controlPanel.getTextFieldPanel().getTextFieldN1().setText(controlPanel.getTextFieldPanel().getDefaultText());
                }
                Window.this.requestFocusInWindow();
            }
        });
        controlPanel.getTextFieldPanel().getTextFieldN2().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {}

            @Override
            public void focusLost(FocusEvent focusEvent) {
                try {
                    habitat.setN2(Integer.parseInt(controlPanel.getTextFieldPanel().getTextFieldN2().getText()));
                } catch (NumberFormatException ex) {
                    new ErrorDialog("N2 field must be an integer!");
                    controlPanel.getTextFieldPanel().getTextFieldN2().setText(controlPanel.getTextFieldPanel().getDefaultText());
                }
                Window.this.requestFocusInWindow();
            }
        });

        controlPanel.getTextFieldPanel().getTextFieldTimeLife1().addActionListener(actionEvent -> {
            try {
                habitat.setTimeLife1(Integer.parseInt(controlPanel.getTextFieldPanel().getTextFieldTimeLife1().getText()));
            } catch (NumberFormatException ex) {
                new ErrorDialog("TimeLife1 field must be an integer!");
                controlPanel.getTextFieldPanel().getTextFieldTimeLife1().setText(controlPanel.getTextFieldPanel().getDefaultText());
            }
            requestFocusInWindow();
        });
        controlPanel.getTextFieldPanel().getTextFieldTimeLife2().addActionListener(actionEvent -> {
            try {
                habitat.setTimeLife2(Integer.parseInt(controlPanel.getTextFieldPanel().getTextFieldTimeLife2().getText()));
            } catch (NumberFormatException ex) {
                new ErrorDialog("TimeLife2 field must be an integer!");
                controlPanel.getTextFieldPanel().getTextFieldTimeLife2().setText(controlPanel.getTextFieldPanel().getDefaultText());
            }
            requestFocusInWindow();
        });

        controlPanel.getTextFieldPanel().getTextFieldTimeLife1().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {}

            @Override
            public void focusLost(FocusEvent focusEvent) {
                try {
                    habitat.setTimeLife1(Integer.parseInt(controlPanel.getTextFieldPanel().getTextFieldTimeLife1().getText()));
                } catch (NumberFormatException ex) {
                    new ErrorDialog("TimeLife1 field must be an integer!");
                    controlPanel.getTextFieldPanel().getTextFieldTimeLife1().setText(controlPanel.getTextFieldPanel().getDefaultText());
                }
                Window.this.requestFocusInWindow();
            }
        });
        controlPanel.getTextFieldPanel().getTextFieldTimeLife2().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {}

            @Override
            public void focusLost(FocusEvent focusEvent) {
                try {
                    habitat.setTimeLife2(Integer.parseInt(controlPanel.getTextFieldPanel().getTextFieldTimeLife2().getText()));
                } catch (NumberFormatException ex) {
                    new ErrorDialog("TimeLife2 field must be an integer!");
                    controlPanel.getTextFieldPanel().getTextFieldTimeLife2().setText(controlPanel.getTextFieldPanel().getDefaultText());
                }
                Window.this.requestFocusInWindow();
            }
        });

        controlPanel.getTextFieldPanel().getTextFieldT().addActionListener(actionEvent -> {
            try {
                habitat.setT(Integer.parseInt(controlPanel.getTextFieldPanel().getTextFieldT().getText()));
            } catch (NumberFormatException ex) {
                new ErrorDialog("T field must be an integer!");
                controlPanel.getTextFieldPanel().getTextFieldT().setText(controlPanel.getTextFieldPanel().getDefaultText());
            }
            requestFocusInWindow();
        });
        controlPanel.getTextFieldPanel().getTextFieldT().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {}

            @Override
            public void focusLost(FocusEvent focusEvent) {
                try {
                    habitat.setT(Integer.parseInt(controlPanel.getTextFieldPanel().getTextFieldT().getText()));
                } catch (NumberFormatException ex) {
                    new ErrorDialog("T field must be an integer!");
                    controlPanel.getTextFieldPanel().getTextFieldT().setText(controlPanel.getTextFieldPanel().getDefaultText());
                }
                Window.this.requestFocusInWindow();
            }
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

        controlPanel.getComboBoxListPanel().getComboBoxPriorityBigAI().addActionListener(actionEvent -> {
            try {
                System.out.println("Big (pr before): " + habitat.getBigBirdAI().getPriority());
                int value = controlPanel.getComboBoxListPanel().getComboBoxPriorityBigAI().getSelectedIndex();
                habitat.getBigBirdAI().setPriority(value == 0 ? 1 : value * 5);
                System.out.println("Big (pr after): " + habitat.getBigBirdAI().getPriority());
            } catch (NullPointerException ex) {
                new ErrorDialog("Please, start a simulation");
            }
        });
        controlPanel.getComboBoxListPanel().getComboBoxPrioritySmallAI().addActionListener(actionEvent -> {
            try {
                System.out.println("Small (pr before): " + habitat.getSmallBirdAI().getPriority());
                int value = controlPanel.getComboBoxListPanel().getComboBoxPrioritySmallAI().getSelectedIndex();
                habitat.getSmallBirdAI().setPriority(value == 0 ? 1 : value * 5);
                System.out.println("Small (pr after): " + habitat.getSmallBirdAI().getPriority());
            } catch (NullPointerException ex) {
                new ErrorDialog("Please, start a simulation");
            }
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
        controlPanel.getButtonPanel().getButtonCurrentBirds().addActionListener(actionEvent -> {
            if (habitat.getState() != STATE.STOPPED) {
                new CurrentBirdsDialog(BirdArray.getBirdArray().getMap(), BirdArray.getBirdArray().getList());
            }
        });
        controlPanel.getButtonPanel().getButtonPauseContinueBigBirdAI().addActionListener(actionEvent -> {
            habitat.getBigBirdAI().changeState();
            controlPanel.getButtonPanel().getButtonPauseContinueBigBirdAI().setText(habitat.getBigBirdAI().isRunning() ? "Pause AI big" : "Continue AI big");
        });
        controlPanel.getButtonPanel().getButtonPauseContinueSmallBirdAI().addActionListener(actionEvent -> {
            habitat.getSmallBirdAI().changeState();
            controlPanel.getButtonPanel().getButtonPauseContinueSmallBirdAI().setText(habitat.getSmallBirdAI().isRunning() ? "Pause AI small" : "Continue AI small");
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                requestFocusInWindow();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    writeConf(confFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        requestFocusInWindow();
        setVisible(true);
    }

    private void readConf(String fileName) throws IOException {
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(fileName), 200);
        int ch;
        String line = "";
        int id = 0;
        while((ch = is.read()) != -1) {
            if (!String.valueOf((char)ch).equals("\n")) {
                line += String.valueOf((char)ch);
            } else {
                switch (id) {
                    case 0:
                        controlPanel.getCheckBoxPanel().getCheckBox().setSelected(line == "1" ? true : false);
                        break;
                    case 1:
                        TIME_LABEL_VISIBLE = line == "1" ? true : false;
                        if (TIME_LABEL_VISIBLE) {
                            controlPanel.getRadioButtonPanel().getShowTimeLabel().setSelected(true);
                        } else {
                            controlPanel.getRadioButtonPanel().getHideTimeLabel().setSelected(true);
                        }
                        break;
                    case 2:
                        controlPanel.getTextFieldPanel().getTextFieldN1().setText(line);
                        habitat.setN1(Integer.parseInt(line));
                        break;
                    case 3:
                        controlPanel.getTextFieldPanel().getTextFieldN2().setText(line);
                        habitat.setN2(Integer.parseInt(line));
                        break;
                    case 4:
                        controlPanel.getTextFieldPanel().getTextFieldTimeLife1().setText(line);
                        habitat.setTimeLife1(Integer.parseInt(line));
                        break;
                    case 5:
                        controlPanel.getTextFieldPanel().getTextFieldTimeLife2().setText(line);
                        habitat.setTimeLife2(Integer.parseInt(line));
                        break;
                    case 6:
                        controlPanel.getTextFieldPanel().getTextFieldT().setText(line);
                        habitat.setT(Integer.parseInt(line));
                        break;
                    case 7:
                        controlPanel.getSliderPanel().getSliderP1().setValue(Integer.parseInt(line));
                        controlPanel.getComboBoxListPanel().getComboBoxP1().setSelectedIndex(Integer.parseInt(line) / 10);
                        habitat.setP1(Double.parseDouble(line) / 100.0);
                        break;
                    case 8:
                        controlPanel.getSliderPanel().getSliderK().setValue(Integer.parseInt(line));
                        controlPanel.getComboBoxListPanel().getListK().setSelectedIndex(Integer.parseInt(line) / 10);
                        habitat.setK(Double.parseDouble(line) / 100.0);
                        break;
                    case 9:
                        controlPanel.getComboBoxListPanel().getComboBoxPriorityBigAI().setSelectedIndex(Integer.parseInt(line));
                        habitat.getBigBirdAI().setPriority(Integer.parseInt(line));
                        break;
                    case 10:
                        controlPanel.getComboBoxListPanel().getComboBoxPrioritySmallAI().setSelectedIndex(Integer.parseInt(line));
                        habitat.getSmallBirdAI().setPriority(Integer.parseInt(line));
                        break;
                }
                line = "";
                id++;
            }
        }
        is.close();
    }

    private void writeConf(String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(fileName));
        String conf = controlPanel.getCheckBoxPanel().getCheckBox().isSelected() ? "1\n" : "0\n";
        conf += TIME_LABEL_VISIBLE ? "1\n" : "0\n";
        conf += controlPanel.getTextFieldPanel().getTextFieldN1().getText() + '\n';
        conf += controlPanel.getTextFieldPanel().getTextFieldN2().getText() + '\n';
        conf += controlPanel.getTextFieldPanel().getTextFieldTimeLife1().getText() + '\n';
        conf += controlPanel.getTextFieldPanel().getTextFieldTimeLife2().getText() + '\n';
        conf += controlPanel.getTextFieldPanel().getTextFieldT().getText() + '\n';
        conf += controlPanel.getSliderPanel().sliderP1.getValue() + "\n";
        conf += controlPanel.getSliderPanel().sliderK.getValue() + "\n";
        conf += controlPanel.getComboBoxListPanel().getComboBoxPriorityBigAI().getSelectedIndex() + "\n";
        conf += controlPanel.getComboBoxListPanel().getComboBoxPrioritySmallAI().getSelectedIndex() + "\n";
        fos.write(conf.getBytes());
        fos.close();
    }

    private void UPLOAD(String fileName) {
        System.out.println("U");
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(BirdArray.getBirdArray().getMap());
            oos.writeObject(BirdArray.getBirdArray().getSet());
            oos.writeObject(BirdArray.getBirdArray().getList());
            oos.close();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private void DOWNLOAD(String fileName) {
        System.out.println("D");
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new FileInputStream(fileName));
            BirdArray.getBirdArray().setMap((HashMap<Integer, String>) ois.readObject(), habitat.getTime());
            BirdArray.getBirdArray().setSet((TreeSet<Integer>) ois.readObject());
            BirdArray.getBirdArray().setList((LinkedList<Bird>) ois.readObject());
            ois.close();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private void START() {
        if (habitat.getState() == STATE.STOPPED) {
            habitat.beginSimulation();
            controlPanel.getButtonPanel().getButtonStart().setEnabled(false);
            controlPanel.getButtonPanel().getButtonStop().setEnabled(true);
            controlPanel.getButtonPanel().getButtonPauseContinue().setEnabled(true);
            controlPanel.getButtonPanel().getButtonCurrentBirds().setEnabled(true);
            controlPanel.getButtonPanel().getButtonPauseContinue().setText("Pause");
            controlPanel.getButtonPanel().getButtonPauseContinueBigBirdAI().setEnabled(true);
            controlPanel.getButtonPanel().getButtonPauseContinueBigBirdAI().setText("Pause AI big");
            controlPanel.getButtonPanel().getButtonPauseContinueSmallBirdAI().setEnabled(true);
            controlPanel.getButtonPanel().getButtonPauseContinueSmallBirdAI().setText("Pause AI small");
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
        controlPanel.getButtonPanel().getButtonPauseContinueBigBirdAI().setEnabled(true);
        controlPanel.getButtonPanel().getButtonPauseContinueBigBirdAI().setText("Pause AI big");
        controlPanel.getButtonPanel().getButtonPauseContinueSmallBirdAI().setEnabled(true);
        controlPanel.getButtonPanel().getButtonPauseContinueSmallBirdAI().setText("Pause AI small");
    }

    private void STOP() {
        if (habitat.getState() != STATE.STOPPED) {
            if (controlPanel.getCheckBoxPanel().getCheckBox().isSelected()) {
                habitat.pauseSimulation();
                controlPanel.getButtonPanel().getButtonStart().setEnabled(false);
                controlPanel.getButtonPanel().getButtonStop().setEnabled(false);
                controlPanel.getButtonPanel().getButtonPauseContinue().setEnabled(false);
                controlPanel.getButtonPanel().getButtonCurrentBirds().setEnabled(false);
                controlPanel.getButtonPanel().getButtonPauseContinue().setText("Pause/Continue");
                controlPanel.getButtonPanel().getButtonPauseContinueBigBirdAI().setEnabled(false);
                controlPanel.getButtonPanel().getButtonPauseContinueBigBirdAI().setText("Pause/Continue AI big");
                controlPanel.getButtonPanel().getButtonPauseContinueSmallBirdAI().setEnabled(false);
                controlPanel.getButtonPanel().getButtonPauseContinueSmallBirdAI().setText("Pause/Continue AI small");
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
                timeLabel.setText(habitat.getTime());
            }
        }, 0, habitat.getPeriod());
    }

    private class MenuBar extends JMenuBar {
        public MenuBar() {
            JMenu menuRun = new JMenu("Run");
            menuRun.setToolTipText("Run settings. start, stop, pause, upload, download");
            menuRun.add("Start").addActionListener(actionEvent -> START());
            menuRun.add("Stop").addActionListener(actionEvent -> STOP());
            menuRun.add("Pause").addActionListener(actionEvent -> PAUSE());
            menuRun.add("Continue").addActionListener(actionEvent -> CONTINUE());
            menuRun.add("Upload").addActionListener(actionEvent -> UPLOAD(datFile));
            menuRun.add("Download").addActionListener(actionEvent -> DOWNLOAD(datFile));

            JMenu menuConsole = new JMenu("Console");
            menuConsole.setToolTipText("Click here to run terminal");
            menuConsole.addMenuListener(new MenuListener() {
                @Override
                public void menuSelected(MenuEvent menuEvent) {;
                    new ConsoleDialog();
                }

                @Override
                public void menuDeselected(MenuEvent menuEvent) {

                }

                @Override
                public void menuCanceled(MenuEvent menuEvent) {

                }
            });
            JMenu menuAbout = new JMenu("About");
            menuAbout.setToolTipText("Info about laboratory work");
            menuAbout.add("Author").addActionListener(actionEvent -> System.out.println("Danil Charushin"));
            menuAbout.add("Version").addActionListener(actionEvent -> System.out.println("v1.0"));

            add(menuRun);
            add(menuConsole);
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
            private JButton buttonCurrentBirds;
            private JButton buttonPauseContinueSmallBirdAI;
            private JButton buttonPauseContinueBigBirdAI;

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

                buttonCurrentBirds = new JButton("Current birds");
                buttonCurrentBirds.setFocusable(false);
                buttonCurrentBirds.setEnabled(false);

                buttonPauseContinueSmallBirdAI = new JButton("Pause/Continue AI small");
                buttonPauseContinueSmallBirdAI.setFocusable(false);
                buttonPauseContinueSmallBirdAI.setEnabled(false);

                buttonPauseContinueBigBirdAI = new JButton("Pause/Continue AI big");
                buttonPauseContinueBigBirdAI.setFocusable(false);
                buttonPauseContinueBigBirdAI.setEnabled(false);

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

                gbc.gridy++;
                gbc.gridx = 0;

                add(buttonCurrentBirds, gbc);
                gbc.gridx++;
                add(buttonPauseContinueBigBirdAI, gbc);
                gbc.gridx++;
                add(buttonPauseContinueSmallBirdAI, gbc);
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
            public JButton getButtonCurrentBirds() {
                return buttonCurrentBirds;
            }
            public JButton getButtonPauseContinueBigBirdAI() {
                return buttonPauseContinueBigBirdAI;
            }
            public JButton getButtonPauseContinueSmallBirdAI() {
                return buttonPauseContinueSmallBirdAI;
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
                checkBox.setSelected(true);
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
            private JTextField textFieldTimeLife1;
            private JTextField textFieldTimeLife2;
            private JTextField textFieldT;
            final private String defaultText = "1000";

            public TextFieldPanel() {
                setLayout(new GridBagLayout());
                setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;

                add(new JLabel("BigBird period (N1), ms: "), gbc);
                gbc.gridy++;
                add(new JLabel("SmallBird period (N2), ms: "), gbc);
                gbc.gridy++;
                add(new JLabel("BigBird TimeLife, ms: "), gbc);
                gbc.gridy++;
                add(new JLabel("SmallBird TimeLife, ms: "), gbc);
                gbc.gridy++;
                add(new JLabel("AI period, ms: "), gbc);

                gbc.gridx++;
                gbc.gridy = 0;
                gbc.weightx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;

                textFieldN1 = new JTextField(10);
                textFieldN1.setText(defaultText);
                add(textFieldN1, gbc);

                gbc.gridy++;
                textFieldN2 = new JTextField(10);
                textFieldN2.setText(defaultText);
                add(textFieldN2, gbc);

                gbc.gridy++;
                textFieldTimeLife1 = new JTextField(10);
                textFieldTimeLife1.setText(defaultText);
                add(textFieldTimeLife1, gbc);

                gbc.gridy++;
                textFieldTimeLife2 = new JTextField(10);
                textFieldTimeLife2.setText(defaultText);
                add(textFieldTimeLife2, gbc);

                gbc.gridy++;
                textFieldT = new JTextField(10);
                textFieldT.setText(defaultText);
                add(textFieldT, gbc);
            }

            public JTextField getTextFieldN1() {
                return textFieldN1;
            }

            public JTextField getTextFieldN2() {
                return textFieldN2;
            }

            public JTextField getTextFieldTimeLife1() {
                return textFieldTimeLife1;
            }

            public JTextField getTextFieldTimeLife2() {
                return textFieldTimeLife2;
            }

            public JTextField getTextFieldT() {
                return textFieldT;
            }

            public String getDefaultText() {
                return defaultText;
            }
        }

        private class ComboBoxListPanel extends JPanel {
            private JComboBox comboBoxP1;
            private JComboBox comboBoxPriorityBigAI;
            private JComboBox comboBoxPrioritySmallAI;
            private JList listK;

            public ComboBoxListPanel() {
                setLayout(new BorderLayout());
                setBorder(BorderFactory.createLineBorder(Color.RED, 5));

                JPanel panelPriority = new JPanel(new BorderLayout());
                JPanel pr1 = new JPanel(new BorderLayout());
                JPanel pr2 = new JPanel(new BorderLayout());
                pr1.add(new JLabel("Big AI: "), BorderLayout.NORTH);
                pr2.add(new JLabel("Small AI: "), BorderLayout.NORTH);
                comboBoxPriorityBigAI = new JComboBox();
                comboBoxPriorityBigAI.addItem("min priority");
                comboBoxPriorityBigAI.addItem("norm priority");
                comboBoxPriorityBigAI.addItem("max priority");
                comboBoxPriorityBigAI.setFocusable(false);
                comboBoxPriorityBigAI.setSelectedIndex(0);

                comboBoxPrioritySmallAI = new JComboBox();
                comboBoxPrioritySmallAI.addItem("min priority");
                comboBoxPrioritySmallAI.addItem("norm priority");
                comboBoxPrioritySmallAI.addItem("max priority");
                comboBoxPrioritySmallAI.setFocusable(false);
                comboBoxPrioritySmallAI.setSelectedIndex(0);

                pr1.add(comboBoxPriorityBigAI, BorderLayout.SOUTH);
                pr2.add(comboBoxPrioritySmallAI, BorderLayout.SOUTH);
                panelPriority.add(pr1, BorderLayout.EAST);
                panelPriority.add(pr2, BorderLayout.WEST);

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
                comboBoxP1.setSelectedIndex(10);
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
                listK.setSelectedIndex(10);
                listK.setListData(vector);
                listK.setFocusable(false);
                JScrollPane scroll = new JScrollPane(listK);
                scroll.setPreferredSize(new Dimension(50, 100));
                panelK.add(scroll, BorderLayout.CENTER);
                add(panelP1, BorderLayout.WEST);
                add(panelK, BorderLayout.EAST);
                add(panelPriority, BorderLayout.CENTER);
            }

            public JComboBox getComboBoxP1() {
                return comboBoxP1;
            }

            public JComboBox getComboBoxPriorityBigAI() {
                return comboBoxPriorityBigAI;
            }

            public JComboBox getComboBoxPrioritySmallAI() {
                return comboBoxPrioritySmallAI;
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
                sliderP1.setValue(100);
                panel.add(sliderP1, gbc);
                gbc.gridy++;

                panel.add((new JLabel("K: ")), gbc);
                gbc.gridy++;

                sliderK = new JSlider();
                sliderK.setFocusable(false);
                sliderK.setValue(100);
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

            buttonCancel.addActionListener(actionEvent -> {
                CONTINUE();
                dispose();
            });

            buttonOK.addActionListener(actionEvent -> {
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
            setModal(true);
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

    private class CurrentBirdsDialog extends JDialog {
        private JTextArea infoArea;
        private JButton buttonOK;
        private int DIALOG_WIDTH = 200;
        private int DIALOG_HEIGHT = 200;

        public CurrentBirdsDialog(HashMap<Integer, String> map, LinkedList<Bird> list) {
            setTitle("INFO");
            setModal(true);
            setBounds(
                    SCREEN_WIDTH / 2 - DIALOG_WIDTH / 2,
                    SCREEN_HEIGHT / 2 - DIALOG_HEIGHT / 2,
                    DIALOG_WIDTH,
                    DIALOG_HEIGHT
            );
            setLayout(new BorderLayout());
            setBackground(new Color(222,222,222));
            infoArea = new JTextArea();
            infoArea.setBounds(0, 0, 100, 100);
            infoArea.setBackground(new Color(222,222,222));
            infoArea.setFont(new Font("Helvetica", Font.ITALIC, 12));
            infoArea.setFocusable(false);

            String text = "Alive birds (" + map.size() + "):\n";

            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                Bird bird = null;
                for (Bird b : list) {
                    if (b.hashCode() == entry.getKey()) {
                        bird = b;
                    }
                }
                text += (bird.getClass() + ", id: " + entry.getKey() + ", born time: " + entry.getValue() + "\n");
            }

            buttonOK = new JButton("OK");
            buttonOK.addActionListener(actionEvent -> {
                CurrentBirdsDialog.this.dispose();
            });

            add(new JScrollPane(infoArea), BorderLayout.CENTER);
            add(buttonOK, BorderLayout.SOUTH);

            infoArea.setText(text);
            setVisible(true);
        }
    }

    private class ConsoleDialog extends JDialog {
        private JTextArea infoArea;
        private int DIALOG_WIDTH = 600;
        private int DIALOG_HEIGHT = 300;
        public ConsoleDialog() {
            setTitle("CONSOLE");
            setModal(false);
            setAlwaysOnTop(true);
            setBounds(
                    0,
                    0,
                    DIALOG_WIDTH,
                    DIALOG_HEIGHT
            );
            setLayout(new BorderLayout());
            setBackground(new Color(222,222,222));

            PipedInputStream pis = new PipedInputStream();
            PipedOutputStream pos = new PipedOutputStream();
            try {
                pis.connect(pos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            infoArea = new JTextArea();
            infoArea.setBackground(new Color(222,222,222));
            infoArea.setFont(new Font("Helvetica", Font.ITALIC, 18));
            infoArea.setText("-- BIRDS TERMINAL --\n");
            infoArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        String in = infoArea.getText();
                        int index = in.substring(0, in.length() - 2).lastIndexOf('\n');
                        String cmd = in.substring(index == -1 ? 0 : index, in.length() - 1);
                        cmd = cmd.replace("\n", "");
                        if (cmd.startsWith("setk ")) {
                            int k = Integer.parseInt(cmd.substring(cmd.indexOf(" ") + 1));
                            try {
                                pos.write((byte) k);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            infoArea.append("Set k to " + k + "\n");
                        } else if (cmd.startsWith("clear")) {
                            infoArea.setText("-- BIRDS TERMINAL --\n");
                        } else if (cmd.startsWith("exit")) {
                            try {
                                pis.close();
                                pos.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            ConsoleDialog.this.dispose();
                        } else {
                            infoArea.append("Unknown command. Available: setk <k> | clear | exit\n");
                        }
                    }
                }
            });
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(10);
                        byte in[] = new byte[1];
                        if (pis.available() > 0) {
                            pis.read(in);
                            int data = in[0];
                            Window.this.habitat.setK(data / 100.0);
                            controlPanel.getSliderPanel().getSliderK().setValue(data);
                            controlPanel.getComboBoxListPanel().getListK().setSelectedIndex((int) (controlPanel.getSliderPanel().getSliderK().getValue()
                                    / 10.0));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    try {
                        pis.close();
                        pos.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            thread.setName("Console I/0 deamon");
            thread.setDaemon(true);
            thread.start();
            add(new JScrollPane(infoArea), BorderLayout.CENTER);
            setVisible(true);
        }
    }
}