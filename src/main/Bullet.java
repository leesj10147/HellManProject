package main;

import core.*;

import java.awt.*;
import java.util.ArrayList;

public class Bullet extends GameObject implements Battleable
{
    public void setTarget(Vector2 target)
    {
        this.target = target;
    }

    private Vector2 target;
    private double speed;
    private double damage;
    public final Team team;
    public Bullet(Team team, double damage, Vector2 target, double speed, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(team, damage, target, speed, x, y, WIDTH, HEIGHT, id, handler, RenderOrder.Default.order);

    }

    public Bullet(Team team, double damage, Vector2 target, double speed, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.target = target;
        this.speed = speed;
        this.damage = damage;
        this.team = team;
        this.setIgnoreCollision(true);
    }

    @Override
    public void tick()
    {

        ArrayList<GameObject> others = handler.Collide(this);
        for (GameObject other : others)
        {
            if (!(other instanceof Battleable)) continue;
            if (other instanceof BasicInfantry && ((BasicInfantry) other).team != this.team)
            {
                this.attack(damage, (BasicInfantry) other);
                handler.removeObject(this);
            }
        }
        Vector2 now = new Vector2(x + WIDTH / 2, y + HEIGHT / 2);
        if (new Rectangle((int)x - 20, (int)y-20, getWIDTH()+40, getHEIGHT()+40).contains(target.getPoint()))
        {
            handler.removeObject(this);
        }
        Vector2 vel = new Vector2(target.x - now.x, target.y - now.y).normalize();
        vel.multiply(speed);
        velX = vel.x;
        velY = vel.y;
        x += velX;
        y += velY;
    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.setColor(Color.red);
        g2d.draw(getBounds());
    }

    @Override
    public Rectangle getBounds()
    {
        return new Rectangle((int) x, (int) y, WIDTH, HEIGHT);
    }

    public void attack(double damage, BasicInfantry target)
    {
        target.applyDamage(this, damage);
        GameManager.playSound("sound\\hit-sound2.wav", false);
    }
    @Override
    public void applyDamage(Battleable attacker, double damage)
    {

    }
}
