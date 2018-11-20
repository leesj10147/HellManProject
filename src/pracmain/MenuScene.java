package pracmain;

import core.*;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MenuScene extends Scene
{
    private Font exitFont;
    private Font playFont;
    public MenuScene(Handler handler)
    {
        super(handler);
        exitFont = new Font("Serif", Font.BOLD, 100);
        playFont = new Font("Serif", Font.BOLD, 100);
    }

    private int exitX = Game.WIDTH / 2 - 250;
    private int exitY = Game.HEIGHT / 2 - 50;
    private int exitWidth = 500;
    private int exitHeight = 200;


    private int playX = Game.WIDTH / 2 - 250;
    private int playY = Game.HEIGHT / 2 - 300;
    private int playWidth = 500;
    private int playHeight = 200;
    @Override
    public void tick()
    {
        Point e = MouseInput.getLocation();
        MouseEvent event = MouseInput.pollEvent();

        if (exitX <= e.getX() && e.getX() <= exitX + exitWidth && exitY <= e.getY() && e.getY() <= exitY + exitHeight)
        {
            exitFont = new Font("Serif", Font.BOLD, Math.min(exitFont.getSize() + 3, 150));
            if (event != null && event.getID() == MouseEvent.MOUSE_CLICKED)
                GameManager.closeOperator();
        } else
        {
            exitFont = new Font("Serif", Font.BOLD, Math.max(exitFont.getSize() - 3, 100));
        }
        if (playX <= e.getX() && e.getX() <= playX + playWidth && playY <= e.getY() && e.getY() <= playY + playHeight)
        {
            playFont = new Font("Serif", Font.BOLD, Math.min(playFont.getSize() + 3, 150));
            if (event != null && event.getID() == MouseEvent.MOUSE_CLICKED)
            {
                GameManager.changeScene(PlayScene.class);
            }
        } else
        {
            playFont = new Font("Serif", Font.BOLD, Math.max(playFont.getSize() - 3, 100));
        }
    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.setColor(Color.white);
        g2d.drawRect(exitX, exitY, exitWidth, exitHeight);
        g2d.setFont(exitFont);
        int width = g2d.getFontMetrics().stringWidth("Exit");
        int height = g2d.getFontMetrics().getHeight();
        g2d.drawString("Exit", exitX + (exitWidth - width) / 2, exitY + exitFont.getSize());

        g2d.drawRect(playX, playY, playWidth, playHeight);
        g2d.setFont(playFont);
        width = g2d.getFontMetrics().stringWidth("Play");
        height = g2d.getFontMetrics().getHeight();
        g2d.drawString("Play", playX + (playWidth - width) / 2, playY + playFont.getSize());
    }
}
