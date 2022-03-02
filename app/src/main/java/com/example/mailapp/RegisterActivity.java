package com.example.mailapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Page to create an account
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText inputfirstname, inputLastName, inputEmail, inputPhone, inputAddress, inputZIP, inputLocation, inputPassword, inputConfirmPwd;
    private ArrayList<EditText> inputs = new ArrayList<>();
    private TextView btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Take back all the values entered by the new user
        inputfirstname = findViewById(R.id.inputfirstname);
        inputs.add(inputfirstname);
        inputLastName = findViewById(R.id.InputFullname);
        inputs.add(inputLastName);
        inputEmail = findViewById(R.id.InputEmail);
        inputs.add(inputEmail);
        inputPhone = findViewById(R.id.InputPhone);
        inputs.add(inputPhone);
        inputAddress = findViewById(R.id.InputAddress);
        inputs.add(inputAddress);
        inputZIP = findViewById(R.id.InputZip);
        inputs.add(inputZIP);
        inputLocation = findViewById(R.id.InputLocation);
        inputs.add(inputLocation);
        inputPassword = findViewById(R.id.InputPassword);
        inputs.add(inputPassword);
        inputConfirmPwd = findViewById(R.id.InputConfirmPassword);
        inputs.add(inputConfirmPwd);

        btnRegister = findViewById(R.id.RegisterBtn);
        btnLogin = findViewById(R.id.LoginLinkBtn);

        //Creation of the link back to Login Page
        btnLogin.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    /**
     * Called when the user taps on the Create Account button
     */
    public void Register(View view) {
        //Transform fields from the user in string
        String stlastname = inputLastName.getText().toString();
        String stfirstname = inputfirstname.getText().toString();
        String stmail = inputEmail.getText().toString();
        String stphone = inputPhone.getText().toString();
        String staddress = inputAddress.getText().toString();
        String stzip = inputZIP.getText().toString();
        String stloca = inputLocation.getText().toString();
        String stpwd = inputPassword.getText().toString();
        String stConfpwd = inputConfirmPwd.getText().toString();

        if (!InputsAreGood())
            Toast.makeText(getApplicationContext(), "Make Sure all Fields are Valid",Toast.LENGTH_SHORT).show();
        else
        {
            //TODO Save Data in The Database
                //postWorker = new PostWorker();

                //postWorker.iD_PostWorker = cpt;
                //postWorker.firstname = stfirstname;
                //postWorker.lastName = stlastname;
                //postWorker.address = staddress;
                //postWorker.login = stmail ;
                //postWorker.password = stpwd;
                //postWorker.phone= stphone;
                //postWorker.region = stloca;
                //postWorker.zip  = stzip;
                //add that user to the database
                //database.postWorkerDao().insertAll(postWorker);

            //Launching the login page after saving data in the Database
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            //To "notify" the customer his account has been created
            Toast.makeText(getApplicationContext(), "Account Created",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * To know if all the fields are checked and Valid
     * Return true if all is ok
     */
    public boolean InputsAreGood() {
        int IsOk = 0;

        boolean[] booleans = new boolean[4];

        //Check if all entries has been completed
        booleans[0] = CheckEmpty();
        booleans[1] = CheckEmailInvalid(inputEmail.getText().toString());
        booleans[2] = CheckPasswordWeak(inputPassword.getText().toString());
        booleans[3] = CheckSamePwd(inputPassword.getText().toString(), inputConfirmPwd.getText().toString());

        for (boolean b : booleans){
            if (b){
                IsOk++;
            }
        }

        return IsOk <= 0;
    }

    /**
     * To Check if all the fields have been completed
     * Return true if the 1 input is Empty
     */
    public boolean CheckEmpty() {
        int IsEmpty = 0;

        //For each check if empty
        for (EditText in : inputs) {
            if (in.getText().toString().isEmpty()) {
                showError(in, "Can not be empty");
                //If empty add 1 to IsEmpty
                IsEmpty++;
            }
        }
        return IsEmpty > 0;
    }

    /**
     * To Check if the Confirm password entered is the same as the Password
     * Return true if the two Pwd are not the same
     */
    public boolean CheckSamePwd(String pwd, String ConfPwd){

        if (!inputPassword.getText().toString().equals(inputConfirmPwd.getText().toString())){
            showError(inputConfirmPwd, "Password are not the Same");
            return true;
        }
        else
            return false;
    }


    /**
     * To Check if the password entered is strong or not
     * Return true if the Pwd is Weak
     */
    public boolean CheckPasswordWeak(String password) {
        boolean isWeak = true;
        int passwordLength = 8, upChars = 0, lowChars = 0;
        int special = 0, digits = 0;
        char ch;

        int totalChar = password.length();
        if (totalChar < passwordLength) {
            System.out.println("\nThe Password is invalid !");
            return true;
        } else {
            for (int i = 0; i < totalChar; i++) {
                ch = password.charAt(i);
                if (Character.isUpperCase(ch))
                    upChars = 1;
                else if (Character.isLowerCase(ch))
                    lowChars = 1;
                else if (Character.isDigit(ch))
                    digits = 1;
                else
                    special = 1;
            }
        }
        if (upChars == 1 && lowChars == 1 && digits == 1 && special == 1) {
            System.out.println("\nThe Password is Strong.");
            isWeak = false;
        } else
            System.out.println("\nThe Password is Weak.");

        return isWeak;
    }

    /**
     * To Check if the email entered is Valid
     * Return true if the Email is Invalid
     */
    public boolean CheckEmailInvalid(String myemail) {
        String email = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(email);
        if (myemail == null)
            return true;

        if (!pat.matcher(myemail).matches()) {
            showError(inputEmail, "Email is not Valid");
            return true;
        } else
            return false;
    }

    /**
     * To Add the Red Info with a message in the field
     */
    private void showError(EditText input, String s) {
        input.setError(s);
    }
}