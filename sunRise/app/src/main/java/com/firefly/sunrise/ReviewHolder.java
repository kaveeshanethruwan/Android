package com.firefly.sunrise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewHolder extends RecyclerView.Adapter<ReviewHolder.ImgeViewHolder> {

    private Context mContext;
    private List<ReviewModel> mUploads;

    private ViewHolder.OnItemClickListener mListner;


//    public interface OnItemClickListener{
   //     void onItemClick(View view, int position);
  //  }
    public void setOnItemClickstner(ViewHolder.OnItemClickListener listner){
        mListner=listner;
    }

    public ReviewHolder(Context context,List<ReviewModel>model )
    {
        mContext=context;
        mUploads=model;


    }
    @NonNull
    @Override
    public ReviewHolder.ImgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.reviews,parent,false);
        return new ReviewHolder.ImgeViewHolder(v,mListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder.ImgeViewHolder holder, int position) {

        ReviewModel uploadCurrent = mUploads.get(position);

        if(uploadCurrent.getRate()>4){
            holder.textViewname.setText(uploadCurrent.getName());
            holder.textViewdes.setText(uploadCurrent.getComment());
            holder.rtbar.setRating(uploadCurrent.getRate());
            holder.rtbar.setRating(uploadCurrent.getRate());
        }

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImgeViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewname;
        public TextView textViewdes;
        public RatingBar rtbar;

        public ImgeViewHolder(@NonNull final View itemView, final ViewHolder.OnItemClickListener listner) {
            super(itemView);

            textViewname=itemView.findViewById(R.id.reName);
            textViewdes=itemView.findViewById(R.id.reDescription);
            rtbar=itemView.findViewById(R.id.rerate);



        }
    }


}
