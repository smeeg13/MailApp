package com.example.mailapp.database.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.util.OnAsyncEventListener;

public class DeleteMail extends AsyncTask<MailEntity, Void, Void> {

    private MyDatabase database;
    private OnAsyncEventListener callback;
    private Exception exception;

    public DeleteMail(Context context, OnAsyncEventListener callback) {
        database = MyDatabase.getInstance(context);
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(MailEntity... params) {
        try {
            for (MailEntity mail : params)
                database.mailDao().delete(mail);
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