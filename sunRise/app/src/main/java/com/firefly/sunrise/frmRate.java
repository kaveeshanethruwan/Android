package com.firefly.sunrise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class frmRate extends AppCompatActivity {

    private RatingBar rb;
    private EditText txtcomment;
    private Button btnsubmit;

    DatabaseReference reff;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String esource;
    String pkey,keys;

    String cusname;

    Float ratSize;

    Rating rating;
    long maxid=0;

    ProgressBar mProgressCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_rate);

        firebaseAuth=FirebaseAuth.getInstance();

        mProgressCircle = findViewById(R.id.progress_circle);
        mProgressCircle.bringToFront();
        mProgressCircle.setVisibility(View.GONE);

       // startupDb();
        rate();
        getEmail();
        btnEvent();

    }

    //rate
    public void rate(){

        rb=(RatingBar) findViewById(R.id.txtrate);
        txtcomment=(EditText) findViewById(R.id.txtcomment);
        btnsubmit=(Button) findViewById(R.id.btnrate);

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratSize=rating;

            }
        });
    }

    //getEmail
    public void getEmail(){
        try {
            firebaseUser = firebaseAuth.getCurrentUser();
            esource = firebaseUser.getEmail();
            check();

        }
        catch (Exception e)
        {
            Toast.makeText(frmRate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //countRows
//    public void startupDb(){
//        rating = new Rating();
//
//        reff = FirebaseDatabase.getInstance().getReference().child("Customer_Rate");
//
//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    maxid = dataSnapshot.getChildrenCount();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    //verifyForGetName
    public void check(){
        DatabaseReference reff= FirebaseDatabase.getInstance().getReference().child("Customer_Rate");

        reff.orderByChild("email").equalTo(esource).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                       getLastRecord();
                }
                else{
                       // getCustomerName();
                    cusname="EatAndTreat User";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    //getLastRate
    public void getLastRecord(){
        try{

            Query get = FirebaseDatabase.getInstance().getReference("Customer_Rate").orderByChild("email").equalTo(esource);

            get.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        keys=ds.getKey();
                        String dreview=ds.child("comment").getValue().toString();
                        Float drate=Float.parseFloat(ds.child("rate").getValue().toString());

                        rb.setRating(drate);
                        txtcomment.setText(dreview);

                        mProgressCircle.setVisibility(View.GONE);
                       // Toast.makeText(frmRate.this, keys, Toast.LENGTH_SHORT).show();


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(frmRate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    //getCustomerName
    public void getCustomerName(){
        Query get = FirebaseDatabase.getInstance().getReference("customer_details").orderByChild("cusemail").equalTo(esource);

        get.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    cusname=ds.child("cusname").getValue().toString();
                    //Toast.makeText(frmRate.this,cusname, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //btnEvent
    public void btnEvent(){
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtcomment.getText().toString().equals("") || txtcomment.getText().toString().equals("stillnot") || ratSize<=0){
                    Toast.makeText(frmRate.this, "Please rate and provide a valid comment", Toast.LENGTH_SHORT).show();

                }
                else{
                    verifyUser();
                }
            }
        });
    }

    //verify user
    public void verifyUser(){
        try {
            mProgressCircle.setVisibility(View.VISIBLE);

            DatabaseReference reff= FirebaseDatabase.getInstance().getReference().child("Customer_Rate");

            reff.orderByChild("email").equalTo(esource).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){


                        alreadyRate();

                    } else {

                        newUser();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        catch (Exception e){
            Toast.makeText(frmRate.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    //new user
    public void newUser(){
    try{

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        final String formattedDate = df.format(c);

        rating=new Rating();
        reff= FirebaseDatabase.getInstance().getReference().child("Customer_Rate");

        String email=esource.trim();
        String date=formattedDate.trim();
        Float r=ratSize;
        String cm=txtcomment.getText().toString();


        rating.setEmail(email);
        rating.setDate(date);
        rating.setRate(r);
        rating.setComment(cm);
        rating.setName(cusname.trim());


        //reff.child(String.valueOf(maxid + 1)).setValue(rating);
        reff.push().setValue(rating);

        //reff.child(String.valueOf(maxid + 1)).setValue(rating);


        txtcomment.setText("");
        rb.setRating(0F);
        mProgressCircle.setVisibility(View.GONE);


        logoutAlert();
    }
        catch (Exception e){
        Toast.makeText(frmRate.this, e.getMessage(), Toast.LENGTH_SHORT).show();

    }
    }

    //alreadyRate
    public void alreadyRate(){
        try{

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        final String formattedDate = df.format(c);

        reff= FirebaseDatabase.getInstance().getReference();

        reff.child("Customer_Rate").child(keys).child("date").setValue(formattedDate.trim());
        reff.child("Customer_Rate").child(keys).child("rate").setValue(ratSize);
        reff.child("Customer_Rate").child(keys).child("comment").setValue(txtcomment.getText().toString().trim());


            txtcomment.setText("");
            rb.setRating(0F);
            mProgressCircle.setVisibility(View.GONE);

            logoutAlert();
    }
        catch (Exception e){
        Toast.makeText(frmRate.this, e.getMessage(), Toast.LENGTH_SHORT).show();

    }
    }


    //swetalert
    public void logoutAlert(){
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Thank you")
                .setContentText("Thank You For Your Rating!")
                .setConfirmText("Ok").setConfirmButtonBackgroundColor(R.color.colorPrimary).setConfirmButtonTextColor(R.color.colorPrimary)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        finish();
                        sDialog.dismissWithAnimation();

                    }
                })//.setCancelButtonBackgroundColor(R.color.colorPrimary).setCancelButtonTextColor(R.color.colorPrimary)

                .show();
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
