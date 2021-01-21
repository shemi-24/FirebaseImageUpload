package com.example.firebaseuploadimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificationActivity extends AppCompatActivity {
    public Button resend;
    public TextView notverify;
    FirebaseAuth fAuth;
    //FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        resend=findViewById(R.id.verifybt1);
        notverify=findViewById(R.id.verifytext2);
        /*firebaseUser=fAuth.getCurrentUser();
        if(!firebaseUser.isEmailVerified())
        {
            resend.setVisibility(View.VISIBLE);
            notverify.setVisibility(View.VISIBLE);
        }*/
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser=fAuth.getCurrentUser();
                firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Verification Mail is Sent to Your Mail",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Error:"+e.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }

        });

    }
}