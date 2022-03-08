package com.example.mailapp.database.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.async.postworker.CreatePostWorker;
import com.example.mailapp.database.async.postworker.DeletePostWorker;
import com.example.mailapp.database.async.postworker.UpdatePostWorker;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.util.OnAsyncEventListener;

import java.util.List;

public class PostworkerRepository {

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

    public LiveData<List<PostWorkerEntity>> getAllPostworkers(Context context) {
        return MyDatabase.getInstance(context).postWorkerDao().getAll();
    }

    public LiveData<PostWorkerEntity> getPostworkerById(final int id, Context context) {
        return MyDatabase.getInstance(context).postWorkerDao().getById(id);
    }

    public LiveData<PostWorkerEntity> getPostworkerByEmail(final String email, Context context) {
        return MyDatabase.getInstance(context).postWorkerDao().getByEmail(email);
    }

    public LiveData<PostWorkerEntity> getPostworkerByName(final String firstname, final String lastname, Context context) {
        return MyDatabase.getInstance(context).postWorkerDao().getByName(firstname, lastname);
    }

    public void updateBackgroundSetting(final int idPostWorker, final String background, Context context) {
        MyDatabase.getInstance(context).postWorkerDao().updatePostWorkerBackGround(idPostWorker, background);
    }


    public void insert(final PostWorkerEntity postWorker, OnAsyncEventListener callback, Context context) {
        new CreatePostWorker(context, callback).execute(postWorker);
    }

    public void update(final PostWorkerEntity postWorker, OnAsyncEventListener callback, Context context) {
        new UpdatePostWorker(context, callback).execute(postWorker);
    }

    public void delete(final PostWorkerEntity postWorker, OnAsyncEventListener callback, Context context) {
        new DeletePostWorker(context, callback).execute(postWorker);
    }

}
