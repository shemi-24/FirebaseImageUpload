package com.example.firebaseuploadimage;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainPage extends AppCompatActivity {
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private TextView upload,gallary,nav_user,nav_email;
    private ImageView nav_profile;
    private NavigationView navigationView;
    FirebaseAuth fAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        //upload=findViewById(R.id.uploadtview);
        //gallary=findViewById(R.id.gallarytview);
        //nav_user=findViewById(R.id.nav_text1);
        //nav_email=findViewById(R.id.nav_text2);
        //nav_profile=findViewById(R.id.img_view);
        //navigationView=findViewById(R.id.nav_view);
        Toolbar toolbar=findViewById(R.id.toolbarsearch);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, drawerLayout,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle.syncState();
        /*upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),GridViewItems.class));
            }
        });
        fAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser=fAuth.getCurrentUser();
        final String currentUser=firebaseUser.getUid();
        firebaseFirestore.collection("User Details").whereEqualTo("uId",currentUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for(DocumentSnapshot documentSnapshot:task.getResult())
                    {
                        nav_user.setText((CharSequence) documentSnapshot.get("User Name"));
                        nav_email.setText((CharSequence)documentSnapshot.get("Email"));
                    }
                }
            }
        });*/
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
        //super.onBackPressed();

    }

}