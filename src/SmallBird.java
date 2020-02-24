import javax.swing.*;
import java.awt.*;

public class SmallBird extends Bird
{
    private static Image image;
    private static int COUNT = 0;

    public SmallBird(int cordX, int cordY) {
        this.cordX = cordX;
        this.cordY = cordY;
        COUNT++;
    }

    public void drawBird(Graphics g) {
        g.drawImage(image, cordX, cordY, null);
    }

    @Override
    public void move() {
        System.out.println("SmallBird moved");
    }

    public static void SET_IMAGE(String imagePath) {
        image = new ImageIcon(imagePath).getImage();
    }

    public static void ZERO_COUNT() {
        COUNT = 0;
    }

    public static int GET_COUNT() {
        return COUNT;
    }

    public static Image GET_IMAGE() {
        return image;
    }

    @Override
    public String toString() {
        return "SmallBird in " +  cordX + ";" + cordY;
    }
}
