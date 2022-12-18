package com.example.socket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        String user_data = intent.getStringExtra("user_data");
        TextView solde = (TextView) findViewById(R.id.solde);

        int posEm = user_data.indexOf("solde\":");
        posEm += String.valueOf("solde\":\"").length();
        int posPass = user_data.indexOf("\"action");
        posPass -= 2;
        String sold = user_data.substring(posEm, posPass);
        solde.setText(String.valueOf(sold));


    }


}