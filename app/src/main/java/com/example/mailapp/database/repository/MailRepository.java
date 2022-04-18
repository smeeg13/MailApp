package com.example.mailapp.database.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.mailapp.Enums.Messages;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.firebase.MailListLiveData;
import com.example.mailapp.database.firebase.MailLiveData;
import com.example.mailapp.util.OnAsyncEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MailRepository {
    private static final String TAG = "MailRepository";
    private static MailRepository instance;

    private MailRepository() {

    }

    public static MailRepository getInstance() {
        if (instance == null) {
            synchronized (PostworkerRepository.class) {
                if (instance == null) {
                    instance = new MailRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<MailEntity> getMailById(final String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("mails")
                .child(id);
        return new MailLiveData(reference);
    }

    public LiveData<List<MailEntity>> getAllByPostworker(final String idWorker) {
        ArrayList<String> idsmailsofWorker = new ArrayList<>();
        DatabaseReference mailReference = FirebaseDatabase.getInstance()
                .getReference("mails");

        DatabaseReference workerReference = FirebaseDatabase.getInstance()
                .getReference("postworkers")
                .child(idWorker)
                .child("mails");
        workerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    idsmailsofWorker.add(childDataSnapshot.getKey());
                } //take back the key for the node Log.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return new MailListLiveData(mailReference, idsmailsofWorker, idWorker);
    }

    public void insert(final MailEntity mail, OnAsyncEventListener callback) {

        //Ajout dans mail en générant id du mail
        FirebaseDatabase.getInstance().getReference("mails")
                .child(mail.getIdMail())
                .setValue(mail, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                        Log.d(TAG, Messages.MAIL_CREATED.toString());
                    }
                });
        FirebaseDatabase.getInstance()
                .getReference("postworkers")
                .child(mail.getIdPostWorker())
                .child("mails")
                .child(mail.getIdMail())
                .setValue(mail.getIdMail(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                        Log.d(TAG, "mail add to postworker : " + mail.getIdPostWorker());
                    }
                });
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
        //Delete de l'id mail dans postworker
        FirebaseDatabase.getInstance()
                .getReference("postworkers")
                .child(FirebaseAuth.getInstance().getUid())
                .child("mails")
                .child(mail.getIdMail())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });

        //Delete du mail
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

