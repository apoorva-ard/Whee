package com.example.whee4.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.whee4.Adapter.RecyclerViewAdapterUL;
import com.example.whee4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileLost extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseUser user;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter ;

    ProgressDialog progressDialog;

    List<UploadInfo> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProfileLost.this.setTitle("Lost uploads");

        setContentView(R.layout.activity_profile_lost);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_ul);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading images From Firebase.");
        progressDialog.show();

        user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Lost");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    UploadInfo itemUploadInfo = postSnapshot.getValue(UploadInfo.class);
                    if(itemUploadInfo.getUserId().equals(userId)) {
                        itemUploadInfo.setKey(postSnapshot.getKey());
                        list.add(itemUploadInfo);
                    }
                }
                adapter = new RecyclerViewAdapterUL(ProfileLost.this , list);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileLost.this, "Fetching failed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}