package com.example.firebaseuploadimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
//import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class GridViewItems extends AppCompatActivity implements ImageAdapter.OnItemClickListener  {
    RecyclerView recyclerView;
    //CardView cardView;
    ImageAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage mStorage;
    ProgressBar progressBar;
    Toolbar toolbar;
    SearchView searchView;
    private ValueEventListener mDBListener;
    private List<Upload> mUploads;
    //ArrayList<Array> arr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_items);
        recyclerView=findViewById(R.id.recyclerView);
        toolbar=findViewById(R.id.toolbarsearch);
        //setSupportActionBar(toolbar);
        //toolbar=findViewById(R.id.toolbarSearch);
        //setSupportActionBar(toolbar);
        //searchView=findViewById(R.id.serach);
        //cardView=findViewById(R.id.cardView);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3,recyclerView.VERTICAL,false));
        progressBar=findViewById(R.id.grid_progress);

       // progressDialog=new ProgressDialog(getApplicationContext());
        //progressDialog.setMessage("Loading Images Please Wait...");
        //progressDialog.show();
        //GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        //recyclerView.setLayoutManager(gridLayoutManager);
        mUploads=new ArrayList<>();
        mAdapter=new ImageAdapter(getApplicationContext(),mUploads);
        progressBar.setVisibility(View.VISIBLE);
        //if(mUploads.isEmpty())
        //{
          //  for(int i=1;i<=5;i++)
           // {
             //   Toast.makeText(getApplicationContext(),"No items Added....",LENGTH_LONG).show();
            //}

        //}
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(mUploads.contains(query))
                {
                    mAdapter.getFilter().filter(query);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Item not found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }

        });*/
        mStorage=FirebaseStorage.getInstance();
        //Toast.makeText(getApplicationContext(),mAdapter.getItemCount(),LENGTH_LONG).show();
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(GridViewItems.this);
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("image_uploads");
        //Toast.makeText(getApplicationContext(),mDatabaseRef.toString(),Toast.LENGTH_LONG).show();
        mDBListener=mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUploads.clear();
                for(DataSnapshot postSnapshot:snapshot.getChildren()){
                    //Toast.makeText(getApplicationContext(),"hiiiiiiiii",Toast.LENGTH_SHORT).show();
                    Upload upload=postSnapshot.getValue(Upload.class);
                    assert upload != null;
                    upload.setDatabaseKey(postSnapshot.getKey());
                    Log.d("tag",upload.getBrand());
                    mUploads.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
          //      progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            //    progressDialog.dismiss();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.example,menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);
        //setSupportActionBar(toolbar);
        SearchView searchView=(SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!mUploads.contains(query)) {
                    Toast.makeText(getApplicationContext(), "Item not found", Toast.LENGTH_SHORT).show();
                } else {
                    mAdapter.getFilter().filter(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }
    @Override
    public void onItemClick(int position) {
        //Toast.makeText(getApplicationContext(),"Normal click:"+position,Toast.LENGTH_SHORT).show();
        //open new intent to view full data
        Upload getItem=mUploads.get(position);
        //String Text1=getItem.getProductName();
        //String Image=getItem.getImg_url();
        //Log.d("tag",Image);
        //Toast.makeText(getApplicationContext(),Image.toString(),Toast.LENGTH_SHORT).show();
        //String Text2=getItem.getBrand();
        //String Text3=getItem.getDescription();
        //ExampleItem getItemDetails;
        //getItemDetails = new ExampleItem(Image,Text1,Text2,Text3);
        //Upload upload=new Upload(Image,Text1,Text2,Text3);
        //upload.setImg_url(Image);
        //upload.setProductName(Text1);
        //upload.setBrand(Text2);
        //upload.setDescription(Text3);

        //Log.d("tag",Text1);
        //Log.d("tag",Text2);
        Intent intent=new Intent(getApplicationContext(),ItemDetails.class);
        intent.putExtra("Image",mUploads.get(position).getImg_url().toString());
        intent.putExtra("Text1",mUploads.get(position).getProductName().toString());
        intent.putExtra("Text2",mUploads.get(position).getBrand().toString());
        intent.putExtra("Text3",mUploads.get(position).getDescription().toString());
        startActivity(intent);

    }

    @Override
    public void onViewAndEditClick(int position) {
        //open new intent to view full data
        Upload getItem=mUploads.get(position);
        //String Text1=getItem.getProductName();
        //String Image=getItem.getImg_url();
        //Log.d("tag",Image);
        //Toast.makeText(getApplicationContext(),Image.toString(),Toast.LENGTH_SHORT).show();
        //String Text2=getItem.getBrand();
        //String Text3=getItem.getDescription();
        //ExampleItem getItemDetails;
        //getItemDetails = new ExampleItem(Image,Text1,Text2,Text3);
        //Upload upload=new Upload(Image,Text1,Text2,Text3);
        //upload.setImg_url(Image);
        //upload.setProductName(Text1);
        //upload.setBrand(Text2);
        //upload.setDescription(Text3);

        //Log.d("tag",Text1);
        //Log.d("tag",Text2);
        Intent intent=new Intent(getApplicationContext(),ItemDetails.class);
        intent.putExtra("Image",mUploads.get(position).getImg_url().toString());
        intent.putExtra("Text1",mUploads.get(position).getProductName().toString());
        intent.putExtra("Text2",mUploads.get(position).getBrand().toString());
        intent.putExtra("Text3",mUploads.get(position).getDescription().toString());
        startActivity(intent);

    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem=mUploads.get(position);
        final String selectedKey=selectedItem.getKey();
        StorageReference imageRef=mStorage.getReferenceFromUrl(selectedItem.getImg_url());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        mDatabaseRef.child(selectedKey).removeValue();
                        Toast.makeText(getApplicationContext(),"Item Deleted.",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.example,menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchManager searchManager=(SearchManager) GridViewItems.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView=(SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(GridViewItems.this.getComponentName()));
        //searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }*/
}