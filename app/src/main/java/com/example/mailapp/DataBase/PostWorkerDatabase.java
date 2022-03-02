package com.example.mailapp.DataBase;

import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mailapp.DataBase.Dao.MailDao;
import com.example.mailapp.DataBase.Dao.PostWorkerDao;
import com.example.mailapp.DataBase.Tables.Mail;
import com.example.mailapp.DataBase.Tables.PostWorker;

@Database(entities = {PostWorker.class}, exportSchema = false,version = 1)
public abstract class PostWorkerDatabase extends RoomDatabase {
//TODO For better performance user singleton pattern https://www.youtube.com/watch?v=qO56SL856xc&ab_channel=yoursTRULY
    private static final String DB_NAME ="postWorker_DB";
    private static PostWorkerDatabase instance;

    public static synchronized PostWorkerDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), PostWorkerDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

   public abstract PostWorkerDao postWorkerDao();
}
