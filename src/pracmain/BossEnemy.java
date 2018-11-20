package pracmain;

import core.Game;
import core.Handler;
import core.ID;
import core.RenderOrder;

import java.awt.*;

public class BossEnemy extends BasicEnemy
{
    public BossEnemy(int WIDTH, int HEIGHT, Color color, ID id, Handler handler)
    {
        this(WIDTH, HEIGHT, color, id, handler, RenderOrder.Default.order);
    }

    public BossEnemy(int WIDTH, int HEIGHT, Color color, ID id, Handler handler, int renderOrder)
    {
        super((Game.WIDTH - WIDTH) / 2, -HEIGHT, WIDTH, HEIGHT, color, id, handler, renderOrder);
    }

    private long timer = System.currentTimeMillis();

    @Override
    public void tick()
    {
        if (y < 30d)
        {
            velY = 2d;
            super.tick();
            return;
        }
        velY = 0;
        if (velX < 0) velX -= 0.05;
        else velX += 0.05;
        velX = Math.min(velX, 20);
        velX = Math.max(velX, -20);
        if (System.currentTimeMillis() - timer >= 100 - Math.abs(velX) * 2)
        {
            timer = System.currentTimeMillis();
            handler.addObject(new RandomBullet(x + WIDTH / 2, y + HEIGHT / 2, 7, 7, Color.CYAN, ID.Enemy, handler, RenderOrder.BackGround.order));
        }
        super.tick();
    }
}
