package com.example.mailapp.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mailapp.BaseApplication;
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

    public LiveData<List<PostWorkerEntity>> getAllPostworkers(Application application) {
        return ((BaseApplication) application).getDatabase().postWorkerDao().getAll();
    }

    public LiveData<PostWorkerEntity> getPostworkerById(final int id, Application application) {
        return ((BaseApplication) application).getDatabase().postWorkerDao().getById(id);
    }


    public LiveData<PostWorkerEntity> getPostworkerByEmail(final String email, Application application) {
        return ((BaseApplication) application).getDatabase().postWorkerDao().getByEmail(email);

    }

    public LiveData<PostWorkerEntity> getPostworkerByName(final String firstname, final String lastname, Application application) {
        return ((BaseApplication) application).getDatabase().postWorkerDao().getByName(firstname,lastname);
    }

    public void updatePostWorkerBackground(final String background, final String email){
        updatePostWorkerBackground(background,email);
    }

    public void insert(final PostWorkerEntity postWorker, OnAsyncEventListener callback, Application application) {
        new CreatePostWorker(application, callback).execute(postWorker);
    }

    public void update(final PostWorkerEntity postWorker, OnAsyncEventListener callback,Application application) {
        new UpdatePostWorker(application, callback).execute(postWorker);
    }

    public void delete(final PostWorkerEntity postWorker, OnAsyncEventListener callback, Application application) {
        new DeletePostWorker(application, callback).execute(postWorker);
    }

}
