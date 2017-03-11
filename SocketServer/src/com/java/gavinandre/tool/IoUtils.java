package com.java.gavinandre.tool;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by gavinandre on 17-3-11.
 */
public class IoUtils {
    public static void closeQuickly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeSocket(Socket closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}