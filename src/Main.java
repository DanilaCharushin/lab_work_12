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
        int N1 = 500;
        int N2 = 500;
        double P1 = 1;
        double K = 0.5;
        int PERIOD = 100;

        new Window(700,500,  new Habitat(N1, N2, P1, K, PERIOD));
    }
}