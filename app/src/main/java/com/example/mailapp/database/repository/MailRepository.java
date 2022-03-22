package com.example.mailapp.database.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.mailapp.BaseApplication;
import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.async.mail.CreateMail;
import com.example.mailapp.database.async.mail.DeleteMail;
import com.example.mailapp.database.async.mail.UpdateMail;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.util.OnAsyncEventListener;

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

    public LiveData<List<MailEntity>> getAllMails(Application application) {
        return ((BaseApplication) application).getDatabase().mailDao().getAll();
    }

    public LiveData<List<MailEntity>> getAllByStatus(final String status, Application application) {
        return ((BaseApplication) application).getDatabase().mailDao().getAllByStatus(status);
    }

    public LiveData<List<MailEntity>> getAllByMailType(final String mailType, Application application) {
        return ((BaseApplication) application).getDatabase().mailDao().getAllByMailType(mailType);
    }

    public LiveData<List<MailEntity>> getAllByCity(final String city, Application application) {
        return ((BaseApplication) application).getDatabase().mailDao().getAllByCity(city);
    }

    public LiveData<MailEntity> getMailById(final int id, Application application) {
        return ((BaseApplication) application).getDatabase().mailDao().getById(id);
    }

    public LiveData<List<MailEntity>> getAllByPostworker(final String emailWorker, Application application) {
        return ((BaseApplication) application).getDatabase().mailDao().getAllByPostworker(emailWorker);
    }

    public void insert(final MailEntity mail, OnAsyncEventListener callback, Application a) {
        new CreateMail(a, callback).execute(mail);
    }

    public void update(final MailEntity mail, OnAsyncEventListener callback, Application a) {
        new UpdateMail(a, callback).execute(mail);
    }

    public void delete(final MailEntity mail, OnAsyncEventListener callback, Application a) {
        new DeleteMail(a, callback).execute(mail);
    }
}

