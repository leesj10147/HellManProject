package main;

import core.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BasicInfantry extends GameObject implements Battleable
{
    public final Team team;

    protected double damage;
    protected double speed;
    protected double delayBetweenAttack;
    protected double attackRange;
    protected boolean notMove;
    protected boolean notAttack;
    protected double angle;
    public double MAX_HP;
    protected transient BufferedImage image;
    protected transient BufferedImage originalImage;
    public double getHp()
    {
        return hp;
    }

    protected double hp;

    public Vector2 getDestination()
    {
        return destination;
    }

    public void setDestination(Vector2 destination)
    {
        this.destination = destination;
    }

    public void setDestination(Point destination)
    {
        setDestination(new Vector2(destination.x, destination.y));
    }

    protected Vector2 destination;

    protected BasicInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(team, 50, x, y, WIDTH, HEIGHT, id, handler, RenderOrder.Default.order);
    }

    public BasicInfantry(Team team, double MAX_HP, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        this(team, MAX_HP, x, y, image.getWidth(), image.getHeight(), id, handler, renderOrder);
        this.image = image;
        this.originalImage = image;
    }

    public void moveToNotIntersection()
    {

        ArrayList<GameObject> object = handler.Collide(this);
        for (GameObject tempObject : object)
        {
            Vector2 v = this.getMidPoint();

            v.add(-tempObject.getMidPoint().x, -tempObject.getMidPoint().y);
            if (v.magnitude() <= 10)
            {
                Vector2 vec = new Vector2(Math.random(), Math.random());
                vec = vec.normalize();
                vec.multiply(10);
                x += vec.x;
                y += vec.y;
                return;
            }
        }
        for (int i = 0; i < object.size(); ++i)
        {
            GameObject temp = object.get(i);
            Vector2 v = this.getMidPoint();
            v.add(-temp.getMidPoint().x, -temp.getMidPoint().y);
            double len = (velX * velX + velY * velY + 300) / v.magnitude();
            v = v.normalize();
            v.multiply(len);
            x += v.x;
            y += v.y;
            recentTickMoved = false;
        }
    }

    protected BasicInfantry(Team team, double MAX_HP, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.damage = 5;
        this.speed = 10;
        this.delayBetweenAttack = 333;
        this.team = team;
        this.hp = MAX_HP;
        this.attackRange = 5;
        this.MAX_HP = MAX_HP;

    }

    private long lastAttackTime = 0;

    @Override
    public void tick()
    {

        if (originalImage != null)
        {
            if (selectedByMouse)
            {
                image = GameManager.setEdge(originalImage, Color.green);
            }
            else image = originalImage;
        }
        if (!notAttack)
        {
            ArrayList<GameObject> infatries = handler.findObjectsById(ID.Infantry);
            infatries.addAll(handler.findObjectsById(ID.Tower));
            infatries.addAll(handler.findObjectsById(ID.Nexus));
            //velY = velX = 0;
            for (GameObject object : infatries)
            {
                BasicInfantry infantry = (BasicInfantry) object;
                if (!infantry.ignoreCollision && infantry.team != this.team && getAttackBounds().intersects(infantry.getBounds()) &&
                            System.currentTimeMillis() - lastAttackTime >= delayBetweenAttack)
                {
                    this.attack(infantry);
                    lastAttackTime = System.currentTimeMillis();
                }
            }

        }
        if (destination != null && !notMove)
        {
            Vector2 now = new Vector2(x + WIDTH / 2, y + HEIGHT / 2);
            if (now.distance(destination) > 10)
            {
                Vector2 d = new Vector2(destination.x - now.x, destination.y - now.y).normalize();
                d.multiply(speed);
                velX = d.x;
                velY = d.y;
            } else
            {
                velY = velX = 0;
                this.destination = null;
            }
        }
        if (destination == null)
            velY = velX = 0;
        if (selectedByMouse && KeyInput.isKeyPressed(KeyEvent.VK_S))
        {
            this.setDestination((Vector2) null);
            velY = velX = 0;
            if (this instanceof GoblinGangMiniInfantry)
            {
                ((GoblinGangMiniInfantry) this).getGroup().setDestination((Vector2) null);
            }
            if (GameManager.scene instanceof BattleScene)
            {
                ((BattleScene) GameManager.scene).unSelect(this);
            }
        }
        if (!notMove)
        {
            x += velX;
            y += velY;
        }
        recentTickMoved = true;
        this.hp = Math.min(this.hp, this.MAX_HP);

    }

    protected boolean recentTickMoved = false;
    protected double realAngle;

    @Override
    public void render(Graphics2D g2d)
    {
        //////////////
        if (this.image != null)
        {
            final AffineTransform oldTransform = g2d.getTransform();
            final AffineTransform at = new AffineTransform(oldTransform);

            if (Math.atan2(velY, velX) != 0 && recentTickMoved)
            {
                double beta = realAngle - Math.atan2(velY, velX);
                if (Math.sin(beta) < 0) realAngle += 0.1;
                else realAngle -= 0.1;
                if (Math.abs(realAngle - angle) > 0.2) angle = realAngle;
            }
            at.rotate(angle, x + image.getWidth() / 2, y + image.getHeight() / 2);
            g2d.setTransform(at);
            g2d.drawImage(image, (int) x, (int) y, null);
            g2d.setTransform(oldTransform);
        } //else
        {
            if (selectedByMouse) g2d.setColor(Color.green);
            else g2d.setColor(Color.cyan);
            g2d.draw(getBounds());
        }
        ////////////////
        g2d.setColor(Color.black);
        g2d.fillRect((int) x + 20, (int) y - 10, (int) MAX_HP, 5);
        g2d.setColor(Color.red);
        g2d.fillRect((int) x + 20, (int) y - 10, (int) this.hp, 5);

        AffineTransform oldTransform = g2d.getTransform();
        AffineTransform at = new AffineTransform(oldTransform);
        at.scale(0.5, 0.5);
        g2d.setTransform(at);
        if (this.team == Team.Red)
            g2d.drawImage(RED_TEAM, (int) (x - 20) * 2, (int) (y - 30) * 2, null);
        else if (this.team == Team.Blue)
            g2d.drawImage(BLUE_TEAM, (int) (x - 20) * 2, (int) (y - 30) * 2, null);
        g2d.setTransform(oldTransform);

        g2d.setColor(Color.yellow);
        g2d.draw(getAttackBounds());
        g2d.setColor(Color.MAGENTA);
        g2d.fillRect((int) this.getMidPoint().x - 5, (int) this.getMidPoint().y - 5, 10, 10);
    }

    @Override
    public Rectangle getBounds()
    {
        return new Rectangle((int) x + WIDTH / 6, (int) y + HEIGHT / 6, WIDTH - WIDTH / 3, HEIGHT - HEIGHT / 3);
    }

    public Rectangle getAttackBounds()
    {
        Rectangle rect = getBounds();
        rect.x -= attackRange;
        rect.y -= attackRange;
        rect.width += 2 * attackRange;
        rect.height += 2 * attackRange;
        return rect;
    }

    public void attack(Battleable target)
    {
        target.applyDamage(this, this.damage);
    }

    @Override
    public void applyDamage(Battleable attacker, double damage)
    {
        this.hp -= damage;
    }

    @Override
    public String toString()
    {
        return getMidPoint().toString();
    }
}
