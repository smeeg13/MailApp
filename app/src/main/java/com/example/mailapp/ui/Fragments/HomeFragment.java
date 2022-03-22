package com.example.mailapp.ui.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mailapp.database.dao.PostWorkerDao;
import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.R;


public class HomeFragment extends Fragment {
    TextView inputConnectedAs;
    PostWorkerEntity postWorkerEntity;
    PostWorkerDao postWorkerDao;
    MyDatabase myDatabase;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // myDatabase = MyDatabase.getInstance(this.getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
      //  inputConnectedAs = v.findViewById(R.id.HomeConnectedAsTextView);

        return v;
    }

}