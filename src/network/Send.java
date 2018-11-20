package network;

import core.GameObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Send
{
    protected int sendPort, receivePort;
    protected InetAddress receiveAddress;
    private DatagramSocket socket;

    public Send(InetAddress receiveAddress)
    {
        if (this instanceof TcpSend) return;
        this.sendPort = 3537;
        this.receivePort = 3539;
        this.receiveAddress = receiveAddress;
        try
        {
            this.socket = new DatagramSocket(sendPort);
            sendInt(1111111);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void sendInt(int number)
    {
        byte[] data = new byte[4];
        for (int i = 0; i < 4; ++i)
        {
            int shift = i << 3;
            data[3 - i] = (byte) ((number & (0xff << shift)) >>> shift);
        }
        DatagramPacket packet = new DatagramPacket(data, 4, receiveAddress, receivePort);
        try
        {
            socket.send(packet);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void sendObject(GameObject object)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
            byte[] Buf = baos.toByteArray();
            int number = Buf.length;
            byte[] data = new byte[4];
            for (int i = 0; i < 4; ++i)
            {
                int shift = i << 3;
                data[3 - i] = (byte) ((number & (0xff << shift)) >>> shift);
            }
            DatagramPacket packet = new DatagramPacket(data, 4, receiveAddress, receivePort);
            socket.send(packet);
            packet = new DatagramPacket(Buf, Buf.length, receiveAddress, receivePort);
            //System.out.println(Buf.length + " is length");
            socket.send(packet);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
