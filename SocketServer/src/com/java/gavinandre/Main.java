package com.java.gavinandre;

import com.java.gavinandre.http.SimpleHttpServer;

/**
 * Created by gavinandre on 17-2-21.
 * Socket Samples
 */
public class Main {

    public static void main(String[] args) throws Exception {

        //new SocketTcpServer();
        //new SocketIMServer();
        //new SocketUploadServer();
        //new SocketUdpServer();
        new SimpleHttpServer().start();
    }
}
