package com.example.whee4;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Found extends Fragment {

    View view;
    Button button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_found, container, false);

        //example -- should use view.
        //button = (Button)view.findViewById(R.id.);

        //TODO: found module java implementation

        return view;
    }
}