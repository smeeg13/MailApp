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
import com.example.mailapp.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createMenus();
    }

    private void remplaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public void createMenus(){
        Toolbar toolbar = null;
        ActivityHomeBinding binding = null;
        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        toolbar =findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);

        remplaceFragment(new HomeFragment());

        binding.topNavBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.settings:
                    remplaceFragment(new SettingsFragment());
                    break;
                case R.id.Logout:
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    break;
            }
            return true;
        });
        binding.topNavBar.setSelected(false);

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
        setContentView(binding.getRoot());

    }
}