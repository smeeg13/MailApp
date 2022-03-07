package com.example.mailapp.SessionManagement;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mailapp.DataBase.Entities.PostWorker;

public class SessionManagement {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_postWorker";

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public void saveSession(PostWorker postWorker) {
        //save session of postWorker whenever user is logged in
        int id = postWorker.getiD_PostWorker();

        editor.putInt(SESSION_KEY, id).commit();
    }

    public int getSession() {
        //return postworker whose session is saved
        // the int is the id of the postworker !

        return sharedPreferences.getInt(SESSION_KEY,-1);
    }

    public void removeSession(){
        // we put the default session int back => -1
        editor.putInt(SESSION_KEY,-1).commit();
    }


}
