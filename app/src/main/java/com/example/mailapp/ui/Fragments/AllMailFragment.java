package com.example.mailapp.ui.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mailapp.R;
import com.example.mailapp.adapter.RecyclerAdapter;
import com.example.mailapp.adapter.RecyclerAdapterForAll;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.ui.BaseActivity;
import com.example.mailapp.util.MyAlertDialog;
import com.example.mailapp.util.OnAsyncEventListener;
import com.example.mailapp.util.RecyclerViewItemClickListener;
import com.example.mailapp.viewModel.MailListViewModel;
import com.example.mailapp.viewModel.MailViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AllMailFragment extends Fragment {

    private static final String TAG = "AllMailFragment";

    private FloatingActionButton backHome;
    private RecyclerView recyclerView;

    private List<MailEntity> mailsAll;
    private RecyclerAdapterForAll<MailEntity> adapter;
    private String workerConnectedEmailStr;
    private MailViewModel currentViewModel;
    private MailEntity currentMail;
    private MailListViewModel viewModelAllMail;

    public AllMailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_mail, container, false);

        backHome = v.findViewById(R.id.backHomeButtonFromAll);
        recyclerView = v.findViewById(R.id.AllRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));//Reverse = true for the last one go on top

        SharedPreferences settings = getActivity().getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        workerConnectedEmailStr = settings.getString(BaseActivity.PREFS_USER, null);
        String workerConnectedIdStr = settings.getString(BaseActivity.PREFS_ID_USER, null);
        int workerConnectedIdInt = Integer.parseInt(workerConnectedIdStr);
        System.out.println("On create View WorkerConnected : " + workerConnectedEmailStr);
        System.out.println("On create View WorkerConnected ID : " + workerConnectedIdStr);

        mailsAll = new ArrayList<>();
        adapter = new RecyclerAdapterForAll<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(String todo, View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                Log.d(TAG, "clicked on: " + mailsAll.get(position).getIdMail());

                if (todo.equals("edit")) { //Clicked on edit
                    System.out.println("btn clicked edit");
                    Bundle datas = new Bundle();
                    datas.putInt("MailID", mailsAll.get(position).getIdMail());
                    datas.putBoolean("Enable", false);
                    replaceFragment(new MailDetailFragment(), datas);
                } else { //Clicked on done
                    System.out.println("btn clicked done");
//                    //TODO Update the status of the mail choosed to done


                    //Take back the mail choosed and display the infos
                    MailViewModel.Factory factory2 = new MailViewModel.Factory(
                            getActivity().getApplication(), mailsAll.get(position).getIdMail());
                    currentViewModel = ViewModelProviders.of(getActivity(), factory2).get(MailViewModel.class);

                    currentViewModel.getMail().observe(getActivity(), mailEntity -> {
                        if (mailEntity != null) {
                            currentMail = mailEntity;
                        }
                    });

                    currentMail.setStatus("Done");

                    viewModelAllMail.updateMail(currentMail, new OnAsyncEventListener() {
                        @Override
                        public void onSuccess() {
                            System.out.println("Status Update : success for mail ID : " + mailsAll.get(position).getIdMail());
                        }

                        @Override
                        public void onFailure(Exception e) {
                            System.out.println("Status NOT Update : FAILURE ERROR for mail ID : " + mailsAll.get(position).getIdMail());
                            System.out.println(e);
                        }
                    });

                }
            }

            //Long click on more btn
            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "longClicked position:" + position);
                Log.d(TAG, "longClicked on: " + mailsAll.get(position).getIdMail());
                //Delete
                createDeleteDialog(position);
            }
        });
        recyclerView.setAdapter(adapter);



        MailListViewModel.Factory factory2 = new MailListViewModel.Factory(
                getActivity().getApplication(), workerConnectedIdInt);
        viewModelAllMail = ViewModelProviders.of(this, factory2).get(MailListViewModel.class);
        viewModelAllMail.getOwnMails().observe(getViewLifecycleOwner(), mailEntities -> {
            if (mailEntities != null) {
                mailsAll = mailEntities;
                adapter.setMdata(mailsAll);
            }
        });

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getViewModelStore().clear();
                System.out.println("Arrow Back Home Clicked");
                getFragmentManager().beginTransaction()
                        .replace(R.id.HomeFrameLayout, new HomeFragment())
                        .commit();
            }
        });

        return v;
    }


    private void replaceFragment(MailDetailFragment newfragment, Bundle datas) {
        newfragment.setArguments(datas);
        getFragmentManager().beginTransaction()
                .replace(R.id.HomeFrameLayout, newfragment)
                .commit();
    }

    private void createDeleteDialog(final int position) {
        final MailEntity mail = mailsAll.get(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.row_delete_item, null);
        String separator = System.lineSeparator();
        String msg = "You're going to delete a mail : " + separator +
                "ID of the Mail : " + mail.idMail + separator + "Are you sure ?";
        final MyAlertDialog myAlert = new MyAlertDialog(getContext(), "Delete Mail", msg, "Yes, Delete");
        myAlert.DeleteMail(mail, viewModelAllMail, view);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Delete Mail");
        alertDialog.setCancelable(false);
    }


    public void getMails() {

    }

    public static String getTAG() {
        return TAG;
    }

}