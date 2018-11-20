package network;

import core.GameObject;
import main.BattleScene;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class TcpReceive extends Receive
{
    private ObjectInputStream ois;
    public TcpReceive(InputStream is)
    {
        this.port = 3539;
        try
        {
            ois = new ObjectInputStream(is);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public boolean hasData()
    {
        return !queue.isEmpty();
    }

    @Override
    public int receiveInt()
    {
        if (ois == null) return -1;
        try
        {
            return ois.readInt();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public GameObject nextObject()
    {
        return queue.poll();
    }

    @Override
    public void tick()
    {
        try
        {
            GameObject obj = (GameObject)ois.readUnshared();
            queue.offer(obj);
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
