import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SmallBird extends Bird
{
    private static Image image;
    private static int COUNT = 0;

    public SmallBird(int cordX, int cordY) {
        this.cordX = cordX;
        this.cordY = cordY;
        COUNT++;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image == null)
            return;
        g.drawImage(image, cordX, cordY, this);
    }

    @Override
    public void move() {
        System.out.println("SmallBird moved");
    }

    public static void SET_IMAGE(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
