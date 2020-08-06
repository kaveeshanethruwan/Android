package com.firefly.sunrise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class frmTakeOrder extends AppCompatActivity {

     TextView textViewname;
     TextView textViewdes;
     TextView textViewprice;
     TextView textViewdis;
     ImageView imageView;

     EditText txtqty;
   //  TextView tvdate;
     Button btnverify;
     int countDate;


   // private ImageView imgdate;
    int year_x,month_x,day_x;
    static  final  int DIALOG_ID=0;
    Date date;
    String ttt,email;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    DatabaseReference reff;

    //getImageUrl
    String name,imgUrl,des,price,dis;
    int imgStatus,countLikedPeople;
    ImageView favHeart,addtocartimg,imgsend;
    FavModel favModel;
    TextView flabel,addtocarttv,addtocart;

    String caketype;

    byte[] bytes;
    Bitmap bmp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_take_order);

        firebaseAuth=FirebaseAuth.getInstance();

        setFromRec();
        getCakeType();

        getEmail();
        checkAlreadyLike();
     //   countLikedPeople();
        getImageUrl();
        likeBtn();
       // getCakeType();
        setDateV();
        showDialog();
        verifyOrder();
        send();

    }

    //send
    public void send(){
        imgsend=findViewById(R.id.imgSend);
        imgsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_SEND);

                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, name);
                i.putExtra(Intent.EXTRA_TEXT, des);

                startActivity(Intent.createChooser(i, "Share via"));
            }
        }
        );

    }


    //getTypeOfCake
    public void getCakeType(){
        Query get = FirebaseDatabase.getInstance().getReference("Items").orderByChild("name").equalTo(name);

        get.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    caketype=ds.child("type").getValue().toString();
                  //   Toast.makeText(frmTakeOrder.this,caketype, Toast.LENGTH_SHORT).show();
                    verifyItem();
                    if(caketype.equals("Functioncake")||caketype.equals("NormalCake")){
                    }
                    else{
                        btnverify.setVisibility(View.INVISIBLE);

                    }

                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    //ifExists
    public boolean ifExists()
    {
        SQLiteDatabase mydb = openOrCreateDatabase("eatandtreats", MODE_PRIVATE, null);
        mydb.execSQL("create table if not exists cart(email varchar,caketype varchar,cakename varchar,cakeprice varchar,cakediscount varchar,qty varchar,amount varchar,imageUrl varchar,weight varchar,cakemessage varchar)");

        Cursor cursor = null;
        String checkQuery = "SELECT email FROM cart WHERE cakename= '"+name + "'";
        cursor=mydb.rawQuery(checkQuery,null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    //setFromRecycleview
    public void setFromRec(){
        textViewname=findViewById(R.id.nName);
        textViewdes=findViewById(R.id.nDescription);
        textViewprice=findViewById(R.id.nPrice);
        textViewdis=findViewById(R.id.nDiscount);
        imageView=findViewById(R.id.nImageView);
        addtocartimg=findViewById(R.id.imgaddtocart);
        addtocarttv=findViewById(R.id.tvaddtocart);

        bytes=getIntent().getByteArrayExtra("image");
        name=getIntent().getStringExtra("name");
        des=getIntent().getStringExtra("des");
        price=getIntent().getStringExtra("price");
        dis=getIntent().getStringExtra("dis");

        bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);

        textViewname.setText(name);
        textViewdes.setText("Description : "+des);
        textViewprice.setText("Rs."+price+".00");
        if(dis.equals("")){
            textViewdis.setText("No offer avalable");

        }
        else{
            textViewdis.setText("Offer Rs."+dis+" from latest price");
            textViewdis.setTextColor(Color.parseColor("#D51031"));
            textViewdis.setTypeface(Typeface.DEFAULT_BOLD);


        }
        imageView.setImageBitmap(bmp);
    }

    //adtocart
    public void btnAddtoCart(){

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtqty.getText().toString().equals("")){
                    Toast.makeText(frmTakeOrder.this, "Set a Quantity", Toast.LENGTH_SHORT).show();

                }else {
                if(ifExists()){
                    //addtocart.setText("Item Added");
                    Toast.makeText(frmTakeOrder.this, "Item Already Exhist", Toast.LENGTH_SHORT).show();

                }
                else{

                    String weight="n/a",message="n/a";
                    if(dis.equals("")){
                        dis="0";
                    }
                   // Toast.makeText(frmTakeOrder.this, dis, Toast.LENGTH_SHORT).show();

                  //  byte[] data = getBitmapAsByteArray(bmp);

                    int totalPrice=0,totalDiscount=0,amount=0;
                    totalPrice=Integer.parseInt(price) *Integer.parseInt(txtqty.getText().toString());
                    totalDiscount=Integer.parseInt(dis) *Integer.parseInt(txtqty.getText().toString());
                    amount=totalPrice-totalDiscount;

                    SQLiteDatabase mydb = openOrCreateDatabase("eatandtreats", MODE_PRIVATE, null);
                    mydb.execSQL("create table if not exists cart(email varchar,caketype varchar,cakename varchar,cakeprice varchar,cakediscount varchar,qty varchar,amount varchar,imageUrl varchar,weight varchar,cakemessage varchar)");
                    mydb.execSQL("insert into cart values('" + email + "','" + caketype + "','" + name + "','" + price + "','"+dis+"','"+txtqty.getText().toString()+"','"+String.valueOf(amount)+"','"+imgUrl+"','"+weight+"','"+message+"')");
                    addtocart.setText("Item Added");
                    Drawable  drawable  = getResources().getDrawable(R.drawable.ic_check_box_black_24dp);
                    addtocartimg.setImageDrawable(drawable);
                    startActivity(new Intent(frmTakeOrder.this,frmCart.class));
                    finish();


                }
                }
            }
        });

    }

    //converting
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }


    //current user
    public void getEmail(){
        try {
            firebaseUser = firebaseAuth.getCurrentUser();
            email = firebaseUser.getEmail();
        }
        catch (Exception e)
        {
            Toast.makeText(frmTakeOrder.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //getImageUrl
    public void getImageUrl(){
        try{

            Query get = FirebaseDatabase.getInstance().getReference("Items").orderByChild("name").equalTo(name);

            get.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        imgUrl=ds.child("imageUrl").getValue().toString();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //pDialog.dismissWithAnimation();

        }
        catch (Exception e)
        {
            Toast.makeText(frmTakeOrder.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    //checkAlreadyLike
    public void checkAlreadyLike(){
        try{
            reff = FirebaseDatabase.getInstance().getReference().child("MyFavourites").child(encodeUserEmail(email));

            reff.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        imgStatus=0;
                        Drawable drawable  = getResources().getDrawable(R.drawable.ic_favorite_black_24dp);
                        favHeart.setImageDrawable(drawable);

                    }
                    else{
                        imgStatus=1;
                        Drawable  drawable  = getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp);
                        favHeart.setImageDrawable(drawable);
                        flabel.setText("Click here to Add Favourites");


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        catch (Exception e){
            Toast.makeText(frmTakeOrder.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    //countLikedPeople
    public void countLikedPeople(){
       try {
         //  Query applesQuery = FirebaseDatabase.getInstance().getReference().child("MyFavourites").child(encodeUserEmail(email)).orderByChild("name").equalTo(name);
         //  Toast.makeText(frmTakeOrder.this,name,Toast.LENGTH_SHORT).show();
           Query getNew = FirebaseDatabase.getInstance().getReference("MyFavourites").orderByKey();//.child(p).orderByChild("name").equalTo(name);
          // Query getNew = FirebaseDatabase.getInstance().getReference("MyFavourites").or;
           getNew.orderByChild("name").equalTo(name).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  countLikedPeople=(int) dataSnapshot.getChildrenCount();

                   Toast.makeText(frmTakeOrder.this,String.valueOf(countLikedPeople),Toast.LENGTH_SHORT).show();

               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });

       }catch(Exception e) {
           Toast.makeText(frmTakeOrder.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    //addtoFav
    public void likeBtn(){
        try {
            favHeart = findViewById(R.id.imgFav);
            flabel = findViewById(R.id.favlabel);

            favHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                       if (imgStatus==0) {
                           removeFromFav();
                       }
                       else{
                           addToFav();
                       }
                }
            });
        }
        catch (Exception e){
            Toast.makeText(frmTakeOrder.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    //addToFav
    public void addToFav(){
        favModel = new FavModel();
        reff = FirebaseDatabase.getInstance().getReference().child("MyFavourites").child(encodeUserEmail(email));

        String fname = name.trim();
        String favdes = des.trim();
        Integer favprice = Integer.parseInt(price.trim());
        Integer favdis;
        if(dis.equals("")){
            String disValue="0";
            favdis = Integer.parseInt(disValue.trim());

        }
        else{
            favdis = Integer.parseInt(dis.trim());
        }
        String favurl = imgUrl.trim();

        favModel.setName(fname);
        favModel.setDescription(favdes);
        favModel.setPrice(favprice);
        favModel.setDiscount(favdis);
        favModel.setImageUrl(favurl);

        reff.push().setValue(favModel);
        Drawable drawable  = getResources().getDrawable(R.drawable.ic_favorite_black_24dp);
        favHeart.setImageDrawable(drawable);
        imgStatus=0;
        flabel.setText("");


    }

    //removeFromFav
    public void removeFromFav(){

        Query applesQuery = FirebaseDatabase.getInstance().getReference().child("MyFavourites").child(encodeUserEmail(email)).orderByChild("name").equalTo(name);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    imgStatus=1;
                    flabel.setText("Click here to Add Favourites");
                    Drawable  drawable  = getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp);
                    favHeart.setImageDrawable(drawable);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    //encodeEmail
    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }

    //setdateVariable
    public void setDateV(){
        final Calendar cal=Calendar.getInstance();
        year_x=cal.get(Calendar.YEAR);
        month_x=cal.get(Calendar.MONTH);
        day_x=cal.get(Calendar.DAY_OF_MONTH);
    }

    //date
    public void showDialog(){
      //  imgdate=findViewById(R.id.imgDate);

//        imgdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                showDialog(DIALOG_ID);
//            }
//        });
    }

    //dialog
    @Override
    protected Dialog onCreateDialog(int id){
        if(id==DIALOG_ID)
            return new DatePickerDialog(this,dpickerListner,year_x,month_x,day_x);
        return  null;
    }

    //setListner
    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            year_x = year;
            month_x = month + 1;
            day_x = dayOfMonth;

            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(Calendar.MONTH, month_x - 1);
            calendar.set(Calendar.YEAR, year_x);
            calendar.set(Calendar.DAY_OF_MONTH, day_x);

            date = calendar.getTime();


            SimpleDateFormat dfdf = new SimpleDateFormat("yyyy/MMM/dd");
            ttt = dfdf.format(date);

         //   tvdate.setText(ttt);
          //  Toast.makeText(frmTakeOrder.this, ttt, Toast.LENGTH_SHORT).show();




        }
    };


    //verify order
    public void verifyOrder(){
        txtqty=findViewById(R.id.txtqty);
       // tvdate=findViewById(R.id.txtdate);
        btnverify=findViewById(R.id.btnverifyorder);

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtqty.getText().toString().equals("0")||txtqty.getText().toString().equals("")){
                    Toast.makeText(frmTakeOrder.this, "Please Enter a Valid Quantity", Toast.LENGTH_SHORT).show();
                }
               // else if(tvdate.getText().toString().equals("")){
                //    Toast.makeText(frmTakeOrder.this, "Select a  Delivery Date", Toast.LENGTH_SHORT).show();

              //  }
          //      else{

                 //   Date c = Calendar.getInstance().getTime();
                  //  SimpleDateFormat df = new SimpleDateFormat("yyyy/MMM/dd");
                  //  String today = df.format(c);

                 //   if(date.compareTo(c) == 0 || date.compareTo(c) < 0){
                   //     Toast.makeText(frmTakeOrder.this,"Please Select Future Date",Toast.LENGTH_LONG).show();
                   //     tvdate.setText("");

                 //   }
                    else{
                        checkdate();
                    }

              //  }

            }
        });
    }

    //setAddToCart
    public void setAddToCart(){

    }
    //veryfty work day or not
    public void checkdate(){
        try{
        reff= FirebaseDatabase.getInstance().getReference().child("HolyDays");
        reff.orderByChild("holiday").equalTo(ttt).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Toast.makeText(frmTakeOrder.this,"Sorry.This date we will not provide our service!",Toast.LENGTH_SHORT).show();
                    String title="Service Unavailable";
                    String context="Sorry.This date we will not provide our service!.Please check another date.";
                    String ok="ok";

                    sweetAlert(title,context,ok);
                  //  tvdate.setText("");

                } else {

                   // checkOrderAmount();

                    if(caketype.equals("Functioncake")  || caketype.equals("NormalCake")){
                       // startActivity(new Intent(frmTakeOrder.this,frmSelectDelivery.class));

                        Drawable xdrawable = imageView.getDrawable();
                        Bitmap xbitmap = ((BitmapDrawable) xdrawable).getBitmap();


                        Intent intent = new Intent(frmTakeOrder.this,frmSelectDelivery.class);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        xbitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bytes = stream.toByteArray();

                        intent.putExtra("sendemail", email);
                        intent.putExtra("sendcaketype", caketype);
                        intent.putExtra("sendname", name);
                        intent.putExtra("sendurl", imgUrl);
                        intent.putExtra("sendprice", price);
                        intent.putExtra("senddis", dis);
                        intent.putExtra("sendqty",txtqty.getText().toString() );
                        intent.putExtra("senddate", ttt);
                        intent.putExtra("sendimage", imgUrl);


                        startActivity(intent);
                        finish();

                    }
                    else{
                        addtocarttv.setText("ADD TO CART");
                        Drawable drawable  = getResources().getDrawable(R.drawable.ic_shopping_cart_black_24dp);
                        addtocartimg.setImageDrawable(drawable);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
        catch (Exception e){
        Toast.makeText(frmTakeOrder.this,e.getMessage(),Toast.LENGTH_SHORT).show();

    }

    }

    //check order amount
//    public void checkOrderAmount(){
//        try{
//
//            reff= FirebaseDatabase.getInstance().getReference().child("Orders");
//            reff.child(tvdate.getText().toString()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    if(dataSnapshot.exists()){
//                        countDate=(int) dataSnapshot.getChildrenCount();
//                    }
//                    else{
//                        countDate=0;
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
//        catch (Exception e){
//            Toast.makeText(frmTakeOrder.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//
//        }
//    }

    public void sweetAlert(String title,String context,String accept){
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(title)
                .setContentText(context)
                .setCustomImage(R.drawable.ic_sentiment_dissatisfied_black_24dp)
                .setConfirmText(accept).setConfirmButtonBackgroundColor(R.color.btncolor).setConfirmButtonTextColor(R.color.text_color)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        sDialog.dismissWithAnimation();



                    }
                })//.setCancelButtonBackgroundColor(R.color.colorPrimary).setCancelButtonTextColor(R.color.colorPrimary)
              //  .setCancelButton("Ignore", new SweetAlertDialog.OnSweetClickListener() {
                   // @Override
                 //   public void onClick(SweetAlertDialog sDialog) {
               //         sDialog.dismissWithAnimation();
              //      }
              //  })
                .show();
    }

    //verify item
    public void verifyItem(){
        // cakeweight=findViewById(R.id.txtweight);
        //cakemessage=findViewById(R.id.txtcakemessage);
        addtocart=findViewById(R.id.tvaddtocart);

        if(ifExists()){
            addtocart.setText("Item Added");
            Drawable  drawable  = getResources().getDrawable(R.drawable.ic_check_box_black_24dp);
            addtocartimg.setImageDrawable(drawable);
            //Toast.makeText(frmSelectDelivery.this, "already", Toast.LENGTH_SHORT).show();

        }
        else{

            try {

                if (caketype.equals("Functioncake") || caketype.equals("NormalCake")) {

                }
                else {
                   // Toast.makeText(frmTakeOrder.this, caketype, Toast.LENGTH_SHORT).show();
                    addtocart.setText("Add To Cart");
                    Drawable  drawable  = getResources().getDrawable(R.drawable.ic_shopping_cart_black_24dp);
                    addtocartimg.setImageDrawable(drawable);
                    btnAddtoCart();
                }
                // addtocart.setText("Add To Cart");
                // btnAddtoCart();

                // SQLiteDatabase mydb = openOrCreateDatabase("eatandtreats", MODE_PRIVATE, null);
                //  mydb.execSQL("create table if not exists cart(email varchar,caketype varchar,cakename varchar,cakeprice varchar,cakediscount varchar,date vrachar,qty varchar,image bytes)");
                //  mydb.execSQL("insert into cart values('" + reciveemail + "','" + recivecaketype + "','" + recivename + "','" + reciveprice + "','"+recivedis+"','"+recivedate+"','"+reciveqty+"','"+bytes+"')");
            }
            catch (Exception e){
                Toast.makeText(frmTakeOrder.this,e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
