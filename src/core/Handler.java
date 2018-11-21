package core;

import main.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

public class Handler
{
    private java.util.TreeSet<GameObject> objects = new java.util.TreeSet<>();
    private Queue<GameObject> addList = new LinkedList<>();
    private Queue<GameObject> removeList = new LinkedList<>();

    public void tick()
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
        while (!addList.isEmpty()) objects.add(addList.poll());
        while (!removeList.isEmpty()) objects.remove(removeList.poll());
    }

    public void render(Graphics2D g2d)
    {
        objects.forEach(i -> i.render(g2d));
    }

    public void addObject(GameObject object)
    {
        this.addList.add(object);
    }

    public void removeObject(GameObject object)
    {
        this.removeList.add(object);
    }

    public GameObject findObjectById(ID id)
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
    public TreeSet<GameObject> getMyTeamObject(Team team)
    {
        TreeSet<GameObject> ret = new TreeSet<>();
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
            else ret.add(obj);
        }
        return ret;
    }
    public ArrayList<GameObject> findObjectsById(ID id)
    {
        ArrayList<GameObject> res = new ArrayList<>();
        for (GameObject tempObject : objects)
            if (tempObject.getId() == id) res.add(tempObject);
        return res;
    }

    public void removeObjectsById(ID id)
    {
        objects.forEach(i ->
        {
            if (i.getId() == id) removeObject(i);
        });
    }

    public void clear()
    {
        removeList.addAll(objects);
        removeList.addAll(addList);
    }

    public int size()
    {
        return objects.size();
    }

    public ArrayList<GameObject> Collide(GameObject object)
    {
        ArrayList<GameObject> result = new ArrayList<>();
        for (GameObject tempObject : objects)
            if (tempObject.getBounds().intersects(object.getBounds()) && tempObject != object && !tempObject.isIgnoreCollision())
            {
                result.add(tempObject);
            }
        return result;
    }
    public ArrayList<GameObject> Collide(Rectangle bound)
    {
        ArrayList<GameObject> result = new ArrayList<>();
        for (GameObject tempObject : objects)
        {
            if (tempObject.getBounds().intersects(bound) && !tempObject.isIgnoreCollision()) result.add(tempObject);
        }
        return result;
    }
    public java.util.TreeSet<GameObject> getObjects()
    {
        return objects;
    }
}
