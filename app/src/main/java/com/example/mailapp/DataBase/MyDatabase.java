package com.example.mailapp.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mailapp.DataBase.Dao.MailDao;
import com.example.mailapp.DataBase.Dao.PostWorkerDao;
import com.example.mailapp.DataBase.Entities.Mail;
import com.example.mailapp.DataBase.Entities.PostWorker;

@Database(entities = {PostWorker.class, Mail.class}, version = 5)
public abstract class MyDatabase extends RoomDatabase {
    //TODO For better performance user singleton pattern https://www.youtube.com/watch?v=qO56SL856xc&ab_channel=yoursTRULY
    private static final String DB_NAME = "MyDatabase_DB";
    private static MyDatabase instance;


    public static synchronized MyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build();

        }
        return instance;
    }

    public abstract PostWorkerDao postWorkerDao();

    public abstract MailDao mailDao();
}
