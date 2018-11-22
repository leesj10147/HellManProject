package network;

import core.GameObject;
import core.Handler;
import core.ID;
import main.Team;

import java.awt.*;
import java.util.ArrayList;
import java.util.TreeSet;

public class CheckGameObject extends GameObject
{

    public final Team team;
    public ArrayList<String> list;
    public long syncTime;
    public CheckGameObject(Handler handler, Team team)
    {
        this(Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0, ID.NetworkChecker, handler, team);
        this.syncTime = System.currentTimeMillis();
    }

    private CheckGameObject(double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, Team team)
    {
        super(x, y, WIDTH, HEIGHT, id, handler);
        this.team = team;
        TreeSet<GameObject> set = handler.getMyTeamObject(team);
        list = new ArrayList<>(set.size());
        if (this.team == Team.Red)
            set.forEach(a -> list.add(a.distinguish.substring(3)));
        else
            set.forEach(a -> list.add(a.distinguish.substring(4)));

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
