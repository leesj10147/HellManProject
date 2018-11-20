package main;

import core.*;
import network.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.TreeSet;
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
    private Rectangle[] sector;
    private Team[] teamOfSector;

    public BattleScene(Handler handler)
    {
        super(handler);
        setInfo();
        Nexus test6;
        if (team == Team.Red)
            test6 = new Nexus(team, 1000 , 1000, GameManager.loadImage("cannon.png"), ID.Nexus, handler, RenderOrder.ForeGround.order);
        else
            test6 = new Nexus(team, 0  , 0, GameManager.loadImage("cannon.png"), ID.Nexus, handler, RenderOrder.ForeGround.order);
        handler.addObject(test6);
        sector = new Rectangle[9];
        teamOfSector = new Team[9];
        for (int i = 0; i < 9; ++i)
        {
            int r = i / 3, c = i % 3;
            teamOfSector[i] = Team.Neutrality;
            sector[i] = new Rectangle(1400 * r, 990 * c, 1400, 990);
        }
        teamOfSector[0] = Team.Red;
        teamOfSector[8] = Team.Blue;
        System.out.println("end constructor");
        GameManager.scene = this;
        GameManager.endMake = true;
    }

    public Team getTeamOfSector(Point p)
    {
        for (int i = 0; i < sector.length; ++i)
            if (sector[i].contains(p)) return teamOfSector[i];
        return Team.Neutrality;
    }

    private void setInfo()
    {
        class MyDialogPopup extends JDialog
        {
            AtomicBoolean end = new AtomicBoolean(false);

            MyDialogPopup()
            {
                setBounds(100, 100, 500, 175);
                setTitle("Type Your Info");
                setLocationRelativeTo(null);
                Container con = getContentPane();
                con.setLayout(new GridLayout(5, 2, 10, 10));

                JLabel label1 = new JLabel("Team(Red: Server, Blue: Client)", JLabel.TRAILING);
                con.add(label1);
                JTextField textField1 = new JTextField(30);
                label1.setLabelFor(textField1);
                con.add(textField1);


                JLabel label4 = new JLabel("otherAddress", JLabel.TRAILING);
                con.add(label4);
                JTextField textField4 = new JTextField(30);
                label1.setLabelFor(textField4);
                con.add(textField4);

                JLabel label5 = new JLabel("OK : ", JLabel.TRAILING);
                con.add(label5);
                JButton button = new JButton("OK");
                label1.setLabelFor(button);
                con.add(button);
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
                        Socket s =null;
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

    private void updateReceive()
    {
        while (receive.hasData())
        {
            GameObject obj = receive.nextObject();

            if (obj instanceof CheckGameObject)
            {

                CheckGameObject chk = (CheckGameObject) obj;
 /*               System.out.print(chk.a);
                if (chk.team == Team.Red) System.out.print("Red");
                else if (chk.team == Team.Blue) System.out.print("Blue");

                if (getTeam() == Team.Red) System.out.println("  Red");
                else if (getTeam() == Team.Blue) System.out.println("  Blue");*/
                TreeSet<GameObject> chkTeam = handler.getMyTeamObject(chk.team);
                for (GameObject object : chkTeam)
                {
                    String toChk = object.distinguish.substring(chk.team == Team.Red ? 3 : 4);
                    if (chk.list.contains(toChk)) continue;
                    handler.getObjects().remove(object);
                }
                continue;
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
                    infantry.originalImage = GameManager.loadImage("cannon.png");
                    infantry.image = GameManager.loadImage("cannon.png");
                } else
                {
                    infantry.originalImage = GameManager.loadImage("BasicInfantry.png");
                    infantry.image = GameManager.loadImage("BasicInfantry.png");
                }
            }
            obj.handler = handler;
            handler.getObjects().add(obj);

        }
    }

    private void updateSend()
    {
        TreeSet<GameObject> tosend = handler.getMyTeamObject(team);
        for (GameObject object : tosend)
        {
            send.sendObject(object);

        }
        send.sendObject(new CheckGameObject(handler, team));
    }

    public static boolean isOurTeam(GameObject object)
    {
        if (object instanceof BasicInfantry)
        {
            if (((BasicInfantry) object).team == team) return true;
            else return false;
        }
        if (object instanceof Bullet)
        {
            if (((Bullet) object).team == team) return true;
            else return false;
        }
        if (object instanceof Nexus)
        {
            if (((Nexus) object).team == team) return true;
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
                    if (object.isMustUpdateMouseClickedLocation() && isOurTeam(object))
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
                java.util.TreeSet<GameObject> set = handler.getObjects();
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
        else if (p.x >= Game.WIDTH) cameraX += 50;
        if (p.y <= 0) cameraY -= 50;
        else if (p.y >= Game.HEIGHT) cameraY += 50;
    }

    public static Point getOnMapLocation(Point p)
    {
        return new Point(p.x + cameraX, p.y + cameraY);
    }

    @Override
    public void render(Graphics2D g2d)
    {
        g2d.translate(-cameraX, -cameraY);
        ///////////////////////////////////////////////Main Main Main
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        for (int i = 0; i < sector.length; ++i)
        {
            if (teamOfSector[i] == Team.Red) g2d.setColor(Color.magenta);
            else if (teamOfSector[i] == Team.Blue) g2d.setColor(Color.cyan);
            else g2d.setColor(Color.ORANGE);
            g2d.fill(sector[i]);
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
