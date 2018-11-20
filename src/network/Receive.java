package network;


import core.GameObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Receive implements Runnable
{
    public int getPort()
    {
        return port;
    }

    protected int port;
    private DatagramSocket socket;
    protected Queue<GameObject> queue = new ConcurrentLinkedDeque<>();

    public Receive()
    {
        if (this instanceof TcpReceive) return;
        this.port = 3539;//3539
        try
        {
            this.socket = new DatagramSocket(port);
            System.out.println(receiveInt());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean hasData()
    {
        return !queue.isEmpty();
    }
    public int receiveInt()
    {
        byte[] data = new byte[4];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try
        {
            socket.receive(packet);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        int len = 0;
        for (int i = 0; i < 4; ++i)
        {
            len |= (data[3 - i] & 0xff) << (i << 3);
        }
        return len;
    }
    public GameObject nextObject()
    {
        return queue.poll();
    }
    public void tick()
    {
        try
        {
            byte[] data = new byte[4];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            socket.receive(packet);
            int len = 0;
            for (int i = 0; i < 4; ++i)
            {
                len |= (data[3 - i] & 0xff) << (i << 3);
            }
            byte[] buffer = new byte[len];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = new ObjectInputStream(bais);
            GameObject go = (GameObject) ois.readObject();
            queue.offer(go);
            ois.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        while(true)
        {
            tick();
        }
    }
}
