package network;

import core.GameObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;

public class TcpSend extends Send
{
    private ObjectOutputStream oos;
    public TcpSend(InetAddress receiveAddress, OutputStream ops)
    {
        super(receiveAddress);
        this.sendPort = 3537;
        this.receivePort = 3539;
        try
        {
            oos = new ObjectOutputStream(ops);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void sendInt(int number)
    {
        if (oos == null) return;
        try
        {
            oos.writeInt(number);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void sendObject(GameObject object)
    {
        try
        {
            oos.writeObject(object);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
