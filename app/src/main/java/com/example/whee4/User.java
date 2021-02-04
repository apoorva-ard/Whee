package com.example.whee4;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

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

        LinearLayout l = (LinearLayout) view.findViewById(R.id.linearlayout_1);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_found = new Intent(getActivity(), FoundActivity.class);
                startActivity(activity_found);
            }

        });
        LinearLayout l1 = (LinearLayout) view.findViewById(R.id.linearlayout_2);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_lost = new Intent(getActivity(), ProfileLost.class);
                startActivity(activity_lost);
            }
        });
        LinearLayout l2 = (LinearLayout) view.findViewById(R.id.linearlayout_4);
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_settings = new Intent(getActivity(), ProfileSettings.class);
                startActivity(activity_settings);
            }
        });
        LinearLayout l3 = (LinearLayout) view.findViewById(R.id.linearlayout_5);
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_logout = new Intent(getActivity(), ProfileLogout.class);
                startActivity(activity_logout);
            }
        });
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