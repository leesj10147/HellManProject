package core;

import main.BattleScene;
import main.Team;
import network.CheckGameObject;
import network.DamageInfo;

import java.awt.*;
import java.io.Serializable;

public abstract class GameObject implements Comparable, Serializable
{

    public boolean isMustUpdateMouseClickedLocation()
    {
        return mustUpdateMouseClickedLocation;
    }

    protected boolean mustUpdateMouseClickedLocation = false;
    public Point mouseClickedOnMapLocation;
    public transient Handler handler;
    protected double x;
    protected double y;
    protected ID id;
    protected double velX;
    protected double velY;
    protected int renderOrder;

    public boolean isIgnoreCollision()
    {
        return ignoreCollision;
    }

    public void setIgnoreCollision(boolean ignoreCollision)
    {
        this.ignoreCollision = ignoreCollision;
    }

    protected boolean ignoreCollision;

    public boolean isSelectedByMouse()
    {
        return selectedByMouse;
    }

    public void setSelectedByMouse(boolean selectedByMouse)
    {
        this.selectedByMouse = selectedByMouse;
    }

    protected boolean selectedByMouse = false;

    public int getWIDTH()
    {
        return WIDTH;
    }

    public int getHEIGHT()
    {
        return HEIGHT;
    }

    protected final int WIDTH;
    protected final int HEIGHT;

    public GameObject(double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(x, y, WIDTH, HEIGHT, id, handler, RenderOrder.Default.order);
    }

    private static int serial = 10;
    private final int distinguishCode;
    public GameObject(double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        this.x = x;
        this.y = y;
        this.id = id;
        this.handler = handler;
        this.renderOrder = renderOrder;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.mouseClickedOnMapLocation = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        serial++;
        distinguishCode = BattleScene.getTeam() == Team.Red ? serial : serial + (int)1e9;
    }

    public abstract void tick();

    public abstract void render(Graphics2D g2d);

    public abstract Rectangle getBounds();

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void setId(ID id)
    {
        this.id = id;
    }

    public ID getId()
    {
        return id;
    }

    public void setVelX(double velX)
    {
        this.velX = velX;
    }

    public void setVelY(double velY)
    {
        this.velY = velY;
    }

    public double getVelX()
    {
        return velX;
    }

    public double getVelY()
    {
        return velY;
    }

    public int getRenderOrder()
    {
        return renderOrder;
    }

    public void setRenderOrder(int renderOrder)
    {
        this.renderOrder = renderOrder;
    }

    @Override
    public int hashCode()
    {
        return distinguishCode;
    }
    @Override
    public boolean equals(Object obj)
    {
        return hashCode() == obj.hashCode();
    }

    @Override
    public int compareTo(Object o)
    {
        GameObject tempObject = (GameObject) o;
        if (this.renderOrder == tempObject.renderOrder) return this.hashCode() - tempObject.hashCode();
        return this.renderOrder - tempObject.renderOrder;
    }

    public Vector2 getMidPoint()
    {
        return new Vector2(x + WIDTH / 2, y + HEIGHT / 2);

    }
}
