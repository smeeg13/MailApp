package com.example.mailapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mailapp.BaseApplication;
import com.example.mailapp.Enums.Messages;
import com.example.mailapp.R;
import com.example.mailapp.database.repository.PostworkerRepository;
import com.example.mailapp.util.MyAlertDialog;

/**
 * Page For the Login
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mail, pwd;
    private TextView SignUpbtn;
    private PostworkerRepository postworkerRepository;

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

        //Stores the new values entered
        String stmail = mail.getText().toString();
        String stpwd = pwd.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for empty password, if the user entered one.
        if (stpwd.isEmpty()) {
            showError(pwd, Messages.EMPTY_FIELDS.toString());
            pwd.setText("");
            focusView = pwd;
            cancel = true;
        }

        // Check for a empty email address.
        if (TextUtils.isEmpty(stmail)) {
            showError(mail, Messages.EMPTY_FIELDS.toString());
            focusView = mail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            System.out.println("--------");
            System.out.println("## LOGIN NOT OK");
            System.out.println("--------");
            focusView.requestFocus();
        } else {
            postworkerRepository.signIn(stmail, stpwd, task -> {
                if (task.isSuccessful()) {
                    System.out.println("--------");
                    System.out.println("## LOGIN OK");
                    System.out.println("--------");
                    Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                    startActivity(intent);
                    mail.setText("");
                    pwd.setText("");
                } else {
                    System.out.println(Messages.WRONG_INFO);
                    mail.setError(getString(R.string.error_invalid_email));
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
        MyAlertDialog dialog = new MyAlertDialog(this, "Confirmation, Closing App",
                "The program will end. Are you sure ?","Yes, Close");
        dialog.killProgram();
        dialog.setThemeID(R.style.AlertDialogCustom);
    }
}