package main;

import core.GameObject;
import core.Handler;
import core.ID;
import core.RenderOrder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SkeletonBombInfantry extends BasicInfantry
{
    public SkeletonBombInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(team, x, y, WIDTH, HEIGHT, id, handler, RenderOrder.Default.order);
    }

    public SkeletonBombInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(team, 1, x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.damage = 20;
        this.hp = 1;
        this.speed = 20;
        this.notAttack = true;
    }

    public SkeletonBombInfantry(Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        this(team, x, y, image.getWidth(), image.getHeight(), id, handler, renderOrder);
        this.image = image;
        this.originalImage = image;
    }

    @Override
    public void tick()
    {
        super.tick();
        moveToNotIntersection();
        if (this.hp <= 0)
        {
            ArrayList<GameObject> towers = handler.findObjectsById(ID.Tower);
            towers.addAll(handler.findObjectsById(ID.Nexus));
            boolean attacked = false;
            if (towers.size() != 0)
            {
                for (GameObject object : towers)
                {
                    BasicTower tower = (BasicTower) object;

                    if (tower.team != this.team && this.getAttackBounds().intersects(tower.getBounds()))
                    {
                        tower.applyDamage(this, this.damage * 10);
                        attacked = true;
                    }
                }
            }
            if (!attacked)
            {
                ArrayList<GameObject> infantrys = handler.findObjectsById(ID.Infantry);
                for (GameObject object : infantrys)
                {
                    BasicInfantry basicInfantry = (BasicInfantry) object;
                    if (this.team != basicInfantry.team && this.getBombBound().intersects(basicInfantry.getBounds()))
                    {
                        basicInfantry.applyDamage(this, this.damage);
                    }
                }
            }
            handler.removeObject(this);
        }
    }

    public Rectangle getBombBound()
    {
        Rectangle rect = getBounds();
        rect.x -= 20;
        rect.y -= 20;
        rect.width += 2 * 20;
        rect.height += 2 * 20;
        return rect;
    }
}
