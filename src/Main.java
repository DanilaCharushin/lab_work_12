import javax.swing.*;
import java.awt.*;

public class Main
{
    public static void main(String args[])
    {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
            new Window(700,500);
        });
    }
}