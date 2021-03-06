package com.example.whee4.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whee4.Adapter.RecyclerViewAdapter1;
import com.example.whee4.Activity.MainActivity;
import com.example.whee4.R;
import com.example.whee4.Activity.UploadInfo;
import com.example.whee4.Activity.UploadLost;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Lost extends Fragment {
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ProgressDialog progressDialog;

    List<UploadInfo> list = new ArrayList<>();
    View view;
    FloatingActionButton fab;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Lost");

        view = inflater.inflate(R.layout.fragment_lost, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Images From Firebase.");
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("Lost");

        String currUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    UploadInfo imageUploadInfo = postSnapshot.getValue(UploadInfo.class);
                    if(!imageUploadInfo.getUserId().equals(currUser))
                        list.add(imageUploadInfo);
                }

                adapter = new RecyclerViewAdapter1(getContext(), list);

                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();

            }
        });

        fab=(FloatingActionButton)view.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent touploadlost = new Intent(getActivity(), UploadLost.class);
                startActivity(touploadlost);
            }
        });
        return view;
    }
}