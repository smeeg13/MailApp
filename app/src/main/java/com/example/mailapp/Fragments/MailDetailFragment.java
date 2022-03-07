package com.example.mailapp.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.example.mailapp.DataBase.Entities.Mail;
import com.example.mailapp.DataBase.Entities.PostWorker;
import com.example.mailapp.Enums.ToastsMsg;
import com.example.mailapp.R;
import com.example.mailapp.SessionManagement.SessionManagement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MailDetailFragment extends MailFrag {
    private FloatingActionButton EditButton;
    private FloatingActionButton DeleteButton;

    private Boolean aBoolean = true;

    private ArrayList<EditText> editTexts = new ArrayList<>();
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
    private DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
    private String Todaydate = df.format(Calendar.getInstance().getTime());
    TextView shipType ;
    String shipTypeStr ;

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
        DeleteButton = v.findViewById(R.id.DetailDeleteButton);
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMail();
            }
        });


        return v;
    }

    @Override
    protected void loadLayoutFromResIdToViewStub(View coreFragmentView, ViewGroup container) {

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

        shipType = v.findViewById(R.id.DetailShipDateEditText);
         shipTypeStr = shipType.getText().toString() ;

        //By default not enable
        enableEdit(false);
    }

    public void editMode() {

        if (aBoolean == true) {
            enableEdit(true);
            aBoolean = false;
            EditButton.setImageResource(R.drawable.ic_baseline_save_24);
        } else {
            if (checkEmpty(editTexts, letter, packages,amail,
                    bmail,  recmail,dueDate)){
                Toast.makeText(getActivity().getBaseContext(), ToastsMsg.EMPTY_FIELDS.toString(), Toast.LENGTH_SHORT).show();

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

            mailType = chooseMailType(letter, packages, weight);
            String shipType = chooseShippingType(amail, bmail, recmail, dueDate);
            calculateShipDate(shipTypeStr);

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

    private void deleteMail(){

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setCancelable(true);
        builder.setTitle("Deleting Mail ");
        builder.setMessage("Are you sure you want to delete it ? ");
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO Delete mail on the DB
                        System.out.println("## Mail Has Been Deleted");
                        Toast.makeText(getActivity().getBaseContext(), ToastsMsg.MAIL_DELETED.toString(), Toast.LENGTH_SHORT).show();
                        //Go back to home fragment
                        replaceFragment(new HomeFragment());
                    }
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

    public void calculateShipDate(String shipTypeStr) {
        shipTypeStr = shipType.getText().toString();
        if (shipTypeStr.equals("")){
            shipTypeStr = "B-Mail";
            bmail.setChecked(true);
            amail.setChecked(false);
            recmail.setChecked(false);
        }

        String dueDatestr = "";  // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(df.parse(Todaydate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        switch (shipTypeStr) {
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
}