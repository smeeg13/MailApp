package com.example.mailapp.util;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.mailapp.Enums.Messages;
import com.example.mailapp.R;
import com.example.mailapp.database.entities.MailEntity;
import com.example.mailapp.ui.LoginActivity;
import com.example.mailapp.viewModel.MailListViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class MyAlertDialog {

    private int ThemeID;
    String dialogYesBtn, dialogNoBtn = "Cancel";
    Context context;
    AlertDialog.Builder myAlert;


    public MyAlertDialog(Context context, String title, String msg, String yesMsg) {
        setThemeID(R.style.AlertDialogCustom);
        myAlert = new AlertDialog.Builder(context, getThemeID());
        setDialogTitle(title);
        setDialogMsg(msg);
        setDialogYesBtn(yesMsg);
        this.context = context;
    }

    public void killProgram() {
        myAlert.setPositiveButton(dialogYesBtn, (dialog, which) -> {
            dialog.dismiss();
            System.out.println("------------------------");
            System.out.println("KILL PROCESS DONE");
            System.out.println(" by the User");
            System.out.println("------------------------");
            FirebaseAuth.getInstance().signOut();
            System.out.println("USER : DISCONNECTED");
            //if you want to kill app . from other then your main avtivity.(Launcher)
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        });
        myAlert.setNegativeButton(dialogNoBtn, (dialog, which) -> dialog.dismiss());
        myAlert.show();
    }

    public void backToLoginPage() {
        myAlert.setPositiveButton(dialogYesBtn, (dialog, which) -> {
            dialog.dismiss();
            System.out.println("------------------------");
            System.out.println("LOGOUT  DONE");
            System.out.println(" by the User");
            System.out.println("------------------------");
            FirebaseAuth.getInstance().signOut();
            System.out.println("USER : DISCONNECTED");

            Intent intent = new Intent(context, LoginActivity.class);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(intent);
            System.out.println("BACK TO LOGIN PAGE");
        });
        myAlert.setNegativeButton(dialogNoBtn, (dialog, which) -> dialog.dismiss());
        myAlert.show();
    }

    public boolean DeleteMail(MailEntity mail, MailListViewModel viewModel, View view) {
        final boolean[] isDeleted = {false};
        myAlert.setPositiveButton(dialogYesBtn, (dialog, which) -> {
            viewModel.deleteMail(mail, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    System.out.println("deleteAccount: success");
                    isDeleted[0] = true;

                }

                @Override
                public void onFailure(Exception e) {
                    System.out.println("deleteAccount: failure ERROR : ");
                    System.out.println(e);
                    isDeleted[0] = false;
                }
            });
            Toast.makeText(context.getApplicationContext(), Messages.MAIL_DELETED.toString(), Toast.LENGTH_LONG).show();
        });

        myAlert.setNegativeButton(dialogNoBtn, (dialog, which) -> dialog.dismiss());
        myAlert.setView(view);
        myAlert.show();
        return isDeleted[0];
    }

    public int getThemeID() {
        return ThemeID;
    }
    public void setThemeID(int themeID) {
        ThemeID = themeID;
    }
    public void setDialogTitle(String title) {
        myAlert.setTitle(title);
    }
    public void setDialogMsg(String msg) {
        myAlert.setMessage(msg);
    }
    public void setDialogYesBtn(String dialogYesBtn) {
        this.dialogYesBtn = dialogYesBtn;
    }
}

