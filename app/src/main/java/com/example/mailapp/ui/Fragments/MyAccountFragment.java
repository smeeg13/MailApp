package com.example.mailapp.ui.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mailapp.BaseApplication;
import com.example.mailapp.Enums.Messages;
import com.example.mailapp.database.async.mail.UpdateMail;
import com.example.mailapp.database.dao.PostWorkerDao;
import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.R;

import com.example.mailapp.database.repository.MailRepository;
import com.example.mailapp.database.repository.PostworkerRepository;
import com.example.mailapp.ui.BaseActivity;
import com.example.mailapp.ui.LoginActivity;
import com.example.mailapp.util.OnAsyncEventListener;
import com.example.mailapp.viewModel.MailViewModel;
import com.example.mailapp.viewModel.PostWorkerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;


public class MyAccountFragment extends Fragment {

    final private int ID_ADMIN = 3;
    private static final String TAG = "AccountDetails";

    private TextView inputEmail, inputFirstnameAndLastname, inputPhone, inputZip, inputLocation, inputPassword, inputConfirmPassword, inputTitle, inputAddress;
    private FloatingActionButton inputfloatingEditButton;
    private Button inputDeleteButton;
    private ImageView inputaccountImage;
    private Boolean aBoolean = true;
    private PostWorkerEntity postWorkerEntity;
    private PostWorkerEntity postWorkerAdmin;
    private PostWorkerDao postWorkerDao;
    private MyDatabase myDatabase;
    private View v;

    private List<MailEntity> mails;
    private String firstname, lastname;
    private ArrayList<TextView> inputs = new ArrayList<>();
    private PostworkerRepository postworkerRepository;
    private SharedPreferences settings;
    private String sharedPrefMail;
    private PostWorkerViewModel postWorkerViewModel;
    private MailViewModel mailViewModel;
    private MailRepository mailRepository, mailRepository2;
    private int numberOfMails=0;
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
            if (!InputsAreGood()) {
                Toast.makeText(getContext(), Messages.INVALID_FIELDS.toString(), Toast.LENGTH_SHORT).show();
//            inputPassword.setText("");
//            inputConfirmPwd.setText("");
            } else {

                enableEdit(false);
                postWorkerEntity.setPassword(inputPassword.getText().toString());
                postWorkerEntity.setEmail(inputEmail.getText().toString());
                postWorkerEntity.setZip(inputZip.getText().toString());
                postWorkerEntity.setCity(inputLocation.getText().toString());
                postWorkerEntity.setAddress(inputAddress.getText().toString());
                postWorkerEntity.setPhone(inputPhone.getText().toString());

                inputfloatingEditButton.setImageResource(R.drawable.ic_baseline_edit_24);
                aBoolean = true;

                postWorkerViewModel.updatePostWorker(postWorkerEntity, new OnAsyncEventListener() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_account, container, false);
        inputfloatingEditButton = v.findViewById(R.id.AccountEditButton);


        initialize(v);
        inputfloatingEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode();
            }
        });
        inputDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("DELETE METHOD");
                deleteAccount();
            }
        });

        return v;
    }


    public void enableEdit(boolean b) {
        // sub method
        if (b == true) {

            //inputEmail.setEnabled(true);
            inputPhone.setEnabled(true);
            inputAddress.setEnabled(true);
            inputZip.setEnabled(true);
            inputLocation.setEnabled(true);
            inputPassword.setEnabled(true);
            inputConfirmPassword.setVisibility(View.VISIBLE);
            inputConfirmPassword.setEnabled(true);

        } else {
            //inputEmail.setEnabled(false);
            inputPhone.setEnabled(false);
            inputAddress.setEnabled(false);
            inputZip.setEnabled(false);
            inputLocation.setEnabled(false);
            inputPassword.setEnabled(false);
            inputConfirmPassword.setVisibility(View.INVISIBLE);
            inputConfirmPassword.setEnabled(false);
        }
    }

    public void deleteAccount() {

        AlertDialog.Builder ab = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
        ab.setTitle("Confirmation of Delete");
        ab.setMessage("You will be delete your account. Are you sure ?");
        ab.setPositiveButton("Yes", (dialog, which) -> {

            if (inputEmail.getText().toString().equals("admin")) {
                Toast.makeText(getActivity().getBaseContext(), "You can't delete the admin account !", Toast.LENGTH_SHORT).show();
            } else {
                //get list of the postWorker mails
                mails = new ArrayList<MailEntity>();
                mailRepository.getAllByPostworker(postWorkerEntity.getIdPostWorker(), getActivity().getApplication()).observe(getActivity(), mailEntities -> {

                    for (MailEntity mail : mailEntities) {
                        mail.setIdPostWorker(ID_ADMIN);

                        mailRepository.update(mail, new OnAsyncEventListener() {
                            @Override
                            public void onSuccess() {
                                System.out.println("Mail id" + mail.getIdMail() + "has been redirected to admin");
                                numberOfMails ++;
                                if (numberOfMails == mailEntities.size()){
                                    System.out.println("number of mails variable :"+numberOfMails);
                                    postWorkerViewModel.deletePostWorker(postWorkerEntity, new OnAsyncEventListener() {
                                        @Override
                                        public void onSuccess() {
                                            System.out.println(Messages.ACCOUNT_DELETED);
                                            numberOfMails = 0;
                                        }

                                        //
                                        @Override
                                        public void onFailure(Exception e) {
                                            System.out.println(Messages.ACCOUNT_DELETED_FAILED);
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onFailure(Exception e) {
                                System.out.println("Mail id" + mail.getIdMail() + "has been NOT redirected to admin");
                            }
                        }, getActivity().getApplication());
                    }

                });

                //postworkerRepository.delete(postWorkerEntity, new OnAsyncEventListener() {
                //   @Override
                //   public void onSuccess() {
                //       System.out.println("ACCOUNT DELETED");
                //   }
                //
                //   @Override
                //   public void onFailure(Exception e) {
                //       System.out.println("ACCOUNT DELETE FAILED");
                //   }
                //  },getActivity().getApplication());


                SharedPreferences.Editor editor = getActivity().getSharedPreferences(BaseActivity.PREFS_NAME, 0).edit();
                editor.remove(BaseActivity.PREFS_NAME);
                editor.remove(BaseActivity.PREFS_USER);

                editor.apply();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                startActivity(intent);
            }

        });
        ab.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        ab.show();

    }

    public void initialize(View v) {
        mailRepository = ((BaseApplication) getActivity().getApplication()).getMailRepository();
        postworkerRepository = ((BaseApplication) getActivity().getApplication()).getPostworkerRepository();
        inputDeleteButton = v.findViewById(R.id.AccountDeletePostWorker);
        inputfloatingEditButton = v.findViewById(R.id.AccountEditButton);

        //inputs opened for modifications
        inputFirstnameAndLastname = v.findViewById(R.id.AccountFirstnameLastnameTitle);
        inputEmail = v.findViewById(R.id.AccountEmailTextView);
        inputEmail.setEnabled(false);
        inputPhone = v.findViewById(R.id.AccountPhoneTextView);
        inputs.add(inputPhone);
        inputAddress = v.findViewById(R.id.AccountAddressTextView);
        inputs.add(inputAddress);
        inputZip = v.findViewById(R.id.AccountZipTextView);
        inputs.add(inputZip);
        inputLocation = v.findViewById(R.id.AccountLocationTextView);
        inputs.add(inputLocation);
        inputPassword = v.findViewById(R.id.AccountPasswordTextView);
        inputs.add(inputPassword);
        inputConfirmPassword = v.findViewById(R.id.AccountConfirmPassword);
        inputs.add(inputConfirmPassword);

        settings = getActivity().getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        sharedPrefMail = settings.getString(BaseActivity.PREFS_USER, null);

        PostWorkerViewModel.Factory factory = new PostWorkerViewModel.Factory(getActivity().getApplication(), sharedPrefMail);
        postWorkerViewModel = ViewModelProviders.of(this, factory).get(PostWorkerViewModel.class);
        postWorkerViewModel.getClient().observe(getActivity(), postworker -> {

            if (postworker != null) {

                postWorkerEntity = postworker;

                firstname = postWorkerEntity.getFirstname();

                lastname = postWorkerEntity.getLastname();

                inputFirstnameAndLastname.setText(firstname + " " + lastname);

                inputEmail.setText(postWorkerEntity.getEmail());

                inputPassword.setText(postWorkerEntity.getPassword());

                inputConfirmPassword.setText(postWorkerEntity.getPassword());

                inputPhone.setText(postWorkerEntity.getPhone());

                inputAddress.setText(postWorkerEntity.getAddress());

                inputZip.setText(postWorkerEntity.getZip());

                inputLocation.setText(postWorkerEntity.getCity());

            }
        });


    }

    public boolean InputsAreGood() {
        int IsOk = 0;

        boolean[] booleans = new boolean[4];

        //Check if all entries has been completed
        booleans[0] = CheckEmpty();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches())
            booleans[1] = true;
        booleans[2] = CheckPasswordWeak(inputPassword.getText().toString());
        System.out.println(inputPassword.getText().toString());
        booleans[3] = CheckSamePwd(inputPassword.getText().toString(), inputConfirmPassword.getText().toString());

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
        for (TextView in : inputs) {
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

        if (!pwd.equals(ConfPwd)) {
            showError(inputConfirmPassword, "Password are not the Same");
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
        } else {
            System.out.println("\n## The Password is Weak.");
            showError(inputPassword, "Password too weak");
            System.out.println(inputPassword.getText().toString());
            System.out.println(inputConfirmPassword.getText().toString());
        }


        return isWeak;
    }


    /**
     * To Add the Red Info with a message in the field
     */
    public static void showError(TextView input, String s) {
        input.setError(s);
    }
}

