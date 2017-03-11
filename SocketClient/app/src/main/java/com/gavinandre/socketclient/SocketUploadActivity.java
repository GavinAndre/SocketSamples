package com.gavinandre.socketclient;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gavinandre.socketclient.tool.SocketTool;
import com.gavinandre.socketclient.tool.StreamTool;
import com.gavinandre.socketclient.tool.UploadHelper;

import java.io.File;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

/**
 * Created by gavinandre on 17-2-22.
 */

public class SocketUploadActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private Button btnUpload;
    private Button btnStop;
    private ProgressBar progressBar;
    private TextView tvResult;

    private UploadHelper uploadHelper;
    private boolean flag = true;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressBar.setProgress(msg.getData().getInt("length"));
            float num = (float) progressBar.getProgress() / (float) progressBar.getMax();
            int result = (int) (num * 100);
            tvResult.setText(result + "%");
            if (progressBar.getProgress() == progressBar.getMax()) {
                Toast.makeText(SocketUploadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket_upload_activity);
        initView();
        uploadHelper = new UploadHelper(this);
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.filename);
        btnUpload = (Button) findViewById(R.id.upload_button);
        btnStop = (Button) findViewById(R.id.stop_button);
        progressBar = (ProgressBar) findViewById(R.id.upload_bar);
        tvResult = (TextView) findViewById(R.id.result);
        btnUpload.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_button:
                String filename = editText.getText().toString();
                flag = true;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    File file = new File(Environment.getExternalStorageDirectory(), filename);
                    if (file.exists()) {
                        progressBar.setMax((int) file.length());
                        uploadFile(file);
                    } else {
                        Toast.makeText(SocketUploadActivity.this, "文件并不存在~", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SocketUploadActivity.this, "SD卡不存在或者不可用", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.stop_button:
                flag = false;
                break;
        }
    }

    /**
     * 上传文件
     *
     * @param uploadFile
     */
    private void uploadFile(final File uploadFile) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String sourceid = uploadHelper.getBindId(uploadFile);
                    Socket socket = new Socket(SocketTool.HOST, SocketTool.PORT);
                    OutputStream outStream = socket.getOutputStream();
                    String head = "Content-Length=" + uploadFile.length() + ";filename=" + uploadFile.getName()
                            + ";sourceid=" + (sourceid != null ? sourceid : "") + "\r\n";
                    outStream.write(head.getBytes());

                    PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
                    String response = StreamTool.readLine(inStream);
                    String[] items = response.split(";");
                    String responseSourceid = items[0].substring(items[0].indexOf("=") + 1);
                    String position = items[1].substring(items[1].indexOf("=") + 1);
                    if (sourceid == null) {//代表原来没有上传过此文件，往数据库添加一条绑定记录
                        uploadHelper.save(responseSourceid, uploadFile);
                    }
                    RandomAccessFile fileOutStream = new RandomAccessFile(uploadFile, "r");
                    fileOutStream.seek(Integer.valueOf(position));
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    int length = Integer.valueOf(position);
                    while (flag && (len = fileOutStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                        length += len;//累加已经上传的数据长度
                        Message msg = new Message();
                        msg.getData().putInt("length", length);
                        handler.sendMessage(msg);
                    }
                    if (length == uploadFile.length()) uploadHelper.delete(uploadFile);
                    fileOutStream.close();
                    outStream.close();
                    inStream.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}