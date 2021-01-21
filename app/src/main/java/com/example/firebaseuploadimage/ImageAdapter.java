package com.example.firebaseuploadimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> implements Filterable {
    private Context ctx;
    private List<Upload> mUploads;
    private List<Upload> mUploadsFull;
    public static OnItemClickListener mListener;
    public ImageAdapter(Context context,List<Upload> image_uploads){
        this.ctx=context;
        this.mUploads=image_uploads;
        this.mUploadsFull=new ArrayList<>(image_uploads);

    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Upload uploadCurrent=mUploads.get(position);
        holder.productName.setText(uploadCurrent.getProductName());
        //String a=uploadCurrent.getBrand();
        //uploadCurrent.setBrand(a);
        //Picasso.get().load(uploadCurrent.getImg_url()).fit().centerCrop().into(holder.productImage);
        Glide.with(ctx).load(uploadCurrent.getImg_url()).into(holder.productImage);
        //Toast.makeText(ImageAdapter.class,holder.productImage.setImageBitmap();)
        //holder.productImage.setImageURI(uploadCurrent.getImg_url().toString());
        //Bitmap bitmap=(Bitmap)
        //holder.productImage.setImageBitmap();


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    @Override
    public Filter getFilter() {
        return mUploadFilter;
    }

    private final Filter mUploadFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Upload> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mUploadsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Upload item : mUploadsFull) {
                    if (item.getProductName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //mUploads.clear();
            mUploads.addAll((List<? extends Upload>) results.values);
            notifyDataSetChanged();//refresh the list with filtered data
        }
    };

    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener {
        public TextView productName;



        public ImageView productImage;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            productName=itemView.findViewById(R.id.productname);
            productImage=itemView.findViewById(R.id.productimage);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onClick(View v) {
            if(mListener!=null){
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem viewAndEdit=menu.add(menu.NONE,1,1,"View and Edit");
            MenuItem delete=menu.add(menu.NONE,2,2,"Delete");
            viewAndEdit.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener!=null){
                 int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    switch(item.getItemId()) {
                        case 1:
                            mListener.onViewAndEditClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
                }
            return false;
            }

    }
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onViewAndEditClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
}

