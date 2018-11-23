package main;

import core.GameManager;
import core.GameObject;
import core.Handler;
import core.ID;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Farm extends BasicBarrier
{
    public Farm(Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        super(team, x, y, image, id, handler, renderOrder);
        this.MAX_HP = 1500;
        this.hp = 1500;
    }
    private transient long lastGiveMineTime = 0;
    @Override
    public void tick()
    {
        super.tick();
        if (this.team != BattleScene.getTeam()) return;
        if (BattleScene.syncedCurrentTime() - lastGiveMineTime < 3000) return;
        lastGiveMineTime = BattleScene.syncedCurrentTime();
        for (GameObject obj : handler.getObjects())
        {
            if (obj instanceof MoneyMakerInfantry && obj.getMidPoint().distance(this.getMidPoint()) < 200)
            {
                if (((MoneyMakerInfantry) obj).team != this.team) continue;
                ((MoneyMakerInfantry) obj).crop+=2;
                GameManager.playSound("sound/채집sound3.wav", false);
            }
        }
    }
}
