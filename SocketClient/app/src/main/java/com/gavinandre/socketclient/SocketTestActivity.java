package com.gavinandre.socketclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gavinandre.socketclient.tool.SocketTool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketTestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SocketTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket_test_activity);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new Thread() {
            @Override
            public void run() {
                try {
                    acceptServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void acceptServer() throws IOException {

        //1.创建客户端Socket，指定服务器地址和端口
        //Log.i(TAG, "客户端：before socket");
        Socket socket = new Socket(SocketTool.HOST, SocketTool.PORT);
        socket.setSoTimeout(5000);
        //Log.i(TAG, "客户端：after socket");
        //2.获取输出流，向服务器端发送信息
        OutputStream os = socket.getOutputStream();//字节输出流
        PrintWriter pw = new PrintWriter(os);//将输出流包装为打印流
        //获取客户端的IP地址
        String ip = SocketTool.getIP();
        //3.客户端发送消息
        pw.write("客户端 " + ip + " 接入服务器！");
        pw.flush();
        Log.i(TAG, "2.客户端发送信息: 客户端 " + ip + " 接入服务器！");
        socket.shutdownOutput();//关闭输出流
        //4.客户端等待服务端返回的消息
        InputStream is = socket.getInputStream();     //获取输入流;
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String info;
        while ((info = br.readLine()) != null) {//循环读取客户端的信息
            Log.i(TAG, "5.客户端接收服务端信息: " + info);
        }
        //socket.shutdownInput();//关闭输入流
        socket.close();
    }
}
