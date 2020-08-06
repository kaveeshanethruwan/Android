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
        View v = LayoutInflater.from(mContext).inflate(R.layout.row,parent,false);
        return new ImgeViewHolder(v,mListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgeViewHolder holder, int position) {

        Model uploadCurrent = mUploads.get(position);
        holder.textViewname.setText(uploadCurrent.getName());
        holder.textViewdes.setText(uploadCurrent.getDescription());
        holder.textViewprice.setText(uploadCurrent.getPrice().toString());
        if(!uploadCurrent.getDiscount().toString().equals("0")){
            holder.textViewOfferlabel.setText("Offer Rs.");
            holder.textViewdis.setText(uploadCurrent.getDiscount().toString());

        }

        Picasso.get().load(uploadCurrent.getImageUrl()).placeholder(R.drawable.goal).fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImgeViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewname;
        public TextView textViewdes;
        public TextView textViewprice;
        public TextView textViewdis;
        public ImageView imageView;
        public TextView textViewOfferlabel;

        public ImgeViewHolder(@NonNull final View itemView, final OnItemClickListener listner) {
            super(itemView);

           // textViewname=itemView.findViewById(R.id.text_view_name);
           // imageView = itemView.findViewById(R.id.image_view_upload);
             textViewname=itemView.findViewById(R.id.rName);
             textViewdes=itemView.findViewById(R.id.rDescription);
             textViewprice=itemView.findViewById(R.id.rPrice);
             textViewdis=itemView.findViewById(R.id.rDiscount);
             imageView=itemView.findViewById(R.id.rImageView);
             textViewOfferlabel=itemView.findViewById(R.id.rofferlabel);


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
