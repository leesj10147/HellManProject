package main;

import core.Handler;
import core.ID;
import core.RenderOrder;

import java.awt.image.BufferedImage;

public class GoblinGangMiniInfantry extends BasicInfantry
{
    public GoblinGangInfantry getGroup()
    {
        return group;
    }

    private GoblinGangInfantry group;

    public GoblinGangMiniInfantry(GoblinGangInfantry group, Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(group, team, x, y, WIDTH, HEIGHT, id, handler, RenderOrder.Default.order);
    }

    public GoblinGangMiniInfantry(GoblinGangInfantry group, Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(team, 30, x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.damage = 3;
        this.hp = 30;
        this.speed = 10;
        this.group = group;
        this.attackRange = 10;
    }

    public GoblinGangMiniInfantry(GoblinGangInfantry group, Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        this(group, team, x, y, image.getWidth(), image.getHeight(), id, handler, renderOrder);
        this.image = image;
        this.originalImage = image;
    }

    @Override
    public void tick()
    {
        if (this.destination != null) group.setDestination(this.destination);
        if (group.getDestination() != null) this.destination = group.getDestination();
        super.tick();

        moveToNotIntersection();
        if (this.hp <= 0)
        {
            handler.removeObject(this);
            group.myGroup.remove(this);
        }
    }
}
