package com.java.gavinandre.test;

import com.java.gavinandre.tool.SocketTool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by gavinandre on 17-2-21.
 * Socket 建立连接
 */
public class SocketTest {

    public SocketTest() throws IOException {

        //1.创建一个服务端Socket，即ServerSocket，指定绑定的端口，并监听此端口
        ServerSocket serverSocket = new ServerSocket(SocketTool.PORT);
        String ip = SocketTool.getIP();
        System.out.println("1.服务端已就绪，等待客户端接入，服务端ip地址: " + ip);
        //2.调用accept()等待客户端连接,建立连接后，accept()返回一个最近创建的Socket对象，
        //该Socket对象绑定了客户程序的IP地址或端口号
        Socket socket = serverSocket.accept();
        //3.连接后获取输入流，读取客户端信息
        InputStream is= socket.getInputStream();     //获取输入流
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader br  = new BufferedReader(isr);
        String info;
        while ((info = br.readLine()) != null) {//循环读取客户端的信息
            System.out.println("3.服务端接收客户端信息: " + info);
        }
        socket.shutdownInput();//关闭输入流
        //4.服务端收到客户端消息后返回一条消息
        OutputStream os = socket.getOutputStream();//字节输出流
        PrintWriter pw = new PrintWriter(os);//将输出流包装为打印流
        pw.write("服务端 " + ip + " 返回消息！");
        pw.flush();
        System.out.println("4.服务端返回信息给客户端: " + "服务端 " + ip + " 返回消息！");
        //socket.shutdownOutput();//关闭输出流
        socket.close();
    }

}
