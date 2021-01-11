package com.example.whee4;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class User extends Fragment implements View.OnClickListener{

    View view;
    Button logoutbtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        logoutbtn = view.findViewById(R.id.logoutbutton);
        logoutbtn.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logoutbutton:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent( (MainActivity)getActivity() , StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }
}