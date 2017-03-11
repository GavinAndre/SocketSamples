package com.gavinandre.socketclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView1 = (TextView) findViewById(R.id.socket_sample_1);
        TextView textView2 = (TextView) findViewById(R.id.socket_sample_2);
        TextView textView3 = (TextView) findViewById(R.id.socket_sample_3);
        TextView textView4 = (TextView) findViewById(R.id.socket_sample_4);
        TextView textView5 = (TextView) findViewById(R.id.socket_sample_5);

        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
        textView4.setOnClickListener(this);
        textView5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.socket_sample_1:
                intent = new Intent(this, SocketTcpActivity.class);
                startActivity(intent);
                break;
            case R.id.socket_sample_2:
                intent = new Intent(this, SocketIMActivity.class);
                startActivity(intent);
                break;
            case R.id.socket_sample_3:
                intent = new Intent(this, SocketUploadActivity.class);
                startActivity(intent);
                break;
            case R.id.socket_sample_4:
                intent = new Intent(this, SocketUdpActivity.class);
                startActivity(intent);
                break;
            case R.id.socket_sample_5:
                intent = new Intent(this, SocketHttpActivity.class);
                startActivity(intent);
                break;
        }
    }
}
