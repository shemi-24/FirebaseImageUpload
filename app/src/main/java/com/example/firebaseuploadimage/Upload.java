package com.example.firebaseuploadimage;

import android.net.Uri;

public class Upload {
    String ProductName;
    String Brand;
    String Description;
    String Img_url;
    private String DatabaseKey;

    public Upload(){//empty constructor needed
         }
    public Upload(String product_name, String brand, String description, String img_url)
    {
        if(product_name.trim().equals("")||img_url.trim().equals(""))
        {
            product_name="No Name";
            img_url="Null";
        }
        ProductName=product_name;
        Brand=brand;
        Description=description;
        Img_url=img_url;


    }
    public String getProductName()
    {
        return ProductName;
    }
    //public String getName() {
      //  return Name;
    //}

    public String getBrand() {
        return Brand;
    }
    public String getDescription()
    {
        return Description;
    }
    public String getImg_url() {
        return Img_url;
    }

    public void setProductName(String product_name) {
        ProductName = product_name;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }
    public void setDescription(String description){
        Description=description;
    }

    public void setImg_url(String img_url) {
        Img_url = img_url;
    }
    public String getKey(){ return DatabaseKey;}
    public void setDatabaseKey(String key){ DatabaseKey=key;}
}
