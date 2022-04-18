package com.example.mailapp.ui.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mailapp.BaseApplication;
import com.example.mailapp.Enums.Messages;
import com.example.mailapp.Enums.Status;
import com.example.mailapp.R;
import com.example.mailapp.adapter.RecyclerAdapterForAll;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.repository.MailRepository;
import com.example.mailapp.database.repository.PostworkerRepository;
import com.example.mailapp.util.MyAlertDialog;
import com.example.mailapp.util.OnAsyncEventListener;
import com.example.mailapp.util.RecyclerViewItemClickListener;
import com.example.mailapp.viewModel.MailListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;

public class AllMailFragment extends Fragment {

    private static final String TAG = "AllMailFragment";

    private List<MailEntity> mailsAll;
    private RecyclerAdapterForAll<MailEntity> adapter;
    private MailListViewModel viewModelAllMail;
    private MailRepository mailRepository;


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
        mailRepository = ((BaseApplication) getActivity().getApplication()).getMailRepository();
        FloatingActionButton backHome = v.findViewById(R.id.backHomeButtonFromAll);
        RecyclerView recyclerView = v.findViewById(R.id.AllRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));//Reverse = true for the last one go on top

        mailsAll = new ArrayList<>();
        adapter = new RecyclerAdapterForAll<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(String todo, View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                Log.d(TAG, "clicked on: " + mailsAll.get(position).getIdMail());

                if (todo.equals("edit")) { //Clicked on edit
                    System.out.println("btn clicked edit");
                    Bundle datas = new Bundle();
                    datas.putString("MailID", mailsAll.get(position).getIdMail());
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
                Log.d(TAG, "longClicked on: " + mailsAll.get(position).getIdMail());
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
                adapter.setMdata(mailsAll);
            }
        });

        backHome.setOnClickListener(view -> {
            getActivity().getViewModelStore().clear();
            System.out.println("Arrow Back Home Clicked");
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.HomeFrameLayout, new HomeFragment())
                        .commit();
            }
        });

        return v;
    }
    private void UpdateStatusMailChoose(int position) {
        MailEntity currentMail = new MailEntity();
        currentMail.setIdMail(mailsAll.get(position).getIdMail());
        currentMail = mailsAll.get(position);
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
                "ID of the Mail : " + mail.getIdMail() + separator + "Are you sure ?";
        final MyAlertDialog myAlert = new MyAlertDialog(getContext(), "Delete Mail", msg, "Yes, Delete");
        boolean hasBeenDeleted = myAlert.DeleteMail(mail, viewModelAllMail, view);
        if (hasBeenDeleted){
            PostworkerRepository repo = ((BaseApplication)getActivity().getApplication()).getPostworkerRepository();
            repo.removeAMail(FirebaseAuth.getInstance().getUid(), mail.getIdMail(), new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    System.out.println(Messages.MAIL_DELETED);
                }

                @Override
                public void onFailure(Exception e) {
                    System.out.println(Messages.MAIL_DELETED_FAILED);
                }
            });
        }

    }

    public static String getTAG() {
        return TAG;
    }

}