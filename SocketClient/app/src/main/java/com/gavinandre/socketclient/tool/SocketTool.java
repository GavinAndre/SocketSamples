package com.gavinandre.socketclient.tool;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by gavinandre on 17-2-23.
 */

public class SocketTool {

    public static final String HOST = "192.168.123.69";
    public static final int PORT = 10086;

    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getIP() {
        String ip = null;
        try {
            Enumeration en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) en.nextElement();
                if (ni.isVirtual() || "virbr0".equals(ni.getName())) {
                    continue;
                }
                Enumeration<InetAddress> ee = ni.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress ia = ee.nextElement();
                    if (!ia.isLoopbackAddress() &&
                            !(ia instanceof Inet6Address)
                            ) {
                        ip = ia.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }
}
