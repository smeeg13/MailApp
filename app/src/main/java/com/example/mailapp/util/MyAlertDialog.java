package com.example.mailapp.util;

import android.app.AlertDialog;
import android.content.Context;

import com.example.mailapp.R;

public class MyAlertDialog extends AlertDialog.Builder {

    private final int ThemeID = R.style.AlertDialogCustom;

    public MyAlertDialog(Context context) {
        super(context);
    }

    public MyAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.setTitle("Confirmation of Disconnection");
        this.setMessage("You will be logged out. Are you sure ?");
        this.setPositiveButton("Yes, Log Out", (dialog, which) -> {
            dialog.dismiss();
            //if you want to kill app . from other then your main avtivity.(Launcher)
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            //if you want to finish just current activity
            // LoginActivity.this.finish();
        });
        this.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        this.show();
    }
}
