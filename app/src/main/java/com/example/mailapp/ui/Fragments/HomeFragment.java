package com.example.mailapp.ui.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mailapp.Enums.Messages;
import com.example.mailapp.adapter.RecyclerAdapter;
import com.example.mailapp.adapter.RecyclerViewInterface;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.R;
import com.example.mailapp.ui.BaseActivity;
import com.example.mailapp.util.MyAlertDialog;
import com.example.mailapp.util.OnAsyncEventListener;
import com.example.mailapp.util.RecyclerViewItemClickListener;
import com.example.mailapp.viewModel.MailListViewModel;
import com.example.mailapp.viewModel.MailViewModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment  implements RecyclerViewInterface {
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
        String workerConnectedIdStr = settings.getString(BaseActivity.PREFS_ID_USER,null);
        int workerConnectedIdInt = Integer.parseInt(workerConnectedIdStr);
        System.out.println("On create View WorkerConnected : "+workerConnectedEmailStr);
        System.out.println("On create View WorkerConnected ID : "+workerConnectedIdStr);

        mails = new ArrayList<>();
        adapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                Log.d(TAG, "clicked on: " + mails.get(position).getIdMail());

                //TODO Create bundle and send the id of the mail choose to Mail detail frag
                        //To transmit the account choosed

                Bundle datas = new Bundle();
                datas.putInt("MailID", mails.get(position).getIdMail()); //put -1 cause we want to create a new one
                datas.putBoolean("Enable",false);
                replaceFragment(new MailDetailFragment(), datas);
                        //intent.putExtra("accountId", accounts.get(position).getId());
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "longClicked position:" + position);
                Log.d(TAG, "longClicked on: " + mails.get(position).getIdMail());
                //To do multiple selection

                //Delete
                createDeleteDialog(position);

            }
        });
        recyclerView.setAdapter(adapter);

        MailListViewModel.Factory factory = new MailListViewModel.Factory(
                getActivity().getApplication(), workerConnectedIdInt);
        viewModel = ViewModelProviders.of(this, factory).get(MailListViewModel.class);
        viewModel.getOwnMails().observe(getViewLifecycleOwner(), mailEntities -> {
            if (mailEntities != null) {
                mails = mailEntities;
                adapter.setMdata(mails);
            }
        });

        return v;
    }

    private void replaceFragment(MailDetailFragment newfragment, Bundle datas) {
        newfragment.setArguments(datas);
        getFragmentManager().beginTransaction()
                .replace(R.id.HomeFrameLayout, newfragment).commit();
    }

    private void createDeleteDialog(final int position) {
        final MailEntity mail = mails.get(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.row_delete_item, null);

        String msg = "Are you sure you want to delete the mail "+ mail.idMail+" ?";
        final MyAlertDialog myAlert = new MyAlertDialog(getContext(),"Delete Mail",msg,"Yes, Delete");
        myAlert.DeleteMail(mail, viewModel,view);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Delete Mail");
        alertDialog.setCancelable(false);

//        final TextView deleteMessage = view.findViewById(R.id.tv_delete_item);
//        deleteMessage.setText("Are you sure you want to delete the mail "+ mail.idMail+" ?");
//
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes, Delete", (dialog, which) -> {
//            viewModel.deleteMail(mail, new OnAsyncEventListener() {
//                @Override
//                public void onSuccess() {
//                    Log.d(TAG, "deleteAccount: success");
//                }
//
//                @Override
//                public void onFailure(Exception e) {
//                    Log.d(TAG, "deleteAccount: failure", e);
//                }
//            });
//            Toast.makeText(getActivity().getBaseContext(), Messages.MAIL_DELETED.toString(), Toast.LENGTH_LONG).show();
//
//        });
//
//        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> alertDialog.dismiss());
//        alertDialog.setView(view);
//        alertDialog.show();
    }


    public void getMails(){

    }

    public static String getTAG() {
        return TAG;
    }

    @Override
    public void onItemClick(int position) {

    }
}