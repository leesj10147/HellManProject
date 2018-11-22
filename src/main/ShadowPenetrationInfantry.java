package main;

import core.GameManager;
import core.Handler;
import core.ID;
import core.RenderOrder;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ShadowPenetrationInfantry extends BasicInfantry
{
    public ShadowPenetrationInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler)
    {
        this(team, x, y, WIDTH, HEIGHT, id, handler, RenderOrder.Default.order);
    }

    public ShadowPenetrationInfantry(Team team, double x, double y, int WIDTH, int HEIGHT, ID id, Handler handler, int renderOrder)
    {
        super(team, 2, x, y, WIDTH, HEIGHT, id, handler, renderOrder);
        this.damage = 30;
        this.hp = 20;
        this.speed = 18;
        this.nowPosition = team;
        this.inputTeamPositionTime = BattleScene.syncedCurrentTime();
        this.nowPosition = team;
    }

    public ShadowPenetrationInfantry(Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        this(team, x, y, image.getWidth(), image.getHeight(), id, handler, renderOrder);
        this.image = image;
        this.originalImage = image;
    }

    private long inputTeamPositionTime;
    private Team nowPosition;
    private int notSeeTime = 3000;

    @Override
    public void tick()
    {
        super.tick();
        moveToNotIntersection();
        if (GameManager.scene instanceof BattleScene)
        {
            BattleScene bs = (BattleScene) GameManager.scene;
            Team now = bs.getTeamOfSector(this.getMidPoint().getPoint());
            if (now != nowPosition)
            {
                nowPosition = now;
                inputTeamPositionTime = BattleScene.syncedCurrentTime();
            }
        }
        if (this.hp <= 0) handler.removeObject(this);
    }

    @Override
    public void attack(Battleable target)
    {
        this.inputTeamPositionTime = Integer.MIN_VALUE;
        super.attack(target);
    }

    @Override
    public void render(Graphics2D g2d)
    {
        if (nowPosition != this.team && BattleScene.syncedCurrentTime() - inputTeamPositionTime <= notSeeTime)
        {
            float alpha = 0.1f;
            if (this.team != BattleScene.getTeam()) alpha = 0;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.render(g2d);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            this.ignoreCollision = true;
        } else
        {
            this.ignoreCollision = false;
            super.render(g2d);
        }
    }
}
