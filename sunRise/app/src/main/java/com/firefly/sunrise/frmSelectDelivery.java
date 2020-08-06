package com.firefly.sunrise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class frmSelectDelivery extends AppCompatActivity {

    Spinner sweight;
    String [] type={"1","1.5","2"};
    String reciveemail,recivecaketype,recivename,reciveimgurl,reciveprice,recivedis,recivedate,reciveqty,reciveImg;
    TextView addtocart,cakemessage;
    Spinner cakeweight;
    byte[] bytes;
    ImageView imgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_select_delivery);

        setCakeWeght();
        reciveData();
        verifyItem();

    }

    //verify item
    public void verifyItem(){
        cakeweight=findViewById(R.id.txtweight);
        imgstatus=findViewById(R.id.imgstatus);
        cakemessage=findViewById(R.id.txtcakemessage);
        addtocart=findViewById(R.id.adddtocart);

        if(ifExists()){
            addtocart.setText("Item Added");
            Drawable drawable  = getResources().getDrawable(R.drawable.ic_check_box_black_24dp);
            imgstatus.setImageDrawable(drawable);
            //Toast.makeText(frmSelectDelivery.this, "already", Toast.LENGTH_SHORT).show();

        }
        else{
            addtocart.setText("Add To Cart");
            Drawable drawable  = getResources().getDrawable(R.drawable.ic_shopping_cart_black_24dp);
            imgstatus.setImageDrawable(drawable);
            btnAddtoCart();

            // SQLiteDatabase mydb = openOrCreateDatabase("eatandtreats", MODE_PRIVATE, null);
            //  mydb.execSQL("create table if not exists cart(email varchar,caketype varchar,cakename varchar,cakeprice varchar,cakediscount varchar,date vrachar,qty varchar,image bytes)");
            //  mydb.execSQL("insert into cart values('" + reciveemail + "','" + recivecaketype + "','" + recivename + "','" + reciveprice + "','"+recivedis+"','"+recivedate+"','"+reciveqty+"','"+bytes+"')");


        }
    }

    //recivedata
    public void reciveData(){
        reciveemail=getIntent().getStringExtra("sendemail");
        recivecaketype=getIntent().getStringExtra("sendcaketype");
        recivename=getIntent().getStringExtra("sendname");
        reciveimgurl=getIntent().getStringExtra("sendimgurl");
        reciveprice=getIntent().getStringExtra("sendprice");
        recivedis=getIntent().getStringExtra("senddis");
        recivedate=getIntent().getStringExtra("senddate");
        reciveqty=getIntent().getStringExtra("sendqty");
        reciveImg=getIntent().getStringExtra("sendimage");

    }

    //adtocart
    public void btnAddtoCart(){

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(ifExists()){
                    //addtocart.setText("Item Added");
                    Toast.makeText(frmSelectDelivery.this, "Item Already Exhist", Toast.LENGTH_SHORT).show();

                }
                else{

                    if(recivedis.equals("")){
                        recivedis="0";
                    }

                    float totalPrice=0,totalDiscount=0,amount=0;
                    totalPrice=Float.parseFloat(reciveprice) * Float.parseFloat(reciveqty)*Float.parseFloat(sweight.getSelectedItem().toString());
                    totalDiscount=Float.parseFloat(recivedis) * Float.parseFloat(reciveqty)*Float.parseFloat(sweight.getSelectedItem().toString()) ;


                    amount=totalPrice-totalDiscount;
                    int a = (int) amount;

                    String veryfycakemessage;

                    if(cakemessage.getText().toString().equals("")){
                        veryfycakemessage = "not given";
                    }
                    else{
                        veryfycakemessage = cakemessage.getText().toString();

                    }

                    SQLiteDatabase mydb = openOrCreateDatabase("eatandtreats", MODE_PRIVATE, null);
                    mydb.execSQL("create table if not exists cart(email varchar,caketype varchar,cakename varchar,cakeprice varchar,cakediscount varchar,qty varchar,amount varchar,imageUrl varchar,weight varchar,cakemessage varchar)");
                    mydb.execSQL("insert into cart values('" + reciveemail + "','" + recivecaketype + "','" + recivename + "','" + reciveprice + "','"+recivedis+"','"+reciveqty+"','"+a+"','"+reciveImg+"','"+sweight.getSelectedItem().toString()+"','"+veryfycakemessage+"')");
                    addtocart.setText("Item Added");
                    Drawable drawable  = getResources().getDrawable(R.drawable.ic_check_box_black_24dp);
                    imgstatus.setImageDrawable(drawable);
                    startActivity(new Intent(frmSelectDelivery.this,frmCart.class));
                    finish();



                }
            }
        });

    }

    public boolean ifExists()
    {
        SQLiteDatabase mydb = openOrCreateDatabase("eatandtreats", MODE_PRIVATE, null);
        mydb.execSQL("create table if not exists cart(email varchar,caketype varchar,cakename varchar,cakeprice varchar,cakediscount varchar,qty varchar,amount varchar,imageUrl varchar,weight varchar,cakemessage varchar)");

        Cursor cursor = null;
        String checkQuery = "SELECT email FROM cart WHERE cakename= '"+recivename + "'";
        cursor=mydb.rawQuery(checkQuery,null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    //seet spinner
    public void setCakeWeght(){
        sweight=findViewById(R.id.txtweight);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.custom_spinner,type);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        sweight.setAdapter(adapter);
    }
}
