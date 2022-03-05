package com.example.mailapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.mailapp.DataBase.Dao.PostWorkerDao;
import com.example.mailapp.DataBase.MyDatabase;
import com.example.mailapp.DataBase.Tables.PostWorker;
import com.example.mailapp.R;
import com.example.mailapp.SessionManagement.SessionManagement;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    PostWorker postWorker;
    PostWorkerDao postWorkerDao;
    MyDatabase myDatabase;
    View v;
    Spinner inputSpinner;
    SessionManagement sessionManagement;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // declaration
        sessionManagement = new SessionManagement(this.getContext());
        myDatabase = MyDatabase.getInstance(this.getContext());
        v = inflater.inflate(R.layout.fragment_settings, container, false);

        //SPINNER
        inputSpinner = v.findViewById(R.id.SettingsSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.locationsAvailables_array, android.R.layout.simple_spinner_dropdown_item);
        inputSpinner.setAdapter(adapter);
        inputSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.println(Log.INFO, "Settings", "SettingsSpinner used -> " + inputSpinner.getSelectedItem() + " selected");
                System.out.println(inputSpinner.getSelectedItem());

                myDatabase.postWorkerDao().updatePostWorkerBackGround(sessionManagement.getSession(),inputSpinner.getSelectedItem().toString());
                PostWorker p = myDatabase.postWorkerDao().getPostWorkerByid(sessionManagement.getSession());
                postWorker.postWorkerToString(postWorker);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return v;
    }
}