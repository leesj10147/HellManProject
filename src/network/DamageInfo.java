package network;


import core.GameObject;
import core.Handler;
import core.ID;
import core.RenderOrder;

import java.awt.*;

public class DamageInfo extends GameObject
{
    public int targetHashCode;
    public double damage;
    public DamageInfo(double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(x, y, WIDTH, HEIGHT, id, handler, RenderOrder.BackGround.order);
    }

    public DamageInfo(double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.ignoreCollision = true;
        this.renderOrder = Integer.MAX_VALUE;
    }
    public DamageInfo(int targetHashCode, double damage)
    {
        this(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0, ID.DamageInfo, null);
        this.targetHashCode = targetHashCode;
        this.damage = damage;
    }
    @Override
    public void tick()
    {

    }

    @Override
    public void render(Graphics2D g2d)
    {

    }

    @Override
    public Rectangle getBounds()
    {
        return new Rectangle(Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0);
    }
}
