package com.example.mailapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText mail, pwd;
    private Button loginBtn;
    private TextView SignUpbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        }
    public void SignUpLink(View view){
        SignUpbtn = findViewById(R.id.SignupLinkBtn);
    SignUpbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
    });
    }

    /** Called when the user taps the Login button */
    public void Login(View view) {
        mail =  findViewById(R.id.inputEmailLogin);
        pwd = findViewById(R.id.inputPasswordLogin);
        loginBtn = findViewById(R.id.LoginBtn);

        String stmail = mail.getText().toString();
        String stpwd = pwd.getText().toString();

        if (stmail.equals("HES") && stpwd.equals("1234"))
        {
//
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
           // Toast.makeText(getBaseContext(), "Connection Starting",Toast.LENGTH_SHORT).show();

        }
        else if (stmail.equals("") )
        {
            showError(mail, "Cannot be empty");
            Toast.makeText(getBaseContext(), "Enter Both Email And Password",Toast.LENGTH_SHORT).show();
            if ( stpwd.equals(""))
            {
                showError(pwd, "Cannot be empty");
                Toast.makeText(getApplicationContext(), "Enter Both Email And Password",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            showError(mail, "Wrong informations");
            showError(pwd, "Wrong informations");
            Toast.makeText(getBaseContext(), "Wrong Email or Password entered", Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(EditText input, String s){
        input.setError(s);
    }
}