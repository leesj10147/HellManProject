package core;

import java.awt.*;

public abstract class Scene
{
    protected Handler handler;
    public Scene(Handler handler)
    {
        this.handler = handler;
    }
    public abstract void tick();
    public abstract void render(Graphics2D g2d);
    protected  static int cameraX;
    public static int getCameraX()
    {
        return cameraX;
    }

    public static int getCameraY()
    {
        return cameraY;
    }

    protected static  int cameraY;
}
