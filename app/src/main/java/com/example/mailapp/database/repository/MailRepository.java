package com.example.mailapp.database.repository;

import androidx.lifecycle.LiveData;

import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.firebase.MailListLiveData;
import com.example.mailapp.database.firebase.MailLiveData;
import com.example.mailapp.util.OnAsyncEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MailRepository {


    private static MailRepository instance;

    private MailRepository(){

    }
    public static MailRepository getInstance(){
        if(instance ==null){
            synchronized (PostworkerRepository.class){
                if (instance == null){
                    instance = new MailRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<MailEntity> getMailById(final String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("postworkers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("mails")
                .child(id);
        return new MailLiveData(reference);
    }

    public LiveData<List<MailEntity>> getAllByPostworker(final String idWorker) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("postworker")
                .child(idWorker)
                .child("mails");
        return new MailListLiveData(reference, idWorker);
    }

    //TODO HOW TO get only those in progress ??
    public LiveData<List<MailEntity>> getInProgressByPostworker(final String idWorker,String status){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("postworker")
                .child(idWorker)
                .child("mails");
        return new MailListLiveData(reference, idWorker);
    }

    public void insert(final MailEntity mail, OnAsyncEventListener callback) {
          }

    public void update(final MailEntity mail, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("mails")
                .child(mail.getIdMail())
                .updateChildren(mail.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final MailEntity mail, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("mails")
                .child(mail.getIdMail())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}

