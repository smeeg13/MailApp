package com.example.mailapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity {
TextView txtUsrname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        txtUsrname = (TextView) findViewById(R.id.textView2);
        Bundle bundle = getIntent().getExtras();
        String data1 = bundle.getString("uname");

        txtUsrname.setText(data1);
    }
}