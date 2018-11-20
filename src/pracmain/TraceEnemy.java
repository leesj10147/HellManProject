package pracmain;

import core.GameObject;
import core.Handler;
import core.ID;
import core.RenderOrder;

import java.awt.*;

public class TraceEnemy extends BasicEnemy
{
    private GameObject target;
    private double speed;

    public TraceEnemy(GameObject target, double x, double y, int WIDTH, int HEIGHT, double speed, Color color, ID id, Handler handler)
    {
        this(target, x, y, WIDTH, HEIGHT, speed, color, id, handler, RenderOrder.Default.order);
    }

    public TraceEnemy(GameObject target, double x, double y, int WIDTH, int HEIGHT, double speed, Color color, ID id, Handler handler, int renderOrder)
    {
        super(x, y, WIDTH, HEIGHT, color, id, handler, renderOrder);
        this.target = target;
        this.speed = speed;
    }


    @Override
    public void tick()
    {
        double nowX = x + WIDTH / 2;
        double nowY = y + HEIGHT / 2;

        double targetX = target.getX() + target.getWIDTH() / 2;
        double targetY = target.getY() + target.getHEIGHT() / 2;

        double dx = targetX - nowX;
        double dy = targetY - nowY;
        double dist = Math.sqrt(dx * dx + dy * dy);
        dx /= dist;
        dy /= dist;
        dx *= speed;
        dy *= speed;
        velX = dx;
        velY = dy;

        super.tick();
    }

    @Override
    public void render(Graphics2D g2d)
    {

    }
}
