package com.example.mailapp.ui.Fragments;

import static com.example.mailapp.ui.Fragments.MapFragment.readCitiesAuthorizedFromJSON;
import static com.example.mailapp.ui.RegisterActivity.showError;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.mailapp.BaseApplication;
import com.example.mailapp.Enums.Messages;
import com.example.mailapp.Enums.Status;
import com.example.mailapp.R;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.repository.PostworkerRepository;
import com.example.mailapp.util.OnAsyncEventListener;
import com.example.mailapp.viewModel.MailViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


public class MailDetailFragment extends Fragment {
    private static final String TAG = "MailFragment";
    private static final String CENTRAL_ID ="U9w13HzxjbbAHQ2dfMlMADRcbwk2";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd MMMM yyyy");
    private static final String TODAY = DATE_FORMAT.format(Calendar.getInstance().getTime());

    //Management Variables
    private Boolean enableEdit = true;
    private Boolean isEditMode;
    private String idMailChooseFromList;
    private MailEntity currentMail;
    private MailViewModel currentViewModel;
    private String workerConnectedIdStr;
    private View v;

    //UI variables
    private final ArrayList<EditText> editTexts = new ArrayList<>();
    private EditText mailFrom, mailTo, weight, address, zip, city;
    private RadioGroup shipTypeRGroup, mailTypeRGroup;
    private RadioButton letter, packages, amail, bmail, recmail;
    private String mailTypeChoosed, shipTypeChoosed, shipDateStr;
    private TextView  idnumber, idnumTextStr, dueDate;
    private Switch assignedToMe;
    private FloatingActionButton editAddButton, deleteButton, backHomeBtn;

    private FragmentManager fragmentManager;

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
        workerConnectedIdStr = FirebaseAuth.getInstance().getCurrentUser().getUid();
        initialize(v);

        //Take back the id of the mail we Choose on the list
        Bundle data = getArguments();
        boolean putEnable = false;
        if (data != null) {
            putEnable = data.getBoolean("Enable");
            idMailChooseFromList = data.getString("MailID");
        }
        enableEdit(putEnable);
        editAddButton.setImageResource(R.drawable.ic_baseline_edit_24);

        //Decide if we add or edit depending on if we received a valid mail id or not
        if (idMailChooseFromList == null) { //We want to create one
            editAddButton.setImageResource(R.drawable.ic_baseline_add_24);
            deleteButton.hide();
            idnumTextStr.setVisibility(View.INVISIBLE);
            idnumber.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity().getBaseContext(), "Now you can Create a new mail !", Toast.LENGTH_SHORT).show();
            isEditMode = false;
        } else { //We want to edit the one choosed !
            editAddButton.setImageResource(R.drawable.ic_baseline_edit_24);
            deleteButton.show();
            idnumTextStr.setVisibility(View.VISIBLE);
            idnumber.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity().getBaseContext(), "Now you can Edit the mail choosed !", Toast.LENGTH_SHORT).show();
            isEditMode = true;
        }
        fragmentManager = getFragmentManager();
         AtomicReference<String> oldWorkerID = new AtomicReference<>();
        //Take back the mail choosed and display the infos
        MailViewModel.Factory factory2 = new MailViewModel.Factory(
                getActivity().getApplication(),  idMailChooseFromList);
        currentViewModel = new ViewModelProvider(requireActivity(), factory2).get(MailViewModel.class);
        if (isEditMode) {
            currentViewModel.getMail().observe(getActivity(), mailEntity -> {
                if (mailEntity != null) {
                    currentMail = mailEntity;
                    currentMail.setIdMail(idMailChooseFromList);
                   oldWorkerID.set(currentMail.getIdPostWorker());
                    initializeFieldWithMailData(currentMail);
                }
            });
        }

        //Instantiate actions for buttons
        editAddButton.setOnClickListener(v -> changes(oldWorkerID));
        deleteButton.setOnClickListener(view -> deleteMail());
        backHomeBtn.setOnClickListener(view -> BackHome());
        shipTypeRGroup.setOnCheckedChangeListener(new MyOncheckChangeListener(shipTypeRGroup));
        mailTypeRGroup.setOnCheckedChangeListener(new MyOncheckChangeListener(mailTypeRGroup));

        return v;
    }

    private void BackHome() {
        getActivity().getViewModelStore().clear();
        getFragmentManager().beginTransaction()
                .replace(R.id.HomeFrameLayout, new HomeFragment())
                .commit();
    }

    public void initialize(View v) {

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
        idnumTextStr = v.findViewById(R.id.MailIdStrTextView);
        assignedToMe = v.findViewById(R.id.DetailAssignedToSwitch);
        assignedToMe.setChecked(true);
        dueDate = v.findViewById(R.id.DetailShipDateEditText);
        editAddButton = v.findViewById(R.id.DetailEditButton);
        deleteButton = v.findViewById(R.id.DetailDeleteButton);
        backHomeBtn = v.findViewById(R.id.BackHomeButton);
        editAddButton.setImageResource(R.drawable.ic_baseline_edit_24);


    }

    private void changes(AtomicReference<String> oldWorkerID) {
        if (isEditMode) //Update of a given mail

            editMode(oldWorkerID);
        else { //Creation of a new mail
            if (checkEmpty(editTexts, mailTypeRGroup, shipTypeRGroup, packages,
                    dueDate)) {
                Toast.makeText(getActivity().getBaseContext(), Messages.EMPTY_FIELDS.toString(), Toast.LENGTH_SHORT).show();
            } else {
                MailEntity newMail = takeBackInfoIntoMail();
                //Check on City
                if (!readCitiesAuthorizedFromJSON(city.getText().toString(),getActivity())) {
                    city.setError("City doesn't exist in Swiss");
                } else {
                    newMail.setStatus(Status.IN_PROGRESS.toString());
                    newMail.setReceiveDate(TODAY);

                    DatabaseReference reference = FirebaseDatabase.getInstance()
                            .getReference("mails");
                    newMail.setIdMail(reference.push().getKey());
                    assignToAWorker(newMail);

                    currentViewModel.createMail(newMail, new OnAsyncEventListener() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, " Create Mail : success");
                            //Toast.makeText(getActivity(), Messages.MAIL_CREATED.toString(), Toast.LENGTH_LONG).show();
                            replaceFragment(new HomeFragment());
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.d(TAG, " Create Mail : failure", e);
                            Toast.makeText(getActivity(), Messages.MAIL_CREATED_FAILED.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }
    }

    public void editMode(AtomicReference<String> oldWorkerID) {
        if (!enableEdit) {
            if (checkEmpty(editTexts, mailTypeRGroup, shipTypeRGroup, packages,
                    dueDate)) {
                Toast.makeText(getActivity().getBaseContext(), Messages.EMPTY_FIELDS.toString(), Toast.LENGTH_SHORT).show();
            } else {
                enableEdit(false);
                //Save all the modification to the database
                currentMail = takeBackInfoIntoMail();
                //Check on City
                if (!readCitiesAuthorizedFromJSON(city.getText().toString(),getActivity())) {
                    city.setError("City doesn't exist in Swiss");
                } else {
                    reAssignToAWorker(currentMail, String.valueOf(oldWorkerID));
                    currentMail.setStatus(Status.IN_PROGRESS.toString());
                    currentMail.setReceiveDate(TODAY);
                    currentViewModel.updateMail(currentMail, new OnAsyncEventListener() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, " Update Mail: success");
                            editAddButton.setImageResource(R.drawable.ic_baseline_edit_24);
                            enableEdit = true;
                            currentMail = null;
                        }
                        @Override
                        public void onFailure(Exception e) {
                            Log.d(TAG, " Update Mail: failure", e);
                        }
                    });
                    getActivity().getViewModelStore().clear();
                }
            }
        } else {
            editAddButton.setImageResource(R.drawable.ic_baseline_save_24);
            enableEdit(true);
            enableEdit = false;
        }
    }

    private void deleteMail() {
        final MailEntity mail = currentMail;

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setCancelable(true);
        builder.setTitle("Delete Mail ");
        builder.setMessage("Are you sure you want to delete the mail : " + idnumber.getText().toString() + " ? ");
        builder.setPositiveButton(android.R.string.yes, (dialog, which) ->
                currentViewModel.deleteMail(mail, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, " Delete Mail: success");
                        Toast.makeText(getActivity().getBaseContext(), Messages.MAIL_DELETED.toString(), Toast.LENGTH_SHORT).show();
                        //Go back to home fragment
                        getActivity().getViewModelStore().clear();
                        replaceFragment(new HomeFragment());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, " Delete Mail: failure", e);
                    }
                }));
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            //Do Nothing
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
            letter.setEnabled(true);
            packages.setEnabled(true);
            amail.setEnabled(true);
            bmail.setEnabled(true);
            recmail.setEnabled(true);
            assignedToMe.setEnabled(true);
            editAddButton.setImageResource(R.drawable.ic_baseline_save_24);
        } else {
            mailFrom.setEnabled(false);
            mailTo.setEnabled(false);
            address.setEnabled(false);
            zip.setEnabled(false);
            city.setEnabled(false);
            weight.setEnabled(false);
            letter.setEnabled(false);
            packages.setEnabled(false);
            amail.setEnabled(false);
            bmail.setEnabled(false);
            recmail.setEnabled(false);
            assignedToMe.setEnabled(true);
            assignedToMe.setEnabled(false);
            editAddButton.setImageResource(R.drawable.ic_baseline_edit_24);
        }
    }

    private void reAssignToAWorker(MailEntity mail, String oldWorkerID){

        if (assignedToMe.isChecked()) {
            mail.setIdPostWorker(workerConnectedIdStr);
        } else {
            PostworkerRepository repo = ((BaseApplication)getActivity().getApplication()).getPostworkerRepository();

            mail.setIdPostWorker(CENTRAL_ID);

            repo.insertANewMail(CENTRAL_ID, mail.getIdMail(), new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    if (!Objects.equals(oldWorkerID, mail.getIdPostWorker())){
                    repo.removeAMail(workerConnectedIdStr, mail.getIdMail(), new OnAsyncEventListener() {
                        @Override
                        public void onSuccess() {
                            System.out.println(TAG+" : "+"Success ; Removing the mail from the worker : "+workerConnectedIdStr);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            System.out.println(TAG+" : "+"Failed removing the mail from postworker : "+workerConnectedIdStr);
                        }
                    });
                }
                }

                @Override
                public void onFailure(Exception e) {
                    System.out.println(TAG+" : "+"Failed adding the mail to postworker : "+ CENTRAL_ID);
                }
            });


        }
    }

    private void assignToAWorker(MailEntity newMail){
        if (assignedToMe.isChecked()) {
            newMail.setIdPostWorker(workerConnectedIdStr);
        } else {
            PostworkerRepository repo = ((BaseApplication)getActivity().getApplication()).getPostworkerRepository();
            newMail.setIdPostWorker(CENTRAL_ID);

            repo.insertANewMail(CENTRAL_ID, newMail.getIdMail(), new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                   }

                @Override
                public void onFailure(Exception e) {
                    System.out.println(TAG+" : "+"Failed adding the mail to postworker : "+CENTRAL_ID);
                }
            });
        }
    }

//Insert the mail information inside a new mail Entity
    private MailEntity takeBackInfoIntoMail() {
        final MailEntity newMail = new MailEntity();
        newMail.setIdMail(idMailChooseFromList);

        newMail.setMailFrom(mailFrom.getText().toString());
        newMail.setMailTo(mailTo.getText().toString());
        newMail.setMailType(mailTypeChoosed);
        String weightstr = weight.getText().toString();
        if (weightstr.equals("")) {
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

    //initialize info of the mail to display
    @SuppressLint("SetTextI18n")
    private void initializeFieldWithMailData(MailEntity mail) {
        idnumber.setText(mail.getIdMail());
        mailFrom.setText(mail.getMailFrom());
        mailTo.setText(mail.getMailTo());
        switch (mail.getMailType()) {
            case "Letter":
                letter.setChecked(true);
                weight.setEnabled(false);
                break;
            case "Package":
                packages.setChecked(true);
                weight.setText(Integer.toString(mail.getWeight()));
                break;
        }
        switch (mail.getShippingType()) {
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
        dueDate.setText(mail.getShippedDate());
        address.setText(mail.getAddress());
        zip.setText(mail.getZip());
        city.setText(mail.getCity());

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
                c.setTime(Objects.requireNonNull(DATE_FORMAT.parse(TODAY)));
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
            dueDate.setError("Please select a shipping type");
        }
        return IsEmpty > 0;
    }

    //Change fragment
    public void replaceFragment(Fragment newfragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.HomeFrameLayout, newfragment);
        fragmentTransaction.commit();
    }

//Add the icon to notify an error
    public void resetError() {
        for (EditText eText : editTexts) {
            eText.setError(null);
        }
        packages.setError(null);
        recmail.setError(null);
        dueDate.setError(null);
    }

    public static String getTAG() {
        return TAG;
    }

    private class MyOncheckChangeListener implements RadioGroup.OnCheckedChangeListener {
        RadioGroup groupConcerned;

        public MyOncheckChangeListener(RadioGroup groupConcerned) {
            this.groupConcerned = groupConcerned;
        }

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            int id = radioGroup.getCheckedRadioButtonId();
            RadioButton rb = (RadioButton) v.findViewById(id);
            if (radioGroup == mailTypeRGroup) {
                mailTypeChoosed = rb.getText().toString();
                if (mailTypeChoosed.equals("Letter")) {
                    weight.setText("0");
                    weight.setEnabled(false);
                } else
                    weight.setEnabled(true);
            } else {
                shipTypeChoosed = rb.getText().toString();
                shipDateStr = calculateShipDate(shipTypeChoosed);
                dueDate.setText(shipDateStr);
            }
        }
    }
}

