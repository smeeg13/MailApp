package com.example.mailapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mailapp.BaseApplication;
import com.example.mailapp.Enums.Messages;
import com.example.mailapp.R;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.database.repository.PostworkerRepository;
import com.example.mailapp.util.OnAsyncEventListener;

import java.util.ArrayList;

/**
 * Page to create an account
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText inputfirstname, inputLastName, inputEmail, inputPhone, inputAddress, inputZIP, inputLocation, inputPassword, inputConfirmPwd;
    private final ArrayList<EditText> inputs = new ArrayList<>();
    private PostworkerRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        repository = ((BaseApplication) getApplication()).getPostworkerRepository();
        initialize();
       }

    protected void initialize(){
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
        TextView btnLogin = findViewById(R.id.RegisterLoginBtn);
        // ONCLICK LOGIN BUTTON
        btnLogin.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }


    /**
     * Called when the user click on the Create Account button
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


        //Check if any is empty or if pwd & email are invalid & if the 2 pwd entered are same
        if (!InputsAreGood()){
            Toast.makeText(getApplicationContext(), Messages.INVALID_FIELDS.toString(), Toast.LENGTH_SHORT).show();
        }
        else {
            //Create the post worker with info entered
            PostWorkerEntity newWorker = new PostWorkerEntity(stfirstname,stlastname,staddress, stmail,stpwd,stphone,stloca,stzip);
            newWorker.setBackground("white");
            repository.register(newWorker, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "createUserWithEmail: success");
                    setResponse(true);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "createUserWithEmail: failure", e);
                    setResponse(false);
                }
            });
        }
    }

    private void setResponse(Boolean response) {
        if (response) {
            Toast.makeText(getApplication(),Messages.ACCOUNT_CREATED.toString(),Toast.LENGTH_LONG);
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            inputEmail.setError(Messages.EMAIL_ALREADY_EXIST.toString());
            inputEmail.requestFocus();
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
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches()){
            booleans[1] = true;
            inputEmail.setError(Messages.INVALID_EMAIL.toString());
        }
        booleans[2] = CheckPasswordWeak(inputPassword.getText().toString());
        booleans[3] = CheckSamePwd(inputPassword.getText().toString(), inputConfirmPwd.getText().toString());

        for (boolean b : booleans) {
            if (b)
                IsOk++;
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
                showError(in, getString(R.string.empty_field));
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
            showError(inputConfirmPwd, Messages.NOT_SAME_PWD.toString());
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
        String pattern = getString(R.string.pwdPattern);
        boolean matches = password.matches(pattern);

        if (matches){
            isWeak=false;
            System.out.println(R.string.pwd_strong);
        }
        else {
            isWeak=true;
            System.out.println(Messages.WEAK_PWD);
            showError(inputPassword,Messages.WEAK_PWD.toString());
        }
        return isWeak;
    }


    /**
     * To Add the Red Info with a message in the field
     */
    public static void showError(EditText input, String s) {
        input.setError(s);
    }
}