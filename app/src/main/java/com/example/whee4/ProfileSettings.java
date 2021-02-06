package com.example.whee4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProfileSettings extends AppCompatActivity {
SwitchMaterial switchMat;
Button changePassword;
DatabaseReference dbref;
EditText oldPassword,newPassword;
FirebaseUser user;
ImageView editUName;
ProgressDialog progressDialog;
TextView delAcc;
SharedPreferences sharedPreferences=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        progressDialog = new ProgressDialog(ProfileSettings.this);



        //change password
        changePassword=(Button)findViewById(R.id.changePass);
        oldPassword=(EditText)findViewById(R.id.oldpassword);
        newPassword=(EditText)findViewById(R.id.newpassword) ;
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldp=oldPassword.getText().toString().trim();
                String newp=newPassword.getText().toString().trim();
                if(TextUtils.isEmpty(oldp)){
                    Toast.makeText(getApplicationContext(),"Enter your current password...",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newp.length()<=6){
                    Toast.makeText(getApplicationContext(),"Your password should contain atleast 7 characters..",Toast.LENGTH_SHORT).show();
                    return;
                }
                updatePassword(oldp,newp);
            }
        });


//delete account
        delAcc=(TextView) findViewById(R.id.deleteAccount);

        delAcc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                user = FirebaseAuth.getInstance().getCurrentUser();
                progressDialog.setTitle("Deleting Account...");
                progressDialog.show();
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();;
                            Toast.makeText(getApplicationContext(), "Acccount Deleted...", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ProfileSettings.this, RegisterActivity.class));
                        }else
                        {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });


//night mode
        switchMat=findViewById(R.id.switchMaterial);
        sharedPreferences=getSharedPreferences("night",0);
        Boolean booleanValue=sharedPreferences.getBoolean("night_mode",true);
        if(booleanValue)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchMat.setChecked(true);
        }
        switchMat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchMat.setChecked(true);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("night_mode",false);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Night Mode Activated", Toast.LENGTH_LONG).show();
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchMat.setChecked(false);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("night_mode",false);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Night Mode Deactivated", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void updatePassword(String oldp, String newp) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential= EmailAuthProvider.getCredential(user.getEmail(),oldp);
        user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.setTitle("Updating Password...");
                progressDialog.show();
                user.updatePassword(newp)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Password Updated....", Toast.LENGTH_LONG).show();
                                oldPassword.setText("");
                                newPassword.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext()," e.getMessage()", Toast.LENGTH_LONG).show();
                                oldPassword.setText("");
                                newPassword.setText("");
                            }
                        });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        oldPassword.setText("");
                        newPassword.setText("");
                    }
                });

    }
}