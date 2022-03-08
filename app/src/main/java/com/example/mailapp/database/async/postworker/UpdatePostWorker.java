package com.example.mailapp.database.async.postworker;

import android.content.Context;
import android.os.AsyncTask;

import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.entities.PostWorkerEntity;
import com.example.mailapp.util.OnAsyncEventListener;

public class UpdatePostWorker extends AsyncTask<PostWorkerEntity, Void,Void> {
    private MyDatabase database;
    private OnAsyncEventListener callback;
    private Exception exception;

    public UpdatePostWorker(Context context, OnAsyncEventListener callback){
        database = MyDatabase.getInstance(context);
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(PostWorkerEntity... params) {
        try {
            for (PostWorkerEntity postWorker : params)
                database.postWorkerDao().update(postWorker);
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
