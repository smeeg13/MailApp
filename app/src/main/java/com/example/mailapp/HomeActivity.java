package com.example.mailapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mailapp.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar =findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);

        remplaceFragment(new HomeFragment());
        binding.topNavBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.settings:
                    remplaceFragment(new SettingsFragment());
                    break;
                case R.id.Logout:
                    remplaceFragment(new LogoutFragment());
                    break;
            }
            return true;
        });

        binding.bottomNavBar.setSelectedItemId(R.id.Home);

        binding.bottomNavBar.setOnItemSelectedListener(item2 -> {

            switch (item2.getItemId()){
                case R.id.AddNewMail:
                    remplaceFragment(new AddNewFragment());
                    break;
                case R.id.Home:
                    remplaceFragment(new HomeFragment());
                    break;
                case R.id.Map:
                    remplaceFragment(new MapFragment());
                    break;
                case R.id.MyAccount:
                    remplaceFragment(new MyAccountFragment());
                    break;
            }
            return true;
        });
    }

    private void remplaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}