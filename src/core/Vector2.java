package core;

import java.awt.*;
import java.io.Serializable;

public class Vector2 implements Serializable
{
    private static final long serialVersionUID = 1113L;
    public static final Vector2 down = new Vector2(0, 1);
    public static final Vector2 left = new Vector2(-1, 0);
    public static final Vector2 one = new Vector2(1, 1);
    public static final Vector2 right = new Vector2(1, 0);
    public static final Vector2 up = new Vector2(0, -1);
    public static final Vector2 zero = new Vector2(0, 0);
    public double x, y;

    public Vector2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (obj instanceof Vector2)
        {
            Vector2 v2 = (Vector2) obj;
            if (v2.x == x && v2.y == y) return true;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return (int) (x * 10007 + y);
    }

    public double magnitude()
    {
        return Math.sqrt(x * x + y * y);
    }

    public double sqrMagnitude()
    {
        return x * x + y * y;
    }

    public void multiply(double a)
    {
        this.x *= a;
        this.y *= a;
    }

    public void add(double x, double y)
    {
        this.x += x;
        this.y += y;
    }

    public void add(double a)
    {
        this.x += a;
        this.y += a;
    }

    public Vector2 normalize()
    {
        double sz = magnitude();
        return new Vector2(x / sz, y / sz);
    }

    public double distance(Vector2 v2)
    {
        return Math.sqrt((v2.x - this.x) * (v2.x - this.x) + (v2.y - this.y) * (v2.y - this.y));
    }

    public Point getPoint()
    {
        return new Point((int) x, (int) y);
    }

    @Override
    public String toString()
    {
        return "(" + x + "," + y + ")";
    }
}
