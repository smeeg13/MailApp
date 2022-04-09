package com.example.mailapp.ui.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.mailapp.R;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.ui.BaseActivity;

public class SettingsFragment extends Fragment {

    private PostWorkerEntity postWorkerEntity;

    private View v;
    private Spinner inputSpinner;
    private SharedPreferences settings;
    private SharedPreferences sharedPreferences;
    private String sharedPrefMail, sharedPrefBackground = null;
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
        settings = getActivity().getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        sharedPrefBackground = settings.getString(BaseActivity.PREFS_BACKGROUND, null);
        editor = getActivity().getSharedPreferences(BaseActivity.PREFS_NAME, 0).edit();



        System.out.println("YOUR PREFERED COLOR IS : " + sharedPrefBackground);

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
                System.out.println("black");
            }
        });

        return v;
    }

    private void changebackground(String color) {
        if (buttonBlack.isChecked()) {
            editor.putString(BaseActivity.PREFS_BACKGROUND, "black");
            editor.apply();
            sharedPrefBackground =  settings.getString(BaseActivity.PREFS_BACKGROUND, null);
            System.out.println("editor : black -> "+sharedPrefBackground);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (buttonWhite.isChecked()) {

            editor.putString(BaseActivity.PREFS_BACKGROUND, "white");
            editor.apply();
            sharedPrefBackground =  settings.getString(BaseActivity.PREFS_BACKGROUND, null);
            System.out.println("editor : white -> "+ sharedPrefBackground);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    private void initialize(View v) {
        buttonBlack = v.findViewById(R.id.SettingsBlackRadio);
        buttonWhite = v.findViewById(R.id.SettingsWhiteRadio);
        radioGroup = v.findViewById(R.id.SettingsRadioGroup);

        if (sharedPrefBackground == null){
            System.out.println("the background is null");

        }else{
            if (sharedPrefBackground.equals("white")) {
                buttonWhite.setChecked(true);
                buttonBlack.setChecked(false);
            } else {
                buttonWhite.setChecked(false);
                buttonBlack.setChecked(true);
            }
        }

    }


}

