package main;

import core.GameObject;
import core.Handler;
import core.ID;
import core.RenderOrder;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class NeukinShamanInfantry extends BasicInfantry
{
    public NeukinShamanInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(team, x, y, WIDTH, HEIGHT, id, handler, RenderOrder.Default.order);
    }

    public NeukinShamanInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(team, 40, x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.damage = 0;
        this.attackRange = 3;
        this.delayBetweenAttack = Integer.MAX_VALUE;
        this.hp = 40;
        this.speed = 10;
        this.notAttack = true;
    }
    public NeukinShamanInfantry(Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        this(team, x, y, image.getWidth(), image.getHeight(), id, handler, renderOrder);
        this.image = image;
        this.originalImage = image;
    }
    private long lastTime = 0;

    @Override
    public void tick()
    {
        super.tick();
        moveToNotIntersection();
        if (this.hp<=0)
        {
            handler.removeObject(this);
            return;
        }
        long nowTime = BattleScene.syncedCurrentTime();
        if (nowTime - lastTime <= 3000) return;
        lastTime = nowTime;
        ArrayList<GameObject> infantries = handler.findObjectsById(ID.Infantry);
        for (GameObject object : infantries)
        {
            if (object == this) continue;
            BasicInfantry infantry = (BasicInfantry) object;
            if (infantry.getMidPoint().distance(this.getMidPoint()) < 100)
            if (infantry.team == this.team) infantry.hp += 3;
        }
    }
}
