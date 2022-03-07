package com.example.mailapp.database.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.async.CreateMail;
import com.example.mailapp.database.async.CreatePostWorker;
import com.example.mailapp.database.async.DeleteMail;
import com.example.mailapp.database.async.DeletePostWorker;
import com.example.mailapp.database.async.UpdateMail;
import com.example.mailapp.database.async.UpdatePostWorker;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.entities.PostWorkerEntity;

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

    public LiveData<List<MailEntity>> getAllMails(Context context) {
        return MyDatabase.getInstance(context).mailDao().getAll();
    }

    public LiveData<List<MailEntity>> getAllByStatus(final String status, Context context) {
        return MyDatabase.getInstance(context).mailDao().getAllByStatus(status);
    }

    public LiveData<List<MailEntity>> getAllByMailType(final String mailType, Context context) {
        return MyDatabase.getInstance(context).mailDao().getAllByMailType(mailType);
    }

    public LiveData<List<MailEntity>> getAllByCity(final String city, Context context) {
        return MyDatabase.getInstance(context).mailDao().getAllByCity(city);
    }

    public LiveData<MailEntity> getMailById(final int id, Context context) {
        return MyDatabase.getInstance(context).mailDao().getById(id);
    }


    public void insert(final MailEntity mail, OnAsyncEventListener callback, Context context) {
        new CreateMail(context, callback).execute(mail);
    }

    public void update(final MailEntity mail, OnAsyncEventListener callback, Context context) {
        new UpdateMail(context, callback).execute(mail);
    }

    public void delete(final MailEntity mail, OnAsyncEventListener callback, Context context) {
        new DeleteMail(context, callback).execute(mail);
    }

}

