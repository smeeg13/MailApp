package com.example.mailapp.database.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.util.OnAsyncEventListener;

import java.security.Policy;

public class CreatePostWorker extends AsyncTask<PostWorkerEntity, Void, Void> {

    private MyDatabase database;
    private OnAsyncEventListener callback;
    private Exception exception;

    public CreatePostWorker(Context context, OnAsyncEventListener callback) {
        database = MyDatabase.getInstance(context);
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(PostWorkerEntity... params) {
        try {
            for (PostWorkerEntity postworker : params)
                database.postWorkerDao().insert(postworker);
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (callback != null) {
            if (exception == null) {
                callback.onSuccess();
            } else {
                callback.onFailure(exception);
            }
        }
    }
}