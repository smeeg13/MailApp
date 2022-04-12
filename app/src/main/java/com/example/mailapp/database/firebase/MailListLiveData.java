package com.example.mailapp.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.mailapp.database.entities.MailEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MailListLiveData extends LiveData<List<MailEntity>> {

    private static final String TAG = "MailListLiveData";

    private final DatabaseReference mailreference;
    private final ArrayList<String> mailsofworker;
    private final String worker;
    private final MyValueEventListener listener = new MyValueEventListener();

    public MailListLiveData(DatabaseReference mailreference,ArrayList<String> workeridsMails, String idWorker) {
        this.mailreference = mailreference;
        this.worker = idWorker;
        this.mailsofworker = workeridsMails;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        mailreference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");

    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toMails(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + mailreference, databaseError.toException());
        }
    }

    private List<MailEntity> toMails(DataSnapshot snapshot) {
        List<MailEntity> mails = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            boolean isAssignedTo = false;
            for (String s : mailsofworker){
                if (s.equals(childSnapshot.getKey())) {
                    isAssignedTo = true;
                    System.out.println(" ||| MAIL : " + childSnapshot.getKey());
                    System.out.println(" ||| IS ASSIGN TO ME");
                }
            }

            if (isAssignedTo) {
                MailEntity entity = childSnapshot.getValue(MailEntity.class);
                //Take bake the right mail
                entity.setIdMail(childSnapshot.getKey());
                entity.setIdPostWorker(worker);
                mails.add(entity);
            }
        }
        return mails;
    }
}
