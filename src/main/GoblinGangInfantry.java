package main;

import core.Handler;
import core.ID;
import core.RenderOrder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GoblinGangInfantry extends BasicInfantry
{

    public GoblinGangInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(team, x, y, WIDTH, HEIGHT, id, handler, RenderOrder.Default.order);
    }

    public GoblinGangInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(team, 50, x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.myGroup = new ArrayList<>();
        for (int i = 0; i < 3; ++i)
        {
            GoblinGangMiniInfantry t = new GoblinGangMiniInfantry(this, this.team, this.x + i * (WIDTH / 4) + 5, this.y + i * (HEIGHT / 4) + 5, this.WIDTH / 4, this.HEIGHT / 4, ID.Infantry, handler, RenderOrder.Default.order);
            myGroup.add(t);
            handler.addObject(t);
        }
        this.ignoreCollision = true;
        this.notAttack = true;
        this.damage = 0;
        this.speed = 10;
        this.renderOrder = Integer.MAX_VALUE;
    }

    public ArrayList<GoblinGangMiniInfantry> myGroup;

    public GoblinGangInfantry(Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        super(team, 50, x, y, 0, 0, id, handler, renderOrder);
        this.myGroup = new ArrayList<>();
        for (int i = 0; i < 3; ++i)
        {
            GoblinGangMiniInfantry t = new GoblinGangMiniInfantry(this, team, x + i * image.getWidth() + 5, y + i * image.getHeight() + 5, image, id, handler, renderOrder);
            myGroup.add(t);
            handler.addObject(t);
        }
        this.ignoreCollision = true;
        this.notAttack = true;
        this.damage = 0;
        this.speed = 10;
        this.renderOrder = Integer.MAX_VALUE;
    }

    public void tick()
    {
        super.tick();
        if (this.myGroup.size() == 0) handler.removeObject(this);
        if (destination != null)
        {
            double distsum = 0;
            for (GoblinGangMiniInfantry g : myGroup)
            {
                distsum += g.getMidPoint().distance(destination);
            }
            if (distsum <= myGroup.size() * 50)
            {
                for (GoblinGangMiniInfantry g : myGroup)
                {
                    g.notMove = true;
                    g.destination = null;
                }
            } else
                for (GoblinGangMiniInfantry g : myGroup) g.notMove = false;
        } else
            for (GoblinGangMiniInfantry g : myGroup)
            {
                g.notMove = true;
                g.destination = null;
            }
    }

    public void render(Graphics2D g2d)
    {
        //super.render(g2d);
    }
}
