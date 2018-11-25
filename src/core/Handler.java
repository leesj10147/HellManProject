package core;

import main.*;
import network.CheckGameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class Handler
{
    private ConcurrentSkipListSet<GameObject> objects = new ConcurrentSkipListSet<GameObject>();
    private Queue<GameObject> addList = new ConcurrentLinkedQueue<>();
    private Queue<GameObject> removeList = new ConcurrentLinkedQueue<>();

    public Handler()
    {

    }
    public synchronized void tick()
    {
        objects.forEach(GameObject::tick);
        /*String str;
        if (BattleScene.getTeam() == Team.Red)
        {
            str = "Red";
        } else
            str = "Blue";
        for (GameObject obj : objects)
            if (obj.distinguish.startsWith(str))
            {
                obj.tick();
            }*/
        for (GameObject object : objects)
        {
            if (objects.contains(object) == false)
                System.out.println("what?");
        }
        while (!addList.isEmpty()) objects.add(addList.poll());
        while (!removeList.isEmpty())
        {
            if (GameManager.scene instanceof BattleScene)
            {
                ((BattleScene) GameManager.scene).sendObject(new CheckGameObject(this, removeList.peek().hashCode()));
            }
            objects.remove(removeList.poll());

        }
    }

    public synchronized void render(Graphics2D g2d)
    {
        objects.forEach(i -> i.render(g2d));
    }

    public synchronized void addObject(GameObject object)
    {
        this.addList.add(object);
    }

    public synchronized void removeObject(GameObject object)
    {
        this.removeList.add(object);
    }

    public synchronized GameObject findObjectById(ID id)
    {
        for (GameObject tempObject : objects)
        {
            if (tempObject.getId() == id)
            {
                return tempObject;
            }
        }
        return null;
    }
    public synchronized ConcurrentSkipListSet<GameObject> getMyTeamObject(Team team)
    {
        ConcurrentSkipListSet<GameObject> ret = new ConcurrentSkipListSet<>();
        for (GameObject obj : objects)
        {
            if (obj instanceof BasicInfantry)
            {
                if (((BasicInfantry) obj).team == team) ret.add(obj);
            }
            else if (obj instanceof Bullet)
            {
                if (((Bullet) obj).team == team) ret.add(obj);
            }
            else if (obj instanceof Nexus)
            {
                if (((Nexus) obj).team == team) ret.add(obj);
            }
        }
        return ret;
    }
    public synchronized ConcurrentLinkedQueue<GameObject> findObjectsById(ID id)
    {
        ConcurrentLinkedQueue<GameObject> res = new ConcurrentLinkedQueue<>();
        for (GameObject tempObject : objects)
            if (tempObject.getId() == id) res.add(tempObject);
        return res;
    }

    public synchronized void removeObjectsById(ID id)
    {
        objects.forEach(i ->
        {
            if (i.getId() == id) removeObject(i);
        });
    }

    public synchronized void clear()
    {
        removeList.addAll(objects);
        removeList.addAll(addList);
    }

    public synchronized int size()
    {
        return objects.size();
    }

    public synchronized ConcurrentLinkedQueue<GameObject> Collide(GameObject object)
    {
        ConcurrentLinkedQueue<GameObject> result = new ConcurrentLinkedQueue<>();
        for (GameObject tempObject : objects)
            if (tempObject.getBounds().intersects(object.getBounds()) && tempObject != object && !tempObject.isIgnoreCollision())
            {
                result.add(tempObject);
            }
        return result;
    }
    public synchronized ConcurrentLinkedQueue<GameObject> Collide(Rectangle bound)
    {
        ConcurrentLinkedQueue<GameObject> result = new ConcurrentLinkedQueue<>();
        for (GameObject tempObject : objects)
        {
            if (tempObject.getBounds().intersects(bound) && !tempObject.isIgnoreCollision()) result.add(tempObject);
        }
        return result;
    }
    public synchronized ConcurrentSkipListSet<GameObject> getObjects()
    {
        return objects;
    }
}
