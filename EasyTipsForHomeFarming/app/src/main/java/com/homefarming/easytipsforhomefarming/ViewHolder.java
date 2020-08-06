package com.homefarming.easytipsforhomefarming;

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

import de.hdodenhof.circleimageview.CircleImageView;


public class ViewHolder extends RecyclerView.Adapter<ViewHolder.ImgeViewHolder> {

    private Context mContext;
    private List<ModelItem> mUploads;

    private OnItemClickListener mListner;


    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClickstner(OnItemClickListener listner){
        mListner=listner;
    }

    public ViewHolder(Context context,List<ModelItem>model )
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

        ModelItem uploadCurrent = mUploads.get(position);
        holder.rowName.setText(uploadCurrent.getiName());
        holder.rowDate.setText(uploadCurrent.getIdate());
        Picasso.get().load(uploadCurrent.getiImgUrl()).placeholder(R.drawable.goal).fit().centerCrop().into(holder.rowImageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImgeViewHolder extends RecyclerView.ViewHolder{

        public TextView rowName;
        public TextView rowDate;
        public CircleImageView rowImageView;

        public ImgeViewHolder(@NonNull final View itemView, final OnItemClickListener listner) {
            super(itemView);


            rowName=itemView.findViewById(R.id.iName);
            rowDate=itemView.findViewById(R.id.iDate);
            rowImageView=itemView.findViewById(R.id.iImageView);


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
