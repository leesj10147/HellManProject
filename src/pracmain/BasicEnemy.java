package pracmain;

import core.*;

import java.awt.*;

public class BasicEnemy extends GameObject
{
    private Color color;

    public BasicEnemy(double x, double y, int WIDTH, int HEIGHT, Color color, ID id, Handler handler)
    {
        this(x, y, WIDTH, HEIGHT, 0, 0, color, id, handler);
    }

    public BasicEnemy(double x, double y, int WIDTH, int HEIGHT, Color color, ID id, Handler handler, int renderOrder)
    {
        this(x, y, WIDTH, HEIGHT, 0, 0, color, id, handler, renderOrder);
    }

    public BasicEnemy(double x, double y, int WIDTH, int HEIGHT, double velX, double velY, Color color, ID id, Handler handler)
    {
        this(x, y, WIDTH, HEIGHT, velX, velY, color, id, handler, RenderOrder.Default.order);
    }

    public BasicEnemy(double x, double y, int WIDTH, int HEIGHT, double velX, double velY, Color color, ID id, Handler handler, int renderOrder)
    {
        super(x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.velX = velX;
        this.velY = velY;
        this.color = color;
    }

    @Override
    public Rectangle getBounds()
    {
        return new Rectangle((int)x, (int)y, WIDTH, HEIGHT);
    }


    @Override
    public void tick()
    {
        y += velY;
        x += velX;
        if (y < 0 || y > Game.HEIGHT - HEIGHT) velY *= -1;
        if (x < 0 || x > Game.WIDTH - WIDTH) velX *= -1;

        handler.addObject(new Trail(ID.Trail, color, getBounds(), -2.7f, handler, RenderOrder.BackGround.order));
    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.setColor(color);
        g2d.fill(getBounds());
    }
}
