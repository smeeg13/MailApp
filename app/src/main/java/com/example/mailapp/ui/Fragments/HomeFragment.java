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

import com.example.mailapp.adapter.RecyclerAdapter;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.R;
import com.example.mailapp.ui.BaseActivity;
import com.example.mailapp.util.MyAlertDialog;
import com.example.mailapp.util.RecyclerViewItemClickListener;
import com.example.mailapp.viewModel.MailListViewModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment  {
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

        SharedPreferences settings = getActivity().getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        workerConnectedEmailStr = settings.getString(BaseActivity.PREFS_USER, null);
        String workerConnectedIdStr = settings.getString(BaseActivity.PREFS_ID_USER,null);
        int workerConnectedIdInt = Integer.parseInt(workerConnectedIdStr);
        System.out.println("On create View WorkerConnected : "+workerConnectedEmailStr);
        System.out.println("On create View WorkerConnected ID : "+workerConnectedIdStr);

        mails = new ArrayList<>();
        adapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(String todo,View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                Log.d(TAG, "clicked on: " + mails.get(position).getIdMail());

                if (todo.equals("edit")){ //Clicked on edit
                    System.out.println("btn clicked edit");
                    //TODO Create bundle and send the id of the mail choose to Mail detail frag

                Bundle datas = new Bundle();
                datas.putInt("MailID", mails.get(position).getIdMail());
                datas.putBoolean("Enable",false);
                replaceFragment(new MailDetailFragment(), datas);
//                        //intent.putExtra("accountId", accounts.get(position).getId());
                }
                else { //Clicked on done
                    System.out.println("btn clicked done");
                    //TODO Update the status of the mail choosed to done

                }
            }

            //Long click on more btn
            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "longClicked position:" + position);
                Log.d(TAG, "longClicked on: " + mails.get(position).getIdMail());
                //Delete
                createDeleteDialog(position);
            }
        });
        recyclerView.setAdapter(adapter);

        //TODO Get back own mails NOT DONE and not take all own mails
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
                .replace(R.id.HomeFrameLayout, newfragment)
                .commit();
    }

    private void createDeleteDialog(final int position) {
        final MailEntity mail = mails.get(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.row_delete_item, null);
        String separator = System.lineSeparator();
        String msg = "You're going to delete a mail : "+separator+
                        "ID of the Mail : "+mail.idMail+separator+"Are you sure ?";
        final MyAlertDialog myAlert = new MyAlertDialog(getContext(),"Delete Mail",msg,"Yes, Delete");
        myAlert.DeleteMail(mail, viewModel,view);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Delete Mail");
        alertDialog.setCancelable(false);
    }


    public void getMails(){

    }

    public static String getTAG() {
        return TAG;
    }

}