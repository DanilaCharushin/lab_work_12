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
        int N1 = 5000;
        int N2 = 5000;
        double P1 = 1;
        double K = 0.5;
        /*Scanner cin = new Scanner(System.in);
        System.out.println("N1 = ");
        N1 = cin.nextInt();
        System.out.println("N2 = ");
        N2 = cin.nextInt();
        System.out.println("P1 = ");
        P1 = cin.nextDouble();
        System.out.println("K = ");
        K = cin.nextDouble();*/
        new Habitat(500, 500, N1, N2, P1, K);
    }
}