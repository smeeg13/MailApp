package com.example.mailapp.Fragments;

import static com.example.mailapp.RegisterActivity.showError;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mailapp.DataBase.Dao.MailDao;
import com.example.mailapp.DataBase.Dao.PostWorkerDao;
import com.example.mailapp.DataBase.MyDatabase;
import com.example.mailapp.DataBase.Tables.Mail;
import com.example.mailapp.DataBase.Tables.PostWorker;
import com.example.mailapp.R;
import com.example.mailapp.SessionManagement.SessionManagement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MailDetailFragment extends Fragment {
    private FloatingActionButton EditButton;
    private Boolean aBoolean = true;

    private ArrayList<EditText> editTexts = new ArrayList<>();
    private EditText mailFrom, mailTo, weight, address, zip, city;
    private RadioButton letter, packages, amail, bmail, recmail;
    private String mailType, shipType;
    private TextView idnumber, dueDate;
    private Switch assignedToMe;

    private Mail mail;
    private MailDao mailDao;
    private PostWorker postWorker;
    private PostWorkerDao postWorkerDao;
    private MyDatabase myDatabase;
    private SessionManagement sessionManagement;
    private View v;
    private DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
    private String Todaydate = df.format(Calendar.getInstance().getTime());

    public MailDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_mail_detail, container, false);

        //Take back the id of the mail we want
        getParentFragmentManager().setFragmentResultListener("mailID", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                String idMail = result.getString("mailID");
                mailFrom = v.findViewById(R.id.DetailMailFromEditText);
                mailFrom.setText(idMail);

                //TODO Take back all the info in the DB
            }
        });

        EditButton = v.findViewById(R.id.DetailEditButton);
        sessionManagement = new SessionManagement(this.getContext());
        initialize(v);

        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode();
            }
        });

        return v;
    }

    public void initialize(View v) {

        myDatabase = MyDatabase.getInstance(this.getContext());

        //TODO Take back info of the choosen mail
//        mail = myDatabase.mailDao().getMailByid(idnumber.getText().toString());
        mailFrom = v.findViewById(R.id.DetailMailFromEditText);
        editTexts.add(mailFrom);
        mailTo = v.findViewById(R.id.DetailMailToEditText);
        editTexts.add(mailTo);
        address = v.findViewById(R.id.DetailAddressEditText);
//      address.setText(mail.getAddress());
        editTexts.add(address);
        zip = v.findViewById(R.id.DetailZipEditText);
        editTexts.add(zip);
        city = v.findViewById(R.id.DetailCityEditText);
        editTexts.add(city);
        weight = v.findViewById(R.id.DetailWeightEditText);
        letter = v.findViewById(R.id.DetailLetterRadioBtn);
        packages= v.findViewById(R.id.DetailPackageRadioBtn);
        amail = v.findViewById(R.id.DetailAMailRadioBtn);
        bmail = v.findViewById(R.id.DetailBMailRadioBtn);
        recmail = v.findViewById(R.id.DetailRecomMailRadioBtn);
        assignedToMe= v.findViewById(R.id.DetailAssignedToSwitch);
        dueDate = v.findViewById(R.id.DetailShipDateEditText);

        //By default not enable
        enableEdit(false);
    }

    public void editMode() {

        if (aBoolean == true) {
            enableEdit(true);
            aBoolean = false;
            EditButton.setImageResource(R.drawable.ic_baseline_save_24);
        } else {
            if (checkEmpty()){
                Toast.makeText(getActivity().getBaseContext(), "Check if all fields are completed", Toast.LENGTH_SHORT).show();

            }else {
                enableEdit(false);
                //TODO save all the modification to the database
//            postWorker.setZip(inputZip.getText().toString());
//            postWorker.setRegion(inputLocation.getText().toString());
//            postWorker.setAddress(inputAddress.getText().toString());
//            postWorker.setPhone(inputPhone.getText().toString());
//            myDatabase.postWorkerDao().updatePostWorkerLocation(postWorker.getiD_PostWorker(), postWorker.getRegion(), postWorker.getZip(), postWorker.getAddress(), postWorker.getPhone());
                EditButton.setImageResource(R.drawable.ic_baseline_edit_24);
                aBoolean = true;
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
            letter.setEnabled(true);
            packages.setEnabled(true);
            amail.setEnabled(true);
            bmail.setEnabled(true);
            recmail.setEnabled(true);
            assignedToMe.setEnabled(true);
            System.out.println("## Is enabled");

            chooseMailType();
            chooseShippingType();

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
            assignedToMe.setEnabled(false);
            System.out.println("## Is  NOT enabled");
        }
    }

    /**
     * To Check if all the fields have been completed
     * Return true if the 1 input is Empty
     */
    public boolean checkEmpty() {
        int IsEmpty = 0;

        //For each check if empty
        for (EditText in : editTexts) {
            if (in.getText().toString().isEmpty()) {
                showError(in, "Can not be empty");
                //If empty add 1 to IsEmpty
                IsEmpty++;
            }
        }
        //Check if letter or package has been choosed
        if (!letter.isChecked() && !packages.isChecked()) {
            IsEmpty++;
        }
        //Check if shipping type has been choosed
        if (!amail.isChecked() && !bmail.isChecked() && !recmail.isChecked()) {
            IsEmpty++;
        }

        System.out.println("## One or more fields are empty !");

        return IsEmpty > 0;
    }

    public String chooseMailType(){
        //Selecting letter
        letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (letter.isChecked()){
                    packages.setChecked(false);
                    weight.setEnabled(false);
                    weight.setText("0");
                    mailType = "letter";
                }
            }
        });

        packages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (packages.isChecked()){
                    letter.setChecked(false);
                    weight.setEnabled(true);
                    mailType = "packages";
                }
            }
        });
        return  mailType;
    }

    public String chooseShippingType() {
        amail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bmail.setChecked(false);
                recmail.setChecked(false);
                shipType = "A-Mail";
                dueDate.setText(calculateShipDate());
            }
        });

        bmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amail.setChecked(false);
                recmail.setChecked(false);
                shipType = "B-Mail";
                dueDate.setText(calculateShipDate());
            }
        });
        recmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amail.setChecked(false);
                bmail.setChecked(false);
                shipType = "Recommended";
                dueDate.setText(calculateShipDate());
            }
        });

        return shipType;
    }

    public String calculateShipDate() {
        String dueDatestr = "";  // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(df.parse(Todaydate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        switch (shipType) {
            case "A-Mail":
                //A-mail are send in 1 day
                c.add(Calendar.DATE, 1);  // number of days to add
                dueDatestr = df.format(c.getTime());  // dt is now the new date
                break;

            case "B-Mail":
                //b-mail are send in 3 day
                c.add(Calendar.DATE, 3);
                dueDatestr = df.format(c.getTime());
                break;

            case "Recommended":
                // rec mail are send in 2
                c.add(Calendar.DATE, 2);
                dueDatestr = df.format(c.getTime());
                break;
        }
        return dueDatestr;
    }
}