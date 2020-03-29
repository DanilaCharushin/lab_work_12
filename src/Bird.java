import java.awt.*;

public abstract class Bird implements IBehaviour
{
    protected int cordX;
    protected int cordY;

    public Bird(int cordX, int cordY) {
        this.cordX = cordX;
        this.cordY = cordY;
    }

    public abstract void drawBird(Graphics g);
}
