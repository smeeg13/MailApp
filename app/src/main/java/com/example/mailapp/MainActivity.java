package com.example.mailapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends MenuForAllActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private Toolbar toolbar;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.myAppBar);
        setSupportActionBar(toolbar);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

    }


//    /** Called when the user taps the Sign Up button */
//    public void Signup(View view) {
//        // Do something in response to button
//        String stname = usrname.getText().toString();
//
//        Intent intent = new Intent(this, RegisterActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("uname",stname);
//        intent.putExtras(bundle);
//        startActivity(intent);
//
//    }

}