package com.example.mailapp.ui;

import static com.example.mailapp.R.id.*;
import static com.example.mailapp.R.id.AddNewBtn;
import static com.example.mailapp.R.id.LogoutBtn;
import static com.example.mailapp.R.id.SettingsBtn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mailapp.databinding.ActivityBaseBinding;
import com.example.mailapp.ui.Fragments.AddNewFragment;
import com.example.mailapp.ui.Fragments.HomeFragment;
import com.example.mailapp.ui.Fragments.MapFragment;
import com.example.mailapp.ui.Fragments.MyAccountFragment;
import com.example.mailapp.ui.Fragments.SettingsFragment;
import com.example.mailapp.R;
import com.example.mailapp.util.MyAlertDialog;

public class BaseActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "SharedPrefs";
    public static final String PREFS_USER = "LoggedIn";

    protected Toolbar toolbar;
    protected ActivityBaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
    }

    private void initialize() {
        //Create the top navigation bar

        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        toolbar = findViewById(myToolbar);
        setSupportActionBar(toolbar);

        //Config of nav bar's actions
        setActions();

        //By default show home
        remplaceFragment(new HomeFragment());
        binding.HomeTopNavBar.setSelected(false);
        binding.HomeBottomNavBar.setSelectedItemId(HomeBtn);

        setContentView(binding.getRoot());
    }

    public void remplaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(HomeFrameLayout, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Creation of the actions done by the two navigation bar
     */
    protected void setActions(){
        //Home Nav Bar
        binding.HomeTopNavBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case SettingsBtn:
                    remplaceFragment(new SettingsFragment());
                    break;

                case LogoutBtn:
                    logout();
                    break;
            }
            return true;
        });

        //Bottom Nav Bar
        binding.HomeBottomNavBar.setOnItemSelectedListener(item2 -> {
            switch (item2.getItemId()) {
                case AddNewBtn:
                    remplaceFragment(new AddNewFragment());
                    break;
                case HomeBtn:
                    remplaceFragment(new HomeFragment());
                    break;
                case MapBtn:
                    remplaceFragment(new MapFragment());
                    break;
                case AccountBtn:
                    remplaceFragment(new MyAccountFragment());
                    break;
            }
            return true;
        });
    }

    public void logout(){
        AlertDialog.Builder ab = new AlertDialog.Builder(this,R.style.AlertDialogCustom);
        ab.setTitle("Confirmation of Disconnection");
        ab.setMessage("You will be logged out. Are you sure ?");
        ab.setPositiveButton("Yes, Log Out", (dialog, which) -> {
            SharedPreferences.Editor editor = getSharedPreferences(BaseActivity.PREFS_NAME, 0).edit();
            editor.remove(BaseActivity.PREFS_USER);
            editor.apply();

            Intent intent= new  Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });
        ab.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        ab.show();
    }
    /**
     * Method to show a confirmation box for leaving the app
     */
    @Override
    public void onBackPressed() {
        MyAlertDialog ab = new MyAlertDialog(this,R.style.AlertDialogCustom);
    }
}