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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MyAccountFragment extends Fragment {
    TextView inputEmail, inputPhone, inputZip, inputLocation, inputPassword, inputConfirmPassword,inputTitle, inputAddress;
    FloatingActionButton inputfloatingEditButton;
    ImageView inputaccountImage;
    Boolean aBoolean = true;
    PostWorker postWorker;
    PostWorkerDao postWorkerDao;
    MyDatabase myDatabase;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    public MyAccountFragment newInstance(String param1, String param2) {
        MyAccountFragment fragment = new MyAccountFragment();
        Bundle args = new Bundle();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //connection to database
        //initialize();
        //linking all views in order to set them enabled

    }

    public void editMode() {

        if (aBoolean == true) {
            enableEdit(true);
            aBoolean = false;
        } else {
            enableEdit(false);
            aBoolean = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_account, container, false);
        inputfloatingEditButton = v.findViewById(R.id.AccountEditButton);
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

        myDatabase = MyDatabase.getInstance(this.getContext());

        // for (int i = 0; i <10 ; i++) {
        //    postWorker = new PostWorker();
        //     postWorker.login = "abdulla";
        //     postWorker.password = "wow";
        //     myDatabase.postWorkerDao().addPostWorker(postWorker);
        // }

        //inputTitle = (TextView) v.findViewById(R.id.AccountFirstnameLastnameTitle);
       // inputTitle.setText(postWorker.firstname);
        inputfloatingEditButton = v.findViewById(R.id.AccountEditButton);
        inputEmail = v.findViewById(R.id.AccountEmailTextView);
        inputPhone = v.findViewById(R.id.AccountPhoneTextView);
        inputAddress = v.findViewById(R.id.AccountAddressTextView);
        inputZip = v.findViewById(R.id.AccountZipTextView);
        inputLocation = v.findViewById(R.id.AccountLocationTextView);
        inputPassword = v.findViewById(R.id.AccountPasswordTextView);
        inputConfirmPassword = v.findViewById(R.id.AccountConfirmPassword);
    }
}