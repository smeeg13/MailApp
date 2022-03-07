package com.example.mailapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.mailapp.Enums.Messages;
import com.example.mailapp.R;
import com.example.mailapp.SessionManagement.SessionManagement;
import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.dao.PostWorkerDao;
import com.example.mailapp.database.entities.PostWorkerEntity;

import java.util.List;

/**
 * Page For the Login
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mail, pwd;
    private TextView SignUpbtn;

    private PostWorkerEntity postWorkerEntity;
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
        List<PostWorkerEntity> list;
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
            Toast.makeText(getBaseContext(), Messages.WRONG_INFO.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkLogin(String login, String password) {
        LiveData<List<PostWorkerEntity>> list;
        list = myDatabase.postWorkerDao().getAll();
        System.out.println(mail.getText().toString());
        System.out.println(pwd.getText().toString());
        for  (PostWorkerEntity postWorkerEntity : list) {
            if (postWorkerEntity.getEmail().equals(login) && postWorkerEntity.getPassword().equals(password)) {
                System.out.println("login cible "+login);
                System.out.println("postworker login"+ postWorkerEntity.getEmail());
                System.out.println("password cible "+login);
                System.out.println("postworker password"+ postWorkerEntity.getPassword());
                System.out.println("POST WORKER HAS BEEN FOUND");
                SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
                sessionManagement.saveSession(postWorkerEntity);
                return true;
            }
        }
        System.out.println("## POST WORKER HAS NOT BEEN FOUND");
        return false;
    }

    private void showError(EditText input, String s) {
        input.setError(s);
    }

    public void postWorkersListToString(List<PostWorkerEntity> postWorkerEntities) {
        for (PostWorkerEntity postWorkerEntity : postWorkerEntities) {
            System.out.println("/////////////////");
            System.out.println("ID of post worker :" + postWorkerEntity.getIdPostWorker());
            System.out.println("Login :" + postWorkerEntity.getEmail());
            System.out.println("Passowrd :"+ postWorkerEntity.getPassword());
            System.out.println("Firstname :" + postWorkerEntity.getFirstname());
            System.out.println("Lastname :" + postWorkerEntity.getLastname());
            System.out.println("Phone :" + postWorkerEntity.getPhone());
            System.out.println("Address :" + postWorkerEntity.getAddress());
            System.out.println("Region :" + postWorkerEntity.getCity());
            System.out.println("Zip :" + postWorkerEntity.getZip());


        }
    }
}