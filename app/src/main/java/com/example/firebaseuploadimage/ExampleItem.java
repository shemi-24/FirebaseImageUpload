package com.example.firebaseuploadimage;

import android.os.Parcel;
import android.os.Parcelable;

public class ExampleItem    {
    private String mImageResource;
    private String mtext1;
    private String mtext2;
    private String mtext3;
    public ExampleItem() {

    }
    public ExampleItem(String image, String text1, String text2, String text3){
        mImageResource=image;
        mtext1=text1;
        mtext2=text2;
        mtext3=text3;
    }

    /*protected ExampleItem(Parcel in) {
        mImageResource = in.readString();
        mtext1 = in.readString();
        mtext2 = in.readString();
        mtext3 = in.readString();


    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mImageResource);
        dest.writeString(mtext1);
        dest.writeString(mtext2);
        dest.writeString(mtext3);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExampleItem> CREATOR = new Creator<ExampleItem>() {
        @Override
        public ExampleItem createFromParcel(Parcel in) {
            return new ExampleItem(in);
        }

        @Override
        public ExampleItem[] newArray(int size) {
            return new ExampleItem[size];
        }
    };*/

    public String getmImageResource() {
        return mImageResource;
    }

    public void setmImageResource(String mImageResource) {
        this.mImageResource = mImageResource;
    }

    public String getMtext1() {
        return mtext1;
    }

    public void setMtext1(String mtext1) {
        this.mtext1 = mtext1;
    }

    public String getMtext2() {
        return mtext2;
    }

    public void setMtext2(String mtext2) {
        this.mtext2 = mtext2;
    }

    public String getMtext3() {
        return mtext3;
    }

    public void setMtext3(String mtext3) {
        this.mtext3 = mtext3;
    }
}
