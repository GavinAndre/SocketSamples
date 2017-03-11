package com.gavinandre.socketclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.gavinandre.socketclient.tool.HttpPost;
import com.gavinandre.socketclient.tool.SocketTool;

public class SocketHttpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SocketHttpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket_http_activity);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new Thread() {
            @Override
            public void run() {
                try {
                    HttpPost httpPost = new HttpPost(SocketTool.HOST);
                    // 设置两个参数
                    httpPost.addParam("username", "鲁提辖");
                    httpPost.addParam("pwd", "lutixia");
                    // 执行请求
                    httpPost.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
