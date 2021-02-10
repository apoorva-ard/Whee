package com.example.whee4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileFound extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseUser user;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter ;

    ProgressDialog progressDialog;

    List<UploadInfo> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProfileFound.this.setTitle("Found Uploads");

        setContentView(R.layout.activity_profile_found);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_uf);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading Images From Firebase.");
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference("Found");
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
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

                adapter = new RecyclerViewAdapterUF(ProfileFound.this , list);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileFound.this, "Fetching failed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}