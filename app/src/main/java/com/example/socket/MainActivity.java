package com.example.socket;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    private EditText edtxt, pass;
    private Button button;
    MyThread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setThreadPolicy(policy);
        edtxt = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.pass);
        button = (Button)findViewById(R.id.button);

        mThread = new MyThread();
        new Thread(mThread).start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = edtxt.getText().toString();
                String p = pass.getText().toString();
                JSONObject user_data = new JSONObject();
                try {
                    user_data.put("email", txt);
                    user_data.put("password", p);
                    user_data.put("solde", "100");
                    user_data.put("action", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mThread.senMsg(String.valueOf(user_data));
            }
        });


    }


    private class MyThread implements Runnable{
        private String HOST = "192.168.0.13";
        private int PORT = 4444;
        private volatile String msg = "";
        Socket socket;
        DataOutputStream dos;
        DataInputStream dis;

        @Override
        public void run() {
            try {
                socket = new Socket(HOST, PORT);
                dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(msg);
                dos.flush();
                dis = new DataInputStream(socket.getInputStream());
                String res = dis.readUTF();

                dis.close();
                dos.close();
                socket.close();
                edtxt.setText("");
                pass.setText("");
                Intent in = new Intent(MainActivity.this, HomeActivity.class);
                in.putExtra("user_data", res);
                startActivity(in);

                finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void senMsg(String msg){
            this.msg = msg;
            run();
        }
    }
}
