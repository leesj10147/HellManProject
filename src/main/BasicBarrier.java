package main;

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
        this.MAX_HP = 1000;
        this.notAttack = true;
        this.printBottom = false;
    }
    @Override
    public void attack(Battleable target)
    {
        return;
    }
    public BasicBarrier(Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        this(team, x, y, image.getWidth(), image.getHeight(), id, handler, renderOrder);
        this.image = image;
        this.originalImage = image;
    }
    @Override
    public Rectangle getBounds()
    {
        return new Rectangle((int) x , (int) y, WIDTH, HEIGHT );
    }

}
