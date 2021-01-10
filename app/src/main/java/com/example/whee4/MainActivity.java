package com.example.whee4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    EditText varname,varemail,varphone,varpassword,varbranch;
    Button btnsignup;
    DatabaseReference reff;
    Member member;
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

    public void register(){
        varname=(EditText)findViewById(R.id.name);
        varemail=(EditText)findViewById(R.id.email);
        varphone=(EditText)findViewById(R.id.phone);
        varbranch=(EditText)findViewById(R.id.branch);
        varpassword=(EditText)findViewById(R.id.password);
        btnsignup=(Button)findViewById(R.id.signUpButton);

        member= new Member();
        reff= FirebaseDatabase.getInstance().getReference().child("Member");

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member.setName(varname.getText().toString());
                member.setEmail(varemail.getText().toString());
                member.setPhone(varphone.getText().toString());
                member.setBranch(varbranch.getText().toString());
                member.setPassword(varpassword.getText().toString());

                reff.push().setValue(member);
                Toast.makeText(MainActivity.this,"User Registered",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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