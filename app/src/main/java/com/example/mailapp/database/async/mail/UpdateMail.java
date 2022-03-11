package com.example.mailapp.database.async.mail;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.example.mailapp.BaseApplication;
import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.util.OnAsyncEventListener;

public class UpdateMail extends AsyncTask<MailEntity, Void,Void> {
    private Application application;
    private OnAsyncEventListener callback;
    private Exception exception;

    public UpdateMail(Application application, OnAsyncEventListener callback){
        this.application = application;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(MailEntity... params) {
        try {
            for (MailEntity mail : params)
                ((BaseApplication)application).getDatabase().mailDao().update(mail);
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
