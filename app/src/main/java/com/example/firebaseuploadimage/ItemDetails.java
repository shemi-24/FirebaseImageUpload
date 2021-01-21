package com.example.firebaseuploadimage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class ItemDetails extends AppCompatActivity {
    private ImageView Image,edit1,edit2,edit3;
    private Button save;
    private TextView ItemName,ItemBrand,ItemDescription;
    Uri image_uri;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Image=findViewById(R.id.itemdetailsimgview);
        ItemName=findViewById(R.id.itemdetailsitemname);
        ItemBrand=findViewById(R.id.itemdetailsitembrand);
        ItemDescription=findViewById(R.id.itemdetailsitemdescription);
        //edit1=findViewById(R.id.nameedit);
        //edit2=findViewById(R.id.brandedit);
        //edit3=findViewById(R.id.descedit);
        //save=findViewById(R.id.savebutton);
        String image= Objects.requireNonNull(getIntent().getExtras()).getString("Image");
        image_uri= Uri.parse(image);
        //Toast.makeText(getApplicationContext(),image_uri.toString(),Toast.LENGTH_SHORT).show();
        //Upload upload=new Upload();
        //ExampleItem exampleItem=intent.getParcelableExtra("hello");
        //String imageRes=exampleItem.getmImageResource();
        String line1=getIntent().getExtras().getString("Text1");
        String line2=getIntent().getExtras().getString("Text2");
        String line3=getIntent().getExtras().getString("Text3");
        //String line2=exampleItem.getMtext2();
        //String line3=exampleItem.getMtext3();
        //Log.d("tag",image.toString());
        //ExampleItem exampleItem=new ExampleItem();
        /*String imageRes=upload.getImg_url();
        String line1=upload.getProductName();
        String line2=upload.getBrand();
        String line3=upload.getDescription();*/
        //Upload upload=new Upload();
        //String imageRes =upload.getImg_url();
        Glide.with(getApplicationContext()).load(image_uri).into(Image);
        ItemName.setText(line1);
        ItemBrand.setText(line3);
        ItemDescription.setText(line2);
    }
}