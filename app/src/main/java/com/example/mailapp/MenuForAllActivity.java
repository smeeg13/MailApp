package com.example.mailapp;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MenuForAllActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.first_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.Logout:
                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        return false;
                    }
                });
                break;
            case R.id.settings:
//                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
////                    @Override
////                    public boolean onMenuItemClick(MenuItem menuItem) {
////                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
////                        return false;
////                    }
////                });
//                Toast.makeText(this,"Settings will open",Toast.LENGTH_SHORT).show();
//                break;
        }
        return true;
    }


}
