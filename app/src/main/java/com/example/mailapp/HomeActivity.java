package com.example.mailapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mailapp.Fragments.AddNewFragment;
import com.example.mailapp.Fragments.HomeFragment;
import com.example.mailapp.Fragments.MapFragment;
import com.example.mailapp.Fragments.MyAccountFragment;
import com.example.mailapp.Fragments.SettingsFragment;
import com.example.mailapp.SessionManagement.SessionManagement;
import com.example.mailapp.databinding.ActivityHomeBinding;


public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createMenus();
    }

    private void createMenus() {
        //Create the top navigation bar
        Toolbar toolbar;
        ActivityHomeBinding binding;
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);

        //Create the new session for the user
        SessionManagement sessionManagement = new SessionManagement(HomeActivity.this);

        remplaceFragment(new HomeFragment());

        //Creation of the actions done by the two navigation bar
        binding.HomeTopNavBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.SettingsBtn:

                    remplaceFragment(new SettingsFragment());
                    break;
                case R.id.LogoutBtn:
                    sessionManagement.removeSession();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    break;
            }
            return true;
        });
        binding.HomeTopNavBar.setSelected(false);
        binding.HomeBottomNavBar.setSelectedItemId(R.id.HomeBtn);

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
        setContentView(binding.getRoot());
    }

    public void remplaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.HomeFrameLayout, fragment);
        fragmentTransaction.commit();
    }
}