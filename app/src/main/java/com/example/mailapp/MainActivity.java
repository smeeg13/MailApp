package com.example.mailapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    EditText usrname, pwd;
    Button loginBtn, signupBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usrname = (EditText) findViewById(R.id.UsernameTxt);
        pwd = (EditText) findViewById(R.id.PasswordTxt);
        loginBtn = (Button) findViewById(R.id.LoginBtn);
        signupBtn = (Button) findViewById(R.id.SignupBtn);
    }
    /** Called when the user taps the Login button */
    public void Login(View view) {
        // Do something in response to button according to conditions
            //Usrname & Pwd should be right AND Should not be empty

        String stname = usrname.getText().toString();
        String stpwd = pwd.getText().toString();

        if (stname.equals("HES") && stpwd.equals("1234"))
        {
            Intent intent = new Intent(this, HomeScreen.class);
            Bundle bundle = new Bundle();
            bundle.putString("uname",stname);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if (stname.equals("") || stpwd.equals(""))
        {
            Toast.makeText(getBaseContext(), "Enter Both Username And Password",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getBaseContext(), "Wrong Username or Password entered",Toast.LENGTH_SHORT).show();

    }

    /** Called when the user taps the Sign Up button */
    public void Signup(View view) {
        // Do something in response to button
    }

}