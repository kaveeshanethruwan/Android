package com.firefly.sunrise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class frmViewMyOrders extends AppCompatActivity {


    private RecyclerView mRecyclrView;
    private ViewHolder mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private List<Model> mUploads;
    Intent i = this.getIntent();

    public View assignView;
    String functionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_view_my_orders);

        functionName=getIntent().getStringExtra("itemName");

        selectFunction();


    }

    //selectFunction
    public void selectFunction(){

        mRecyclrView=findViewById(R.id.recyclerView);
        mRecyclrView.setHasFixedSize(true);
        mRecyclrView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.progress_circle);

        if(functionName.equals("Function")){
            callFunction();
        }
        else if(functionName.equals("Normal")){
            callNormal();
        }
        else if(functionName.equals("Cupcake")){
            callCupcake();
        }
        else if(functionName.equals("Pieces")){
            callPieces();
        }
    }

    //callFunction
    public void callFunction(){
        mUploads = new ArrayList<>();
       // mDatabaseRef= FirebaseDatabase.getInstance().getReference("Items");
        Query getNew = FirebaseDatabase.getInstance().getReference("Items").orderByChild("type").equalTo("Functioncake");

        getNew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Model upload = postSnapshot.getValue(Model.class);
                    mUploads.add(upload);
                }


                mAdapter= new ViewHolder(frmViewMyOrders.this,mUploads);
                mRecyclrView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.GONE);

                mAdapter.setOnItemClickstner(new ViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        assignView=view;
                        try {
                            subWorker();
                        }
                        catch (Exception e){
                            Toast.makeText(frmViewMyOrders.this,"Please wait",Toast.LENGTH_SHORT).show(); }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(frmViewMyOrders.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    //callNormal
    public void callNormal(){
        mUploads = new ArrayList<>();
        // mDatabaseRef= FirebaseDatabase.getInstance().getReference("Items");
        Query getNew = FirebaseDatabase.getInstance().getReference("Items").orderByChild("type").equalTo("NormalCake");

        getNew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Model upload = postSnapshot.getValue(Model.class);
                    mUploads.add(upload);
                }


                mAdapter= new ViewHolder(frmViewMyOrders.this,mUploads);
                mRecyclrView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.GONE);

                mAdapter.setOnItemClickstner(new ViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        assignView=view;
                        try {
                            subWorker();
                        }
                        catch (Exception e){
                            Toast.makeText(frmViewMyOrders.this,"Please wait",Toast.LENGTH_SHORT).show(); }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(frmViewMyOrders.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    //callcupcake
    public void callCupcake(){
        mUploads = new ArrayList<>();
        // mDatabaseRef= FirebaseDatabase.getInstance().getReference("Items");
        Query getNew = FirebaseDatabase.getInstance().getReference("Items").orderByChild("type").equalTo("Cupcake");

        getNew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Model upload = postSnapshot.getValue(Model.class);
                    mUploads.add(upload);
                }


                mAdapter= new ViewHolder(frmViewMyOrders.this,mUploads);
                mRecyclrView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.GONE);

                mAdapter.setOnItemClickstner(new ViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        assignView=view;
                        try {
                            subWorker();
                        }
                        catch (Exception e){
                            Toast.makeText(frmViewMyOrders.this,"Please wait",Toast.LENGTH_SHORT).show(); }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(frmViewMyOrders.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    //callPieces
    public void callPieces(){
        mUploads = new ArrayList<>();
        // mDatabaseRef= FirebaseDatabase.getInstance().getReference("Items");
        Query getNew = FirebaseDatabase.getInstance().getReference("Items").orderByChild("type").equalTo("Pieces");

        getNew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Model upload = postSnapshot.getValue(Model.class);
                    mUploads.add(upload);
                }


                mAdapter= new ViewHolder(frmViewMyOrders.this,mUploads);
                mRecyclrView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.GONE);

                mAdapter.setOnItemClickstner(new ViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        assignView=view;
                        try {
                            subWorker();
                        }
                        catch (Exception e){
                            Toast.makeText(frmViewMyOrders.this,"Please wait",Toast.LENGTH_SHORT).show(); }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(frmViewMyOrders.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    //callSubWorker
    public void subWorker(){
        TextView zname = assignView.findViewById(R.id.rName);
        TextView zdes = assignView.findViewById(R.id.rDescription);
        TextView zprice = assignView.findViewById(R.id.rPrice);
        TextView zdis = assignView.findViewById(R.id.rDiscount);
        ImageView zimage = assignView.findViewById(R.id.rImageView);

        String xname = zname.getText().toString();
        String xdes = zdes.getText().toString();
        String xprice = zprice.getText().toString();
        String xdis = zdis.getText().toString();
        Drawable xdrawable = zimage.getDrawable();
        Bitmap xbitmap = ((BitmapDrawable) xdrawable).getBitmap();

        Intent intent = new Intent(assignView.getContext(), frmTakeOrder.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        xbitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        intent.putExtra("name", xname);
        intent.putExtra("des", xdes);
        intent.putExtra("price", xprice);
        intent.putExtra("dis", xdis);
        intent.putExtra("image", bytes);
        startActivity(intent);
    }

    //callSubWorkerSpecial
    public void subWorkerSpecial(){
        TextView zname = assignView.findViewById(R.id.rName);
        TextView zdes = assignView.findViewById(R.id.rDescription);
        TextView zprice = assignView.findViewById(R.id.rPrice);
        TextView zdis = assignView.findViewById(R.id.rDiscount);
        ImageView zimage = assignView.findViewById(R.id.rImageView);

        String xname = zname.getText().toString();
        String xdes = zdes.getText().toString();
        String xprice = zprice.getText().toString();
        String xdis = zdis.getText().toString();
        Drawable xdrawable = zimage.getDrawable();
        Bitmap xbitmap = ((BitmapDrawable) xdrawable).getBitmap();

        Intent intent = new Intent(assignView.getContext(), frmViewMyOdersSpecial.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        xbitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        intent.putExtra("name", xname);
        intent.putExtra("des", xdes);
        intent.putExtra("price", xprice);
        intent.putExtra("dis", xdis);
        intent.putExtra("image", bytes);
        startActivity(intent);
    }


}
