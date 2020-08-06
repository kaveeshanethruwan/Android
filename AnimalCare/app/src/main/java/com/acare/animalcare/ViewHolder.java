package com.acare.animalcare;
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



public class ViewHolder extends RecyclerView.Adapter<ViewHolder.ImgeViewHolder> {

    private Context mContext;
    private List<Model> mUploads;

    private OnItemClickListener mListner;


    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClickstner(OnItemClickListener listner){
        mListner=listner;
    }

    public ViewHolder(Context context,List<Model>model )
    {
        mContext=context;
        mUploads=model;


    }
    @NonNull
    @Override
    public ImgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.animalrow,parent,false);
        return new ImgeViewHolder(v,mListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgeViewHolder holder, int position) {

        Model uploadCurrent = mUploads.get(position);
        holder.textViewname.setText(uploadCurrent.getTitle());
        holder.textViewaddress.setText(uploadCurrent.getAddress());


        Picasso.get().load(uploadCurrent.getImage()).placeholder(R.drawable.loop).fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImgeViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewname;
        public TextView textViewaddress;
        public ImageView imageView;

        public ImgeViewHolder(@NonNull final View itemView, final OnItemClickListener listner) {
            super(itemView);

            textViewname=itemView.findViewById(R.id.aname);
            textViewaddress=itemView.findViewById(R.id.aaddress);
            imageView=itemView.findViewById(R.id.aImageView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listner!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listner.onItemClick(itemView,position);
                        }
                    }

                }
            });
        }
    }


}
