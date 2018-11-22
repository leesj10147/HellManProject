package main;

import core.GameObject;
import core.Handler;
import core.ID;
import core.RenderOrder;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BasicBarrier extends BasicTower
{
    public BasicBarrier(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(team, x, y, WIDTH, HEIGHT, id, handler, RenderOrder.Default.order);
    }

    public BasicBarrier(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(team, x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.hp = 1000;
        this.notAttack = true;

    }

    public BasicBarrier(Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        super(team, x, y, image, id, handler, renderOrder);
    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.setColor(Color.green);
        g2d.fill(getBounds());
    }

    @Override
    public Rectangle getBounds()
    {
        return new Rectangle((int)x, (int)y, WIDTH, HEIGHT);
    }

}
