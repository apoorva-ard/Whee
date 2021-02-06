package com.example.whee4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UploadFound extends AppCompatActivity {
   FloatingActionButton btnbrowse, btnupload,btncamera;
    EditText iname,fplace,fdate,fdetails ;
    ImageView imgview;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;
    private static final int CAMERA_REQUEST_CODE = 102;
    public static final int CAMERA_PERM_CODE = 101;
    String currentPhotoPath;
    private int mYear,mMonth,mDay;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_found);
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Found");
        btnbrowse = (FloatingActionButton) findViewById(R.id.btnbrowse);
        btnupload= (FloatingActionButton) findViewById(R.id.btnupload);
        btncamera=(FloatingActionButton) findViewById(R.id.btncamera);
        iname = (EditText)findViewById(R.id.iname);
        fplace = (EditText)findViewById(R.id.fplace);
        fdate = (EditText)findViewById(R.id.fdate);
        fdetails = (EditText)findViewById(R.id.fdetails);
        imgview = (ImageView)findViewById(R.id.image_view);
        progressDialog = new ProgressDialog(UploadFound.this);
        user = FirebaseAuth.getInstance().getCurrentUser();

        fdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(UploadFound.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day) {
                                String temp=day + "/" + (month + 1) + "/" + year;
                                fdate.setText(temp);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dpd.show();

            }
        });


        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, Image_Request_Code);
            }
        });
        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermission();
            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadItem();
            }
        });

    }

   void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            dispatchTakePictureIntent();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode == CAMERA_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
               File f = new File(currentPhotoPath);
                imgview.setImageURI(Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                FilePathUri = Uri.fromFile(f);
                mediaScanIntent.setData(FilePathUri);
                this.sendBroadcast(mediaScanIntent);
            }
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    public void uploadItem() {
        String itemName = iname.getText().toString().trim();
        String itemPlace = fplace.getText().toString().trim();
        String itemDate = fdate.getText().toString().trim();
        String itemDetails = fdetails.getText().toString().trim();
        String userId = user.getUid();

        if(itemName.length()==0 || itemPlace.length()==0 || itemDetails.length()==0 || itemDate.length()==0){
            Toast.makeText(getApplicationContext(), "Fields can't be empty!", Toast.LENGTH_LONG).show();
            return;
        }

        if (FilePathUri != null) {
            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();
            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url=uri.toString();
                                    UploadInfo imageUploadInfo = new UploadInfo(itemName,userId,itemPlace,itemDate,itemDetails, url);
                                    String ImageUploadId = databaseReference.push().getKey();
                                    databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                                    Toast.makeText(getApplicationContext(), "Item Uploaded Successfully ", Toast.LENGTH_LONG).show();
                                    iname.setText("");
                                    fplace.setText("");
                                    fdate.setText("");
                                    fdetails.setText("");
                                    imgview.setImageResource(android.R.color.transparent);
                                }
                            });

                        }
                    });
        }
        else {
            String imageUrl="https://firebasestorage.googleapis.com/v0/b/whee-c564b.appspot.com/o/Images%2Fdefault.png?alt=media&token=213d550e-5d70-43b6-968e-0a1f51357be9";
            UploadInfo imageUploadInfo = new UploadInfo(itemName,userId,itemPlace,itemDate,itemDetails, imageUrl);
            String itemUploadId = databaseReference.push().getKey();
            databaseReference.child(itemUploadId).setValue(imageUploadInfo);
            Toast.makeText(getApplicationContext(), "Item Uploaded Successfully ", Toast.LENGTH_LONG).show();
            iname.setText("");
            fplace.setText("");
            fdate.setText("");
            fdetails.setText("");
            imgview.setImageResource(android.R.color.transparent);
        }


    }

   private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
