package com.example.whee4.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.whee4.Fragment.Chat;
import com.example.whee4.Fragment.Found;
import com.example.whee4.Fragment.Lost;
import com.example.whee4.Fragment.User;
import com.example.whee4.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    DatabaseReference reff;

    BottomNavigationView bottomNavigationView;
    Found foundfrag = new Found();
    Lost lostFrag = new Lost();
    Chat chatfrag = new Chat();
    User userfrag = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_found);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_found:
                getSupportFragmentManager().beginTransaction().replace(R.id.menu_container, foundfrag).commit();
                return true;
            case R.id.navigation_lost:
                getSupportFragmentManager().beginTransaction().replace(R.id.menu_container, lostFrag).commit();
                return true;
            case R.id.navigation_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.menu_container, chatfrag).commit();
                return true;
            case R.id.navigation_user:
                getSupportFragmentManager().beginTransaction().replace(R.id.menu_container, userfrag).commit();
                return true;
        }
        return false;
    }
}