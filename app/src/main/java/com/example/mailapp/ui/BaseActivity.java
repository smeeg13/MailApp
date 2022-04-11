package com.example.mailapp.ui;

import static com.example.mailapp.R.id.AboutBtn;
import static com.example.mailapp.R.id.AccountBtn;
import static com.example.mailapp.R.id.AddNewBtn;
import static com.example.mailapp.R.id.HomeBtn;
import static com.example.mailapp.R.id.HomeFrameLayout;
import static com.example.mailapp.R.id.LogoutBtn;
import static com.example.mailapp.R.id.MapBtn;
import static com.example.mailapp.R.id.SettingsBtn;
import static com.example.mailapp.R.id.myToolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mailapp.BaseApplication;
import com.example.mailapp.database.repository.PostworkerRepository;
import com.example.mailapp.databinding.ActivityBaseBinding;
import com.example.mailapp.ui.Fragments.AboutFragment;
import com.example.mailapp.ui.Fragments.HomeFragment;
import com.example.mailapp.ui.Fragments.MailDetailFragment;
import com.example.mailapp.ui.Fragments.MapFragment;
import com.example.mailapp.ui.Fragments.MyAccountFragment;
import com.example.mailapp.ui.Fragments.SettingsFragment;
import com.example.mailapp.util.MyAlertDialog;
import com.example.mailapp.util.OnAsyncEventListener;
import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "SharedPrefs";
    public static final String PREFS_USER = "LoggedIn";
    public static final String PREFS_ID_USER = "idUser";
    public static final String PREFS_MAIL = "";
    public static final String PREFS_BACKGROUND = "Background";
    private SharedPreferences settings;
    private SharedPreferences sharedPreferences;
    private String sharedPrefMail, sharedPrefBackground = null;
    private PostworkerRepository postworkerRepository;

    protected Toolbar toolbar;
    protected ActivityBaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postworkerRepository = ((BaseApplication) getApplication()).getPostworkerRepository();
        initialize();
        replaceFragment(new HomeFragment(), null);
    }

    private void initialize() {
        //Create the top navigation bar

        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        toolbar = findViewById(myToolbar);
        setSupportActionBar(toolbar);

        //Config of nav bar's actions
        setActions();

        //By default show home
        binding.HomeTopNavBar.setSelected(false);
        binding.HomeBottomNavBar.setSelectedItemId(HomeBtn);

        setContentView(binding.getRoot());
    }


    /**
     * Creation of the actions done by the two navigation bar
     */
    protected void setActions(){
        //Home Nav Bar
        binding.HomeTopNavBar.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case SettingsBtn:
                    replaceFragment(new SettingsFragment(), null);
                    break;
                case LogoutBtn:
                    logout();
                    break;
                case AboutBtn:
                    replaceFragment(new AboutFragment(), null);
                    break;
            }
            return true;
        });


        //Bottom Nav Bar
        binding.HomeBottomNavBar.setOnNavigationItemSelectedListener(item2 -> {
            switch (item2.getItemId()) {
                case AddNewBtn:
                     Bundle datas = new Bundle();
                    datas.putInt("MailID", -1); //put -1 cause we want to create a new one
                    datas.putBoolean("Enable",true);
                    replaceFragment(new MailDetailFragment(), datas);
                    break;
                case HomeBtn:
                    replaceFragment(new HomeFragment(),null);
                    break;
                case MapBtn:
                    replaceFragment(new MapFragment(),null);
                    break;
                case AccountBtn:
                    replaceFragment(new MyAccountFragment(),null);
                    break;
            }
            return true;
        });
    }

    public void replaceFragment(Fragment fragment, Bundle bundle) {
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
        MyAlertDialog ab = new MyAlertDialog(this, "Log Out","You will be disconnected, are you sure ?","Yes, Log Out");

        FirebaseAuth.getInstance().signOut();

//        Intent intent = new Intent(this, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        startActivity(intent);

        ab.backToLoginPage();
    }
    /**
     * Method to show a confirmation box for leaving the app
     * Kill program after
     */
    @Override
    public void onBackPressed() {
        MyAlertDialog ab = new MyAlertDialog(this, "Log Out and Close","You will be disconnected and the App will be closed, are you sure ?","Yes Log Out & Close");
        ab.killProgram();
    }
}