import java.awt.*;
import java.io.Serializable;

public abstract class Bird implements IBehaviour, Serializable
{
    protected int cordX;
    protected int cordY;

    public Bird(int cordX, int cordY) {
        this.cordX = cordX;
        this.cordY = cordY;
    }

    public abstract void drawBird(Graphics g);
}
