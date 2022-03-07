package com.example.mailapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mailapp.DataBase.Dao.PostWorkerDao;
import com.example.mailapp.DataBase.MyDatabase;
import com.example.mailapp.DataBase.Entities.PostWorker;
import com.example.mailapp.Enums.ToastsMsg;
import com.example.mailapp.SessionManagement.SessionManagement;

import java.util.List;

/**
 * Page For the Login
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mail, pwd;
    private TextView SignUpbtn;

    private PostWorker postWorker;
    private PostWorkerDao postWorkerDao;
    private MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        SignUpbtn = findViewById(R.id.LoginSignupBtn);
        myDatabase = MyDatabase.getInstance(this.getBaseContext());
        // Creating a link to the register page if client don't have an account
        SignUpbtn.setOnClickListener(view1 -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));
        List<PostWorker> list;
        list = myDatabase.postWorkerDao().getAll();
        postWorkersListToString(list);
        //PostWorker admin =  new PostWorker();
        //admin.setLogin("HES");
        //admin.setPassword("1234");
        //myDatabase.postWorkerDao().addPostWorker(admin);

        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        int session = sessionManagement.getSession();
        // -1 is the default number of the session, if someone is connected it will be not -1
        if (session != -1){
            System.out.println("## POST WORKER ID IS "+session);
            // if the user is still in the session he will go straight to the home activity
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
    }

    /**
     * Called when the user taps the Login button
     */
    public void Login(View view) {
        mail = findViewById(R.id.LoginEmailEditText);
        pwd = findViewById(R.id.LoginPasswordEditText);

        String stmail = mail.getText().toString();
        String stpwd = pwd.getText().toString();

        // TODO Check on the Database if infos are rights

        if (checkLogin(stmail, stpwd)) {
            Intent intent   = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else {
            showError(mail, "Wrong informations");
            showError(pwd, "Wrong informations");
            Toast.makeText(getBaseContext(), ToastsMsg.WRONG_INFO.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkLogin(String login, String password) {
        List<PostWorker> list;
        list = myDatabase.postWorkerDao().getAll();
        System.out.println(mail.getText().toString());
        System.out.println(pwd.getText().toString());
        for  (PostWorker postWorker : list) {
            if (postWorker.getLogin().equals(login) && postWorker.getPassword().equals(password)) {
                System.out.println("login cible "+login);
                System.out.println("postworker login"+ postWorker.getLogin());
                System.out.println("password cible "+login);
                System.out.println("postworker password"+ postWorker.getPassword());
                System.out.println("POST WORKER HAS BEEN FOUND");
                SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
                sessionManagement.saveSession(postWorker);
                return true;
            }
        }
        System.out.println("## POST WORKER HAS NOT BEEN FOUND");
        return false;
    }

    private void showError(EditText input, String s) {
        input.setError(s);
    }

    public void postWorkersListToString(List<PostWorker> postWorkers) {
        for (PostWorker postWorker : postWorkers) {
            System.out.println("/////////////////");
            System.out.println("ID of post worker :" + postWorker.getiD_PostWorker());
            System.out.println("Login :" + postWorker.getLogin());
            System.out.println("Passowrd :"+postWorker.getPassword());
            System.out.println("Firstname :" + postWorker.getFirstname());
            System.out.println("Lastname :" + postWorker.getLastName());
            System.out.println("Phone :" + postWorker.getPhone());
            System.out.println("Address :" + postWorker.getAddress());
            System.out.println("Region :" + postWorker.getRegion());
            System.out.println("Zip :" + postWorker.getZip());


        }
    }
}