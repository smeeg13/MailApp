package com.example.mailapp.ui.Fragments;
import static com.example.mailapp.ui.RegisterActivity.showError;

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


public class MailDetailFragment extends Fragment {
    private static final String TAG = "MailFragment";
    private static final String CENTRAL_EMAIL = "admin";
    private static final int CENTRAL_ID = 3;
    private static final DateFormat DATE_FORMAT= new SimpleDateFormat("dd MMMM yyyy");
    private static final String TODAY = DATE_FORMAT.format(Calendar.getInstance().getTime());

    //Management Variables
    private Boolean enableEdit = true;
    private Boolean isEditMode;
    private int idMailChoose;
    private MailEntity currentMail;
    private MailViewModel mailViewModel;
    private PostWorkerEntity workerConnected, postWorkerResponsible;
    private String workerCoEmail, workerRespEmail;
    private int workerCoId, workerRespId;

    private PostworkerRepository repository;
    private PostWorkerEntity workerAssigned ;
    private PostWorkerViewModel postWorkerViewModel;
    private View v;
    private PostWorkerEntity centralWorker;
//---------------------------------------------------------
    private String workerConnectedEmailStr;

    //UI variables
    private final ArrayList<EditText> editTexts = new ArrayList<>();
    private EditText mailFrom, mailTo, weight, address, zip, city;
    private RadioGroup shipTypeRGroup, mailTypeRGroup;
    private RadioButton letter, packages, amail, bmail, recmail;
    private String mailTypeChoosed, shipTypeChoosed, shipDateStr;
    private TextView postworkerAssigned,idnumber, dueDate;
    private Switch assignedToMe;
    private FloatingActionButton editAddButton;
    private FloatingActionButton deleteButton;

    public MailDetailFragment() {
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

    //Take back the postworker connected
        SharedPreferences settings = getActivity().getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        workerConnectedEmailStr = settings.getString(BaseActivity.PREFS_USER, null);
        System.out.println("-- User taken back from shared pref : "+ workerConnectedEmailStr);
        initialize(v);

    //Instantiate actions for buttons
        editAddButton.setOnClickListener(v -> changes());
        deleteButton.setOnClickListener(view -> deleteMail());
        shipTypeRGroup.setOnCheckedChangeListener(new MyOncheckChangeListener(shipTypeRGroup));
        mailTypeRGroup.setOnCheckedChangeListener(new MyOncheckChangeListener(mailTypeRGroup));
        assignedToMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //Log.v("Switch assigned to State=", ""+isChecked);
            if (isChecked) {//IF assign to me
                postworkerAssigned.setText(workerConnectedEmailStr);
              //  System.out.println("+++  Put assigned to : "+workerConnectedEmailStr);
            } else {
                postworkerAssigned.setText(CENTRAL_EMAIL);
              //  System.out.println("+++  Put assigned to : "+CENTRAL_EMAIL);
            }
        });

    //Take back the id of the mail we Choose on the list
        Bundle data = getArguments();
        boolean putEnable = false;
        if (data != null) {
            putEnable = data.getBoolean("Enable");
            idMailChoose = data.getInt("mailID");
        }
        enableEdit(putEnable);

    //Decide if we add or edit depending on if we received a valid mail id or not
        if (idMailChoose == -1) { //We want to create one
            System.out.println("## Open Page for creating Mail ");
            editAddButton.setImageResource(R.drawable.ic_baseline_add_24);
            deleteButton.hide();
            idnumber.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity().getBaseContext(), "Now you can Create a new mail !", Toast.LENGTH_SHORT).show();
            isEditMode = false;
        } else { //We want to edit the one choosed !
            System.out.println("## Open Detail of Mail choose from list : " + idMailChoose);
            editAddButton.setImageResource(R.drawable.ic_baseline_save_24);
            deleteButton.show();
            idnumber.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity().getBaseContext(), "Now you can Edit the mail choosed !", Toast.LENGTH_SHORT).show();
            isEditMode = true;
        }

        //Take back the mail choosed and display the infos
        MailViewModel.Factory factory2 = new MailViewModel.Factory(
                getActivity().getApplication(), idMailChoose);
        mailViewModel = ViewModelProviders.of(getActivity(), factory2).get(MailViewModel.class);
        if (isEditMode) {
            mailViewModel.getMail().observe(getActivity(), mailEntity -> {
                if (mailEntity != null) {
                    currentMail = mailEntity;
                    initializeFieldWithMailData(currentMail);
                }
            });
        }
        return v;
    }

    public void initialize(View v) {
        repository = ((BaseApplication) getActivity().getApplication()).getPostworkerRepository();

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
        packages = v.findViewById(R.id.DetailPackageRadioBtn);
        shipTypeRGroup = v.findViewById(R.id.ShipTypeRadioGroup);
        amail = v.findViewById(R.id.DetailAMailRadioBtn);
        bmail = v.findViewById(R.id.DetailBMailRadioBtn);
        recmail = v.findViewById(R.id.DetailRecomMailRadioBtn);
        idnumber = v.findViewById(R.id.DetailIDTextView);
        assignedToMe = v.findViewById(R.id.DetailAssignedToSwitch);
        assignedToMe.setChecked(true);
        postworkerAssigned = v.findViewById(R.id.DetailAssignToTextView);
        postworkerAssigned.setText(workerConnectedEmailStr);
        dueDate = v.findViewById(R.id.DetailShipDateEditText);
        editAddButton = v.findViewById(R.id.DetailEditButton);
        deleteButton = v.findViewById(R.id.DetailDeleteButton);
    }

    private void changes() {
        if (isEditMode) //Update of a given mail
            editMode();
        else { //Creation of a new mail
            if (checkEmpty(editTexts, mailTypeRGroup, shipTypeRGroup,  packages,
                    dueDate)) {
                Toast.makeText(getActivity().getBaseContext(), Messages.EMPTY_FIELDS.toString(), Toast.LENGTH_SHORT).show();
            } else {
                MailEntity newMail = takeBackInfoIntoMail();
                newMail.setStatus("In Progress");
                newMail.setReceiveDate(TODAY);
                mailViewModel.createMail(newMail, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "## Create Mail : success");
                        Toast.makeText(getActivity().getBaseContext(), Messages.MAIL_CREATED.toString(), Toast.LENGTH_LONG).show();
                        replaceFragment(new HomeFragment()); //Go back to home after
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "## Create Mail : failure", e);
                        Toast.makeText(getActivity().getBaseContext(), Messages.MAIL_CREATED_FAILED.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public void editMode() {
        if (!enableEdit) {
            if (checkEmpty(editTexts, mailTypeRGroup, shipTypeRGroup,  packages,
                      dueDate)) {
                Toast.makeText(getActivity().getBaseContext(), Messages.EMPTY_FIELDS.toString(), Toast.LENGTH_SHORT).show();
            } else {
                enableEdit(false);
                //Save all the modification to the database
                currentMail = takeBackInfoIntoMail();
                updateThisMail(currentMail);
                editAddButton.setImageResource(R.drawable.ic_baseline_edit_24);
                enableEdit = true;
            }
        } else {
            enableEdit(true);
            enableEdit = false;
            editAddButton.setImageResource(R.drawable.ic_baseline_save_24);
        }
    }

    private void updateThisMail(MailEntity mail) {
        mailViewModel.updateMail(mail, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "## Update Mail: success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "## Update Mail: failure", e);
            }
        });
    }

    private void deleteMail() {
        final MailEntity mail = currentMail;

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setCancelable(true);
        builder.setTitle("Delete Mail ");
        builder.setMessage("Are you sure you want to delete the mail : " + idnumber.getText().toString() + " ? ");
        builder.setPositiveButton(android.R.string.yes, (dialog, which) ->
                mailViewModel.deleteMail(mail, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "## Delete Mail: success");
                        Toast.makeText(getActivity().getBaseContext(), Messages.MAIL_DELETED.toString(), Toast.LENGTH_SHORT).show();
                        //Go back to home fragment
                        replaceFragment(new HomeFragment());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "## Delete Mail: failure", e);
                    }
                }));
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            //Do Noting
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void enableEdit(boolean isEnable) {
        if (isEnable) {
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


    private MailEntity takeBackInfoIntoMail() {
        MailEntity newMail = new MailEntity();
        if (assignedToMe.isChecked()) {
            //TODO Take back id of postworker Connected  : the right not 1
            newMail.setIdPostWorker(1);
        }
        else{
            //TODO Assign to central  not manually would be better
            newMail.setIdPostWorker(3);
        }

        newMail.setMailFrom(mailFrom.getText().toString());
        newMail.setMailTo(mailTo.getText().toString());
        newMail.setMailType(mailTypeChoosed);
        String weightstr = weight.getText().toString();
        if (weightstr.equals("")){
            weightstr = "0"; //weight by default is 0
        }
        newMail.setWeight(Integer.parseInt(weightstr));
        newMail.setShippingType(shipTypeChoosed);
        newMail.setShippedDate(shipDateStr);
        newMail.setAddress(address.getText().toString());
        newMail.setZip(zip.getText().toString());
        newMail.setCity(city.getText().toString());

        return newMail;
    }

    private void initializeFieldWithMailData(MailEntity mail) {
        idnumber.setText(mail.idMail);
        //TODO take back the name of the worker assign
//        repository.getPostworkerById(mail.idPostWorker, getActivity().getApplication()).observe(getViewLifecycleOwner(), postWorkerEntity1 -> {
//            if (postWorkerEntity1 !=null){
//                postWorkerResponsible = postWorkerEntity1;
//            }
//        });
//        if (postWorkerResponsible.email.equals(workerConnected.email)){
//            assignedToMe.setChecked(true);
//        }
//        postworkerAssigned.setText(postWorkerResponsible.email);
        mailFrom.setText(mail.mailFrom);
        mailTo.setText(mail.mailTo);
        switch (mail.mailType) {
            case "Letter":
                letter.setChecked(true);
                weight.setEnabled(false);
                break;
            case "Package":
                packages.setChecked(true);
                weight.setText(mail.weight);
                break;
        }
        switch (mail.shippingType) {
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
        System.out.println("+++  Initialization of fields with data from mail choosed : "+mail.idMail+"  +++");
    }

    /**
     * Used to display the due date according to which shipping type has been choosed
     *
     * @return string for due date
     */
    private String calculateShipDate(String shipTypeStr) {
        String dueDatestr;
        Calendar c = Calendar.getInstance();

        if (shipTypeStr.equals("")) {
            shipTypeStr = "B-Mail";
            c.add(Calendar.DATE, 3);
            dueDatestr = DATE_FORMAT.format(c.getTime());
        } else {
            dueDatestr = "";  // Start date
            try {
                c.setTime(DATE_FORMAT.parse(TODAY));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            switch (shipTypeStr) {
                case "A-Mail": //A-mail are send in 1 day
                    c.add(Calendar.DATE, 1);  // number of days to add
                    dueDatestr = DATE_FORMAT.format(c.getTime());  // dt is now the new date
                    break;

                case "B-Mail": //b-mail are send in 3 day
                    c.add(Calendar.DATE, 3);
                    dueDatestr = DATE_FORMAT.format(c.getTime());
                    break;

                case "Recommended": // rec mail are send in 2
                    c.add(Calendar.DATE, 2);
                    dueDatestr = DATE_FORMAT.format(c.getTime());
                    break;
            }
        }
        System.out.println("+++ Shipping Due Date : " + dueDatestr+", According to ship type "+shipTypeStr);
        return dueDatestr;
    }

    /**
     * To Check if all the fields have been completed
     * Return true if the 1 input is Empty
     */
    private boolean checkEmpty(ArrayList<EditText> editTexts, RadioGroup mailTypeRGroup, RadioGroup shipTypeRGroup, RadioButton packages,
                               TextView dueDate) {
        int IsEmpty = 0;
        resetError();
        //For each check if empty
        for (EditText in : editTexts) {
            if (in.getText().toString().isEmpty()) {
                showError(in, "Can not be empty");
                //If empty add 1 to IsEmpty
                IsEmpty++;
            }
        }
        //Check if Mail type has been choosed
        if (mailTypeRGroup.getCheckedRadioButtonId() == -1) {
            // no radio buttons are checked
            packages.setError("A least 1 must be selected !");
            IsEmpty++;
        }
        if (packages.isChecked()) {
            if (weight.getText().toString().isEmpty()) {
                showError(weight, "Can not be empty");
                IsEmpty++;
            }
        }

        //Check if shipping type has been choosed
        if (shipTypeRGroup.getCheckedRadioButtonId() == -1) {
            // no radio buttons are checked
            recmail.setError("A least 1 must be selected !");
            IsEmpty++;
        }
        if (dueDate.getText().toString().isEmpty() || dueDate.getText().toString().equals(" ")) {
            IsEmpty++;
            //Notice that its this field that is empty
            //dueDate.setBackgroundResource(R.drawable.red_bg);
            dueDate.setError("Please select a shipping type");
        }
        System.out.println("+++  Check Empty done : "+IsEmpty+" are empty  +++");
        return IsEmpty > 0;
    }

    public void replaceFragment(Fragment newfragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.HomeFrameLayout, newfragment);
        System.out.println("+++  HomeFragment layout replaced with "+newfragment.getTag()+"  +++");
        fragmentTransaction.commit();
    }

    public void retrieveCentralUser(){
        System.out.println("Retrieving Central User");
        repository.getPostworkerByEmail("admin",getActivity().getApplication()).observe(getViewLifecycleOwner(), entity ->{
            if (entity != null){
                centralWorker = entity;
//                CENTRAL_EMAIL = centralWorker.getEmail();
//                CENTRAL_ID = centralWorker.getIdPostWorker();
                System.out.println("+++  Central Account retreive : "+centralWorker.toString()+"  +++");
            }
        });
    }

    public void resetError(){
       for(EditText eText : editTexts){
           eText.setError(null);
       }
       packages.setError(null);
       recmail.setError(null);
       dueDate.setError(null);
    }

    private class MyOncheckChangeListener implements RadioGroup.OnCheckedChangeListener {
        RadioGroup groupConcerned;
        public MyOncheckChangeListener(RadioGroup  groupConcerned) {
            this.groupConcerned = groupConcerned;
        }

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            int id=radioGroup.getCheckedRadioButtonId();
            RadioButton rb=(RadioButton) v.findViewById(id);
            if (radioGroup==mailTypeRGroup){
                System.out.println("-- on check changed on mail type group");
                mailTypeChoosed=rb.getText().toString();
                if (mailTypeChoosed.equals("Letter")) {
                    weight.setText("0");
                    weight.setEnabled(false);
                }
                else
                    weight.setEnabled(true);
                System.out.println("mail type choosed : "+mailTypeChoosed);
            }
            else {
                System.out.println("-- on check changed on Ship type group");
                shipTypeChoosed=rb.getText().toString();
                shipDateStr = calculateShipDate(shipTypeChoosed);
                dueDate.setText(shipDateStr);
                System.out.println("ship type choosed : "+shipTypeChoosed);
            }
        }
    }
}

