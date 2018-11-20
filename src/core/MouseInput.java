package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.concurrent.LinkedBlockingQueue;

public class MouseInput implements MouseListener, MouseMotionListener
{
    private static LinkedBlockingQueue<MouseEvent> events = new LinkedBlockingQueue<>(100);
    private static JFrame _frame;
    private static int _xMargin, _yMargin;

    public MouseInput(JFrame frame, int xMargin, int yMargin)
    {
        _frame = frame;
        _xMargin = xMargin;
        _yMargin = yMargin;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        while (!events.offer(e)) events.poll();
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        while (!events.offer(e)) events.poll();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        while (!events.offer(e)) events.poll();
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        while (!events.offer(e)) events.poll();
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        while (!events.offer(e)) events.poll();
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        while (!events.offer(e)) events.poll();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        while (!events.offer(e)) events.poll();
    }

    public static MouseEvent pollEvent()
    {
        return events.poll();
    }

    public static boolean hasEvent()
    {
        return events.size() > 0;
    }

    public static Point getLocation()
    {
        Point p = MouseInfo.getPointerInfo().getLocation();

        p.x -= _frame.getX() + _xMargin;
        p.y -= _frame.getY() + _yMargin;
        return p;
    }
    public static void clearEvent()
    {
        events.clear();
    }
}
