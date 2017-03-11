package com.java.gavinandre.http;

import com.java.gavinandre.tool.IoUtils;
import com.java.gavinandre.tool.SocketTool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gavinandre on 17-3-11.
 * 简单的Http服务器实现
 */
public class SimpleHttpServer extends Thread {

    public static void main(String[] args) {
        new SimpleHttpServer().start();
    }
    // 服务端Socket
    ServerSocket mSocket = null;

    public SimpleHttpServer() {
        try {
            mSocket = new ServerSocket(SocketTool.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mSocket == null) {
            throw new RuntimeException("服务器Socket初始化失败");
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                // 无限循环,进入等待连接状态
                System.out.println("等待连接中");
                // 一旦接收到连接请求,构建一个线程来处理
                new DeliverThread(mSocket.accept()).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class DeliverThread extends Thread {
        Socket mClientSocket;
        // 输入流
        BufferedReader mInputStream;
        // 输出流
        PrintStream mOutputStream;
        // 请求方法,GET、POST等
        String httpMethod;
        // 子路径
        String subPath;
        // 分隔符
        String boundary;
        // 请求参数
        Map<String, String> mParams = new HashMap<String, String>();
        // 请求headers
        Map<String, String> mHeaders = new HashMap<String, String>();
        // 是否已经解析完Header
        boolean isParseHeader = false;

        public DeliverThread(Socket socket) {
            mClientSocket = socket;
        }

        @Override
        public void run() {
            try {
                // 获取输入流
                mInputStream = new BufferedReader(new InputStreamReader(
                        mClientSocket.getInputStream()));
                // 获取输出流
                mOutputStream = new PrintStream(mClientSocket.getOutputStream());
                // 解析请求
                parseRequest();
                // 返回Response
                handleResponse();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 关闭流和Socket
                IoUtils.closeQuickly(mInputStream);
                IoUtils.closeQuickly(mOutputStream);
                IoUtils.closeSocket(mClientSocket);
            }
        }

        private void parseRequest() {
            String line;
            try {
                int lineNum = 0;
                // 从输入流读取客户端发送过来的数据
                while ((line = mInputStream.readLine()) != null) {

                    //第一行为请求行
                    if (lineNum == 0) {
                        parseRequestLine(line);
                    }
                    // 判断是否是数据的结束行
                    if (isEnd(line)) {
                        break;
                    }
                    // 解析header参数
                    if (lineNum != 0 && !isParseHeader) {
                        parseHeaders(line);
                    }
                    // 解析请求参数
                    if (isParseHeader) {
                        parseRequestParams(line);
                    }
                    lineNum++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 是否是结束行
        private boolean isEnd(String line) {
            return line.equals("--" + boundary + "--");
        }

        // 解析请求行
        private void parseRequestLine(String lineOne) {
            String[] tempStrings = lineOne.split(" ");
            httpMethod = tempStrings[0];
            subPath = tempStrings[1];
            System.out.println("请求行,请求方式 : " + tempStrings[0] + ", 子路径 : " + tempStrings[1]
                    + ",HTTP版本 : " + tempStrings[2]);
            System.out.println();
        }

        // 解析header,参数为每个header的字符串
        private void parseHeaders(String headerLine) {
            // header区域的结束符
            if (headerLine.equals("")) {
                isParseHeader = true;
                System.out.println("-----------> header解析完成\n");
                return;
            } else if (headerLine.contains("boundary")) {
                boundary = parseSecondField(headerLine);
                System.out.println("分隔符 : " + boundary);
            } else {
                // 解析普通header参数
                parseHeaderParam(headerLine);
            }
        }

        // 解析单个header
        private void parseHeaderParam(String headerLine) {
            String[] keyvalue = headerLine.split(":");
            mHeaders.put(keyvalue[0].trim(), keyvalue[1].trim());
            System.out.println("header参数名 : " + keyvalue[0].trim() + ", 参数值 : "
                    + keyvalue[1].trim());
        }

        // 解析header中的第二个参数
        private String parseSecondField(String line) {
            String[] headerArray = line.split(";");
            parseHeaderParam(headerArray[0]);
            if (headerArray.length > 1) {
                return headerArray[1].split("=")[1];
            }
            return "";
        }

        // 解析请求参数
        private void parseRequestParams(String paramLine) throws IOException {
            if (paramLine.equals("--" + boundary)) {
                // 读取Content-Disposition行
                String ContentDisposition = mInputStream.readLine();
                // 解析参数名
                String paramName = parseSecondField(ContentDisposition);
                // 读取参数header与参数值之间的空行
                mInputStream.readLine();
                // 读取参数值
                String paramValue = mInputStream.readLine();
                mParams.put(paramName, paramValue);
                System.out.println("参数名 : " + paramName + ", 参数值 : " + paramValue);
            }
        }

        // 返回结果
        private void handleResponse() {
            //模拟处理耗时
            sleep();
            //向输出流写数据
            mOutputStream.println("HTTP/1.1 200 OK");
            mOutputStream.println("Content-Type: application/json");
            mOutputStream.println();
            mOutputStream.println("{\"stCode\":\"success\"}");
        }

        private void sleep() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}