package com.firefly.sunrise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapterNew extends RecyclerView.Adapter<ImageAdapterNew.ImgeViewHolder> {

    private Context mContext;
    private List<Upload> mUploads;

    public ImageAdapterNew(Context context,List<Upload>uploads)
    {
        mContext=context;
        mUploads=uploads;
    }
    @NonNull
    @Override
    public ImgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return new ImgeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgeViewHolder holder, int position) {

        Upload uploadCurrent = mUploads.get(position);
        holder.textViewname.setText(uploadCurrent.getName());
        Picasso.get().load(uploadCurrent.getImageUrl()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImgeViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewname;
        public ImageView imageView;

        public ImgeViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewname=itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
        }
    }
}
