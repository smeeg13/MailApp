package com.example.mailapp.Fragments;

import static com.example.mailapp.R.*;

import static com.example.mailapp.RegisterActivity.showError;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mailapp.DataBase.Dao.MailDao;
import com.example.mailapp.DataBase.Dao.PostWorkerDao;
import com.example.mailapp.DataBase.MyDatabase;
import com.example.mailapp.DataBase.Tables.Mail;
import com.example.mailapp.DataBase.Tables.PostWorker;
import com.example.mailapp.R;
import com.example.mailapp.SessionManagement.SessionManagement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public abstract class MailFrag extends Fragment{
    private final ArrayList<EditText> editTexts = new ArrayList<>();
    private EditText mailFrom, mailTo, weight, address, zip, city;
    private RadioButton letter, packages, amail, bmail, recmail;
    private String mailType;
    private TextView idnumber, dueDate;
    private Switch assignedToMe;

    private Mail mail;
    private MailDao mailDao;
    private PostWorker postWorker;
    private PostWorkerDao postWorkerDao;
    private MyDatabase myDatabase;
    private SessionManagement sessionManagement;
    private View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View coreFragmentView = inflater.inflate(layout.mail_frag, container, false);

       loadLayoutFromResIdToViewStub(coreFragmentView, container);
        initialize(coreFragmentView);
        return coreFragmentView;
    }

    protected abstract void loadLayoutFromResIdToViewStub(View coreFragmentView, ViewGroup container);

    public MailFrag() {
        // Required empty public constructor
    }

    public void initialize(View v){
        mailFrom = v.findViewById(id.MailMailFromEditText);
        mailTo = v.findViewById(id.MailMailToEditText);
        weight = v.findViewById(id.MailWeightEditText);
        address = v.findViewById(id.MailAddressEditText);
        zip = v.findViewById(id.MailZipEditText);
        city = v.findViewById(id.MailCityEditText);
        letter = v.findViewById(id.MailLetterRadioBtn);
        packages = v.findViewById(id.MailPackageRadioBtn);
        amail = v.findViewById(id.MailAMailRadioBtn);
        bmail = v.findViewById(id.MailBMailRadioBtn);
        recmail = v.findViewById(id.MailRecomMailRadioBtn);

        idnumber= v.findViewById(id.MailIDTextView);
        dueDate=v.findViewById(id.DetailShipDateEditText);
        assignedToMe = v.findViewById(id.MailAssignedToSwitch);
    }

    /**
     * To Check if all the fields have been completed
     * Return true if the 1 input is Empty
     */
    public boolean checkEmpty(ArrayList<EditText> editTexts, RadioButton letter, RadioButton packages, RadioButton amail,
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
        //Check if letter or package has been choosed
        if (!letter.isChecked() && !packages.isChecked())
            IsEmpty++;

        //Check if shipping type has been choosed
        if (!amail.isChecked() && !bmail.isChecked() && !recmail.isChecked())
            IsEmpty++;

        if (dueDate.getText().toString().isEmpty() || dueDate.getText().toString().equals(" ")) {
            IsEmpty++;
            //TODO Notice that its this field that is empty
            dueDate.setBackgroundResource(R.drawable.red_bg);
        }

        System.out.println("## One or more fields are empty !");
        return IsEmpty > 0;
    }

    public String chooseMailType(RadioButton letter, RadioButton packages, EditText weight){
        final String[] mailType = new String[1];
        //Selecting letter
        letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (letter.isChecked()){
                    packages.setChecked(false);
                    weight.setEnabled(false);
                    weight.setText("0");
                    mailType[0] = "letter";
                }
            }
        });

        packages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (packages.isChecked()){
                    letter.setChecked(false);
                    weight.setEnabled(true);
                    mailType[0] = "packages";
                }
            }
        });
        return mailType[0];
    }

    public String chooseShippingType(RadioButton amail, RadioButton bmail, RadioButton recmail, TextView dueDate) {
        final String[] shipType = new String[1];
        amail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bmail.setChecked(false);
                recmail.setChecked(false);
                shipType[0] = "A-Mail";
                calculateShipDate(shipType[0],dueDate);
            }
        });

        bmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amail.setChecked(false);
                recmail.setChecked(false);
                shipType[0] = "B-Mail";
                calculateShipDate(shipType[0],dueDate);
            }
        });
        recmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amail.setChecked(false);
                bmail.setChecked(false);
                shipType[0] = "Recommended";
                calculateShipDate(shipType[0],dueDate);
            }
        });

        return shipType[0];
    }

    public void calculateShipDate(String shipType,TextView dueDate) {
        DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String Todaydate = df.format(Calendar.getInstance().getTime());

        String dueDatestr = "";  // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(df.parse(Todaydate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if ("A-Mail".equals(shipType)) {//A-mail are send in 1 day
            c.add(Calendar.DATE, 1);  // number of days to add
            dueDatestr = df.format(c.getTime());  // dt is now the new date
        } else if ("B-Mail".equals(shipType)) {//b-mail are send in 3 day
            c.add(Calendar.DATE, 3);
            dueDatestr = df.format(c.getTime());
        } else if ("Recommended".equals(shipType)) {// rec mail are send in 2
            c.add(Calendar.DATE, 2);
            dueDatestr = df.format(c.getTime());
        }
        dueDate.setText(dueDatestr);
        dueDate.setBackgroundResource(drawable.white_bg);
    }

    public void replaceFragment(Fragment newfragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id.HomeFrameLayout, newfragment);
        fragmentTransaction.commit();
    }
}

