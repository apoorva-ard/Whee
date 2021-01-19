package com.example.whee4;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Lost extends Fragment {

    View view;
    FloatingActionButton fab;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lost, container, false);
        fab=(FloatingActionButton)view.findViewById(R.id.fab2);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent activity2Intent = new Intent(getActivity(), LostFound.class);
                        startActivity(activity2Intent);
                    }
                }
        );
        return view;
    }
}