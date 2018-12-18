package main;

import core.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Nexus extends BasicTower implements Battleable
{
    private transient String[] itemList = new String[11];
    private transient String[] itemDescription = new String[11];
    public transient int gold = 50;
    public transient int crop = 20;
    public transient int odamant = 10;

    public Nexus(Team team, double x, double y, BufferedImage image, ID id, Handler handler, int renderOrder)
    {
        super(team, x, y, GameManager.loadImage("cannon.png"), id, handler, renderOrder);
        this.hp = 2000;
        this.MAX_HP = 2000;
        this.damage = 20;
        this.mustUpdateMouseClickedLocation = true;
        itemList[0] = "BasicInfantry.png";
        itemList[1] = "cannon.png";
        itemList[2] = "BasicInfantry.png";
        itemList[3] = "BasicInfantry.png";
        itemList[4] = "BasicInfantry.png";
        itemList[5] = "BasicInfantry.png";
        itemList[6] = "rowBarrier.png";
        itemList[7] = "colBarrier.png";
        itemList[8] = "BasicInfantry.png";
        itemList[9] = "BasicInfantry.png";
        itemList[10] = "rowBarrier.png";
        itemDescription[0] = "이 친구는 주변의 광산, 농장, 제련소에서 노동을 하며 돈을 벌 수 있습니다.\n 기본적인 공격력과 방어력도 가지고 있지요.\n 가격 : 10gold";
        itemDescription[1] = "적절한 사거리와 적절한 데미지를 가지고, 적절한 크기와 함께 적절한 아름다움을 가진 적절한 대포입니다.\n 적절한 가격으로 적절하게 지을 수 있습니다.\n 가격 : 20gold + 10crop + 5odamant";
        itemDescription[2] = "한 대만 맞으면 부셔져 버릴 것 같은 모습과는 다르게, 끝에 도달하기만 한다면... 콰광!\n 가격 : 10gold + 5crop";
        itemDescription[3] = "하나는 약하지만, 모이면 강력합니다!\n3인조로 뭉쳐 다니는 이 골칫거리들은 하는 짓마다 얄밉기짝이 없다고 하더군요.\n 가격 : 7gold + 10crop";
        itemDescription[4] = "그림자같이 은밀하고 빠르게 침투한다고 하여 그림자 침투병이 되었습니다.\n하지만, 걸친 게 천쪼가리뿐이라 너무나도 연약한 존재이기도 합니다.\n 가격 : 10gold + 10crop + 10odamant";
        itemDescription[5] = "사막잡신 \'느킨\'을 믿는 부족의 주술사입니다.\n이교도라고는 하지만, 아군들의 체력을 채워주는 실력은 그 누구도 부정할 수 없을 만큼 확실합니다.\n 가격 : 8gold + 1odamant";
        itemDescription[6] = "가로 방어막 입니다.\n아무 공격도 하지 않으며, 길을 막아주는 역할만 합니다.\n 가격 : 7gold + 5crop";
        itemDescription[7] = "세로 방어막 입니다.\n아무 공격도 하지 않으며, 길을 막아주는 역할만 합니다.\n 가격 : 7gold + 5crop";
        itemDescription[8] = "원거리 공격이 가능한 마법사 입니다.\n 그러나 집에 박혀 연구만 한 나머지 느린 이동속도와 약한 채력을 가집니다.\n 가격 : 10gold + 3crop";
        itemDescription[9] = "델막샤의 친위대 시절 혹독한 훈련을 받은 불굴의 기사입니다.\n강한 정신력과 높은 채력을 소지하고 있습니다.\n 가격 : 20gold";
        itemDescription[10] = "연막탄 입니다. \n가격 : 30gold";
        this.printBottom = false;
    }

    protected Rectangle[] items()
    {
        Rectangle[] ret = new Rectangle[11];
        if (!new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT).contains(BattleScene.getOnScreenLocation(this.getMidPoint().getPoint())))
        {
            for (int r = 0; r < 2; ++r)
            {
                for (int c = 0; c < 5; ++c)
                {
                    ret[5 * r + c] = new Rectangle(c * 83 + BattleScene.getCameraX(), r * 50 + BattleScene.getCameraY(), 83, 50);
                }
            }
            ret[10] = new Rectangle(5 * 83 + BattleScene.getCameraX(), BattleScene.getCameraY(), 83, 50);
        } else
        {
            for (int r = 0; r < 2; ++r)
            {
                for (int c = 0; c < 5; ++c)
                {
                    ret[5 * r + c] = new Rectangle(c * 83 + (int) this.getMidPoint().x, r * 50 + (int) this.getMidPoint().y, 83, 50);
                }
            }
            ret[10] = new Rectangle(5 * 83 + (int) this.getMidPoint().x, (int) this.getMidPoint().y, 83, 50);
        }
        return ret;
    }

    private boolean selectedArrangementUnit = false;
    private String imageArrangementUnit;
    private int itemNumberOfSelectedUnit;
    private int itemOnMouse = -1;

    boolean canArrange(Point MidPoint)
    {
        ConcurrentLinkedQueue<GameObject> towers = handler.findObjectsById(ID.Tower);
        towers.add(this);
        for (GameObject tower : towers)
        {
            if (tower instanceof BasicTower)
            {
                if (((BasicTower) tower).team != this.team) continue;
                if (tower.getMidPoint().getPoint().distance(MidPoint) < 500) return true;
            }
        }
        return false;
    }

    private transient long lastAddDefaultGoldTime = BattleScene.syncedCurrentTime();

    @Override
    public void tick()
    {
        super.tick();
        if (BattleScene.getTeam() != this.team) return;
        if (BattleScene.syncedCurrentTime() - lastAddDefaultGoldTime >= 10000)
        {
            gold++;
            lastAddDefaultGoldTime = BattleScene.syncedCurrentTime();
        }
        if (selectedArrangementUnit)
        {
            BufferedImage itemImage = GameManager.loadImage(imageArrangementUnit);
            Point p = BattleScene.getOnMapLocation(MouseInput.getLocation());
            p.x -= itemImage.getWidth() / 2;
            p.y -= itemImage.getHeight() / 2;
            Rectangle chk = new Rectangle(p.x, p.y, itemImage.getWidth(), itemImage.getHeight());
            if (!this.mouseClickedOnMapLocation.equals(new Point(Integer.MAX_VALUE, Integer.MIN_VALUE))
                        && canArrange(new Point((int) (chk.x + chk.getWidth() / 2), (int) (chk.y + chk.getHeight() / 2))) && itemNumberOfSelectedUnit == 10)
            {
                handler.addObject(new SmokeShell(this.team, p.x, p.y, itemImage, ID.Smoke, handler, 6000));
            }
            else if (!this.mouseClickedOnMapLocation.equals(new Point(Integer.MAX_VALUE, Integer.MAX_VALUE)) &&
                        canArrange(new Point((int) (chk.x + chk.getWidth() / 2), (int) (chk.y + chk.getHeight() / 2))) && handler.Collide(chk).size() == 0)
            {
                if (itemNumberOfSelectedUnit == 1)
                    handler.addObject(new BasicTower(this.team, p.x, p.y, itemImage, ID.Tower, handler, RenderOrder.ForeGround.order));
                else if (itemNumberOfSelectedUnit == 6)
                    handler.addObject(new BasicBarrier(this.team, p.x, p.y, itemImage, ID.Barrier, handler, RenderOrder.ForeGround.order));
                else if (itemNumberOfSelectedUnit == 7)
                    handler.addObject(new BasicBarrier(this.team, p.x, p.y, itemImage, ID.Barrier, handler, RenderOrder.ForeGround.order));
                selectedArrangementUnit = false;
            }
        }
        Rectangle[] rect = items();
        if (selectedByMouse)
        {
            if (rect[0].contains(mouseClickedOnMapLocation))
            {
                if (gold >= 10)
                {
                    handler.addObject(new MoneyMakerInfantry(team, this.x + this.getWIDTH(), this.y + this.getHEIGHT(), GameManager.loadImage(itemList[0]), ID.Infantry, handler, RenderOrder.Main.order));
                    gold -= 10;
                }
            } else if (rect[1].contains(mouseClickedOnMapLocation))
            {
                if (gold >= 20 && crop >= 10 && odamant >= 5)
                {
                    imageArrangementUnit = itemList[1];
                    selectedArrangementUnit = true;
                    itemNumberOfSelectedUnit = 1;
                    this.mouseClickedOnMapLocation = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    gold -= 20;
                    crop -= 10;
                    odamant -= 5;
                }
            } else if (rect[10].contains(mouseClickedOnMapLocation))
            {
                if (gold >= 30)
                {
                    imageArrangementUnit = itemList[10];
                    selectedArrangementUnit = true;
                    itemNumberOfSelectedUnit = 10;
                    this.mouseClickedOnMapLocation = new Point(Integer.MAX_VALUE, Integer.MIN_VALUE);
                    gold -= 30;
                }
            } else if (rect[2].contains(mouseClickedOnMapLocation))
            {
                if (gold >= 10 && crop >= 5)
                {
                    handler.addObject(new SkeletonBombInfantry(team, this.x + this.getWIDTH(), this.y + this.getHEIGHT(), GameManager.loadImage(itemList[2]), ID.Infantry, handler, RenderOrder.Main.order));
                    gold -= 10;
                    crop -= 5;
                }
            } else if (rect[3].contains(mouseClickedOnMapLocation))
            {
                if (gold >= 7 && crop >= 10)
                {
                    handler.addObject(new GoblinGangInfantry(team, this.x + this.getWIDTH(), this.y + this.getHEIGHT(), GameManager.loadImage(itemList[3]), ID.Infantry, handler, RenderOrder.Main.order));
                    gold -= 7;
                    crop -= 10;
                }
            } else if (rect[4].contains(mouseClickedOnMapLocation))
            {
                if (gold >= 10 && crop >= 10 && odamant >= 10)
                {
                    handler.addObject(new ShadowPenetrationInfantry(team, this.x + this.getWIDTH(), this.y + this.getHEIGHT(), GameManager.loadImage(itemList[4]), ID.Infantry, handler, RenderOrder.Main.order));
                    gold -= 10;
                    crop -= 10;
                    odamant -= 10;
                }
            } else if (rect[5].contains(mouseClickedOnMapLocation))
            {
                if (gold >= 8 && odamant >= 1)
                {
                    handler.addObject(new NeukinShamanInfantry(team, this.x + this.getWIDTH(), this.y + this.getHEIGHT(), GameManager.loadImage(itemList[5]), ID.Infantry, handler, RenderOrder.Main.order));
                    gold -= 8;
                    odamant -= 1;
                }
            } else if (rect[6].contains(mouseClickedOnMapLocation))
            {
                if (gold >= 7 && crop >= 5)
                {
                    imageArrangementUnit = itemList[6];
                    selectedArrangementUnit = true;
                    itemNumberOfSelectedUnit = 6;
                    this.mouseClickedOnMapLocation = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    gold -= 7;
                    crop -= 5;
                }
            } else if (rect[7].contains(mouseClickedOnMapLocation))
            {
                if (gold >= 7 && crop >= 5)
                {
                    imageArrangementUnit = itemList[7];
                    selectedArrangementUnit = true;
                    itemNumberOfSelectedUnit = 7;
                    this.mouseClickedOnMapLocation = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
                    gold -= 7;
                    crop -= 5;
                }
            } else if (rect[8].contains(mouseClickedOnMapLocation))
            {
                if (gold >= 10 && crop >= 3)
                {
                    handler.addObject(new MagicianInfantry(team, this.x + this.getWIDTH(), this.y + this.getHEIGHT(), GameManager.loadImage(itemList[8]), ID.Infantry, handler, RenderOrder.Main.order));
                    gold -= 10;
                    crop -= 3;
                }
            } else if (rect[9].contains(mouseClickedOnMapLocation))
            {
                if (gold >= 20)
                {
                    handler.addObject(new HeavyArmorInfantry(team, this.x + this.getWIDTH(), this.y + this.getHEIGHT(), GameManager.loadImage(itemList[8]), ID.Infantry, handler, RenderOrder.Main.order));
                    gold -= 20;
                }
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

    private Font textFont = new Font("Serif", Font.BOLD, 15);
    private Font moneyFont = new Font("Serif", Font.BOLD, 30);


    private void drawString(Graphics2D g, String text, int x, int y)
    {
        y += g.getFontMetrics().getHeight();
        for (String line : text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
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
            if (itemNumberOfSelectedUnit == 10)
            {
                if (canArrange(new Point((p.x + itemImage.getWidth() / 2), (p.y + itemImage.getHeight() / 2))))
                    itemImage = GameManager.setEdge(itemImage, Color.green);
                else
                    itemImage = GameManager.setEdge(itemImage, Color.red);

            }
            else if (handler.Collide(new Rectangle(p.x, p.y, itemImage.getWidth(), itemImage.getHeight())).size() > 0
                        || !canArrange(new Point((p.x + itemImage.getWidth() / 2), (p.y + itemImage.getHeight() / 2))))
                itemImage = GameManager.setEdge(itemImage, Color.red);
            else
                itemImage = GameManager.setEdge(itemImage, Color.green);

            g2d.drawImage(itemImage, p.x, p.y, null);
        }
        if (itemOnMouse != -1)
        {
            Point p = BattleScene.getOnMapLocation(MouseInput.getLocation());
            g2d.setColor(Color.BLACK);
            g2d.setFont(textFont);
            drawString(g2d, itemDescription[itemOnMouse], p.x, p.y);
        }
        g2d.setFont(moneyFont);
        g2d.setColor(Color.orange);
        g2d.drawString("Gold : " + gold, BattleScene.getCameraX() + Game.WIDTH / 2 - 300, BattleScene.getCameraY() + 150);
        g2d.setColor(Color.orange);
        g2d.drawString("Crop : " + crop, BattleScene.getCameraX() + Game.WIDTH / 2, BattleScene.getCameraY() + 150);
        g2d.setColor(Color.BLUE);
        g2d.drawString("Odamant : " + odamant, BattleScene.getCameraX() + Game.WIDTH / 2 + 300, BattleScene.getCameraY() + 150);
    }

    @Override
    public Rectangle getBounds()
    {
        return new Rectangle((int) x, (int) y, WIDTH, HEIGHT);
    }

}
