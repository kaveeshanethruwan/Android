package com.homefarming.easytipsforhomefarming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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

public class frmModification extends AppCompatActivity {

    private RecyclerView mRecyclrView;
    private ViewHolder mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private List<ModelItem> mUploads;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_modification);

        getSupportActionBar().setTitle("Item Records");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getItemList();

    }

    //getLastRate
//    public void getLastRecord(){
//        try{
//
//            Query get = FirebaseDatabase.getInstance().getReference("Customer_Rate").orderByChild("email").equalTo(esource);
//
//            get.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    for(DataSnapshot ds: dataSnapshot.getChildren()){
//                        keys=ds.getKey();
//                        String dreview=ds.child("comment").getValue().toString();
//                        Float drate=Float.parseFloat(ds.child("rate").getValue().toString());
//
//                        rb.setRating(drate);
//                        txtcomment.setText(dreview);
//
//                        mProgressCircle.setVisibility(View.GONE);
//                        // Toast.makeText(frmRate.this, keys, Toast.LENGTH_SHORT).show();
//
//
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//        }
//        catch (Exception e)
//        {
//            Toast.makeText(frmRate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    //callMyFav
    public void getItemList(){

        mRecyclrView=findViewById(R.id.mod_recyclerView);
        mRecyclrView.setHasFixedSize(true);
        mRecyclrView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.mod_progress_circle);
        mUploads = new ArrayList<>();

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Items");

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    ModelItem upload = postSnapshot.getValue(ModelItem.class);
                    mUploads.add(upload);
                }


                mAdapter= new ViewHolder(frmModification.this,mUploads);
                mRecyclrView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.GONE);

                mAdapter.setOnItemClickstner(new ViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        TextView zname = view.findViewById(R.id.iName);
                        String itemName = zname.getText().toString();


                        Intent intent = new Intent(view.getContext(), frmUpdateDelete.class);
                        intent.putExtra("itemName", itemName);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(frmModification.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    //disable back
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
