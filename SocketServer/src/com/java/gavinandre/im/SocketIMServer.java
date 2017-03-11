package com.java.gavinandre.im;

import com.java.gavinandre.tool.SocketTool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gavinandre on 17-2-22.
 * socket 即时通讯
 */
public class SocketIMServer {

    //定义相关的参数,端口,存储Socket连接的集合,ServerSocket对象
    //以及线程池
    private static final int PORT = 10086;
    private List<Socket> mList = new ArrayList<Socket>();
    private ServerSocket server = null;
    private ExecutorService myExecutorService = null;

    public SocketIMServer() {
        try {
            server = new ServerSocket(PORT);
            //创建线程池
            myExecutorService = Executors.newCachedThreadPool();
            System.out.println("服务端 " + SocketTool.getIP() + " 运行中...\n");
            Socket client = null;
            while (true) {
                client = server.accept();
                mList.add(client);
                myExecutorService.execute(new Service(client));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Service implements Runnable {
        private Socket socket;
        private BufferedReader in = null;
        private String msg = "";

        public Service(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                msg = "用户:" + this.socket.getInetAddress() + "~加入了聊天室"
                        + "当前在线人数:" + mList.size();
                this.sendMsg();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void run() {
            try {
                while (true) {
                    if ((msg = in.readLine()) != null) {
                        if (msg.equals("bye")) {
                            System.out.println("~~~~~~~~~~~~~");
                            mList.remove(socket);
                            in.close();
                            msg = "用户:" + socket.getInetAddress()
                                    + "退出:" + "当前在线人数:" + mList.size();
                            socket.close();
                            this.sendMsg();
                            break;
                        } else {
                            msg = socket.getInetAddress() + "   说: " + msg;
                            this.sendMsg();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //为连接上服务端的每个客户端发送信息
        public void sendMsg() {
            System.out.println(msg);
            int num = mList.size();
            for (int index = 0; index < num; index++) {
                Socket mSocket = mList.get(index);
                PrintWriter pout = null;
                try {
                    pout = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(mSocket.getOutputStream(), "UTF-8")), true);
                    pout.println(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
