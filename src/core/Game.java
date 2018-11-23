package core;

import main.BattleScene;

import java.awt.*;
import java.awt.image.BufferStrategy;

public final class Game extends Canvas implements Runnable
{
    public static final int WIDTH = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
    public static final int HEIGHT = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
    public static final int tickPerSecond = 30;
    private Thread thread;
    private boolean running = false;
    private Handler handler;

    private Game()
    {
        this.addKeyListener(new KeyInput());

        new Window(WIDTH, HEIGHT, this);

        handler = new Handler();
        GameManager.handler = handler;

        //첫번째 Scene
        GameManager.changeScene(BattleScene.class);
    }

    public void start()
    {
        thread = new Thread(this);
        thread.start();
        running = true;

    }

    public void stop()
    {
        running = false;
    }

    @Override
    public void run()
    {
        long lastTime = System.nanoTime();
        double ns = 1e9 / (double) tickPerSecond;
        double delta = 0;
        while (running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1)
            {
                tick();
                render();
                --delta;

            }
        }
    }

    private void tick()
    {
        handler.tick();
        GameManager.tick();
    }

    private void render()
    {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null)
        {
            this.createBufferStrategy(3);
            return;
        }
        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();


        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        GameManager.render(g2d);

        g2d.dispose();
        bs.show();
    }

    public static void main(String[] args)
    {
        new Game();
    }
}
