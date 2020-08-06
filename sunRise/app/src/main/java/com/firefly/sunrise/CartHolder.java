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

public class CartHolder extends RecyclerView.Adapter<CartHolder.ImgeViewHolder> {

    private Context mContext;
    private List<CartItem> mUploads;

    private ViewHolder.OnItemClickListener mListner;


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    public void setOnItemClickstner(ViewHolder.OnItemClickListener listner){
        mListner=listner;
    }

    public CartHolder(Context context,List<CartItem>model )
    {
        mContext=context;
        mUploads=model;


    }
    @NonNull
    @Override
    public CartHolder.ImgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cartitems,parent,false);
        return new CartHolder.ImgeViewHolder(v,mListner);
    }
    @Override
    public void onBindViewHolder(@NonNull CartHolder.ImgeViewHolder holder, int position) {

        CartItem uploadCurrent = mUploads.get(position);
        holder.textViewname.setText(uploadCurrent.getCakename());
        holder.textViewtype.setText(uploadCurrent.getCaketype());
        holder.textViewprice.setText(uploadCurrent.getCakeprice());


       // byte[] cake=uploadCurrent.getCakeimg();
       // Bitmap bitmap=BitmapFactory.decodeByteArray(cake,0,cake.length);
      //  holder.imageViewa.setImageBitmap(bitmap);
        holder.textViewqty.setText(uploadCurrent.getQty());

        int qty=0,price=0,total=0;
        qty=Integer.parseInt(uploadCurrent.getQty());
        price=Integer.parseInt(uploadCurrent.getCakeprice());

       // total=qty*price;

        holder.texttotal.setText(uploadCurrent.getAmount());

        Picasso.get().load(uploadCurrent.getCakeimg()).placeholder(R.drawable.goal).fit().centerCrop().into(holder.imageViewa);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImgeViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewname;
        public TextView textViewtype;
        public TextView textViewprice;
        public TextView textViewqty;
        public ImageView imageViewa;
        public TextView texttotal;

        public ImgeViewHolder(@NonNull final View itemView, final ViewHolder.OnItemClickListener listner) {
            super(itemView);

            // textViewname=itemView.findViewById(R.id.text_view_name);
            // imageView = itemView.findViewById(R.id.image_view_upload);
            textViewname=itemView.findViewById(R.id.cartName);
            textViewqty=itemView.findViewById(R.id.cartQty);
            textViewprice=itemView.findViewById(R.id.cartPrice);
            textViewtype=itemView.findViewById(R.id.cartDiscount);
            imageViewa=itemView.findViewById(R.id.cartImageView);
            texttotal=itemView.findViewById(R.id.carttotal);


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
