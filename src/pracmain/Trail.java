package pracmain;

import core.*;

import java.awt.*;

public class Trail extends GameObject
{
    private float alpha = 1;
    private float deltaAlphaPerSecond;
    private Color color;
    private Rectangle rectangle;

    public Trail(ID id, Color color, Rectangle rectangle, float deltaAlphaPerSecond, Handler handler)
    {
        this(id, color, rectangle, deltaAlphaPerSecond, handler, RenderOrder.Default.order);
    }

    public Trail(ID id, Color color, Rectangle rectangle, float deltaAlphaPerSecond, Handler handler, int renderOrder)
    {
        super(rectangle.x, rectangle.y, rectangle.width, rectangle.height, id, handler, renderOrder);
        this.color = color;
        this.rectangle = rectangle;
        this.deltaAlphaPerSecond = deltaAlphaPerSecond;
    }

    @Override
    public void tick()
    {
        alpha += deltaAlphaPerSecond / (float) Game.tickPerSecond;
        if (alpha < 0) handler.removeObject(this);
    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(color);
        g2d.fill(rectangle);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
    }

    @Override
    public Rectangle getBounds()
    {
        return null;
    }
}
