import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class BirdArray {
    private static BirdArray birdArray;
    private static Vector<Bird> array = new Vector<Bird>();
    private BirdArray() {}

    public static synchronized BirdArray getBirdArray() {
        if (birdArray == null) {
            birdArray = new BirdArray();
        }
        return birdArray;
    }

    public void addBird(Bird bird) {
        array.addElement(bird);
    }

    public void paintBirds(Graphics g) {
        for (Bird bird : array) {
            bird.drawBird(g);
        }
    }

    public void removeAllBirds() {
        array.removeAllElements();
    }
}
