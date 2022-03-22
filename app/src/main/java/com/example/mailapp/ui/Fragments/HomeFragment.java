package com.example.mailapp.ui.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mailapp.adapter.RecyclerAdapter;
import com.example.mailapp.database.dao.PostWorkerDao;
import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.R;
import com.example.mailapp.ui.BaseActivity;
import com.example.mailapp.util.RecyclerViewItemClickListener;
import com.example.mailapp.viewModel.MailListViewModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private RecyclerView recyclerView;
    private List<MailEntity> mails;
    private RecyclerAdapter<MailEntity> adapter;
    private MailListViewModel viewModel;
    private String workerConnectedEmailStr;

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

        recyclerView = v.findViewById(R.id.HomeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));//Reverse = true for the last one go on top
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        SharedPreferences settings = getActivity().getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        workerConnectedEmailStr = settings.getString(BaseActivity.PREFS_USER, null);
        mails = new ArrayList<>();
        adapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                Log.d(TAG, "clicked on: " + mails.get(position).getIdMail());

                //TODO Create bundle and send the id of the mail choose to Mail detail frag
                        //To transmit the account choosed
                        //intent.putExtra("accountId", accounts.get(position).getId());
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "longClicked position:" + position);
                Log.d(TAG, "longClicked on: " + mails.get(position).getIdMail());
                //To do multiple selection
            }
        });

        MailListViewModel.Factory factory = new MailListViewModel.Factory(
                getActivity().getApplication(), workerConnectedEmailStr);
        viewModel = ViewModelProviders.of(this, factory).get(MailListViewModel.class);
        viewModel.getOwnMails().observe(getActivity(), mailEntities -> {
            if (mailEntities != null) {
                mails = mailEntities;
                adapter.setData(mails);
            }
        });

        recyclerView.setAdapter(adapter);

        return v;
    }


    public void getMails(){

    }

}