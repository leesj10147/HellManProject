package main;

import core.GameObject;
import core.Handler;
import core.ID;
import core.RenderOrder;

import java.awt.*;

public class BasicBarrier extends GameObject implements Battleable
{
    public double getHp()
    {
        return hp;
    }

    private double hp;
    public final Team team;
    public BasicBarrier(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(team, x, y, WIDTH, HEIGHT, id, handler, RenderOrder.Default.order);
    }

    public BasicBarrier(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.team = team;
        this.hp = 300;
    }

    @Override
    public void tick()
    {

    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.setColor(Color.green);
        g2d.draw(getBounds());
    }

    @Override
    public Rectangle getBounds()
    {
        return new Rectangle((int)x, (int)y, WIDTH, HEIGHT);
    }

    @Override
    public void applyDamage(Battleable attacker, double damage)
    {

    }
}
