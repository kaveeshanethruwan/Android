package com.firefly.sunrise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class frmView extends AppCompatActivity {

    ListView lv;



    private Spinner comboitemname;
    private ImageView imgdate;
    ValueEventListener listner;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerdatalist;

    private RecyclerView mRecyclrView;
    private ImageAdapterNew mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;
    Intent i = this.getIntent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_view);


        mRecyclrView=findViewById(R.id.recycler_view2);
        mRecyclrView.setHasFixedSize(true);
        mRecyclrView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Items");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    mUploads.add(upload);
                }

                mAdapter= new ImageAdapterNew(frmView.this,mUploads);
                mRecyclrView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(frmView.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });



    }

    //older one
    public void oldLisr(){
        Query get = FirebaseDatabase.getInstance().getReference("Orders");


        spinnerdatalist = new ArrayList<>();
        adapter=new ArrayAdapter<String>(frmView.this,android.R.layout.simple_spinner_dropdown_item,spinnerdatalist);

        lv.setAdapter(adapter);

        listner=get.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot item:dataSnapshot.getChildren()){
                    //  spinnerdatalist.add(item.getValue().toString());

                    // spinnerdatalist.add(item.child("cusname").getValue().toString());
                    spinnerdatalist.add(item.child("itemname").getValue().toString());
                    spinnerdatalist.add(item.child("qty").getValue().toString());
                    spinnerdatalist.add(item.child("orderdate").getValue().toString());


                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
