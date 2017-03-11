package com.java.gavinandre.upload;

import com.java.gavinandre.tool.SocketTool;

/**
 * Created by gavinandre on 17-2-22.
 * Socket 大文件断点续传
 */
public class SocketUploadServer {

    public SocketUploadServer() throws Exception {

        System.out.println("服务端 " + SocketTool.getIP() + " 运行中...\n");

        FileServer s = new FileServer(SocketTool.PORT);
        s.start();
    }
}
