package com.example.mailapp.util;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.mailapp.database.MyDatabase.initializeDemoData;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.mailapp.Enums.Messages;
import com.example.mailapp.R;
import com.example.mailapp.database.MyDatabase;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.ui.BaseActivity;
import com.example.mailapp.ui.LoginActivity;
import com.example.mailapp.viewModel.MailListViewModel;
import com.example.mailapp.viewModel.MailViewModel;

public class MyAlertDialog {

    private  int ThemeID;
    String dialogTitle, dialogMsg, dialogYesBtn, dialogNoBtn = "Cancel";
    Context context;
    AlertDialog.Builder myAlert;


    public MyAlertDialog(Context context, String title, String msg, String yesMsg) {
        setThemeID(R.style.AlertDialogCustom);
        myAlert = new AlertDialog.Builder(context,getThemeID());
        setDialogTitle(title);
        setDialogMsg(msg);
        setDialogYesBtn(yesMsg);
        this.context = context;
    }

    public void killProgram(){
        myAlert.setPositiveButton(dialogYesBtn, (dialog, which) -> {
            dialog.dismiss();
            System.out.println("------------------------");
            System.out.println("KILL PROCESS DONE");
            System.out.println(" by the User");
            System.out.println("------------------------");
            //if you want to kill app . from other then your main avtivity.(Launcher)
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            //if you want to finish just current activity
            // LoginActivity.this.finish();
        });
        myAlert.setNegativeButton(dialogNoBtn, (dialog, which) -> dialog.dismiss());
        myAlert.show();
    }

    public void backToLoginPage(){
        myAlert.setPositiveButton(dialogYesBtn, (dialog, which) -> {
            dialog.dismiss();
            System.out.println("------------------------");
            System.out.println("LOGOUT  DONE");
            System.out.println(" by the User");
            System.out.println("------------------------");
            SharedPreferences.Editor editor = context.getSharedPreferences(BaseActivity.PREFS_NAME, 0).edit();
            editor.remove(BaseActivity.PREFS_NAME);
            editor.remove(BaseActivity.PREFS_USER);
            editor.apply();

            Intent intent= new  Intent(context, LoginActivity.class);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(intent);
        });
        myAlert.setNegativeButton(dialogNoBtn, (dialog, which) -> dialog.dismiss());
        myAlert.show();
    }

    public void DeleteMail(MailEntity mail, MailListViewModel viewModel, View view){
        myAlert.setPositiveButton(dialogYesBtn, (dialog, which) -> {
            viewModel.deleteMail(mail, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    System.out.println( "deleteAccount: success");
                }
                @Override
                public void onFailure(Exception e) {
                    System.out.println("deleteAccount: failure ERROR : ");
                    System.out.println(e);
                }
            });
            Toast.makeText(context.getApplicationContext(), Messages.MAIL_DELETED.toString(), Toast.LENGTH_LONG).show();
        });

        myAlert.setNegativeButton(dialogNoBtn, (dialog, which) -> dialog.dismiss());
        myAlert.setView(view);
        myAlert.show();
    }

    public void resetBD(){
        myAlert.setPositiveButton(dialogYesBtn, (dialog, which) -> {
            System.out.println("------------------------");
            System.out.println("RESET DB DONE");
            System.out.println(" DataBase Reset by the User");
            System.out.println("------------------------");
            initializeDemoData(MyDatabase.getInstance(context));
            Toast.makeText(context, "Demo Data Initialized", Toast.LENGTH_LONG).show();
            Intent intent= new  Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(intent);
        });
        myAlert.setNegativeButton(dialogNoBtn, (dialog, which) -> dialog.dismiss());
        myAlert.show();
    }

    public int getThemeID() {
        return ThemeID;
    }

    public void setThemeID(int themeID) {
        ThemeID = themeID;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public String getDialogMsg() {
        return dialogMsg;
    }

    public void setDialogTitle(String title) {
        myAlert.setTitle(title);
    }

    public void setDialogMsg(String msg) {
        myAlert.setMessage(msg);
    }

    public AlertDialog.Builder getMyAlert() {
        return myAlert;
    }

    public String getDialogYesBtn() {
        return dialogYesBtn;
    }

    public void setDialogYesBtn(String dialogYesBtn) {
        this.dialogYesBtn = dialogYesBtn;
    }

    public String getDialogNoBtn() {
        return dialogNoBtn;
    }

    public void setDialogNoBtn(String dialogNoBtn) {
        this.dialogNoBtn = dialogNoBtn;
    }
}

