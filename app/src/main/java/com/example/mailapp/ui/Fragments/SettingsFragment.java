package com.example.mailapp.ui.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mailapp.Enums.Messages;
import com.example.mailapp.R;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.ui.BaseActivity;
import com.example.mailapp.util.OnAsyncEventListener;
import com.example.mailapp.viewModel.PostWorkerViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private View v;
    private SharedPreferences settings;
    private String sharedPrefBackground = null;
    private RadioButton buttonWhite;
    private RadioButton buttonBlack;
    private SharedPreferences.Editor editor;
    private RadioGroup radioGroup;
    private String workerConnected;


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
        sharedPrefBackground = settings.getString(BaseActivity.PREFS_BACKGROUND, "white");
        editor = settings.edit();
        workerConnected = FirebaseAuth.getInstance().getCurrentUser().getUid();
        System.out.println("YOUR PREFERED COLOR IS : " + sharedPrefBackground);
        initialize(v);

        buttonWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String color = "white";
                changeBackground(color);
            }
        });
        buttonBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String color = "black";
                changeBackground(color);
                System.out.println("black");
            }
        });

        return v;
    }

    private void changeBackground(String color) {
        //Take back user
        PostWorkerViewModel.Factory factory = new PostWorkerViewModel.Factory(getActivity().getApplication(), workerConnected);
        PostWorkerViewModel currentWorkerViewModel = new ViewModelProvider(this, factory).get(PostWorkerViewModel.class);
        currentWorkerViewModel.getWorker().observe(this, postWorker -> {
            if (postWorker != null) {
                PostWorkerEntity currentWorker = postWorker;

                if (buttonBlack.isChecked()) {
                    editor.putString(BaseActivity.PREFS_BACKGROUND, "black");
                    editor.apply();
                    sharedPrefBackground = settings.getString(BaseActivity.PREFS_BACKGROUND, null);
                    System.out.println("editor : black -> " + sharedPrefBackground);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    currentWorker.setBackground(color);
                    saveIntoDB(currentWorkerViewModel, currentWorker);
                } else if (buttonWhite.isChecked()) {
                    editor.putString(BaseActivity.PREFS_BACKGROUND, "white");
                    editor.apply();
                    sharedPrefBackground = settings.getString(BaseActivity.PREFS_BACKGROUND, null);
                    System.out.println("editor : white -> " + sharedPrefBackground);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    currentWorker.setBackground(color);
                    saveIntoDB(currentWorkerViewModel, currentWorker);
                }
            }
        });
    }

    private void saveIntoDB(PostWorkerViewModel currentWorkerViewModel, PostWorkerEntity currentWorker) {
        //Save into db
        currentWorkerViewModel.updatePostWorker(currentWorker, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                System.out.println(Messages.BACKGROUND_UPDATED + " for " + currentWorker.getEmail());
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println(Messages.BACKGROUND_UPDATED_FAILED + " for " + currentWorker.getEmail());
                System.out.println("Error : " + e);
            }
        });
    }

    private void initialize(View v) {
        buttonBlack = v.findViewById(R.id.SettingsBlackRadio);
        buttonWhite = v.findViewById(R.id.SettingsWhiteRadio);
        radioGroup = v.findViewById(R.id.SettingsRadioGroup);

        if (sharedPrefBackground == null) {
            System.out.println("the background is null");
        } else {
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

