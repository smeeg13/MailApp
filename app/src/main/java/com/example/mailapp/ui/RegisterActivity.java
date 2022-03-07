package com.example.mailapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mailapp.Enums.Messages;
import com.example.mailapp.R;
import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.dao.PostWorkerDao;
import com.example.mailapp.database.entities.PostWorkerEntity;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Page to create an account
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText inputfirstname, inputLastName, inputEmail, inputPhone, inputAddress, inputZIP, inputLocation, inputPassword, inputConfirmPwd;
    private ArrayList<EditText> inputs = new ArrayList<>();
    private TextView btnLogin;

    private PostWorkerEntity postWorkerEntity;
    private PostWorkerDao postWorkerDao;
    private MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Data base conneciton
        myDatabase = MyDatabase.getInstance(this.getBaseContext());
        //Take back all the values entered by the new user
        inputfirstname = findViewById(R.id.RegisterFirstnameEditText);
        inputs.add(inputfirstname);
        inputLastName = findViewById(R.id.RegisterLastnameEditText);
        inputs.add(inputLastName);
        inputEmail = findViewById(R.id.RegisterEmailEditText);
        inputs.add(inputEmail);
        inputPhone = findViewById(R.id.RegisterPhoneEditText);
        inputs.add(inputPhone);
        inputAddress = findViewById(R.id.RegisterAddressEditText);
        inputs.add(inputAddress);
        inputZIP = findViewById(R.id.RegisterZIPEditText);
        inputs.add(inputZIP);
        inputLocation = findViewById(R.id.RegisterCityEditText);
        inputs.add(inputLocation);
        inputPassword = findViewById(R.id.RegisterPasswordEditText);
        inputs.add(inputPassword);
        inputConfirmPwd = findViewById(R.id.RegisterConfirmPwdEditText);
        inputs.add(inputConfirmPwd);

        //Creation of the link back to Login Page
        btnLogin = findViewById(R.id.RegisterLoginBtn);
        // ONCLICK LOGIN BUTTON
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

        if (!InputsAreGood())
            Toast.makeText(getApplicationContext(), Messages.INVALID_FIELDS.toString(), Toast.LENGTH_SHORT).show();
        else {
            //TODO Save Data in The Database
            postWorkerEntity = new PostWorkerEntity();

            // postWorker.iD_PostWorker is automatic implemented
            postWorkerEntity.setFirstname(stfirstname);
            postWorkerEntity.setLastname(stlastname);
            postWorkerEntity.setAddress(staddress);
            postWorkerEntity.setEmail(stmail);
            postWorkerEntity.setPassword(stpwd);
            postWorkerEntity.setPhone(stphone);
            postWorkerEntity.setCity(stloca);
            postWorkerEntity.setZip(stzip);
            //add that user to the database
            myDatabase.postWorkerDao().insert(postWorkerEntity);
            System.out.println("## POST WORKER ADDED");
            //Launching the login page after saving data in the Database
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            //To "notify" the customer his account has been created
            Toast.makeText(getApplicationContext(), Messages.ACCOUNT_CREATED.toString(), Toast.LENGTH_LONG).show();
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

        for (boolean b : booleans) {
            if (b) {
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
    public boolean CheckSamePwd(String pwd, String ConfPwd) {

        if (!inputPassword.getText().toString().equals(inputConfirmPwd.getText().toString())) {
            showError(inputConfirmPwd, "Password are not the Same");
            return true;
        } else
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
            System.out.println("\n## The Password is invalid !");
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
            System.out.println("\n## The Password is Strong.");
            isWeak = false;
        } else
            System.out.println("\n## The Password is Weak.");

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
            showError(inputEmail, "## Email is not Valid");
            return true;
        } else
            return false;
    }

    /**
     * To Add the Red Info with a message in the field
     */
    public static void showError(EditText input, String s) {
        input.setError(s);
    }
}