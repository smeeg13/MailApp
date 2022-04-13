package com.example.mailapp;

import android.app.Application;

import com.example.mailapp.database.repository.MailRepository;
import com.example.mailapp.database.repository.PostworkerRepository;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

 
    public PostworkerRepository getPostworkerRepository() {
        return PostworkerRepository.getInstance();
    }

    public MailRepository getMailRepository() {
        return MailRepository.getInstance();
    }

}
