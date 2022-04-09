package com.example.mailapp.ui.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mailapp.R;
import com.example.mailapp.adapter.RecyclerAdapter;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.util.MyAlertDialog;
import com.example.mailapp.util.OnAsyncEventListener;
import com.example.mailapp.util.RecyclerViewItemClickListener;
import com.example.mailapp.viewModel.MailListViewModel;
import com.example.mailapp.viewModel.MailViewModel;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private RecyclerView recyclerView;
    private Button HomeSeeAllMailsBtn;
    private ProgressBar HomeProgressBar;
    private TextView ProgressPercent;
    private List<MailEntity> mailsInProgress;
    private List<MailEntity> mailsAll;
    private RecyclerAdapter<MailEntity> adapter;
    private MailListViewModel viewModel;

    private MailListViewModel mailViewModel;
    private MailViewModel currentViewModel;
    private MailEntity currentMail;
    private MailListViewModel viewModelAllMail;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        ProgressPercent = v.findViewById(R.id.ProgressPercent);
        HomeProgressBar = v.findViewById(R.id.HomeProgressBar);
        HomeSeeAllMailsBtn = v.findViewById(R.id.HomeSeeAllMailsBtn);
        recyclerView = v.findViewById(R.id.HomeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));//Reverse = true for the last one go on top


        System.out.println("On create View WorkerConnected : " + FirebaseAuth.getInstance().getCurrentUser().getUid());

        mailsInProgress = new ArrayList<>();
        adapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(String todo, View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                Log.d(TAG, "clicked on: " + mailsInProgress.get(position).getIdMail());

                if (todo.equals("edit")) { //Clicked on edit
                    System.out.println("btn clicked edit");
                    Bundle datas = new Bundle();
                    datas.putString("MailID", mailsInProgress.get(position).getIdMail());
                    datas.putBoolean("Enable", false);
                    replaceFragment(new MailDetailFragment(), datas);
                } else { //Clicked on done
                    System.out.println("btn clicked done");
                    //Take back the mail choosed and display the infos
                    MailViewModel.Factory factory2 = new MailViewModel.Factory(
                            getActivity().getApplication(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid()
                    );

                    currentViewModel =new ViewModelProvider(requireActivity(), factory2).get(MailViewModel.class);

                    currentViewModel.getMail().observe(getActivity(), mailEntity -> {
                        if (mailEntity != null) {
                            currentMail = mailEntity;
                            currentMail.setStatus("Done");
                        }
                    });

                    currentViewModel.updateMail(currentMail, new OnAsyncEventListener() {
                        @Override
                        public void onSuccess() {
                            System.out.println("Status Update : success for mail ID : " + mailsInProgress.get(position).getIdMail());
                        }

                        @Override
                        public void onFailure(Exception e) {
                            System.out.println("Status NOT Update : FAILURE ERROR for mail ID : " + mailsInProgress.get(position).getIdMail());
                            System.out.println(e);
                        }
                    });

                }
            }

            //Long click on more btn
            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "longClicked position:" + position);
                Log.d(TAG, "longClicked on: " + mailsInProgress.get(position).getIdMail());
                //Delete
                createDeleteDialog(position);
            }
        });
        recyclerView.setAdapter(adapter);

         AtomicInteger sizeMailInProg = new AtomicInteger(-1);
         AtomicInteger sizeAll = new AtomicInteger(-1);


        MailListViewModel.Factory factory2 = new MailListViewModel.Factory(
                getActivity().getApplication(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        viewModelAllMail = new ViewModelProvider(requireActivity(), factory2).get(MailListViewModel.class);
        viewModelAllMail.getOwnMails().observe(getViewLifecycleOwner(), mailEntities -> {
            if (mailEntities != null) {
                mailsAll = mailEntities;
                sizeAll.set(mailsAll.size());
                updateProgressBar(mailsAll.size(),sizeMailInProg.get());
            }
        });

        //Get back own mails NOT DONE
        MailListViewModel.Factory factory = new MailListViewModel.Factory(
                getActivity().getApplication(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        viewModel = new ViewModelProvider(requireActivity(), factory).get(MailListViewModel.class);
        viewModel.getOwnMailsInProgress().observe(getViewLifecycleOwner(), mailEntities -> {
            if (mailEntities != null) {
                mailsInProgress = mailEntities;
                sizeMailInProg.set(mailsInProgress.size());
                adapter.setMdata(mailsInProgress);

                updateProgressBar(sizeAll.get(),mailsInProgress.size());
            }
        });

        HomeSeeAllMailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("## go to all mail Frag");
                getFragmentManager().beginTransaction()
                        .replace(R.id.HomeFrameLayout, new AllMailFragment())
                        .commit();
            }
        });

        return v;
    }

    private void updateProgressBar(int All, int inProg) {
        HomeProgressBar.setMax(All);
        System.out.println("Total mail : "+ All);
        HomeProgressBar.setMin(0);
        HomeProgressBar.setProgress(All-inProg);
        System.out.println("Todo mail : "+ All);
        System.out.println("Total mail done (progression) : "+ HomeProgressBar.getProgress());
        ProgressPercent.setText(HomeProgressBar.getProgress()+" / "+All);
    }

    private void replaceFragment(MailDetailFragment newfragment, Bundle datas) {
        newfragment.setArguments(datas);
        getFragmentManager().beginTransaction()
                .replace(R.id.HomeFrameLayout, newfragment)
                .commit();
    }

    private void createDeleteDialog(final int position) {
        final MailEntity mail = mailsInProgress.get(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.row_delete_item, null);
        String separator = System.lineSeparator();
        String msg = "You're going to delete a mail : " + separator +
                "ID of the Mail : " + mail.getIdMail() + separator + "Are you sure ?";
        final MyAlertDialog myAlert = new MyAlertDialog(getContext(), "Delete Mail", msg, "Yes, Delete");
        myAlert.DeleteMail(mail, viewModel, view);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Delete Mail");
        alertDialog.setCancelable(false);
    }

    public static String getTAG() {
        return TAG;
    }

}