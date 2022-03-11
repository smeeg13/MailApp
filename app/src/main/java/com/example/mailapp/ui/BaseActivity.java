package com.example.mailapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mailapp.BaseApplication;
import com.example.mailapp.databinding.ActivityBaseBinding;
import com.example.mailapp.ui.Fragments.AddNewFragment;
import com.example.mailapp.ui.Fragments.HomeFragment;
import com.example.mailapp.ui.Fragments.MapFragment;
import com.example.mailapp.ui.Fragments.MyAccountFragment;
import com.example.mailapp.ui.Fragments.SettingsFragment;
import com.example.mailapp.R;
import com.example.mailapp.SessionManagement.SessionManagement;

public class BaseActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "SharedPrefs";
    public static final String PREFS_USER = "LoggedIn";

    protected Toolbar toolbar;
    protected ActivityBaseBinding binding;
    protected SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
    }

    private void initialize() {
        //Create the top navigation bar

        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);

        //Create the new session for the user
        sessionManagement = new SessionManagement(BaseActivity.this);

        //Config of nav bar's actions
        setActions();

        //By default show home
        remplaceFragment(new HomeFragment());
        binding.HomeTopNavBar.setSelected(false);
        binding.HomeBottomNavBar.setSelectedItemId(R.id.HomeBtn);

        setContentView(binding.getRoot());
    }

    public void remplaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.HomeFrameLayout, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Creation of the actions done by the two navigation bar
     */
    protected void setActions(){
        //Home Nav Bar
        binding.HomeTopNavBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.SettingsBtn:
                    remplaceFragment(new SettingsFragment());
                    break;

                case R.id.LogoutBtn:
                    logout();
                    break;
            }
            return true;
        });

        //Bottom Nav Bar
        binding.HomeBottomNavBar.setOnItemSelectedListener(item2 -> {
            switch (item2.getItemId()) {
                case R.id.AddNewBtn:
                    remplaceFragment(new AddNewFragment());
                    break;
                case R.id.HomeBtn:
                    remplaceFragment(new HomeFragment());
                    break;
                case R.id.MapBtn:
                    remplaceFragment(new MapFragment());
                    break;
                case R.id.AccountBtn:
                    remplaceFragment(new MyAccountFragment());
                    break;
            }
            return true;
        });
    }

    public void logout(){
        SharedPreferences.Editor editor = getSharedPreferences(BaseActivity.PREFS_NAME, 0).edit();
        editor.remove(BaseActivity.PREFS_USER);
        editor.apply();
        sessionManagement.removeSession();

        Intent intent= new  Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}