package com.example.mailapp.Fragments;

import static com.example.mailapp.RegisterActivity.showError;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
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


public class AddNewFragment extends MailFrag {

    private ArrayList<EditText> editTexts = new ArrayList<>();

    private Button addmail;
    private EditText mailFrom, mailTo, weight, address, zip, city;
    private RadioButton letter, packages, amail, bmail, recmail;
    private String mailType, shipType = "B-Mail";
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
        chooseMailType(letter, packages, weight);
        chooseShippingType(amail, bmail, recmail, dueDate);
        bmail.setChecked(true);
        calculateShipDate(shipType);
        addNewMail(v);
        return v;
    }

    @Override
    protected void loadLayoutFromResIdToViewStub(View coreFragmentView, ViewGroup container) {

    }

    public void initialize(View v) {

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
                boolean anyisempty = checkEmpty(editTexts, letter, packages, amail, bmail, recmail,dueDate);

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


    private boolean isAssignedToMe() {
        boolean isForMe = false;

        if (assignedToMe.isChecked()) {
            isForMe = true;
            //TODO Assign this mail to the post worker connected
        }
        return isForMe;
    }

    public void calculateShipDate(String shipType) {
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
        dueDate.setText(dueDatestr);
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

}