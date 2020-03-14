import javax.swing.*;
import java.awt.*;
import java.util.*;
/*
 * Вариант 12
 * Объекты – птицы. Бывают 2 видов: птенцы и взрослые птицы.
 * Взрослые птицы генерируются каждые N1 секунд с вероятностью P1.
 * Птенцы генерируются каждые N2 секунд при условии,
 * что их количество менее K% от общего числа взрослых птиц, в противном случае – не генерируются.
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