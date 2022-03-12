package com.example.mailapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.LiveData;

import com.example.mailapp.BaseApplication;
import com.example.mailapp.Enums.Messages;
import com.example.mailapp.R;
import com.example.mailapp.SessionManagement.SessionManagement;
import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.dao.PostWorkerDao;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.database.repository.PostworkerRepository;
import com.example.mailapp.util.MyAlertDialog;

import java.util.List;

/**
 * Page For the Login
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mail, pwd;
    private TextView SignUpbtn;

    PostworkerRepository postworkerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        postworkerRepository = ((BaseApplication) getApplication()).getPostworkerRepository();

        //Initialisation
        mail = findViewById(R.id.LoginEmailEditText);
        pwd = findViewById(R.id.LoginPasswordEditText);

        // Creating a link to the register page if client don't have an account
        SignUpbtn = findViewById(R.id.LoginSignupBtn);
        SignUpbtn.setOnClickListener(view1 -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));
    }

    /**
     * Called when the user taps the Login button
     */
    public void Login(View view) {

        // Reset errors.
        mail.setError(null);
        pwd.setError(null);

        String stmail = mail.getText().toString();
        String stpwd = pwd.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (stpwd.isEmpty()) {
            showError(pwd, Messages.EMPTY_FIELDS.toString());
            pwd.setText("");
            focusView = pwd;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(stmail)) {
            showError(mail, Messages.EMPTY_FIELDS.toString());
            focusView = mail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            postworkerRepository.getPostworkerByEmail(stmail, getApplication()).observe(LoginActivity.this, postWorkerEntity -> {
                if (postWorkerEntity != null) {
                    if (postWorkerEntity.getPassword().equals(stpwd)) {
                        // We need an Editor object to make preference changes.
                        // All objects are from android.context.Context
                        SharedPreferences.Editor editor = getSharedPreferences(BaseActivity.PREFS_NAME, 0).edit();
                        editor.putString(BaseActivity.PREFS_USER, postWorkerEntity.getEmail());
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        System.out.println("## LOGIN OK");
                        System.out.println(postWorkerEntity.toString());
                        mail.setText("");
                        pwd.setText("");
                    } else {
                        showError(pwd, Messages.WRONG_INFO.toString());
                        pwd.requestFocus();
                        pwd.setText("");
                    }
                } else {
                    showError(mail, Messages.WRONG_INFO.toString());
                    mail.requestFocus();
                    pwd.setText("");
                }
            });
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
    }

    /**
     * Method to show a confirmation box for leaving the app
     */
    @Override
    public void onBackPressed() {
        //TODO Check prq couleru pas comme les autres ???
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        alert.setTitle("Confirmation of Disconnection");
        alert.setMessage("You will be logged out. Are you sure ?");
        alert.setPositiveButton("Yes, Log Out", (dialog, which) -> {
            dialog.dismiss();
            //if you want to kill app . from other then your main avtivity.(Launcher)
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            //if you want to finish just current activity
            // LoginActivity.this.finish();
        });
        alert.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        alert.show();    }
}