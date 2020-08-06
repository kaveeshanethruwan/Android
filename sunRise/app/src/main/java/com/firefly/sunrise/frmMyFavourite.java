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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class frmMyFavourite extends AppCompatActivity {

    private RecyclerView mRecyclrView;
    private ViewHolder mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private List<Model> mUploads;

    String email;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_my_favourite);

        email=getIntent().getStringExtra("itemName");

        getSupportActionBar().setTitle("My Favourites");
        callMyFav();
    }

    //callMyFav
    public void callMyFav(){

        mRecyclrView=findViewById(R.id.favrecyclerView);
        mRecyclrView.setHasFixedSize(true);
        mRecyclrView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.favprogress_circle);
        mUploads = new ArrayList<>();

       DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("MyFavourites").child(encodeUserEmail(email));

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                 {
                    Model upload = postSnapshot.getValue(Model.class);
                    mUploads.add(upload);
                }


                mAdapter= new ViewHolder(frmMyFavourite.this,mUploads);
                mRecyclrView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.GONE);

                mAdapter.setOnItemClickstner(new ViewHolder.OnItemClickListener() {
                @Override
                   public void onItemClick(View view, int position) {
                       TextView zname = view.findViewById(R.id.rName);
                        TextView zdes = view.findViewById(R.id.rDescription);
                       TextView zprice = view.findViewById(R.id.rPrice);
                       TextView zdis = view.findViewById(R.id.rDiscount);
                        ImageView zimage = view.findViewById(R.id.rImageView);

                        String xname = zname.getText().toString();
                        String xdes = zdes.getText().toString();
                        String xprice = zprice.getText().toString();
                        String xdis = zdis.getText().toString();
                        Drawable xdrawable = zimage.getDrawable();
                        Bitmap xbitmap = ((BitmapDrawable) xdrawable).getBitmap();

                        Intent intent = new Intent(view.getContext(), frmTakeOrder.class);
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
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(frmMyFavourite.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    //encodeEmail
    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

}
