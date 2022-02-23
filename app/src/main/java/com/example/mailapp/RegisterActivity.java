package com.example.mailapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText inputFullName, inputEmail, inputPhone, inputAddress, inputZIP, inputLocation, inputPassword, inputConfirmPwd;
    TextView btnLogin;
    Button btnRegister;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Take back all the values entered by the new user
        inputFullName = findViewById(R.id.InputFullname);
        inputEmail = findViewById(R.id.InputEmail);
        inputPhone =  findViewById(R.id.InputPhone);
        inputAddress =  findViewById(R.id.InputAddress);
        inputZIP =  findViewById(R.id.InputZip);
        inputLocation =  findViewById(R.id.InputLocation);
        inputPassword =  findViewById(R.id.InputPassword);
        inputConfirmPwd =  findViewById(R.id.InputConfirmPassword);

        btnRegister = findViewById(R.id.RegisterBtn);
        btnLogin = findViewById(R.id.LoginLinkBtn);

        //Creation of the link back to Login Page
        btnLogin.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this,LoginActivity.class)));
    }

    /** Called when the user taps the Login button */
    public void Register(View view) {
        //Transform fields from the user in string And add them to an Arraylist
        String stname = inputFullName.getText().toString();
        String stmail = inputEmail.getText().toString();
        String stphone = inputPhone.getText().toString();
        String staddress = inputAddress.getText().toString();
        String stzip = inputZIP.getText().toString();
        String stloca = inputLocation.getText().toString();
        String stpwd = inputPassword.getText().toString();
        String stConfpwd = inputConfirmPwd.getText().toString();

        //Check if all entries has been completed
        if (CheckIfEmpty())
        {
            Toast.makeText(getApplicationContext(), "Make Sure all Fields are completed",Toast.LENGTH_SHORT).show();
        }
        else if (!CheckEmailIsValid(stmail)) //Check if the email entered is a real email or if there's a mistake
        {
            showError(inputEmail,"Email is not Valid");
            Toast.makeText(getApplicationContext(), "The Email entered is not valid", Toast.LENGTH_SHORT).show();
        }
        else if (!CheckStrongPassword(stpwd)) //Check if the password entered is strong enough (true = strong)
        {
            showError(inputPassword,"Password is Too Weak");
            Toast.makeText(getApplicationContext(), "Your Password is Too Weak, please change it", Toast.LENGTH_SHORT).show();
        }
        else if (!stpwd.equals(stConfpwd)) //Check if the confirm password is the same as the password
        {
            showError(inputConfirmPwd,"Password are not the Same");
            Toast.makeText(getApplicationContext(), "The two passwords fields should be the same", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Creation of the account In Progress", Toast.LENGTH_LONG).show();

//            //Now every field should be correct so the account will be created
//            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
//            startActivity(intent);
        }
    }




    /** To Check if all the fields have been completed */
    public boolean CheckIfEmpty() {
        boolean anyisempty = true;

        if (inputFullName.getText().toString().equals("")){
            showError(inputFullName, "Can not be empty");
            return true;
        }else if (inputEmail.getText().toString().equals(""))
        {
            showError(inputEmail, "Can not be empty");
            return true;
        } else if (inputPhone.getText().toString().equals(""))
        {
            showError(inputPhone, "Can not be empty");
            return true;
        } else if (inputAddress.getText().toString().equals(""))
        {
            showError(inputAddress, "Can not be empty");
            return true;
        }
        else if (inputZIP.getText().toString().equals(""))
        {
            showError(inputZIP, "Can not be empty");
            return true;
        }
        else if (inputLocation.getText().toString().equals(""))
        {
            showError(inputLocation, "Can not be empty");
            return true;
        }
        else if (inputPassword.getText().toString().equals(""))
        {
            showError(inputPassword, "Can not be empty");
            return true;
        }
        else if (inputConfirmPwd.getText().toString().equals("")) {
            showError(inputConfirmPwd, "Can not be empty");
            return true;
        }
        else
            return false;
    }

    /** To Check if the password entered is strong or not */
    public boolean CheckStrongPassword(String password){
        boolean isStrong = false;
        int passwordLength=8, upChars=0, lowChars=0;
        int special=0, digits=0;
        char ch;

        int totalChar = password.length();
        if(totalChar<passwordLength)
        {
            System.out.println("\nThe Password is invalid !");
            return false;
        }
        else
        {
            for(int i=0; i<totalChar; i++)
            {
                ch = password.charAt(i);
                if(Character.isUpperCase(ch))
                    upChars = 1;
                else if(Character.isLowerCase(ch))
                    lowChars = 1;
                else if(Character.isDigit(ch))
                    digits = 1;
                else
                    special = 1;
            }
        }
        if(upChars==1 && lowChars==1 && digits==1 && special==1) {
            System.out.println("\nThe Password is Strong.");
            isStrong = true;
        }
        else
            System.out.println("\nThe Password is Weak.");

        return isStrong;
    }

    /** To Check if the email entered is a Valid */
    public boolean CheckEmailIsValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
    private void showError(EditText input, String s){
        input.setError(s);
    }
}