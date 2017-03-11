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

        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.socket_sample_1:
                Intent intent1 = new Intent(this, SocketTestActivity.class);
                startActivity(intent1);
                break;
            case R.id.socket_sample_2:
                Intent intent2 = new Intent(this, SocketIMActivity.class);
                startActivity(intent2);
                break;
            case R.id.socket_sample_3:
                Intent intent3 = new Intent(this, SocketUploadActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
