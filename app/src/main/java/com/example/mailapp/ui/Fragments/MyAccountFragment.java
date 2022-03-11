package com.example.mailapp.ui.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mailapp.database.dao.PostWorkerDao;
import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.R;
import com.example.mailapp.SessionManagement.SessionManagement;
import com.example.mailapp.viewModel.PostWorkerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MyAccountFragment extends Fragment {
    private static final String TAG ="AccountDetails";

    private TextView inputEmail,inputFirstnameAndLastname, inputPhone, inputZip, inputLocation, inputPassword, inputConfirmPassword, inputTitle, inputAddress;
    private FloatingActionButton inputfloatingEditButton;
    private ImageView inputaccountImage;
    private Boolean aBoolean = true;
    private PostWorkerEntity postWorkerEntity;
    private PostWorkerDao postWorkerDao;
    private MyDatabase myDatabase;
    private SessionManagement sessionManagement;
    private View v;

    private PostWorkerViewModel viewModel;

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
            inputfloatingEditButton.setImageResource(R.drawable.ic_baseline_save_24);
        } else {
            enableEdit(false);
            //TODO save all the modification to the database
            postWorkerEntity.setZip(inputZip.getText().toString());
            postWorkerEntity.setCity(inputLocation.getText().toString());
            postWorkerEntity.setAddress(inputAddress.getText().toString());
            postWorkerEntity.setPhone(inputPhone.getText().toString());
            //TODO check if all fields are good
            myDatabase.postWorkerDao().update(postWorkerEntity);
           // initialize(v);
            inputfloatingEditButton.setImageResource(R.drawable.ic_baseline_edit_24);
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

     //   postWorkerEntity = myDatabase.postWorkerDao().getById(sessionManagement.getSession());
        inputFirstnameAndLastname = v.findViewById(R.id.AccountFirstnameLastnameTitle);
        firstname = postWorkerEntity.getFirstname()+" ";
        lastname = postWorkerEntity.getLastname();
        inputFirstnameAndLastname.setText(firstname + lastname);
        inputfloatingEditButton = v.findViewById(R.id.AccountEditButton);
        inputEmail = v.findViewById(R.id.AccountEmailTextView);
        inputEmail.setText(postWorkerEntity.getEmail());
        inputPhone = v.findViewById(R.id.AccountPhoneTextView);
        inputPhone.setText(postWorkerEntity.getPhone());
        inputAddress = v.findViewById(R.id.AccountAddressTextView);
        inputAddress.setText(postWorkerEntity.getAddress());
        inputZip = v.findViewById(R.id.AccountZipTextView);
        inputZip.setText(postWorkerEntity.getZip());
        inputLocation = v.findViewById(R.id.AccountLocationTextView);
        inputLocation.setText(postWorkerEntity.getCity());
        inputPassword = v.findViewById(R.id.AccountPasswordTextView);
        inputConfirmPassword = v.findViewById(R.id.AccountConfirmPassword);
    }


}