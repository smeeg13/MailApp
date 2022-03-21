package com.example.mailapp.database;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mailapp.Enums.Messages;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.database.entities.PostWorkerEntity;

public class DatabaseInitializer {

    public static final String TAG = "DatabaseInitializer";

    public static void populateDatabase(final MyDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
        Log.i(TAG, Messages.INSERT_DEMO_DATA.toString());

    }

    private static void addPostWorker(final MyDatabase db, final String email, final String firstname,
                                      final String lastname, final String password) {
        PostWorkerEntity client = new PostWorkerEntity(email, firstname, lastname, password);
        db.postWorkerDao().insert(client);
    }

    private static void addMail(final MyDatabase db, final String mailfrom, final String mailto,
                                  final String mailType,final String shipType, final String address, final String zip, final String city) {
        MailEntity mail = new MailEntity(mailfrom, mailto, mailType, shipType,address,zip,city);
        db.mailDao().insert(mail);
    }

    private static void populateWithTestData(MyDatabase db) {
        db.postWorkerDao().deleteAll();

        addPostWorker(db, "ms@gmail.com", "Meg", "Solliard","1234");
        addPostWorker(db, "ab@hes-so.com", "Abdullah", "Binjos","1234");
        addPostWorker(db, "admin", "Emilie", "Teodoro","1234");
        addPostWorker(db, "gm@gmail.com", "Gaétan", "Mottet","1234");
        addPostWorker(db, "centrale", "Centrale","Centrale","Centrale");
        db.mailDao().deleteAll();

        addMail(db, "Abdullah", "Meg", "Letter","A-Mail", "Rte", "1965 ","Saviese");
        addMail(db, "Meg", "Emilie", "Packages","B-Mail", "Rte", "1950 ","Sion");
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final MyDatabase database;

        PopulateDbAsync(MyDatabase db) {
            database = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(database);
            return null;
        }

    }
}
