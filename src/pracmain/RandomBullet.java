package pracmain;

import core.Game;
import core.Handler;
import core.ID;
import core.RenderOrder;

import java.awt.*;

public class RandomBullet extends BasicEnemy
{
    public RandomBullet(double x, double y, int WIDTH, int HEIGHT, Color color, ID id, Handler handler)
    {
        this(x, y, WIDTH, HEIGHT, color, id, handler, RenderOrder.Default.order);
    }

    public RandomBullet(double x, double y, int WIDTH, int HEIGHT, Color color, ID id, Handler handler, int renderOrder)
    {
        super(x, y, WIDTH, HEIGHT, color, id, handler, renderOrder);
        velX = (Math.random() * 7)-3;
        velY = 5;
    }

    @Override
    public void tick()
    {
        x += velX;
        y += velY;
        if (y < 0 || y > Game.HEIGHT - HEIGHT || x < 0 || x > Game.WIDTH - WIDTH)
            handler.removeObject(this);
    }
}
