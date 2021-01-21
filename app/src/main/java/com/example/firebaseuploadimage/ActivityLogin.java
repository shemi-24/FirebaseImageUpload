package com.example.firebaseuploadimage;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityLogin extends AppCompatActivity {
    private TextView auth,register,forgot;
    private EditText email,password;
    private Button login;
    private ProgressBar progressBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=findViewById(R.id.logint1);
        progressBar=findViewById(R.id.loginprogress);
        register=findViewById(R.id.loginregister);
        forgot=findViewById(R.id.logint3);
        email=findViewById(R.id.loginemail);
        password=findViewById(R.id.loginpassword);
        login=findViewById(R.id.loginlogin);
        fAuth=FirebaseAuth.getInstance();
        //FirebaseAuth.getInstance().signOut();
        //Toast.makeText(getApplicationContext(),fAuth.getCurrentUser().getEmail().toString(),Toast.LENGTH_LONG).show();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ActivityRegister.class));
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetmail=new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog=new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter Your Mail to Receive the Reset Link.");
                passwordResetDialog.setView(resetmail);
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get the mail and send the reset password link to that mail
                        String mail=resetmail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //reset mail send to recieptient
                                Toast.makeText(getApplicationContext(),"Reset Mail has been Sent to Your Mail.",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Error: Reset Mail is Not Sent"+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                passwordResetDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //closing dialog and back to login activity
                    }
                });
                passwordResetDialog.create().show();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email=email.getText().toString().trim();
                String Password=password.getText().toString().trim();
                if(TextUtils.isEmpty(Email)){
                    email.setError("Email is required..");
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    password.setError("Password should not empty...");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                //register.setClickable(false);
                //email.setClickable(false);
                //password.setClickable(false);
                //email.setEnabled(false);
                //password.setEnabled(false);
                fAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Authentication successful",Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser=fAuth.getCurrentUser();
                            if(firebaseUser.isEmailVerified()){
                                finish();
                                //finish();
                                startActivity(new Intent(getApplicationContext(),MainPage.class));
                            }
                            if(!firebaseUser.isEmailVerified()){
                                email.setEnabled(true);
                                password.setEnabled(true);
                                startActivity(new Intent(getApplicationContext(),VerificationActivity.class));
                            }

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Error:"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            email.setEnabled(true);
                            password.setEnabled(true);
                        }
                    }
                });

            }
        });

    }
}