import javax.swing.*;
import java.awt.*;

public abstract class Bird implements IBehaviour
{
    protected int cordX;
    protected int cordY;

    public abstract void drawBird(Graphics g);
}
