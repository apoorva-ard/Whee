package com.example.whee4;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Found extends Fragment {

    View view;
    FloatingActionButton fab;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_found, container, false);
        fab=(FloatingActionButton)view.findViewById(R.id.fab1);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent activity2Intent = new Intent(getActivity(), UploadFound.class);
                        startActivity(activity2Intent);
                    }
                }
        );
        return view;
    }
}