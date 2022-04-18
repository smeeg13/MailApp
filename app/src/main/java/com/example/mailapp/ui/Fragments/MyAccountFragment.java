package com.example.mailapp.ui.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.mailapp.BaseApplication;
import com.example.mailapp.Enums.Messages;
import com.example.mailapp.R;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.database.repository.MailRepository;
import com.example.mailapp.database.repository.PostworkerRepository;
import com.example.mailapp.ui.LoginActivity;
import com.example.mailapp.util.OnAsyncEventListener;
import com.example.mailapp.viewModel.PostWorkerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class MyAccountFragment extends Fragment {

    private static final String TAG = "AccountDetails";
    private TextView inputEmail, inputFirstnameAndLastname, inputPhone, inputZip, inputLocation, inputPassword, inputConfirmPassword, inputTitle, inputAddress;
    private FloatingActionButton inputfloatingEditButton;
    private Button inputDeleteButton;
    private Boolean aBoolean = true;
    private View v;
    private String firstname, lastname;
    private ArrayList<TextView> inputs = new ArrayList<>();

    private String idWorkerConnected;
    private static final String ID_CENTRALE = "U9w13HzxjbbAHQ2dfMlMADRcbwk2";
    private PostWorkerEntity currentWorker;
    private MailRepository mailRepository;
    private PostWorkerViewModel currentWorkerViewModel;
    private boolean IsCentrale = false;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_account, container, false);
        inputfloatingEditButton = v.findViewById(R.id.AccountEditButton);
        idWorkerConnected = FirebaseAuth.getInstance().getUid();
        initialize(v);

        inputfloatingEditButton.setOnClickListener(v -> editMode());
        inputDeleteButton.setOnClickListener(v -> {
            deleteAccount();
        });

        return v;
    }


    public void initialize(View v) {
        mailRepository = ((BaseApplication) getActivity().getApplication()).getMailRepository();
        PostworkerRepository postworkerRepository = ((BaseApplication) getActivity().getApplication()).getPostworkerRepository();
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

        PostWorkerViewModel.Factory factory = new PostWorkerViewModel.Factory(getActivity().getApplication(),
                FirebaseAuth.getInstance().getCurrentUser().getUid());
        currentWorkerViewModel = new ViewModelProvider(requireActivity(), factory).get(PostWorkerViewModel.class);

        currentWorkerViewModel.getWorker().observe(getActivity(), postWorker -> {
            if (postWorker != null) {
                currentWorker = postWorker;
                firstname = postWorker.getFirstname();
                lastname = postWorker.getLastname();
                inputFirstnameAndLastname.setText(new StringBuilder().append(firstname).append(" ").append(lastname).toString());
                inputEmail.setText(postWorker.getEmail());
                inputPassword.setText(postWorker.getPassword());
                inputConfirmPassword.setText(postWorker.getPassword());
                inputPhone.setText(postWorker.getPhone());
                inputAddress.setText(postWorker.getAddress());
                inputZip.setText(postWorker.getZip());
                inputLocation.setText(postWorker.getCity());
                if (currentWorker.getEmail().equals("centrale@poste.ch")) {
                    inputDeleteButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void editMode() {

        if (aBoolean) {
            enableEdit(true);
            aBoolean = false;
            inputfloatingEditButton.setImageResource(R.drawable.ic_baseline_save_24);
        } else {
            if (!InputsAreGood()) {
                Toast.makeText(getContext(), Messages.INVALID_FIELDS.toString(), Toast.LENGTH_SHORT).show();
            } else {
                enableEdit(false);
                currentWorker.setPassword(inputPassword.getText().toString());
                currentWorker.setEmail(inputEmail.getText().toString());
                currentWorker.setZip(inputZip.getText().toString());
                currentWorker.setCity(inputLocation.getText().toString());
                currentWorker.setAddress(inputAddress.getText().toString());
                currentWorker.setPhone(inputPhone.getText().toString());

                inputfloatingEditButton.setImageResource(R.drawable.ic_baseline_edit_24);
                aBoolean = true;

                currentWorkerViewModel.updatePostWorker(currentWorker, new OnAsyncEventListener() {
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


    public void enableEdit(boolean b) {
        // sub method
        if (b) {
            inputPhone.setEnabled(true);
            inputAddress.setEnabled(true);
            inputZip.setEnabled(true);
            inputLocation.setEnabled(true);
            inputPassword.setEnabled(true);
            inputConfirmPassword.setVisibility(View.VISIBLE);
            inputConfirmPassword.setEnabled(true);
        } else {
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

        AlertDialog.Builder ab = new AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom);
        ab.setTitle("Confirmation of Delete");
        ab.setMessage("You will be delete your account. All the mail assigned to you will be transfered to the central. Are you sure ?");
        ab.setPositiveButton("Yes", (dialog, which) -> {

            //get list of the postWorker mails
            ArrayList<MailEntity> mails = new ArrayList<MailEntity>();
            mailRepository.getAllByPostworker(FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(getActivity(), mailEntities -> {

                if (mailEntities.size()!=0) {
                    for (MailEntity mail : mailEntities) {
                        //Delete mail in the post worker co
                        currentWorkerViewModel.removeAMail(idWorkerConnected, mail.getIdMail(), new OnAsyncEventListener() {
                            @Override
                            public void onSuccess() {
                                System.out.println("mail " + mail.getIdMail() + " has been delete from worker : " + idWorkerConnected);
                                //Add it to the centrale
                                currentWorkerViewModel.insertANewMail(ID_CENTRALE, mail.getIdMail(), new OnAsyncEventListener() {
                                    @Override
                                    public void onSuccess() {
                                        System.out.println("mail " + mail.getIdMail() + " has been ADDED from worker : " + ID_CENTRALE);
                                        deleteWorker();
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        System.out.println("add failed");
                                        System.out.println("Error : " + e);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Exception e) {
                                System.out.println("delete failed");
                                System.out.println("Error : " + e);
                            }
                        });
                    }
                }
                else {
                    deleteWorker();
                }
            });
        });
        ab.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
        ab.show();

    }

    private void deleteWorker() {
        //Delete of the postworker connected
        currentWorkerViewModel.deleteWorker(currentWorker, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                System.out.println("delete of the auth user");
                assert user != null;
                user.delete().addOnCompleteListener(task -> {
                    System.out.println(Messages.ACCOUNT_DELETED);
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    startActivity(intent);
                });
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println(Messages.ACCOUNT_DELETED_FAILED);
                System.out.println("Error : " + e);
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
        for (TextView in : inputs) {
            if (in.getText().toString().isEmpty()) {
                showError(in, getString(R.string.empty_field));
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
            showError(inputConfirmPassword, getString(R.string.pwd_not_same));
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

        if (matches) {
            isWeak = false;
            System.out.println(getString(R.string.pwd_strong));
        } else {
            isWeak = true;
            System.out.println(getString(R.string.pwd_weak));
            showError(inputPassword, String.valueOf(R.string.pwd_weak));
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

