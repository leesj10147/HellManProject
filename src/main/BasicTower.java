package main;

import core.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BasicTower extends BasicInfantry
{
    private static transient BufferedImage bottomImage = GameManager.loadImage("bottom.png");
    protected boolean printBottom = true;

    public BasicTower(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(team, x, y, WIDTH, HEIGHT, id, handler, RenderOrder.Default.order);
    }

    public BasicTower(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(team, 1000, x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.attackRange = 350;
        this.damage = 7;
        this.delayBetweenAttack = 500;
        this.notMove = true;
        this.recentTickMoved = true;
        this.destination = new Vector2(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public BasicTower(Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        this(team, x, y, image.getWidth(), image.getHeight(), id, handler, renderOrder);
        this.image = image;
        this.originalImage = image;
    }

    @Override
    public void attack(Battleable target)
    {
        if (target instanceof GameObject)
        {

            handler.addObject(new Bullet(team, damage, ((GameObject) target).getMidPoint(), 10, this.getMidPoint().x, this.getMidPoint().y, 30, 30, ID.Bullet, handler, RenderOrder.Main.order));
            GameObject o = (GameObject) target;
            this.velX = o.getMidPoint().x - this.getMidPoint().x;
            this.velY = o.getMidPoint().y - this.getMidPoint().y;
        }

    }

    @Override
    public void applyDamage(Battleable attacker, double damage)
    {
        this.hp -= damage;

    }

    @Override
    public void tick()
    {

        this.destination = new Vector2(Integer.MAX_VALUE, Integer.MAX_VALUE);

        super.tick();

        if (this.hp <= 0) handler.removeObject(this);
    }

    @Override
    public void render(Graphics2D g2d)
    {
        if (printBottom)
            g2d.drawImage(bottomImage, (int) x, (int) y, null);
        double realhp = this.hp;
        double realmh = this.MAX_HP;
        this.hp = this.hp / 4;
        this.MAX_HP = this.MAX_HP / 4;
        super.render(g2d);
        this.hp = realhp;
        this.MAX_HP = realmh;
    }
}
