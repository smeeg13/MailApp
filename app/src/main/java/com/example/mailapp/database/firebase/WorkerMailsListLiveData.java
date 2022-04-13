package com.example.mailapp.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.database.pojo.PostworkerWithMails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WorkerMailsListLiveData extends LiveData<List<PostworkerWithMails>> {

    private static final String TAG = "WorkerMailsLiveData";

    private final DatabaseReference reference;
    private final String worker;
    private final WorkerMailsListLiveData.MyValueEventListener listener =
            new WorkerMailsListLiveData.MyValueEventListener();

    public WorkerMailsListLiveData(DatabaseReference ref, String worker) {
        reference = ref;
        this.worker = worker;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toWorkerWithMailsList(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<PostworkerWithMails> toWorkerWithMailsList(DataSnapshot snapshot) {
        List<PostworkerWithMails> postworkerWithMailsList = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            if (!childSnapshot.getKey().equals(worker)) {
                PostworkerWithMails postworkerWithMails = new PostworkerWithMails();
                postworkerWithMails.postWorker = childSnapshot.getValue(PostWorkerEntity.class);
                postworkerWithMails.postWorker.setIdPostWorker(childSnapshot.getKey());
                postworkerWithMails.mails = toMails(childSnapshot.child("mails"),
                        childSnapshot.getKey());
                postworkerWithMailsList.add(postworkerWithMails);
            }
        }
        return postworkerWithMailsList;
    }

    private List<MailEntity> toMails(DataSnapshot snapshot, String workerId) {
        List<MailEntity> accounts = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            MailEntity entity = childSnapshot.getValue(MailEntity.class);
            entity.setIdMail(childSnapshot.getKey());
            entity.setIdPostWorker(workerId);
            accounts.add(entity);
        }
        return accounts;
    }
}
