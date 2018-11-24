package main;

import core.GameObject;
import core.Handler;
import core.ID;
import core.RenderOrder;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MoneyMakerInfantry extends NormalInfantry
{
    protected MoneyMakerInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(team, x, y, WIDTH, HEIGHT, id, handler, RenderOrder.Default.order);
    }

    public MoneyMakerInfantry(Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        this(team, x, y, image.getWidth(), image.getHeight(), id, handler, renderOrder);
        this.image = image;
        this.originalImage = image;
    }
    private Nexus myTeamNexus;
    protected MoneyMakerInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(team, x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        ConcurrentLinkedQueue<GameObject> arr = handler.findObjectsById(ID.Nexus);
        for (GameObject object : arr)
        {
            if (object instanceof Nexus && ((Nexus) object).team == this.team)
                myTeamNexus = (Nexus) object;
        }
    }
    public int gold = 0;
    public int crop = 0;
    public int odamant = 0;
    @Override
    public void tick()
    {
        super.tick();
        if (this.getMidPoint().distance(myTeamNexus.getMidPoint()) < 200)
        {
            //do something
            myTeamNexus.gold += this.gold;
            myTeamNexus.crop += this.crop;
            myTeamNexus.odamant += this.odamant;
            this.gold = this.crop = this.odamant = 0;
        }
    }
}
