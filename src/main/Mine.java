package main;

import core.Handler;
import core.ID;

import java.awt.image.BufferedImage;

public class Mine extends BasicBarrier
{
    public Mine(Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        super(team, x, y, image, id, handler, renderOrder);
        this.MAX_HP = 1500;
        this.hp = 1500;
    }
    @Override
    public void tick()
    {

    }
}
