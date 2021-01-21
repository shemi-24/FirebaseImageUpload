package com.example.firebaseuploadimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileActivity extends AppCompatActivity {
    private ImageView ProfilePhoto,Back;
    private TextView Name,Company,Email,Password,Phone;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth fAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ProfilePhoto=findViewById(R.id.profileimage);
        Back=findViewById(R.id.profileback);
        Name=findViewById(R.id.profilename);
        Company=findViewById(R.id.profilecompany);
        Email=findViewById(R.id.profileemail);
        Password=findViewById(R.id.profilepassword);
        Phone=findViewById(R.id.profilephone);
        fAuth=FirebaseAuth.getInstance();
        firebaseUser=fAuth.getCurrentUser();
        final String currentUser=firebaseUser.getUid();
        firebaseFirestore.collection("User Details").whereEqualTo("uId",currentUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for(DocumentSnapshot documentSnapshot:task.getResult())
                    {
                        Name.setText((CharSequence) documentSnapshot.get("User Name"));
                        Company.setText((CharSequence)documentSnapshot.get("Company Name"));
                        Email.setText((CharSequence)documentSnapshot.get("Email"));
                        Password.setText((CharSequence)documentSnapshot.get("Password"));
                        Phone.setText((CharSequence)documentSnapshot.get("Phone No"));

                    }
                }
            }
        });

    }
}