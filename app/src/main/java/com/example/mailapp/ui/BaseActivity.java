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
import com.example.mailapp.ui.Fragments.AboutFragment;
import com.example.mailapp.ui.Fragments.AddNewFragment;
import com.example.mailapp.ui.Fragments.HomeFragment;
import com.example.mailapp.ui.Fragments.MailDetailFragment;
import com.example.mailapp.ui.Fragments.MapFragment;
import com.example.mailapp.ui.Fragments.MyAccountFragment;
import com.example.mailapp.ui.Fragments.SettingsFragment;
import com.example.mailapp.R;
import com.example.mailapp.util.MyAlertDialog;

public class BaseActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "SharedPrefs";
    public static final String PREFS_USER = "LoggedIn";
    public static final String PREFS_MAIL = "";
    public static final String PREFS_BACKGROUND = "Background";

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
        remplaceFragment(new HomeFragment(), null);
        binding.HomeTopNavBar.setSelected(false);
        binding.HomeBottomNavBar.setSelectedItemId(HomeBtn);

        setContentView(binding.getRoot());
    }


    /**
     * Creation of the actions done by the two navigation bar
     */
    protected void setActions(){
        //Home Nav Bar
        binding.HomeTopNavBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case SettingsBtn:
                    remplaceFragment(new SettingsFragment(), null);
                    break;
                case LogoutBtn:
                    logout();
                    break;
                case AboutBtn:
                    remplaceFragment(new AboutFragment(), null);
                    break;
            }
            return true;
        });


        //Bottom Nav Bar
        binding.HomeBottomNavBar.setOnItemSelectedListener(item2 -> {
            switch (item2.getItemId()) {
                case AddNewBtn:
                     Bundle datas = new Bundle();
                    datas.putInt("mailID", -1); //put -1 cause we want to create a new one
                    datas.putBoolean("Enable",true);
                    remplaceFragment(new MailDetailFragment(), datas);
                    break;
                case HomeBtn:
                    remplaceFragment(new HomeFragment(),null);
                    break;
                case MapBtn:
                    remplaceFragment(new MapFragment(),null);
                    break;
                case AccountBtn:
                    remplaceFragment(new MyAccountFragment(),null);
                    break;
            }
            return true;
        });
    }

    public void remplaceFragment(Fragment fragment, Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(HomeFrameLayout, fragment);
        fragmentTransaction.commit();
    }


    /**
     * MEthod to ask confirmation for Log Out
     * Go to login page after
     */
    public void logout(){
        MyAlertDialog ab = new MyAlertDialog(this, "Log Out","You will be disconnected, are you sure ?","Yes, Log Out", new LoginActivity(), getApplication());
        ab.backToLoginPage();
    }
    /**
     * Method to show a confirmation box for leaving the app
     * Kill program after
     */
    @Override
    public void onBackPressed() {
        MyAlertDialog ab = new MyAlertDialog(this, "Log Out and Close","You will be disconnected and the App will be closed, are you sure ?","Yes Log Out & Close", null, getApplication());
        ab.killProgram();
    }
}