package main;

import core.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Nexus extends BasicTower implements Battleable
{
    private String[] itemList = new String[6];
    private String[] itemDescription = new String[6];

    public Nexus(Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        super(team, x, y, GameManager.loadImage("cannon.png"), id, handler, renderOrder);
        this.hp = 2000;
        this.MAX_HP = 2000;
        this.mustUpdateMouseClickedLocation = true;
        itemList[0] = "BasicInfantry.png";
        itemList[1] = "cannon.png";
        itemList[2] = "BasicInfantry.png";
        itemList[3] = "BasicInfantry.png";
        itemList[4] = "BasicInfantry.png";
        itemList[5] = "BasicInfantry.png";

        itemDescription[0] = "이 보병은 정말 적잘하다고 하는 것이 적절한 설명이라고 생각하는 게 적절하다고 생각합니다.";
        itemDescription[1] = "적절한 사거리와 적절한 데미지를 가지고, 적절한 크기와 함께 적절한 아름다움을 가진 적절한 대포입니다.\n 적절한 가격으로 적절하게 지을 수 있습니다.";
        itemDescription[2] = "한 대만 맞으면 부셔져 버릴 것 같은 모습과는 다르게, 끝에 도달하기만 한다면... 콰광!";
        itemDescription[3] = "하나는 약하지만, 모이면 강력합니다! 3인조로 뭉쳐 다니는 이 골칫거리들은 하는 짓마다 얄밉기짝이 없다고 하더군요.";
        itemDescription[4] = "그림자같이 은밀하고 빠르게 침투한다고 하여 그림자 침투병이 되었습니다.\n하지만, 걸친 게 천쪼가리뿐이라 너무나도 연약한 존재이기도 합니다.";
        itemDescription[5] = "";
        this.printBottom = false;
    }

    protected Rectangle[] items()
    {
        Rectangle[] ret = new Rectangle[6];
        if (!new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT).contains(BattleScene.getOnScreenLocation(this.getMidPoint().getPoint())))
        {
            for (int r = 0; r < 2; ++r)
                for (int c = 0; c < 3; ++c)
                {
                    ret[3 * r + c] = new Rectangle(c * 83 + BattleScene.getCameraX(), r * 50 + BattleScene.getCameraY(), 83, 50);
                }
        } else
        {
            for (int r = 0; r < 2; ++r)
                for (int c = 0; c < 3; ++c)
                {
                    ret[3 * r + c] = new Rectangle(c * 83 + (int) this.getMidPoint().x, r * 50 + (int) this.getMidPoint().y, 83, 50);
                }
        }
        return ret;
    }

    private boolean selectedArrangementUnit = false;
    private String imageArrangementUnit;
    private int itemNumberOfSelectedUnit;
    private int itemOnMouse = -1;

    boolean canArrange(Point MidPoint)
    {
        ArrayList<GameObject> towers = handler.findObjectsById(ID.Tower);
        towers.add(this);
        for (GameObject tower : towers)
        {
            if (tower.getMidPoint().getPoint().distance(MidPoint) < 500) return true;
        }
        return false;
    }

    @Override
    public void tick()
    {
        super.tick();
        if (BattleScene.getTeam() != this.team) return;
        if (selectedArrangementUnit)
        {
            BufferedImage itemImage = GameManager.loadImage(imageArrangementUnit);
            Point p = BattleScene.getOnMapLocation(MouseInput.getLocation());
            p.x -= itemImage.getWidth() / 2;
            p.y -= itemImage.getHeight() / 2;
            Rectangle chk = new Rectangle(p.x, p.y, itemImage.getWidth(), itemImage.getHeight());
            if (!this.mouseClickedOnMapLocation.equals(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE)) &&
                        canArrange(new Point((int) (chk.x + chk.getWidth() / 2), (int) (chk.y + chk.getHeight() / 2))) && handler.Collide(chk).size() == 0)
            {
                if (itemNumberOfSelectedUnit == 1)
                    handler.addObject(new BasicTower(this.team, p.x, p.y, itemImage, ID.Tower, handler, RenderOrder.Main.order));


                selectedArrangementUnit = false;
            }
        }
        Rectangle[] rect = items();
        if (selectedByMouse)
        {
            if (rect[0].contains(mouseClickedOnMapLocation))
            {
                handler.addObject(new NormalInfantry(team, this.x + this.getWIDTH(), this.y + this.getWIDTH(), GameManager.loadImage(itemList[0]), ID.Infantry, handler, RenderOrder.Main.order));
                this.mouseClickedOnMapLocation = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
            } else if (rect[1].contains(mouseClickedOnMapLocation))
            {
                imageArrangementUnit = itemList[1];
                selectedArrangementUnit = true;
                itemNumberOfSelectedUnit = 1;
                this.mouseClickedOnMapLocation = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
            } else if (rect[2].contains(mouseClickedOnMapLocation))
            {
                handler.addObject(new SkeletonBombInfantry(team, this.x + this.getWIDTH(), this.y + this.getWIDTH(), GameManager.loadImage(itemList[2]), ID.Infantry, handler, RenderOrder.Main.order));
            } else if (rect[3].contains(mouseClickedOnMapLocation))
            {
                handler.addObject(new GoblinGangInfantry(team, this.x + this.getWIDTH(), this.y + this.getWIDTH(), GameManager.loadImage(itemList[3]), ID.Infantry, handler, RenderOrder.Main.order));
            } else if (rect[4].contains(mouseClickedOnMapLocation))
            {
                handler.addObject(new ShadowPenetrationInfantry(team, this.x + this.getWIDTH(), this.y + this.getWIDTH(), GameManager.loadImage(itemList[3]), ID.Infantry, handler, RenderOrder.Main.order));
            }
            Point p = BattleScene.getOnMapLocation(MouseInput.getLocation());
            boolean contain = false;
            for (int i = 0; i < rect.length; ++i)
            {
                if (rect[i].contains(p))
                {
                    itemOnMouse = i;
                    contain = true;
                }
            }
            if (!contain)
                itemOnMouse = -1;
        } else itemOnMouse = -1;
        this.mouseClickedOnMapLocation = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public void render(Graphics2D g2d)
    {
        super.render(g2d);
        if (BattleScene.getTeam() != this.team) return;
        if (selectedByMouse)
        {
            g2d.setColor(Color.green);
            Rectangle[] rectangles = items();
            for (int i = 0; i < itemList.length; ++i)
            {
                g2d.draw(rectangles[i]);
                BufferedImage itemImage = GameManager.loadImage(itemList[i]);
                double scaleX = (double) rectangles[i].width / itemImage.getWidth();
                double scaleY = (double) rectangles[i].height / itemImage.getHeight();
                AffineTransform oldTransform = g2d.getTransform();
                AffineTransform newTransform = new AffineTransform(oldTransform);
                newTransform.scale(scaleX, scaleY);
                g2d.setTransform(newTransform);
                g2d.drawImage(itemImage, (int) (rectangles[i].x / scaleX), (int) (rectangles[i].y / scaleY), null);
                g2d.setTransform(oldTransform);
            }
        }
        if (selectedArrangementUnit)
        {
            BufferedImage itemImage = GameManager.loadImage(imageArrangementUnit);
            Point p = BattleScene.getOnMapLocation(MouseInput.getLocation());
            p.x -= itemImage.getWidth() / 2;
            p.y -= itemImage.getHeight() / 2;
            if (handler.Collide(new Rectangle(p.x, p.y, itemImage.getWidth(), itemImage.getHeight())).size() > 0
                        || !canArrange(new Point((p.x + itemImage.getWidth() / 2), (p.y + itemImage.getHeight() / 2))))
                itemImage = GameManager.setEdge(itemImage, Color.red);
            else
                itemImage = GameManager.setEdge(itemImage, Color.green);
            g2d.drawImage(itemImage, p.x, p.y, null);
        }
        if (itemOnMouse != -1)
        {
            Point p = BattleScene.getOnMapLocation(MouseInput.getLocation());
            g2d.setColor(Color.yellow);
            g2d.drawString(itemDescription[itemOnMouse], p.x, p.y);
        }
    }

    @Override
    public Rectangle getBounds()
    {
        return new Rectangle((int) x, (int) y, WIDTH, HEIGHT);
    }

}
