package com.example.mailapp.ui.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.mailapp.database.dao.PostWorkerDao;
import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.R;

import com.example.mailapp.ui.BaseActivity;

public class SettingsFragment extends Fragment {

    private PostWorkerEntity postWorkerEntity;
    private PostWorkerDao postWorkerDao;
    private MyDatabase myDatabase;
    private View v;
    private Spinner inputSpinner;
    private SharedPreferences settings;
    private SharedPreferences sharedPreferences;
    private String sharedPrefMail, sharedPrefBackground;
    private RadioButton buttonWhite;
    private RadioButton buttonBlack;
    private SharedPreferences.Editor editor;
    private RadioGroup radioGroup;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_settings, container, false);
        sharedPrefMail = settings.getString(BaseActivity.PREFS_USER, null);
        sharedPrefBackground = settings.getString(BaseActivity.PREFS_BACKGROUND, null);
        settings = getActivity().getSharedPreferences(BaseActivity.PREFS_NAME, 0);

        System.out.println("YOUR PREFERED COLOR IS : "+sharedPrefBackground);

        initialize(v);

        buttonWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String color = "white";
               changebackground(color);
            }
        });
        buttonBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String color = "black";
                changebackground(color);
                System.out.println("Black");
            }
        });


        return v;
    }

    private void changebackground(String color) {
        if (buttonBlack.isChecked()) {
            editor = getActivity().getSharedPreferences(BaseActivity.PREFS_NAME, 0).edit();
            editor.putString(BaseActivity.PREFS_BACKGROUND, color);
            editor.apply();
            System.out.println("editor : black");
        } else if (buttonWhite.isChecked()) {
            editor = getActivity().getSharedPreferences(BaseActivity.PREFS_NAME, 0).edit();
            editor.putString(BaseActivity.PREFS_BACKGROUND, color);
            editor.apply();
            System.out.println("editor : white");
        }

    }

    private void initialize(View v) {
        buttonBlack = v.findViewById(R.id.SettingsBlackRadio);
        buttonWhite = v.findViewById(R.id.SettingsWhiteRadio);
        radioGroup = v.findViewById(R.id.SettingsRadioGroup);
        if (sharedPrefBackground == null) {
            sharedPrefBackground = "white";
        } else {

            if (sharedPrefBackground.equals("white")) {
                buttonWhite.setChecked(true);

            } else {
                buttonBlack.setChecked(false);
            }
        }


    }



}

