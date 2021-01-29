package com.example.whee4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Found extends Fragment {
    DatabaseReference databaseReference;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter ;

    // Creating Progress dialog
    ProgressDialog progressDialog;

    // Creating List of ImageUploadInfo class.
    List<UploadInfo> list = new ArrayList<>();


    View view;
    FloatingActionButton fab;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_found, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(getActivity());

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Loading Images From Firebase.");

        // Showing progress dialog.
        progressDialog.show();

        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in MainActivity.
        databaseReference = FirebaseDatabase.getInstance().getReference("Found");

        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    UploadInfo imageUploadInfo = postSnapshot.getValue(UploadInfo.class);

                    list.add(imageUploadInfo);
                }

                adapter = new RecyclerViewAdapter(getContext(), list);

                recyclerView.setAdapter(adapter);

                // Hiding the progress dialog.
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });

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