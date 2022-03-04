package com.example.mailapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mailapp.DataBase.Dao.PostWorkerDao;
import com.example.mailapp.DataBase.MyDatabase;
import com.example.mailapp.DataBase.Tables.PostWorker;
import com.example.mailapp.R;
import com.example.mailapp.SessionManagement.SessionManagement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MyAccountFragment extends Fragment {
    TextView inputEmail,inputFirstnameAndLastname, inputPhone, inputZip, inputLocation, inputPassword, inputConfirmPassword, inputTitle, inputAddress;
    FloatingActionButton inputfloatingEditButton;
    ImageView inputaccountImage;
    Boolean aBoolean = true;
    PostWorker postWorker;
    PostWorkerDao postWorkerDao;
    MyDatabase myDatabase;
    SessionManagement sessionManagement;
    View v;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void editMode() {

        if (aBoolean == true) {
            enableEdit(true);
            aBoolean = false;
        } else {
            enableEdit(false);
            //TODO save all the modification to the database
            postWorker.setZip(inputZip.getText().toString());
            postWorker.setRegion(inputLocation.getText().toString());
            postWorker.setAddress(inputAddress.getText().toString());
            postWorker.setPhone(inputPhone.getText().toString());
            myDatabase.postWorkerDao().updatePostWorker(postWorker.getiD_PostWorker(), postWorker.getRegion(), postWorker.getZip(), postWorker.getAddress(), postWorker.getPhone());
           // initialize(v);
            aBoolean = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_my_account, container, false);
        inputfloatingEditButton = v.findViewById(R.id.AccountEditButton);
        sessionManagement = new SessionManagement(this.getContext());

        initialize(v);
        inputfloatingEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode();
            }
        });


        return v;
    }


    public void enableEdit(boolean b) {
        // sub method
        if (b == true) {

            inputEmail.setEnabled(true);
            inputPhone.setEnabled(true);
            inputAddress.setEnabled(true);
            inputZip.setEnabled(true);
            inputLocation.setEnabled(true);
            inputPassword.setEnabled(true);
            inputConfirmPassword.setVisibility(View.VISIBLE);
            inputConfirmPassword.setEnabled(true);

        } else {
            inputEmail.setEnabled(false);
            inputPhone.setEnabled(false);
            inputAddress.setEnabled(false);
            inputZip.setEnabled(false);
            inputLocation.setEnabled(false);
            inputPassword.setEnabled(false);
            inputConfirmPassword.setVisibility(View.INVISIBLE);
            inputConfirmPassword.setEnabled(false);
        }
    }

    public void initialize(View v) {
        String firstname,lastname;

        myDatabase = MyDatabase.getInstance(this.getContext());

        postWorker = myDatabase.postWorkerDao().getPostWorkerByid(sessionManagement.getSession());
        inputFirstnameAndLastname = v.findViewById(R.id.AccountFirstnameLastnameTitle);
        firstname = postWorker.getFirstname()+" ";
        lastname = postWorker.getLastName();
        inputFirstnameAndLastname.setText(firstname + lastname);
        inputfloatingEditButton = v.findViewById(R.id.AccountEditButton);
        inputEmail = v.findViewById(R.id.AccountEmailTextView);
        inputEmail.setText(postWorker.getLogin());
        inputPhone = v.findViewById(R.id.AccountPhoneTextView);
        inputPhone.setText(postWorker.getPhone());
        inputAddress = v.findViewById(R.id.AccountAddressTextView);
        inputAddress.setText(postWorker.getAddress());
        inputZip = v.findViewById(R.id.AccountZipTextView);
        inputZip.setText(postWorker.getZip());
        inputLocation = v.findViewById(R.id.AccountLocationTextView);
        inputLocation.setText(postWorker.getRegion());
        inputPassword = v.findViewById(R.id.AccountPasswordTextView);
        inputConfirmPassword = v.findViewById(R.id.AccountConfirmPassword);
    }


}