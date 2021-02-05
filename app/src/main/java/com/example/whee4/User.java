package com.example.whee4;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class User extends Fragment implements View.OnClickListener{

    View view;
    LinearLayout found, lost, settings, logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user, container, false);

        found = view.findViewById(R.id.linearlayout_1);
        lost = view.findViewById(R.id.linearlayout_2);
        settings = view.findViewById(R.id.linearlayout_4);
        logout = view.findViewById(R.id.linearlayout_5);

        found.setOnClickListener(this);
        lost.setOnClickListener(this);
        settings.setOnClickListener(this);
        logout.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linearlayout_1:
                Intent activity_found = new Intent(getActivity(), ProfileFound.class);
                startActivity(activity_found);
                break;
            case R.id.linearlayout_2:
                Intent activity_lost = new Intent(getActivity(), ProfileLost.class);
                startActivity(activity_lost);
                break;
            case R.id.linearlayout_4:
                Intent activity_settings  = new Intent(getActivity(), ProfileSettings.class);
                startActivity(activity_settings);
                break;
            case R.id.linearlayout_5:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity() , StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }
}