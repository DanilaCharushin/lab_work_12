import javax.swing.*;
import java.awt.*;


/*
* Взрослые и птенцы двигаются в одном направлении (в стае). Направление движения меняется с периодом T и выбирается случайно.
*/

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