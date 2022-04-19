package com.example.mailapp.ui.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mailapp.BaseApplication;
import com.example.mailapp.Enums.Messages;
import com.example.mailapp.Enums.Status;
import com.example.mailapp.R;
import com.example.mailapp.adapter.RecyclerAdapter;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.repository.MailRepository;
import com.example.mailapp.database.repository.PostworkerRepository;
import com.example.mailapp.util.MyAlertDialog;
import com.example.mailapp.util.OnAsyncEventListener;
import com.example.mailapp.util.RecyclerViewItemClickListener;
import com.example.mailapp.viewModel.MailListViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private RecyclerView recyclerView;
    private Button HomeSeeAllMailsBtn;
    private ProgressBar HomeProgressBar;
    private TextView ProgressPercent;
    private MailListViewModel viewModelAllMail;
    private List<MailEntity> mailsInProgress;
    private List<MailEntity> mailsAll;
    private RecyclerAdapter<MailEntity> adapter;
    private MailRepository mailRepository;

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
        mailRepository = ((BaseApplication) getActivity().getApplication()).getMailRepository();

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
                    System.out.println("btn edit clicked ");
                    Bundle datas = new Bundle();
                    datas.putString("MailID", mailsInProgress.get(position).getIdMail());
                    datas.putBoolean("Enable", false);
                    replaceFragment(new MailDetailFragment(), datas);
                } else { //Clicked on done
                    System.out.println("btn clicked done");
                    UpdateStatusMailChoose(position);
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

        MailListViewModel.Factory factory2 = new MailListViewModel.Factory(
                getActivity().getApplication(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        viewModelAllMail = new ViewModelProvider(requireActivity(), factory2).get(MailListViewModel.class);
        viewModelAllMail.getOwnMails().observe(getViewLifecycleOwner(), mailEntities -> {
            if (mailEntities != null) {
                mailsAll = mailEntities;
                int mailAll = mailEntities.size();

                mailsInProgress = mailsAll;
                mailsInProgress.removeIf(mail -> !mail.getStatus().equals("In Progress"));
                int mailInProg = mailsInProgress.size();
                int mailDone = mailAll-mailInProg;
                System.out.println("All mails : " + mailAll);
                System.out.println("Total mail done (progression) : " + mailDone);
                System.out.println("Mail in progress : "+mailInProg);
                updateProgressBar(mailAll, mailInProg, mailDone);
                adapter.setMdata(mailsInProgress);

            }
        });

        HomeSeeAllMailsBtn.setOnClickListener(view -> {
            System.out.println("## go to all mail Frag");
            getFragmentManager().beginTransaction()
                    .replace(R.id.HomeFrameLayout, new AllMailFragment())
                    .commit();
        });
        return v;
    }

    private void UpdateStatusMailChoose(int position) {
        MailEntity currentMail = new MailEntity();
        currentMail.setIdMail(mailsInProgress.get(position).getIdMail());
        currentMail = mailsInProgress.get(position);
        currentMail.setStatus(Status.DONE.toString());

        mailRepository.update(currentMail, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                System.out.println(Messages.MAIL_UPDATED);
            }
            @Override
            public void onFailure(Exception e) {
                System.out.println(Messages.MAIL_UPDATE_FAILED);
            }
        });
    }

    private void updateProgressBar(int all, int inProg, int done) {
        HomeProgressBar.setMax(all);
        HomeProgressBar.setMin(0);
        HomeProgressBar.setProgress(done);
        ProgressPercent.setText(done + " / " + all);
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
        boolean hasBeenDeleted = myAlert.DeleteMail(mail, viewModelAllMail, view);
        if (hasBeenDeleted) {
            //To also remove the mail in the postworker
            PostworkerRepository repo = ((BaseApplication) getActivity().getApplication()).getPostworkerRepository();
            repo.removeAMail(FirebaseAuth.getInstance().getUid(), mail.getIdMail(), new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    System.out.println("Mail Removed of Postworker");
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }
    }

    public static String getTAG() {
        return TAG;
    }
}