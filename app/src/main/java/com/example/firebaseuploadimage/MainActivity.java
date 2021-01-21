package com.example.firebaseuploadimage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageRegistrar;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.*;

public class MainActivity extends AppCompatActivity {
    protected static final int STORAGE_PERMISSION_CODE =4655;
    protected static final int CAPTURE_IMAGE_CODE=1;
    private static final int CAMERA_PERMISSION_CODE = 1;
    protected ImageView img;
    private Button upload;
    ArrayList<Uri> img_uris=new ArrayList<>();
    private ImageView choose_image,capture_image,close;
    private EditText product_name,description,brand;
    private ProgressBar progressBar;
    private TextView show_uploads,textpgrs,get_uri;
    public String img_uri;
    public Uri img_uri1;
    private static final int request_code=1;
    public static final int Pick_image_request=1;
    public static final int pick_camera_request=2;
    protected static final String TAG="MainActivity";
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask upload_task;
    private String imageUri;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StoragePermission();
        CameraPermission();
        //setupViewPager();
        //verifyPermission();
        //@RequiresApi(api = Build.VERSION_CODES.N)
                //private void setupViewPager()
                //{
                img = findViewById(R.id.imgview);
        upload = findViewById(R.id.uploadbtn);
        choose_image = findViewById(R.id.gallarybtn);
        capture_image = findViewById(R.id.camerabtn);
        close=findViewById(R.id.closebutton);
        product_name = findViewById(R.id.uploadtext1);
        brand = findViewById(R.id.uploadtext3);
        description = findViewById(R.id.uploadtext2);
        textpgrs = findViewById(R.id.textpgrs);
        progressBar = findViewById(R.id.pgsbar);
        img.setClickable(true);
        show_uploads = findViewById(R.id.showtview);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_uri=null;
                //img.setImageURI(null);
                Glide.with(getApplicationContext()).load(img_uri).into(img);

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //img.setImageURI(img_uri);
            }
        });
        progressBar.onVisibilityAggregated(Boolean.parseBoolean("false"));
        storageReference = FirebaseStorage.getInstance().getReference("image_uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("image_uploads");
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (upload_task != null && upload_task.isInProgress()) {
                    Toast.makeText(getApplicationContext(), "Please wait while upload is in progress", Toast.LENGTH_LONG).show();
                } else {
                    uploadFile();
                }
                //finish();

            }
        });
        show_uploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GridViewItems.class);
                startActivity(intent);
            }
        });
        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(getApplicationContext(),READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                    openFile();
                }
                else{
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{READ_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_CODE);                }

            }
        });
        capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //askcamerapermission();
                if(ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA)==PackageManager.PERMISSION_GRANTED){
                    captureImage();
                }
                else
                {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{CAMERA},CAPTURE_IMAGE_CODE);
                }

                //img.setImageURI(img_uri);
            }
        });
        }



    /*private void askcamerapermission() {
        if (ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{CAMERA}, CAMERA_PERMISSION_CODE);
        } else
        {
            captureImage();
        }
    }*/

    @RequiresApi(api = Build.VERSION_CODES.N)
        private void verifyPermission ()
        {
            Log.d(TAG, "verifyPermissions: asking for user permissions");
            String[] permissions = {READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE, CAMERA};
            if (ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, permissions[2]) == PackageManager.PERMISSION_GRANTED) {
                //Intent i=new Intent(getApplicationContext(),MainActivity.class);
                //startActivity(i);
                //Toast.makeText(getApplicationContext(),"All permissions granted",Toast.LENGTH_SHORT).show();
                        //exit(0);
                return;
            } else {
                ActivityCompat.requestPermissions(this, permissions, request_code);
            }

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        verifyPermission();
    }
    private void StoragePermission() {
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(this,
                new String[]{READ_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE);
    }//to access the storage media
    private void CameraPermission()
    {
        if(ContextCompat.checkSelfPermission(this,CAMERA)==PackageManager.PERMISSION_GRANTED)
            return;


        ActivityCompat.requestPermissions(this,new String[]{CAMERA},
                CAPTURE_IMAGE_CODE);


    }//to access the camera

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();

            }
        }else if(requestCode==CAPTURE_IMAGE_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();

            }
        }

    }*/


    private void openFile()
    {
        close.setVisibility(View.VISIBLE);
        //Intent i=new Intent();
        //i.setType("image/*");
        //i.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(i,Pick_image_request);
        Intent pickphoto=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickphoto,1);


    }

    @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        img.setImageBitmap(selectedImage);
                        img_uri=selectedImage.toString();
                        Toast.makeText(getApplicationContext(),img_uri.toString(),Toast.LENGTH_LONG).show();

                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                img_uri=selectedImage.toString();
                                Toast.makeText(getApplicationContext(),img_uri.toString(),Toast.LENGTH_LONG).show();
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
        //requestCode=Pick_image_request;
        //resultCode=RESULT_OK;
        /*if(resultCode== RESULT_OK && data!=null && data.getData()!=null )
        {
            img_uri=data.getData();
            //Picasso.get().load(img_uri).into((Target) img);
            //Toast.makeText(getApplicationContext(),img_uri.toString(),Toast.LENGTH_LONG).show();
            Glide.with(getApplicationContext()).load(img_uri).into(img);
            //img.setImageURI(img_uri);
            img_uris.add(img_uri);
            //
        }*/
        /*if(requestCode==pick_camera_request) {

            //img_uri = data.getData();
            //Bitmap bitmap= null;
            //try {
              //  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),img_uri);
            //} catch (IOException e) {
              //  e.printStackTrace();
            //}
            //ByteArrayOutputStream baos=new ByteArrayOutputStream();

            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            //byte[] b = baos.toByteArray();
            //String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

            //byte[] imageAsBytes = Base64.decode(imageEncoded.getBytes(), Base64.DEFAULT);
            //InputStream is=new ByteArrayInputStream(imageAsBytes);
            //bitmap= BitmapFactory.decodeStream(is);
            //img.setImageBitmap(bitmap);
            try{
                img_uri= data.getData();
                Log.e("selectedImageUri "," = " + img_uri);
                if(img_uri!=null){
                    //Toast.makeText(getApplicationContext(),"Image Uri Found",Toast.LENGTH_SHORT).show();
                    Bitmap bmp= BitmapFactory.decodeStream(getContentResolver().openInputStream(img_uri));
                    Log.e("bmp "," = " + bmp);
                    img.setImageBitmap(bmp);
                    Log.e("bmp ", " Displaying Imageview WIth Bitmap !!!!  = ");
                } else {
                    // If selectedImageUri is null check extras for bitmap
                    //Toast.makeText(getApplicationContext(),"Image Uri Found",Toast.LENGTH_SHORT).show();
                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    img.setImageBitmap(bmp);
                }
            }
            catch (FileNotFoundException fe)
            {
                fe.printStackTrace();
            }
            System.out.println("There is no file selected");
            Toast.makeText(getApplicationContext(),"HIIIIIII",Toast.LENGTH_LONG).show();
            //img_uri=img_uri1;
            //img.setImageURI(data.getData());
            //Glide.with(getApplicationContext()).load(img_uri).into(img);
        }*/


            //Bitmap img_uri=(Bitmap) data.getExtras().get("data");
            //img.setImageBitmap(img_uri);


            //img_uri=data.getData();

            ////Picasso.get().load(img_uri).into((Target) img);
            //img.setImageURI(img_uri);
            //Toast.makeText(getApplicationContext(),img_uri.toString(),Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),requestCode,Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),resultCode,Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),data.getData().toString(),Toast.LENGTH_LONG).show();

    }
    private String getFileExtension(String uri)
    {
        ContentResolver contentResolver=getContentResolver();//to find the extension of uploaded image
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(Uri.parse(uri)));
    }
    private void uploadFile()
    {
    if(img_uri!=null)
    {
        // Write a message to the database
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("message");

        //myRef.setValue("connection success");
        progressBar.setVisibility(View.VISIBLE);
        textpgrs.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(),"Uploading Image....",Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),img_uri.toString(),Toast.LENGTH_LONG).show();
        final StorageReference fileReference=storageReference.child("/"+System.currentTimeMillis()+"."+getFileExtension(img_uri));
        StorageMetadata metadata=new StorageMetadata.Builder().setContentType("image/jpg").build();
        //Task<Uri> downloadUri=fileReference.getDownloadUrl();
        //get_uri.setText(downloadUri.toString());
        //Toast.makeText(getApplicationContext(),downloadUri.toString(),Toast.LENGTH_LONG).show();
        upload_task= (UploadTask) fileReference.putFile(Uri.parse(img_uri),metadata)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },500);
                    //HashMap<String,String> hashMap=new HashMap<>();
                    //Uri downloadUri = null;
                    //hashMap.put(downloadUri.toString(),uri.toString());
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUri=uri.toString();
                            Log.d("TAG",downloadUri);
                            String upload_id=databaseReference.push().getKey();//Database entry
                            Upload upload = new Upload(product_name.getText().toString().trim(), brand.getText().toString().trim(), description.getText().toString().trim(), downloadUri);
                            //assert upload_id != null;
                            assert upload_id != null;
                            databaseReference.child(upload_id).setValue(upload);//Database upload
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Image Url not found",Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(getApplicationContext(),"Upload Successful",Toast.LENGTH_LONG).show();
                    finish();
                    close.setVisibility(View.INVISIBLE);
                    Intent i=new Intent(MainActivity.this,
                            MainActivity.class);
                    startActivity(i);

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    choose_image.setClickable(false);
                    product_name.setEnabled(false);
                    description.setEnabled(false);
                    capture_image.setClickable(false);
                    brand.setEnabled(false);
                    show_uploads.setClickable(false);
                    close.setClickable(false);

                    int progress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());//set progress bar
                progressBar.setProgress((int)progress);
                textpgrs.setText(progress+"%");
                Log.d(TAG,"onProgress: File "+progress+"% uploaded");
                }
            });

    }else
    {
        Toast.makeText(getApplicationContext(),"No file selected",Toast.LENGTH_SHORT).show();
    }
    }
    private void captureImage()
    {

        close.setVisibility(View.VISIBLE);
        //ContentValues value=new ContentValues();
        //value.put(MediaStore.Images.Media.TITLE,"New Picture");
        //value.put(MediaStore.Images.Media.DESCRIPTION,"From the camera");
        //img_uri = String.valueOf(getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value));
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT,img_uri);
        startActivityForResult(intent,0);
        //img.setImageURI(img_uri);*/
        //Toast.makeText(getApplicationContext(),img_uri.toString(),Toast.LENGTH_LONG).show();
        //Intent takepicture=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(takepicture,0);
    }

}