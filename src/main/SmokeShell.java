package main;

import core.Handler;
import core.ID;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SmokeShell extends BasicBarrier
{
    public SmokeShell(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        super(team, x, y, WIDTH, HEIGHT, id, handler);
        this.ignoreCollision = true;
    }

    public SmokeShell(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(team, x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.ignoreCollision = true;
    }

    public SmokeShell(Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        super(team, x, y, image, id, handler, renderOrder);
        this.ignoreCollision = true;
    }

    @Override
    public void render(Graphics2D g2d)
    {
        float alpha = 1f;
        if (this.team != BattleScene.getTeam()) alpha = 0.1f;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        super.render(g2d);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

    }
}
