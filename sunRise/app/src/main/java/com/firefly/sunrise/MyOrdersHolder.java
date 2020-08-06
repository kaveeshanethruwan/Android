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



public class MyOrdersHolder extends RecyclerView.Adapter<MyOrdersHolder.ImgeViewHolder> {

    private Context mContext;
    private List<Order_header> mUploads;

    private OnItemClickListener mListner;


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    public void setOnItemClickstner(OnItemClickListener listner){
        mListner=listner;
    }

    public MyOrdersHolder(Context context,List<Order_header>model )
    {
        mContext=context;
        mUploads=model;


    }
    @NonNull
    @Override
    public ImgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.myorderslayout,parent,false);
        return new ImgeViewHolder(v,mListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgeViewHolder holder, int position) {

        Order_header uploadCurrent = mUploads.get(position);

        holder.orderid.setText(String.valueOf(uploadCurrent.getOrder_id()));
        holder.orderdate.setText(uploadCurrent.getOrder_date());
        holder.deliverydate.setText(uploadCurrent.getDelivery_date());
        holder.deliverytime.setText(uploadCurrent.getDelivery_time());
        holder.status.setText(uploadCurrent.getStatus());


       // if(uploadCurrent.getStatus().equals("pending")){
        //     holder.status.setText(uploadCurrent.getStatus());

             Picasso.get().load(R.drawable.goal).placeholder(R.drawable.goal).fit().centerCrop().into(holder.zimageView);

      //  }

       // Picasso.get().load(uploadCurrent.getImageUrl()).placeholder(R.drawable.goal).fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImgeViewHolder extends RecyclerView.ViewHolder{
        public TextView orderid;
        public TextView orderdate;
        public TextView deliverydate;
        public TextView deliverytime;
        public ImageView zimageView;
        public TextView status;

        public ImgeViewHolder(@NonNull final View itemView, final OnItemClickListener listner) {
            super(itemView);


            orderid=itemView.findViewById(R.id.zorderid);
            orderdate=itemView.findViewById(R.id.zorderdate);
            deliverydate=itemView.findViewById(R.id.zdeliverydate);
            deliverytime=itemView.findViewById(R.id.zdeliverytime);
            zimageView=itemView.findViewById(R.id.zzImageView);
            status=itemView.findViewById(R.id.zstatus);


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
