package network;

import core.GameObject;
import core.Handler;
import core.ID;
import main.BattleScene;
import main.Team;

import java.awt.*;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

public class CheckGameObject extends GameObject
{


    public CheckGameObject(Handler handler, int targetcode)
    {
        this(Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0, ID.NetworkChecker, handler, targetcode);

    }
    public int targetcode;
    private CheckGameObject(double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int targetcode)
    {
        super(x, y, WIDTH, HEIGHT, id, handler);
        this.targetcode = targetcode;
    }

    @Override
    public void tick()
    {

    }

    @Override
    public void render(Graphics2D g2d)
    {

    }

    @Override
    public Rectangle getBounds()
    {
        return new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
    }
}
