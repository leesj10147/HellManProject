package main;

import core.Handler;
import core.ID;

import java.awt.image.BufferedImage;

public class NormalInfantry extends BasicInfantry
{
    protected NormalInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        super(team, x, y, WIDTH, HEIGHT, id, handler);
    }

    public NormalInfantry(Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        super(team, 30, x, y, image, id, handler, renderOrder);
    }

    protected NormalInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(team, 30, x, y, WIDTH, HEIGHT, id, handler, renderOrder);
    }
    @Override
    public void tick()
    {
        super.tick();
        moveToNotIntersection();
        if (this.hp <= 0) handler.removeObject(this);
    }
}
