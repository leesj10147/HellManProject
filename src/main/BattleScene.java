package main;

import core.*;
import network.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class BattleScene extends Scene
{
    private static Team team = Team.Red;


    public static Team getTeam()
    {
        return team;
    }

    private InetAddress otherAddress;
    private Receive receive;
    private Send send;
    private Rectangle[] sectors;
    private Team[] teamOfSector;

    public BattleScene(Handler handler)
    {
        super(handler);
        setInfo();
        Nexus test6;
        if (team == Team.Red)
        {
            test6 = new Nexus(team, 100, 100, GameManager.loadImage("cannon.png"), ID.Nexus, handler, 3100);
            handler.addObject(new Mine(team, 500, 500, GameManager.loadImage("mine.png"), ID.Barrier, handler, 3000));
            handler.addObject(new Farm(team, 100, 500, GameManager.loadImage("farm.png"), ID.Barrier, handler, 3000));
            handler.addObject(new OdaMine(team, 500, 100, GameManager.loadImage("odamine.png"), ID.Barrier, handler, 3000));
        } else
        {
            test6 = new Nexus(team, 4000, 2770, GameManager.loadImage("cannon.png"), ID.Nexus, handler, 3100);
            handler.addObject(new Mine(team, 3600, 2370, GameManager.loadImage("mine.png"), ID.Barrier, handler, 3000));
            handler.addObject(new Farm(team, 4000, 2370, GameManager.loadImage("farm.png"), ID.Barrier, handler, 3000));
            handler.addObject(new OdaMine(team, 3600, 2770, GameManager.loadImage("odamine.png"), ID.Barrier, handler, 3000));
        }
        handler.addObject(test6);

        sectors = new Rectangle[9];
        teamOfSector = new Team[9];
        for (int i = 0; i < 9; ++i)
        {
            int r = i / 3, c = i % 3;
            teamOfSector[i] = Team.Neutrality;
            sectors[i] = new Rectangle(1400 * r, 990 * c, 1400, 990);
        }
        teamOfSector[0] = Team.Red;
        teamOfSector[8] = Team.Blue;
        System.out.println("end constructor");
        GameManager.scene = this;
        GameManager.endMake = true;
    }

    public Team getTeamOfSector(Point p)
    {
        for (int i = 0; i < sectors.length; ++i)
            if (sectors[i].contains(p)) return teamOfSector[i];
        return Team.Neutrality;
    }

    private void setInfo()
    {
        class MyDialogPopup extends JDialog
        {
            AtomicBoolean end = new AtomicBoolean(false);
            BufferedImage backgroundImage = GameManager.loadImage("dialogBackground.jpg");
            MyDialogPopup()
            {
                JPanel mp = new JPanel(new FlowLayout())
                {
                    @Override
                    protected void paintComponent(Graphics g)
                    {
                        super.paintComponent(g);
                        g.drawImage(backgroundImage, 0,0,this);
                    }
                    @Override
                    public Dimension getPreferredSize()
                    {
                        Dimension size = super.getPreferredSize();
                        size.width = Math.max(backgroundImage.getWidth(), size.width);
                        size.height = Math.max(backgroundImage.getHeight(), size.height);
                        return size;
                    }
                };
                this.add(mp);
                this.pack();
                setTitle("Type Your Info");
                setLocationRelativeTo(null);

                JLabel label1 = new JLabel("Team(Red: Server, Blue: Client) : ", JLabel.TRAILING);
                mp.add(label1);
                JTextField textField1 = new JTextField(30);
                label1.setLabelFor(textField1);
                mp.add(textField1);


                JLabel label4 = new JLabel("otherAddress : ", JLabel.TRAILING);
                mp.add(label4);
                JTextField textField4 = new JTextField(30);
                label1.setLabelFor(textField4);
                mp.add(textField4);

                JLabel label5 = new JLabel("                                         OK : ", JLabel.TRAILING);
                mp.add(label5);
                JButton button = new JButton("OK");
                label1.setLabelFor(button);
                mp.add(button);
                setVisible(true);
                button.addActionListener((ActionEvent e) ->
                {
                    String t1 = textField1.getText();
                    String t4 = textField4.getText();
                    if (t1.equalsIgnoreCase("red")) team = Team.Red;
                    else team = Team.Blue;
                    try
                    {
                        otherAddress = InetAddress.getByName(t4);
                        System.out.println(otherAddress.isReachable(30));
                        System.out.println(otherAddress.getHostName());
                    } catch (UnknownHostException e1)
                    {
                        e1.printStackTrace();
                        System.exit(0);
                    } catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }

                    if (team == Team.Red)
                    {
                        ServerSocket ss = null;
                        Socket s = null;
                        try
                        {
                            ss = new ServerSocket(3539);
                            System.out.println("a1");
                            s = ss.accept();
                            System.out.println("a2");
                        } catch (IOException e1)
                        {
                            e1.printStackTrace();
                        }
                        try
                        {
                            receive = new TcpReceive(s.getInputStream());
                        } catch (IOException e1)
                        {
                            e1.printStackTrace();
                        }
                        try
                        {
                            send = new TcpSend(otherAddress, s.getOutputStream());
                        } catch (IOException e1)
                        {
                            e1.printStackTrace();
                        }
                    } else
                    {
                        Socket s = null;
                        try
                        {
                            System.out.println("c1");
                            s = new Socket(otherAddress, 3539);
                            System.out.println("c2");
                        } catch (IOException e1)
                        {
                            e1.printStackTrace();
                        }
                        try
                        {
                            send = new TcpSend(otherAddress, s.getOutputStream());
                        } catch (IOException e1)
                        {
                            e1.printStackTrace();
                        }
                        try
                        {
                            receive = new TcpReceive(s.getInputStream());
                        } catch (IOException e1)
                        {
                            e1.printStackTrace();
                        }
                    }
                    try
                    {
                        Thread.sleep(333);
                    } catch (InterruptedException e1)
                    {
                        e1.printStackTrace();
                    }
                    new Thread(receive).start();
                    System.out.println("end setting info");
                    end.set(true);
                    this.setVisible(false);
                    this.dispose();


                });

            }
        }
        MyDialogPopup j = new MyDialogPopup();
        while (!j.end.get())
        {
            //System.out.println("loop");
        }
        GameManager.playSound("sound\\backgroundMusic.wav", true);

    }

    private boolean isMouseSelected = false;
    private int startX, startY;
    private Point onMapStart;
    private HashSet<BasicInfantry> selectedInfantry = new HashSet<>();

    public void unSelect(BasicInfantry infantry)
    {
        selectedInfantry.remove(infantry);
        infantry.setSelectedByMouse(false);
    }

    public void sendObject(GameObject object)
    {
        send.sendObject(object);
    }

    public static long syncedCurrentTime()
    {
        return System.currentTimeMillis();
    }

    private void updateReceive()
    {
        while (receive.hasData())
        {
            GameObject obj = receive.nextObject();

            if (obj instanceof DamageInfo)
            {
                DamageInfo di = (DamageInfo) obj;
                for (GameObject object : handler.getObjects())
                    if (object.hashCode() == di.targetHashCode && object instanceof Battleable)
                    {
                        ((Battleable) object).applyDamage(null, di.damage);
                        break;
                    }
                continue;
            }
            if (obj instanceof CheckGameObject)
            {
                CheckGameObject chk = (CheckGameObject) obj;
                GameObject del = null;
                for (GameObject object : handler.getObjects())
                {
                    if (object.hashCode() == chk.targetcode) del = object;
                }
                if (del == null) continue;
                handler.getObjects().remove(del);
            }
            if (handler.getObjects().contains(obj))
            {
                handler.getObjects().remove(obj);
            }
            if (obj instanceof BasicInfantry)
            {
                BasicInfantry infantry = (BasicInfantry) obj;
                if (selectedInfantry.contains(obj) && ((BasicInfantry) obj).team == team)
                {
                    obj.setSelectedByMouse(true);
                } else
                    obj.setSelectedByMouse(false);
                if (obj instanceof BasicTower)
                {
                    if (obj instanceof BasicBarrier)
                    {
                        if (obj instanceof Mine)
                        {
                            infantry.originalImage = GameManager.loadImage("mine.png");
                            infantry.image = GameManager.loadImage("mine.png");
                        } else if (obj instanceof Farm)
                        {
                            infantry.originalImage = GameManager.loadImage("farm.png");
                            infantry.image = GameManager.loadImage("farm.png");
                        } else if (obj instanceof OdaMine)
                        {
                            infantry.originalImage = GameManager.loadImage("odamine.png");
                            infantry.image = GameManager.loadImage("odamine.png");
                        } else if (obj.getWIDTH() < obj.getHEIGHT())
                        {
                            infantry.originalImage = GameManager.loadImage("colBarrier.png");
                            infantry.image = GameManager.loadImage("colBarrier.png");
                        } else
                        {
                            infantry.image = GameManager.loadImage("rowBarrier.png");
                            infantry.originalImage = GameManager.loadImage("rowBarrier.png");
                        }
                    } else
                    {
                        infantry.originalImage = GameManager.loadImage("cannon.png");
                        infantry.image = GameManager.loadImage("cannon.png");
                    }
                } else
                {
                    if (obj instanceof NeukinShamanInfantry)
                    {
                        infantry.originalImage= GameManager.loadImage("powwow.png");
                        infantry.image= GameManager.loadImage("powwow.png");
                    }

                    else
                    {
                        infantry.originalImage = GameManager.loadImage("BasicInfantry.png");
                        infantry.image = GameManager.loadImage("BasicInfantry.png");
                    }
                }
            }
            obj.handler = handler;
            handler.getObjects().add(obj);

        }
    }

    int cnt = 0;

    private void updateSend()
    {
        ++cnt;
        if (cnt % 3 != 1) return;
        ConcurrentSkipListSet<GameObject> tosend = handler.getMyTeamObject(team);
        for (GameObject object : tosend)
        {
            if (object instanceof  BasicInfantry)
            {
                if (((BasicInfantry) object).hp <= 0)
                {
                    handler.removeObject(object);
                    continue;
                }
            }
            if (object instanceof Bullet)
            {
                if (((Bullet) object).attacked)
                {
                    handler.removeObject(object);
                    continue;
                }
            }
            send.sendObject(object);

        }
    }

    public static boolean isOurTeam(GameObject object, Team whosTeam)
    {
        if (object instanceof BasicInfantry)
        {
            if (((BasicInfantry) object).team == whosTeam) return true;
            else return false;
        }
        if (object instanceof Bullet)
        {
            if (((Bullet) object).team == whosTeam) return true;
            else return false;
        }
        if (object instanceof Nexus)
        {
            if (((Nexus) object).team == whosTeam) return true;
            else return false;
        }
        return false;
    }

    @Override
    public void tick()
    {
        //Server :  receive -> send
        //Client : send -> receive

        if (team == Team.Red)
        {
            updateReceive();
            updateSend();
        } else
        {
            updateSend();
            updateReceive();
        }
        ConcurrentLinkedQueue<GameObject> towers = handler.findObjectsById(ID.Tower);
        for (int i = 1; i < sectors.length - 1; ++i)
        {
            int redCnt = 0;
            int blueCnt = 0;
            for (GameObject tower : towers)
            {
                if (!sectors[i].contains(tower.getMidPoint().getPoint())) continue;
                if (isOurTeam(tower, Team.Red)) ++redCnt;
                else if (isOurTeam(tower, Team.Blue)) ++blueCnt;
            }
            if (redCnt > blueCnt) teamOfSector[i] = Team.Red;
            else if (blueCnt > redCnt) teamOfSector[i] = Team.Blue;
            else teamOfSector[i] = Team.Neutrality;
        }

        if (MouseInput.hasEvent())
        {
            MouseEvent e = MouseInput.pollEvent();
            if (e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1)
            {
                for (BasicInfantry infantry : selectedInfantry)
                {
                    Point t = getOnMapLocation(e.getPoint());
                    infantry.setDestination(t);
                }
                for (GameObject object : handler.getObjects())
                {
                    if (object.isMustUpdateMouseClickedLocation() && isOurTeam(object, getTeam()))
                    {
                        object.mouseClickedOnMapLocation = getOnMapLocation(MouseInput.getLocation());
                    }
                }
            } else if (e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON3)
            {
                isMouseSelected = true;
                this.startX = e.getX();
                this.startY = e.getY();
                onMapStart = getOnMapLocation(new Point(startX, startY));
            } else if (e.getID() == MouseEvent.MOUSE_RELEASED && e.getButton() == MouseEvent.BUTTON3)
            {


                isMouseSelected = false;
                ConcurrentSkipListSet<GameObject> set = handler.getObjects();
                Point onMapE = getOnMapLocation(e.getPoint());
                Rectangle mouseBounds = new Rectangle(onMapStart.x, onMapStart.y, onMapE.x - onMapStart.x, onMapE.y - onMapStart.y);
                for (GameObject object : set)
                {
                    if (mouseBounds.contains(object.getBounds()))
                    {
                        if (object instanceof BasicInfantry)
                        {
                            if (((BasicInfantry) object).team == team)
                            {
                                object.setSelectedByMouse(true);
                                selectedInfantry.add((BasicInfantry) object);
                            }
                        } else if (object instanceof Bullet)
                        {
                            if (((Bullet) object).team == team)
                            {
                                object.setSelectedByMouse(true);
                            }
                        } else if (object instanceof Nexus)
                        {
                            if (((Nexus) object).team == team)
                            {
                                object.setSelectedByMouse(true);
                            }
                        } else
                        {
                            object.setSelectedByMouse(true);
                        }
                    } else if (e.getPoint().distance(new Point(startX, startY)) > 10)
                    {
                        object.setSelectedByMouse(false);
                        if (object instanceof BasicInfantry) selectedInfantry.remove(object);
                    }
                }

            }

        }
        Point p = MouseInput.getLocation();
        if (p.x <= 0) cameraX -= 50;
        else if (p.x >= Game.WIDTH - 1) cameraX += 50;
        if (p.y <= 0) cameraY -= 50;
        else if (p.y >= Game.HEIGHT - 1) cameraY += 50;
    }

    public static Point getOnMapLocation(Point p)
    {
        return new Point(p.x + cameraX, p.y + cameraY);
    }

    public static Point getOnScreenLocation(Point onMapLocation)
    {
        return new Point(onMapLocation.x - cameraX, onMapLocation.y - cameraY);
    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.translate(-cameraX, -cameraY);
        ///////////////////////////////////////////////Main Main Main
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        for (int i = 0; i < sectors.length; ++i)
        {
            if (teamOfSector[i] == Team.Red) g2d.setColor(Color.magenta);
            else if (teamOfSector[i] == Team.Blue) g2d.setColor(Color.cyan);
            else g2d.setColor(Color.ORANGE);
            g2d.fill(sectors[i]);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        handler.render(g2d);


        //////////////////////////////////////////
        g2d.translate(cameraX, cameraY);
        //////////////////////////////////////////////HUD HUD HUD


        if (isMouseSelected)
        {
            g2d.setColor(Color.green);
            Point mp = MouseInput.getLocation();
            int width = mp.x - startX;
            int height = mp.y - startY;

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
            g2d.fillRect(startX, startY, width, height);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            g2d.setColor(Color.white);
            g2d.drawRect(startX, startY, width, height);
        }


    }

}
