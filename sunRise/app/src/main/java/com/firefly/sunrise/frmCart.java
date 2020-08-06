package com.firefly.sunrise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class frmCart extends AppCompatActivity {

    private RecyclerView mRecyclrView;
    private CartHolder mAdapter;
    private ProgressBar mProgressCircle;
    // private DatabaseReference mDatabaseRef;
    private List<CartItem> mUploads;

    //details
    TextView count, amount, callmenu, checkout;
    int sqlcount, sqlamount;
    String iname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_cart);
        getSupportActionBar().setTitle("My Cart");

        setItems();
        setDetails();
        setCount();
        setAmount();
    }



    //setCountAnd
    public void setCount() {
        SQLiteDatabase mydb = openOrCreateDatabase("eatandtreats", MODE_PRIVATE, null);
        mydb.execSQL("create table if not exists cart(email varchar,caketype varchar,cakename varchar,cakeprice varchar,cakediscount varchar,qty varchar,amount varchar,imageUrl varchar,weight varchar,cakemessage varchar)");
        Cursor mCount = mydb.rawQuery("select count(*) from cart", null);
        mCount.moveToFirst();
        sqlcount = mCount.getInt(0);
        count.setText(String.valueOf(sqlcount));
        mCount.close();
        // Toast.makeText(frmCart.this,String.valueOf(count),Toast.LENGTH_SHORT).show();


    }

    //setAmount
    public void setAmount() {
        SQLiteDatabase mydb = openOrCreateDatabase("eatandtreats", MODE_PRIVATE, null);
        mydb.execSQL("create table if not exists cart(email varchar,caketype varchar,cakename varchar,cakeprice varchar,cakediscount varchar,qty varchar,amount varchar,imageUrl varchar,weight varchar,cakemessage varchar)");
        Cursor mCount = mydb.rawQuery("select sum(amount) from cart", null);
        mCount.moveToFirst();
        sqlamount = mCount.getInt(0);
        amount.setText(String.valueOf(sqlamount));
        mCount.close();
        // Toast.makeText(frmCart.this,String.valueOf(count),Toast.LENGTH_SHORT).show();
    }

    //setDetails
    public void setDetails() {
        count = findViewById(R.id.txtcount);
        amount = findViewById(R.id.txtamount);
        callmenu = findViewById(R.id.txtcontinue);
        checkout = findViewById(R.id.txtcheckout);

        callmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(frmCart.this, frmNewHome.class));

            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sqlcount==0){
                    Toast.makeText(frmCart.this,"Cart is empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    startActivity(new Intent(frmCart.this, frmBeforeFinish.class));
                }

            }
        });

    }

    //query
    public void setItems() {


        mRecyclrView = findViewById(R.id.cartrecyclerView);
        mRecyclrView.setHasFixedSize(true);
        mRecyclrView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.cartprogress_circle);

        mUploads = new ArrayList<>();
        // mDatabaseRef= FirebaseDatabase.getInstance().getReference("Items");
        //Query getNew = FirebaseDatabase.getInstance().getReference("Items").orderByChild("type").equalTo("Functioncake");

        // getNew.addValueEventListener(new ValueEventListener() {
        //    @Override
        //    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        //     for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
        //     {
        //          Model upload = postSnapshot.getValue(Model.class);
        //          mUploads.add(upload);
        //       }
        SQLiteDatabase mydb = openOrCreateDatabase("eatandtreats", MODE_PRIVATE, null);
        mydb.execSQL("create table if not exists cart(email varchar,caketype varchar,cakename varchar,cakeprice varchar,cakediscount varchar,qty varchar,amount varchar,imageUrl varchar,weight varchar,cakemessage varchar)");
        Cursor rs = mydb.rawQuery("select caketype,cakename,cakeprice,imageUrl,qty,amount from cart", null);
        if (rs != null) {
            if (rs.moveToFirst()) {
                do {
                    CartItem ca = new CartItem();

                    // Model upload = postSnapshot.getValue(Model.class);
                    ca.setCaketype(rs.getString(0));
                    ca.setCakename(rs.getString(1));
                    ca.setCakeprice(rs.getString(2));
                    //  byte[] ci=rs.getBlob(3);
                    //  ca.setCakeimg(ci);
                    ca.setCakeimg(rs.getString(3));
                    ca.setQty(rs.getString(4));
                    ca.setAmount(rs.getString(5));

                    // ca.setCakeimg(rs.getBlob(3));

                    //byte[] imgByte = rs.getBlob(3);
                    // Bitmap bmp= BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
                    // ca.setCakeimg(rs.getBlob(3));
                    // ca.setCakeimg(bmp);

                    // byte[] imgByte= ca.setCakeimg(rs.getBlob(3));
                    // Model upload = postSnapshot.getValue(Model.class);
                    // allUploads.add(upload);

                    mUploads.add(ca);

                    //    ca.setCakediscount(rs.getString(4));
                    //   ca.setQty(rs.getString(5));
                    //     ca.setImage(rs.getString(6));
                    //     ca.setWeight(rs.getString(7));
                    //     ca.setCakemessage(rs.getString(8));
                }
                while (rs.moveToNext());
            }
        }
        //  mUploads.add(ca);

        mAdapter = new CartHolder(frmCart.this, mUploads);
        mRecyclrView.setAdapter(mAdapter);
        mProgressCircle.setVisibility(View.GONE);


        mAdapter.setOnItemClickstner(new ViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                try {
                   // Toast.makeText(frmCart.this, "done", Toast.LENGTH_SHORT).show();
                    TextView itemname = view.findViewById(R.id.cartName);
                    iname = itemname.getText().toString();
                    itemRemove();
                } catch (Exception e) {
                    Toast.makeText(frmCart.this, "Please wait", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //sweetRemove
    public void itemRemove(){
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Remove Item")
                .setContentText("Do you want to remove '"+iname+"' from the cart?")
                .setCustomImage(R.drawable.ic_remove_shopping_cart_black_24dp)
                .setConfirmText("Yes").setConfirmButtonBackgroundColor(R.color.colorPrimary).setConfirmButtonTextColor(R.color.colorPrimary)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        deleteItem();
                        sDialog.dismissWithAnimation();




                    }
                }).setCancelButtonBackgroundColor(R.color.colorPrimary).setCancelButtonTextColor(R.color.colorPrimary)
                .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    //deleteItem
    public void deleteItem(){
        SQLiteDatabase mydb = openOrCreateDatabase("eatandtreats", MODE_PRIVATE, null);
        mydb.execSQL("create table if not exists cart(email varchar,caketype varchar,cakename varchar,cakeprice varchar,cakediscount varchar,qty varchar,amount varchar,imageUrl varchar,weight varchar,cakemessage varchar)");
        mydb.execSQL("DELETE FROM cart WHERE cakename='"+iname+"'");
        setItems();
        setDetails();
        setCount();
        setAmount();
    }



}

