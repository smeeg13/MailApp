package com.example.mailapp.ui.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mailapp.Enums.Messages;
import com.example.mailapp.Enums.Status;
import com.example.mailapp.database.dao.PostWorkerDao;
import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.R;
import com.example.mailapp.SessionManagement.SessionManagement;
import com.example.mailapp.database.repository.PostworkerRepository;
import com.example.mailapp.ui.BaseActivity;
import com.example.mailapp.util.OnAsyncEventListener;
import com.example.mailapp.viewModel.PostWorkerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.lifecycle.ViewModelProviders;


public class MyAccountFragment extends Fragment {
    private static final String TAG = "AccountDetails";

    private TextView inputEmail, inputFirstnameAndLastname, inputPhone, inputZip, inputLocation, inputPassword, inputConfirmPassword, inputTitle, inputAddress;
    private FloatingActionButton inputfloatingEditButton;
    private ImageView inputaccountImage;
    private Boolean aBoolean = true;
    private PostWorkerEntity postWorkerEntity;
    private PostWorkerDao postWorkerDao;
    private MyDatabase myDatabase;
    private SessionManagement sessionManagement;
    private View v;
    private String firstname, lastname;
    private PostworkerRepository postworkerRepository;
    private SharedPreferences settings;
    private String sharedPrefMail;
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

            inputfloatingEditButton.setImageResource(R.drawable.ic_baseline_edit_24);
            aBoolean = true;
            viewModel.updatePostWorker(postWorkerEntity, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    System.out.println(Messages.ACCOUNT_UPDATED);
                }

                @Override
                public void onFailure(Exception e) {
                    System.out.println(Messages.ACCOUNT_UPDATED_FAILED);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_account, container, false);
        inputfloatingEditButton = v.findViewById(R.id.AccountEditButton);
        //sessionManagement = new SessionManagement(this.getContext());


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

        inputFirstnameAndLastname = v.findViewById(R.id.AccountFirstnameLastnameTitle);
        inputfloatingEditButton = v.findViewById(R.id.AccountEditButton);
        inputEmail = v.findViewById(R.id.AccountEmailTextView);
        inputPhone = v.findViewById(R.id.AccountPhoneTextView);
        inputAddress = v.findViewById(R.id.AccountAddressTextView);
        inputZip = v.findViewById(R.id.AccountZipTextView);
        inputLocation = v.findViewById(R.id.AccountLocationTextView);
        inputPassword = v.findViewById(R.id.AccountPasswordTextView);
        inputConfirmPassword = v.findViewById(R.id.AccountConfirmPassword);

        SharedPreferences settings = getActivity().getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        String sharedPrefMail = settings.getString(BaseActivity.PREFS_USER, null);

        PostWorkerViewModel.Factory factory = new PostWorkerViewModel.Factory(getActivity().getApplication(), sharedPrefMail);

        viewModel = ViewModelProviders.of(this, factory).get(PostWorkerViewModel.class);
        viewModel.getClient().observe(getActivity(), postworker -> {
            if (postworker != null) {

                postWorkerEntity = postworker;

                firstname = postWorkerEntity.getFirstname();

                lastname = postWorkerEntity.getLastname();

                inputFirstnameAndLastname.setText(firstname + lastname);

                inputEmail.setText(postWorkerEntity.getEmail());

                inputPhone.setText(postWorkerEntity.getPhone());

                inputAddress.setText(postWorkerEntity.getAddress());

                inputZip.setText(postWorkerEntity.getZip());

                inputLocation.setText(postWorkerEntity.getCity());

            }
        });




    }


}