package pracmain;

import core.*;

import java.awt.*;
import java.util.Random;

public class PlayScene extends Scene
{

    private final Random random;
    private Player target;
    private PlayHUD hud;

    public PlayScene(Handler handler)
    {
        super(handler);
        this.handler = handler;
        this.random = new Random();
        this.hud = new PlayHUD();
        target = new Player(Game.WIDTH / 2 - 10, Game.HEIGHT / 2 - 10, 8.0d, 20, 20, ID.Player, handler, RenderOrder.Player.order);
        handler.addObject(target);
    }

    @Override
    public void tick()
    {
        if (hud.getScore() / 100 >= hud.getLevel())
        {
            hud.setLevel(hud.getLevel() + 1);
            if (hud.getLevel() == 10)
            {
                handler.removeObjectsById(ID.Enemy);
                handler.addObject(new BossEnemy(30, 30, Color.red, ID.Enemy, handler, RenderOrder.Main.order));
            } else if (hud.getLevel() < 10)
            {
                double speed = 8 + hud.getLevel() / 2;
                int width = Math.min(random.nextInt(10) + 10 + hud.getLevel() * 2, 50);
                int height = Math.min(random.nextInt(10) + 10 + hud.getLevel() * 2, 50);
                double x = random.nextInt(Game.WIDTH - width);
                double y = random.nextInt(Game.HEIGHT - height);
                Color color = new Color(hud.getLevel() * 19 % 255, hud.getLevel() * 101 % 255, hud.getLevel() * 199 % 255);
                int type = random.nextInt(2);
                if (hud.getLevel() <= 5 || type == 0)
                {
                    double theta = random.nextDouble() * 2 * Math.PI;
                    double dx = speed * Math.cos(theta);
                    double dy = speed * Math.sin(theta);
                    handler.addObject(new BasicEnemy(x, y, width, height, dx, dy, color, ID.Enemy, handler, RenderOrder.Main.order));
                } else
                {
                    handler.addObject(new TraceEnemy(target, x, y, width, height, Math.min(speed, 10), color, ID.Enemy, handler, RenderOrder.Main.order));
                }
            }
        }
        hud.setScore(hud.getScore() + 1);
        hud.setHealth(target.getHealth());
        hud.tick();
    }

    @Override
    public void render(Graphics2D g2d)
    {
        hud.render(g2d);
    }
}
