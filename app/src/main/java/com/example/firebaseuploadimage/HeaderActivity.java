package com.example.firebaseuploadimage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HeaderActivity extends AppCompatActivity {
    private ImageView Admin,Customer;
    private TextView AdminText,CustomerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);
        Admin=findViewById(R.id.lay1img);
        Customer=findViewById(R.id.lay2img);
        AdminText=findViewById(R.id.lay1text);
        CustomerText=findViewById(R.id.layout2);
        AdminText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ActivityLogin.class));
            }
        });
        CustomerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),GridViewItems.class));
            }
        });
    }
}