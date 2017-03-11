package com.java.gavinandre.udp;

/**
 * Created by gavinandre on 17-3-11.
 */

import com.java.gavinandre.tool.SocketTool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by gavinandre on 17-2-27.
 */
public class SocketUdpServer {

    public SocketUdpServer() throws IOException {

        /*
         * 接收客户端发送的数据
         */

        //创建一个DatagramSocket对象，并指定监听端口
        DatagramSocket socket = new DatagramSocket(SocketTool.PORT);
        //创建一个byte类型的数组，用于存放接收到得数据
        byte[] data = new byte[1024];
        //创建数据报，用于接收客户端发送的数据
        DatagramPacket packet = new DatagramPacket(data, data.length);
        System.out.println("1.服务端 " + SocketTool.getIP() + " 已经启动，等待客户端发送数据");
        //接收客户端发送的数据，此方法在接收到数据报之前会一直阻塞
        socket.receive(packet);
        //读取客户端发送的数据
        //使用三个参数的String方法。参数一：数据包 参数二：起始位置 参数三：数据包长
        String result = new String(data, 0, packet.getLength());
        System.out.println("2.服务端收到客户端发送的消息：" + result);

        /*
         * 向客户端响应数据
         */

        //从数据报中获取客户端的地址、端口号
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        //创建要包含到数据报中的信息
        byte[] data2 = "Hello World!".getBytes();
        //创建数据报，包含响应的数据信息
        DatagramPacket packet2 = new DatagramPacket(data2, data2.length, address, port);
        //响应客户端
        socket.send(packet2);
        //关闭Socket
        socket.close();
    }
}
