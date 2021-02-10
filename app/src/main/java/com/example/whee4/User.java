package com.example.whee4;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import  com.example.whee4.UploadFound;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
public class User extends Fragment implements View.OnClickListener{

    View view;
    TextView uname;
    ImageView dp;
    LinearLayout found, lost, settings, logout;
    FloatingActionButton camUpload;

    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    DatabaseReference reference;
    FirebaseUser fuser;

    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Profile");

        view = inflater.inflate(R.layout.fragment_user, container, false);

        uname = view.findViewById(R.id.title);
        uname.setText("sample");

        found = view.findViewById(R.id.linearlayout_1);
        dp = view.findViewById(R.id.imageview_profile);
        lost = view.findViewById(R.id.linearlayout_2);
        settings = view.findViewById(R.id.linearlayout_4);
        logout = view.findViewById(R.id.linearlayout_5);
        camUpload=view.findViewById(R.id.cameraupload);

        camUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        String currUser = fuser.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(currUser);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModel um = snapshot.getValue(UserModel.class);
                    uname.setText(um.getUsername());
                    if (um.getImageURL().equals("default")){
                        dp.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        try {
                            Glide.with(getContext()).load(um.getImageURL()).into(dp);
                        }
                        catch (Exception e){
                            //Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        if (imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", ""+mUri);
                        reference.updateChildren(map);
                        Glide.with(getContext()).load(mUri).into(dp);
                        pd.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
}