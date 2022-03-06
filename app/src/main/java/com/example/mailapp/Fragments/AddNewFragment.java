package com.example.mailapp.Fragments;

import static com.example.mailapp.RegisterActivity.showError;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.mailapp.Enums.ToastsMsg;
import com.example.mailapp.R;
import com.example.mailapp.SessionManagement.SessionManagement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class AddNewFragment extends Fragment {

    private ArrayList<EditText> editTexts = new ArrayList<>();

    private Button addmail;
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

    DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
    String Todaydate = df.format(Calendar.getInstance().getTime());

    public AddNewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_add_new, container, false);
        sessionManagement = new SessionManagement(this.getContext());

        initialize(v);
        chooseMailType();
        chooseShippingType();
        addNewMail(v);
        return v;
    }

    private void initialize(View v) {

        myDatabase = MyDatabase.getInstance(this.getContext());

        idnumber = v.findViewById(R.id.AddIDTextView);

        assignedToMe = v.findViewById(R.id.AddAssignedToSwitch);
        mailFrom = v.findViewById(R.id.AddMailFromEditText);
        editTexts.add(mailFrom);
        mailTo = v.findViewById(R.id.AddMailToEditText);
        editTexts.add(mailTo);
        weight = v.findViewById(R.id.AddWeightEditText);
        dueDate = v.findViewById(R.id.AddShipDateEditText);
        address = v.findViewById(R.id.AddAddressEditText);
        editTexts.add(address);
        zip = v.findViewById(R.id.AddZipEditText);
        editTexts.add(zip);
        city = v.findViewById(R.id.AddCityEditText);
        editTexts.add(city);
        letter = v.findViewById(R.id.AddLetterRadioBtn);
        packages = v.findViewById(R.id.AddPackageRadioBtn);
        amail = v.findViewById(R.id.AddAMailRadioBtn);
        bmail = v.findViewById(R.id.AddBMailRadioBtn);
        recmail = v.findViewById(R.id.AddRecomMailRadioBtn);
    }

    public void addNewMail(View v) {
        addmail =(Button) v.findViewById(R.id.AddMailBtn);
        addmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check if every fields are completed
                boolean anyisempty = checkEmpty();

                if (anyisempty) {
                    Toast.makeText(getActivity().getApplicationContext(), ToastsMsg.EMPTY_FIELDS.toString(), Toast.LENGTH_LONG).show();
                } else {
                    //TODO show mail detail page after registered it in DB

//                    mail = new Mail();
//                    mail.setMailFrom(mailFrom.getText().toString());
//                    mail.setMailTo(mailTo.getText().toString());
//                    mail.setMailType(mailType);
//                    mail.setShippingType(shipType);
//                    mail.setAddress(address.getText().toString());
//                    mail.setStatus("In progress");
//                    mail.setReceiveDate(Todaydate);
//                    //  mail.setShippedDate("");

                    if (isAssignedToMe()){
                        //TODO Link with the connected profile
                    }
                    else{
                        //TODO Link to the central
                    }
                    openMailDetail();

                }
            }
        });
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

    private boolean isAssignedToMe() {
        boolean isForMe = false;

        if (assignedToMe.isChecked()) {
            isForMe = true;
            //TODO Assign this mail to the post worker connected
        }
        return isForMe;
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
        System.out.println("## Mail type choosed : "+ mailType);

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
        System.out.println("## Shipping type choosed : "+ shipType);
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

        System.out.println("## Shipping Due Date According to ship type : "+ dueDatestr);

        return dueDatestr;
    }

    private void openMailDetail(){
        String mailfromstr = mailFrom.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("mailID", String.valueOf(mailfromstr));
        getParentFragmentManager().setFragmentResult("mailID", bundle);
        replaceFragment(new MailDetailFragment());
        System.out.println("## Go to Mail Detail");
        System.out.println("## mail from send : "+ mailfromstr);

    }

    private void replaceFragment(Fragment newfragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.HomeFrameLayout, newfragment);
        fragmentTransaction.commit();
    }

}