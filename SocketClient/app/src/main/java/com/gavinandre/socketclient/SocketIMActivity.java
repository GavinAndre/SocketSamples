package com.gavinandre.socketclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gavinandre.socketclient.tool.SocketTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketIMActivity extends AppCompatActivity implements Runnable, View.OnClickListener {
    private static final String TAG = SocketIMActivity.class.getSimpleName();
    //定义相关变量,完成初始化
    private TextView txtshow;
    private EditText editsend;
    private Button btnsend;

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String content = "";
    private StringBuilder sb = null;

    //定义一个handler对象,用来刷新界面
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                sb.append(content);
                txtshow.setText(sb.toString());
                editsend.setText("");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket_im_activity);
        sb = new StringBuilder();
        txtshow = (TextView) findViewById(R.id.txtshow);
        editsend = (EditText) findViewById(R.id.editsend);
        btnsend = (Button) findViewById(R.id.btnsend);
        btnsend.setOnClickListener(this);

        new Thread(SocketIMActivity.this).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsend:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String msg = editsend.getText().toString();
                        if (socket != null && socket.isConnected()) {
                            if (!socket.isOutputShutdown()) {
                                out.println(msg);
                            }
                        }
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket != null && socket.isConnected()) {
                    if (!socket.isOutputShutdown()) {
                        out.println("bye");
                    }
                }
            }
        }).start();
    }

    //重写run方法,在该方法中输入流的读取
    @Override
    public void run() {
        try {
            socket = new Socket(SocketTool.HOST, SocketTool.PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())), true);
            while (true) {
                if (socket.isConnected()) {
                    if (!socket.isInputShutdown()) {
                        if ((content = in.readLine()) != null) {
                            content += "\n";
                            Log.i(TAG, "content: " + content);
                            handler.sendEmptyMessage(0x123);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
