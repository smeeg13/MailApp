package com.example.mailapp.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mailapp.Enums.Messages;
import com.example.mailapp.database.dao.MailDao;
import com.example.mailapp.database.dao.PostWorkerDao;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.entities.PostWorkerEntity;

import java.util.concurrent.Executors;

@Database(entities = {PostWorkerEntity.class, MailEntity.class}, version = 5)
public abstract class MyDatabase extends RoomDatabase {
    //TODO For better performance user singleton pattern https://www.youtube.com/watch?v=qO56SL856xc&ab_channel=yoursTRULY
    private static final String DB_NAME = "MyDatabase_DB2";
    private static MyDatabase instance;
    private static final String TAG = "MyDatabase";

    public abstract PostWorkerDao postWorkerDao();

    public abstract MailDao mailDao();

    private final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();

    public static MyDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (MyDatabase.class){
                if (instance == null){
                    instance = buildDatabase(context.getApplicationContext());
                    instance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private static MyDatabase buildDatabase(final Context appcontext){
        Log.i(TAG, Messages.DB_WILL_BE_CREATED.toString());
        return Room.databaseBuilder(appcontext, MyDatabase.class, DB_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            MyDatabase database = MyDatabase.getInstance(appcontext);
                            DatabaseInitializer.populateDatabase(database);
                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();
                        });
                    }
                }).build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DB_NAME).exists()) {
            Log.i(TAG, Messages.DB_CREATED.toString());
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        isDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return isDatabaseCreated;
    }
}
