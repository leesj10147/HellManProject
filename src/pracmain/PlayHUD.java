package pracmain;

import core.HUD;

import java.awt.*;

public class PlayHUD extends HUD
{
    public int getHealth()
    {
        return health;
    }

    public void setHealth(int health)
    {
        this.health = Math.max(0, health);
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    private int health = 100;
    private int score = 0;
    private int level = 0;

    public void tick()
    {

    }

    public void render(Graphics2D g2d)
    {
        g2d.setColor(Color.gray);
        g2d.fillRect(15, 15, 200, 32);

        g2d.setColor(new Color(255 - health * 255 / 100, health * 255 / 100, 0));
        g2d.fillRect(15, 15, health * 2, 32);

        g2d.setColor(Color.white);
        g2d.drawRect(15, 15, 200, 32);

        g2d.drawString("Score: " + score, 15, 64);
        g2d.drawString("Level: " + level, 15, 75);
    }
}
