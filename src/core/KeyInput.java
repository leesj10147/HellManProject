package core;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ConcurrentSkipListSet;

public class KeyInput extends KeyAdapter
{
    private static ConcurrentSkipListSet<Integer> input = new ConcurrentSkipListSet<>();
    @Override
    public void keyPressed(KeyEvent e)
    {
        input.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        input.remove(e.getKeyCode());
    }
    public static boolean isKeyPressed(int keyCode)
    {
        return input.contains(keyCode);
    }
}
