package com.example.mailapp.database.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.database.firebase.PostWorkerLiveData;
import com.example.mailapp.util.OnAsyncEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostworkerRepository {
    private static final String TAG = "PostWorkerRepository";

    private static PostworkerRepository instance;

    private PostworkerRepository(){

    }
    public static PostworkerRepository getInstance(){
        if(instance ==null){
            synchronized (PostworkerRepository.class){
                if (instance == null){
                    instance = new PostworkerRepository();
                }
            }
        }
        return instance;
    }


    public void signIn(final String email, final String password,
                       final OnCompleteListener<AuthResult> listener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public LiveData<PostWorkerEntity> getPostWorker(final String workerId) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("postworkers")
                .child(workerId);
        return new PostWorkerLiveData(reference);
    }

    public void register(final PostWorkerEntity workerEntity, final OnAsyncEventListener callback) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                workerEntity.getEmail(),
                workerEntity.getPassword()
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                workerEntity.setIdPostWorker(FirebaseAuth.getInstance().getCurrentUser().getUid());
                insert(workerEntity, callback);
            } else {
                callback.onFailure(task.getException());
            }
        });
    }


    public void insert(final PostWorkerEntity postWorker, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("postworkers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(postWorker, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                        FirebaseAuth.getInstance().getCurrentUser().delete()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        callback.onFailure(null);
                                        Log.d(TAG, "Rollback successful: User account deleted");
                                    } else {
                                        callback.onFailure(task.getException());
                                        Log.d(TAG, "Rollback failed: signInWithEmail:failure",
                                                task.getException());
                                    }
                                });
                    } else {
                        callback.onSuccess();
                    }
                });    }

    public void update(final PostWorkerEntity postWorker, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("postworkers")
                .child(postWorker.getIdPostWorker())
                .updateChildren(postWorker.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
        FirebaseAuth.getInstance().getCurrentUser().updatePassword(postWorker.getPassword())
                .addOnFailureListener(
                        e -> Log.d(TAG, "updatePassword failure!", e)
                );    }

    public void delete(final PostWorkerEntity postWorker, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("postworkers")
                .child(postWorker.getIdPostWorker())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

}
