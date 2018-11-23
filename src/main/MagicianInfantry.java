package main;

import core.GameObject;
import core.Handler;
import core.ID;
import core.RenderOrder;

import java.awt.image.BufferedImage;

public class MagicianInfantry extends BasicInfantry
{
    public MagicianInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(team, x, y, WIDTH, HEIGHT, id, handler, RenderOrder.Default.order);
    }

    public MagicianInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(team, 10, x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.damage = 20;
        this.attackRange = 300;
        this.hp = 10;
        this.delayBetweenAttack = 1000;
        this.speed = 6;
    }

    public MagicianInfantry(Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        this(team, x, y, image.getWidth(), image.getHeight(), id, handler, renderOrder);
        this.image = image;
        this.originalImage = image;
    }

    public void tick()
    {
        super.tick();

        moveToNotIntersection();
        if (this.hp <= 0) handler.removeObject(this);
    }

    @Override
    public void attack(Battleable target)
    {
        if (target instanceof GameObject)
        {

            handler.addObject(new Bullet(team, damage, ((GameObject)target).getMidPoint(), 10, this.getMidPoint().x, this.getMidPoint().y, 30, 30, ID.Bullet, handler, RenderOrder.Main.order));
        }
    }
}
