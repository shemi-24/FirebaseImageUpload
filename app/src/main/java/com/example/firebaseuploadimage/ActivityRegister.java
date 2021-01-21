package com.example.firebaseuploadimage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityRegister extends AppCompatActivity {
    public static final String TAG = "TAG";
    private EditText name,email,password,phone,company;
    private TextView already;
    private Button register;
    //private ImageView profile;
    private CircleImageView profile;
    private ProgressBar progress;
    FirebaseAuth fAuth;
    public String downloadUri;
    FirebaseFirestore firestore;
    StorageReference storageReference;
    String userID;
    public String imageUri;

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode != RESULT_CANCELED) {
                switch (requestCode) {
                    case 0:
                        if (resultCode == RESULT_OK && data != null) {
                            Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                            profile.setImageBitmap(selectedImage);
                            imageUri=selectedImage.toString();
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
                                    profile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                    imageUri=selectedImage.toString();
                                    cursor.close();
                                }
                            }

                        }
                        break;
                }
            }
        }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        profile=findViewById(R.id.profileimage);
        name=findViewById(R.id.registertext1);
        email=findViewById(R.id.uploadtext3);
        password=findViewById(R.id.uploadtext2);
        phone=findViewById(R.id.registertext4);
        company=findViewById(R.id.uploadtext1);
        //name.setSelection(2);
        already=findViewById(R.id.registertv1);
        register=findViewById(R.id.registerbt1);
        progress=findViewById(R.id.registerprogress);
        fAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference("profile_image");
        //Toast.makeText(getApplicationContext(),fAuth.getCurrentUser().getEmail().toString(),Toast.LENGTH_LONG).show();
        //check whether the user exist or not
        //if(fAuth.getCurrentUser()!=null){
          //  startActivity(new Intent(getApplicationContext(),MainActivity.class));
            //finish();
        //}
        /*profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options={"Take Photo","Choose from Gallary","Cancel"};
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                builder.setTitle("Choose Your Profile Picture");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(options[which].equals("Take Photo"))
                        {
                            Intent takepicture=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takepicture,0);
                        }
                        else if(options[which].equals("Choose from Gallary"))
                        {
                            Intent pickphoto=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickphoto,1);
                        }
                        else if(options[which].equals("Cancel"))
                        {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });*/

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ActivityLogin.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Name=name.getText().toString().trim();
                final String Email=email.getText().toString().trim();
                final String Password=password.getText().toString().trim();
                final String Company=company.getText().toString().trim();
                final String Phone=phone.getText().toString().trim();
                //final String Image=imageUri;
                //Log.d("tag",Image);
                //Toast.makeText(getApplicationContext(),Image.toString(),Toast.LENGTH_SHORT).show();
                if(TextUtils.isEmpty(Name)){
                    name.setError("Name is required");
                    return;
                }
                if(TextUtils.isEmpty(Company)){
                    company.setError("Required.....");
                }
                if(TextUtils.isEmpty(Email)){
                    email.setError("Email is required..");
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    password.setError("Password should not empty...");
                    return;
                }
                if(TextUtils.isEmpty(Phone)){
                    phone.setError("Enter the correct password");
                    return;
                }
                if(Phone.length()<10){
                    phone.setError("Invalid number");
                }
                //if(TextUtils.equals(Password,Confirm)){
                  //  return;
                //}
                //else{
                  //  confirm.setError("Password did not match");
                //}
                progress.setVisibility(View.VISIBLE);
                //name.setEnabled(false);
                //profile.setClickable(false);
                //email.setEnabled(false);
                //password.setEnabled(false);
                //company.setEnabled(false);
                //phone.setEnabled(false);
                //already.setClickable(false);
                //register with firebase
                fAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ActivityRegister.this,"New user created",Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser=fAuth.getCurrentUser();
                            //assert firebaseUser != null;
                            Toast.makeText(getApplicationContext(),firebaseUser.getEmail(),Toast.LENGTH_LONG).show();
                            //Log.d("tag",firebaseUser.getEmail());
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
                            /*storageReference.putFile(Uri.parse(Image)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                             downloadUri=uri.toString();

                                        }
                                    });
                                    Toast.makeText(getApplicationContext(),"Profile stored successfully",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                 Toast.makeText(getApplicationContext(),"Failed to upload profile",Toast.LENGTH_SHORT).show();
                                }
                            });*/
                            userID=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=firestore.collection("User Details").document(userID);
                            Map<String,Object> user=new HashMap<>();
                            //user.put("UserImage",downloadUri);
                            user.put("User Name",Name);
                            user.put("Company Name",Company);
                            user.put("Email",Email);
                            //user.put("Password",Password.toCharArray());
                            user.put("Phone No",Phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"onSuccess:User profile stored successfully for"+userID);
                                    finish();
                                    startActivity(new Intent(getApplicationContext(),ActivityLogin.class));
                                }
                            });
                        }
                        else{
                            Toast.makeText(ActivityRegister.this,"Error:"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            progress.setVisibility(View.INVISIBLE);
                            //name.setEnabled(true);
                            //profile.setClickable(true);
                            //email.setEnabled(true);
                            //company.setEnabled(true);
                            //password.setEnabled(true);
                            //already.setClickable(true);

                        }
                    }
                });
            }
        });

    }
}
