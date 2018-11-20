package pracmain;

import core.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends GameObject
{
    private double speed;
    private int health = 100;
    public int getHealth()
    {
        return health;
    }
    public Player(double x, double y, double speed, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(x, y, speed, WIDTH, HEIGHT, id, handler, RenderOrder.Default.order);
    }

    public Player(double x, double y, double speed, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.speed = speed;
    }


    @Override
    public Rectangle getBounds()
    {
        return new Rectangle((int) x, (int) y, WIDTH, HEIGHT);
    }


    @Override
    public void tick()
    {
        if (KeyInput.isKeyPressed(KeyEvent.VK_W)) velY = -speed;
        else if (KeyInput.isKeyPressed(KeyEvent.VK_S)) velY = speed;
        else velY = 0;

        if (KeyInput.isKeyPressed(KeyEvent.VK_A)) velX = -speed;
        else if (KeyInput.isKeyPressed(KeyEvent.VK_D)) velX = speed;
        else velX = 0;

        x += velX;
        y += velY;

        if (x < 0) x = 0;
        if (x + WIDTH >= Game.WIDTH) x = Game.WIDTH - WIDTH;
        if (y < 0) y = 0;
        if (y + HEIGHT >= Game.HEIGHT) y = Game.HEIGHT - HEIGHT;

        handler.addObject(new Trail(ID.Trail, Color.white, getBounds(), -2f, handler, RenderOrder.Player.order));
        handler.findObjectsById(ID.Enemy).stream().filter(i -> i.getBounds().intersects(getBounds())).forEach(i -> health = Math.max(0, health - 2));
    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.setColor(Color.white);
        g2d.fill(getBounds());
    }
}
