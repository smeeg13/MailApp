package com.example.mailapp.database.async;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;

import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.dao.MailDao;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.util.OnAsyncEventListener;

public class CreateMail extends AsyncTask<MailEntity, Void, Void> {

    private MyDatabase database;
    private OnAsyncEventListener callback;
    private Exception exception;

    public CreateMail(Context context, OnAsyncEventListener callback) {
        database = MyDatabase.getInstance(context);
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(MailEntity... params) {
        try {
            for (MailEntity mail : params)
                database.mailDao().insert(mail);
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
