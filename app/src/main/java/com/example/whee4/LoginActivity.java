package com.example.whee4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText varemail,varpassword;
    Button btnlogin;
    FirebaseAuth auth;
    TextView ResetPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        varemail = findViewById(R.id.email);
        varpassword = findViewById(R.id.password);
        btnlogin = findViewById(R.id.loginbutton);
ResetPass=(TextView) findViewById(R.id.resetPassword);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = varemail.getText().toString();
                String txt_password = varpassword.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
                } else {
                    auth = FirebaseAuth.getInstance();
                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Logging you in!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        });

        ResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail=new EditText((v.getContext()));
                AlertDialog.Builder passwordReset=new AlertDialog.Builder(v.getContext());
                passwordReset.setTitle("Reset Password");
                passwordReset.setMessage("Enter your email to receive reset link");
                passwordReset.setView(resetMail);
                passwordReset.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth = FirebaseAuth.getInstance();
                        String email=resetMail.getText().toString();
                        auth.sendPasswordResetEmail(email)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                   Toast.makeText(LoginActivity.this,"Reset link has been sent Successfully..",Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this,"Error! Reset Link is not Sent"+e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
                passwordReset.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordReset.create().show();
            }
        });
    }
}