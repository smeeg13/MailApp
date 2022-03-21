package com.example.mailapp.ui.Fragments;

import static com.example.mailapp.ui.RegisterActivity.showError;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mailapp.BaseApplication;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.Enums.Messages;
import com.example.mailapp.R;
import com.example.mailapp.database.repository.PostworkerRepository;
import com.example.mailapp.ui.BaseActivity;
import com.example.mailapp.util.OnAsyncEventListener;
import com.example.mailapp.viewModel.MailViewModel;
import com.example.mailapp.viewModel.PostWorkerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MailDetailFragment extends MailFrag {
    private static final String TAG = "MailFragment";

    private FloatingActionButton editAddButton;
    private FloatingActionButton deleteButton;
    private Boolean enableEdit = true;
    private Boolean isEditMode;

    //UI variables
    private ArrayList<EditText> editTexts = new ArrayList<>();
    private EditText mailFrom, mailTo, weight, address, zip, city;
    private RadioGroup shipTypeRGroup, mailTypeRGroup;
    private RadioButton letter, packages, amail, bmail, recmail;
    private String mailTypeChoosed, shipTypeChoosed;
    private TextView idnumber, dueDate, postworkerAssigned;
    private Switch assignedToMe;

    //Management UI
    private MailEntity mail;
    private MailViewModel mailViewModel;
    private PostWorkerEntity postWorkerEntity;
    private PostWorkerViewModel postWorkerViewModel;
    private View v;

    private DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
    private String todayDate = df.format(Calendar.getInstance().getTime());

    String shipDateStr, postworkerAssignedstr;
    int mailChooseInt;

    public MailDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_mail_detail, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        String workerSession = settings.getString(BaseActivity.PREFS_USER, null);

        //Take the entier postworker connected
        PostWorkerViewModel.Factory factory = new PostWorkerViewModel.Factory(getActivity().getApplication(),workerSession );
        postWorkerViewModel = ViewModelProviders.of(getActivity(), factory).get(PostWorkerViewModel.class);
        postWorkerViewModel.getClient().observe(getActivity(), entity -> {
            if (entity != null) {
                postWorkerEntity = entity;
            }
        });

        //Take back the id of the mail we Choosed on the list
        Bundle datas = getArguments();
        if (datas != null)
            mailChooseInt = datas.getInt("mailID");
        else
            mailChooseInt = -1; //By default if want to create one

        initialize(v, mailChooseInt);

        //Instantiate actions for buttons
        editAddButton.setOnClickListener(v -> {
            changes(mailChooseInt);
            //TODO notice the user
        });
        deleteButton.setOnClickListener(view -> deleteMail());


        //Decide if we add or edit depending on if we received a valid mail id or not
        if (mailChooseInt == -1) { //We want to create one
            System.out.println("@@ OPENING CREATE Mail Page");
            editAddButton.setImageResource(R.drawable.ic_baseline_add_24);
            deleteButton.hide();
            idnumber.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity().getBaseContext(), "Now you can Create a new mail !", Toast.LENGTH_LONG).show();
            isEditMode = false;
        } else { //We want to edit the one choosed !
            System.out.println("@@ OPENING EDIT Mail Page");

            editAddButton.setImageResource(R.drawable.ic_baseline_save_24);
            deleteButton.show();
            idnumber.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity().getBaseContext(), "Now you can Edit the mail choosed !", Toast.LENGTH_LONG).show();
            isEditMode = true;
        }

        //Take back the mail choosed and display the infos
        MailViewModel.Factory factory2 = new MailViewModel.Factory(
                getActivity().getApplication(), mailChooseInt);
        mailViewModel = ViewModelProviders.of(getActivity(), factory2).get(MailViewModel.class);
        if (isEditMode) {
            mailViewModel.getMail().observe(getActivity(), mailEntity -> {
                if (mailEntity != null) {
                    mail = mailEntity;
                    idnumber.setText(mail.idMail);
                    int postworkerOfMail = mail.getIdPostWorker();
                    postworkerAssigned.setText(postworkerOfMail);
                    if(postworkerOfMail == -1 ){
                        postworkerAssigned.setText("Centrale");
                        //TODO ASSIGNED to central the mail
                    }
                    //if (postworkerOfMail.equals(postWorkerEntity.email))
                    //    assignedToMe.setChecked(true);
                   // else
                        assignedToMe.setChecked(false);
                    mailFrom.setText(mail.mailFrom);
                    mailTo.setText(mail.mailTo);
                    switch (mail.mailType){
                        case "Letter":
                            letter.setChecked(true);
                            weight.setEnabled(false);
                            break;
                        case "Packages":
                            packages.setChecked(true);
                            weight.setText(mail.weight);
                            break;
                    }
                    switch (mail.shippingType){
                        case "A-Mail":
                            amail.setChecked(true);
                            break;
                        case "B-Mail":
                            bmail.setChecked(true);
                            break;
                        case "Recommended":
                            recmail.setChecked(true);
                            break;
                    }
                    dueDate.setText(mail.shippedDate);
                    address.setText(mail.address);
                    zip.setText(mail.zip);
                    city.setText(mail.city);
                }
            });
        }
        return v;
    }

    private void changes(int mailChooseInt) {
        if (isEditMode) //Update of a given mail
            editMode();
        else { //Creation of a new mail
            MailEntity newMail = new MailEntity();
            //TODO set new data into the new mail
//            newMail.setOwner(owner);
//            newMail.setBalance(0.0d);
//            newMail.setName(mailChooseInt);
//            viewModel.createAccount(newMail, new OnAsyncEventListener() {
//                @Override
//                public void onSuccess() {
//                    Log.d(TAG, "createAccount: success");
//                }
//
//                @Override
//                public void onFailure(Exception e) {
//                    Log.d(TAG, "createAccount: failure", e);
//                }
//            });
        }
    }


    public void initialize(View v, int mailID) {

        mailFrom = v.findViewById(R.id.DetailMailFromEditText);
        editTexts.add(mailFrom);
        mailTo = v.findViewById(R.id.DetailMailToEditText);
        editTexts.add(mailTo);
        address = v.findViewById(R.id.DetailAddressEditText);
        editTexts.add(address);
        zip = v.findViewById(R.id.DetailZipEditText);
        editTexts.add(zip);
        city = v.findViewById(R.id.DetailCityEditText);
        editTexts.add(city);
        weight = v.findViewById(R.id.DetailWeightEditText);
        mailTypeRGroup = v.findViewById(R.id.MailTypeRadioGroup);
        letter = v.findViewById(R.id.DetailLetterRadioBtn);
        packages= v.findViewById(R.id.DetailPackageRadioBtn);
        shipTypeRGroup = v.findViewById(R.id.ShipTypeRadioGroup);
        amail = v.findViewById(R.id.DetailAMailRadioBtn);
        bmail = v.findViewById(R.id.DetailBMailRadioBtn);
        recmail = v.findViewById(R.id.DetailRecomMailRadioBtn);

        idnumber = v.findViewById(R.id.DetailIDTextView);
        assignedToMe= v.findViewById(R.id.DetailAssignedToSwitch);
        postworkerAssigned = v.findViewById(R.id.DetailAssignToTextView);
        dueDate = v.findViewById(R.id.DetailShipDateEditText);

        editAddButton = v.findViewById(R.id.DetailEditButton);
        deleteButton = v.findViewById(R.id.DetailDeleteButton);

        //By default not enable
        enableEdit(false);
    }

    public void editMode() {
        if (enableEdit == true) {
            enableEdit(true);
            enableEdit = false;
            editAddButton.setImageResource(R.drawable.ic_baseline_save_24);
        } else {
            if (checkEmpty(editTexts,mailTypeRGroup, shipTypeRGroup, letter, packages,amail,
                    bmail,  recmail,dueDate)){
                Toast.makeText(getActivity().getBaseContext(), Messages.EMPTY_FIELDS.toString(), Toast.LENGTH_SHORT).show();

            }else {
                enableEdit(false);
                //TODO save all the modification to the database
                mail.setMailFrom(mailFrom.getText().toString());
                mail.setMailTo(mailTo.getText().toString());
             //   mail.setPostWorker(postworkerAssigned.toString());
                //TODO SET THE REST OF MAIL
                mailViewModel.updateMail(mail, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "## update Mail: success");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "## update Mail: failure", e);
                    }
                });
              editAddButton.setImageResource(R.drawable.ic_baseline_edit_24);
              enableEdit = true;
            }
        }
    }

    public void enableEdit(boolean b) {
        if (b == true) {

            mailFrom.setEnabled(true);
            mailTo.setEnabled(true);
            address.setEnabled(true);
            zip.setEnabled(true);
            city.setEnabled(true);
            weight.setEnabled(true);
            mailTypeRGroup.setEnabled(true);
            shipTypeRGroup.setEnabled(true);
            assignedToMe.setEnabled(true);
            postworkerAssigned.setEnabled(true);
            System.out.println("## Is enabled");

            //TODO
          //  mailTypeChoosed = chooseMailType(letter, packages, weight);
          //  shipTypeChoosed = chooseShippingType(amail, bmail, recmail, dueDate);
            calculateShipDate(shipDateStr);

        } else {
            mailFrom.setEnabled(false);
            mailTo.setEnabled(false);
            address.setEnabled(false);
            zip.setEnabled(false);
            city.setEnabled(false);
            weight.setEnabled(false);
            mailTypeRGroup.setEnabled(false);
            shipTypeRGroup.setEnabled(false);
            postworkerAssigned.setEnabled(false);
            assignedToMe.setEnabled(false);
            System.out.println("## Is  NOT enabled");
        }
    }

    private void deleteMail(){

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setCancelable(true);
        builder.setTitle("Deleting Mail ");
        builder.setMessage("Are you sure you want to delete the mail : "+idnumber.getText().toString()+" ? ");
        builder.setPositiveButton(android.R.string.yes,
                (dialog, which) -> {
                    //TODO Delete mail on the DB
                    System.out.println("## Mail Has Been Deleted");
                    Toast.makeText(getActivity().getBaseContext(), Messages.MAIL_DELETED.toString(), Toast.LENGTH_SHORT).show();
                    //Go back to home fragment
                    replaceFragment(new HomeFragment());
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do Nothing
                }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public String getMailTypeChoosed(){
        // get selected radio button from radioGroup
        int selectedId = mailTypeRGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        RadioButton radioButton = (RadioButton) v.findViewById(selectedId);
        return radioButton.getText().toString();
    }
    public String getShipTypeChoosed(){
        // get selected radio button from radioGroup
        int selectedId = shipTypeRGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        RadioButton radioButton = (RadioButton) v.findViewById(selectedId);
        return radioButton.getText().toString();
    }

    /**
     * Used to display the due date according to which shipping type has been choosed
     * @param shipTypeStr
     * @return string for due date
     */
    public String calculateShipDate(String shipTypeStr) {
        String dueDatestr;
        Calendar c = Calendar.getInstance();

        if (shipTypeStr.equals("")){
            shipTypeStr = "B-Mail";
            c.add(Calendar.DATE, 3);
            dueDatestr = df.format(c.getTime());
        }
        else {
             dueDatestr = "";  // Start date
            try {
                c.setTime(df.parse(todayDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            switch (shipTypeStr) {
                case "A-Mail": //A-mail are send in 1 day
                    c.add(Calendar.DATE, 1);  // number of days to add
                    dueDatestr = df.format(c.getTime());  // dt is now the new date
                    break;

                case "B-Mail": //b-mail are send in 3 day
                    c.add(Calendar.DATE, 3);
                    dueDatestr = df.format(c.getTime());
                    break;

                case "Recommended": // rec mail are send in 2
                    c.add(Calendar.DATE, 2);
                    dueDatestr = df.format(c.getTime());
                    break;
            }
        }
        System.out.println("## Shipping Due Date According to ship type : "+ dueDatestr);
        return dueDatestr;
    }

    /**
     * To Check if all the fields have been completed
     * Return true if the 1 input is Empty
     */
    public boolean checkEmpty(ArrayList<EditText> editTexts,RadioGroup mailTypeRGroup,RadioGroup shipTypeRGroup, RadioButton letter, RadioButton packages, RadioButton amail,
                              RadioButton bmail, RadioButton recmail, TextView dueDate) {
        int IsEmpty = 0;

        //For each check if empty
        for (EditText in : editTexts) {
            if (in.getText().toString().isEmpty()) {
                showError(in, "Can not be empty");
                //If empty add 1 to IsEmpty
                IsEmpty++;
            }
        }
        //Check if Mail type has been choosed
        if (mailTypeRGroup.getCheckedRadioButtonId() == -1)
        {
            // no radio buttons are checked
            packages.setError("A least 1 selected !");
            IsEmpty++;
        }
        if (packages.isChecked()){
            if (weight.getText().toString().isEmpty()){
                showError(weight, "Can not be empty");
                IsEmpty++;
            }
        }

        //Check if shipping type has been choosed
        if (shipTypeRGroup.getCheckedRadioButtonId() == -1)
        {
            // no radio buttons are checked
            recmail.setError("A least 1 selected !");
            IsEmpty++;
        }
        if (dueDate.getText().toString().isEmpty() || dueDate.getText().toString().equals(" ")) {
            IsEmpty++;
            //TODO Notice that its this field that is empty
          //  dueDate.setBackgroundResource(R.drawable.red_bg);
            dueDate.setError("Please select a shipping type");
        }
        return IsEmpty > 0;
    }

    public void replaceFragment(Fragment newfragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.HomeFrameLayout, newfragment);
        fragmentTransaction.commit();
    }


}