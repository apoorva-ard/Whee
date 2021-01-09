package com.example.whee4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText varname,varemail,varphone,varpassword,varbranch;
    Button btnsignup;
    DatabaseReference reff;
    Member member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}