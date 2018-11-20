package main;

import core.Handler;
import core.ID;
import core.RenderOrder;

public class HeavyArmorInfantry extends BasicInfantry
{
    public HeavyArmorInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(team, x, y, WIDTH, HEIGHT, id, handler, RenderOrder.Default.order);
    }

    public HeavyArmorInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(team, 7, x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.damage = 3;
        this.hp = 7;
        this.speed =8;
    }
    @Override
    public void tick()
    {
        super.tick();
    }
}
