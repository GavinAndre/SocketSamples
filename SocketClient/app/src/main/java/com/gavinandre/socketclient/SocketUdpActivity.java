package com.gavinandre.socketclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gavinandre.socketclient.tool.SocketTool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by gavinandre on 17-2-27.
 */

public class SocketUdpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SocketUdpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket_udp_activity);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new Thread() {
            @Override
            public void run() {
                try {
                    sendUDPSocket();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void sendUDPSocket() throws IOException {

        /*
         * 向服务器端发送数据
         */

        //定义服务器的地址、端口号
        InetAddress address = InetAddress.getByName(SocketTool.HOST);
        int port = SocketTool.PORT;
        //创建要包含到数据报中的信息
        byte[] sendData = "Hello Socket From UDP!".getBytes();
        //创建数据报，包含要发送的数据信息
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
        //创建DatagramSocket对象
        DatagramSocket socket = new DatagramSocket();
        //向服务器端发送数据报
        socket.send(sendPacket);

        /*
         * 接收服务器端响应的数据
         */

        //创建一个byte类型的数组，用于存放接收到得数据
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        //接收服务端响应的数据，此方法在接收到数据报之前会一直阻塞
        socket.receive(receivePacket);
        //读取服务端响应的数据
        String result = new String(receiveData, 0, receivePacket.getLength());
        Log.i(TAG, "3.客户端收到服务端响应的消息：" + result);
        //关闭Socket
        socket.close();
    }
}
