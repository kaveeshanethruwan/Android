package com.firefly.sunrise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class frmFinish extends AppCompatActivity {

    private String custype,recivername,reciverphone,reciveraddress,recivercity,reciverlocationtype,date,time,discription,sendermessage,sendername;
    private TextView rname,rcontactnumber,raddress,rddate,rtime,ritemprice,rdeliveryprice,rtotalprice;
    int sqlcount,sqlamount,deliveryCost;
    private Button btnpayment;
    SweetAlertDialog pDialog;

    DatabaseReference reff;
    long maxid=0;
    int id;
    Order_header oh;
    Order_detail od;

    String email;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String gcaketype[];
    String gcakename[];
    String gcakeprice[] ;
    String gcakediscount[];
    String gqty[] ;
    String gweight[];
    String gcakemessage[] ;
    String gamount[] ;

    String formattedDate;
    int buttonCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_finish);

        firebaseAuth=FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Everything Ready...");
        startupDb();
        getEmail();
        setFromBack();
        assignVar();
        setAmount();
        getDeleveryCost();
        setBtnpayment();
    }


    //getImageUrl
    public void getDeleveryCost(){
        try{

            Query get = FirebaseDatabase.getInstance().getReference("CostofSubregion").orderByChild("subregion").equalTo(recivercity);

            get.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        deliveryCost=Integer.parseInt(ds.child("cost").getValue().toString());
                        rdeliveryprice.setText(String.valueOf(deliveryCost));
                        rtotalprice.setText(String.valueOf(deliveryCost +sqlamount));
                       // Toast.makeText(frmFinish.this,String.valueOf(deliveryCost),Toast.LENGTH_SHORT).show();

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
            Toast.makeText(frmFinish.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void setAmount() {
        SQLiteDatabase mydb = openOrCreateDatabase("eatandtreats", MODE_PRIVATE, null);
        mydb.execSQL("create table if not exists cart(email varchar,caketype varchar,cakename varchar,cakeprice varchar,cakediscount varchar,qty varchar,amount varchar,imageUrl varchar,weight varchar,cakemessage varchar)");
        Cursor mCount = mydb.rawQuery("select sum(amount) from cart", null);
        mCount.moveToFirst();
        sqlamount = mCount.getInt(0);
        ritemprice.setText(String.valueOf(sqlamount));
        mCount.close();
        // Toast.makeText(frmCart.this,String.valueOf(count),Toast.LENGTH_SHORT).show();
    }

    //setFromBack
    public void setFromBack(){
        custype=getIntent().getStringExtra("custype");
        recivername=getIntent().getStringExtra("recivername");
        reciverphone=getIntent().getStringExtra("reciverphone");
        reciveraddress=getIntent().getStringExtra("reciveraddress");
        recivercity=getIntent().getStringExtra("recivercity");
        reciverlocationtype=getIntent().getStringExtra("reciverlocationtype");
        date=getIntent().getStringExtra("date");
        time=getIntent().getStringExtra("time");
        discription=getIntent().getStringExtra("discription");
        sendername=getIntent().getStringExtra("sendername");
        sendermessage=getIntent().getStringExtra("sendermessage");
    }

    //assign Variables
    public void assignVar(){
        rname = findViewById(R.id.fname);
        rcontactnumber = findViewById(R.id.fcontactnumber);
        raddress = findViewById(R.id.faddress);
        rddate = findViewById(R.id.fddate);
        rtime = findViewById(R.id.ftime);
        ritemprice = findViewById(R.id.fitemprice);
        rdeliveryprice = findViewById(R.id.fdeliveryprice);
        rtotalprice = findViewById(R.id.ftotalprice);
        btnpayment = findViewById(R.id.btnpayment);


        rname.setText(recivername);
        rcontactnumber.setText(reciverphone);
        raddress.setText(reciveraddress);
        rddate.setText(date);
        rtime.setText(time);


    }

    //click event of payment button
    public void setBtnpayment(){
        btnpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(buttonCount==1){
                   Toast.makeText(frmFinish.this,"Please Wait...",Toast.LENGTH_SHORT).show();

               }
               else{
                   progressing();

               }
            }
        });
    }

    //show fetching...
    public void progressing(){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching...");
        pDialog.setCancelable(false);
        pDialog.show();
        setCount();

    }
    Cursor mCakeType;

    //fetch data to arrays
    public void setCount() {
        SQLiteDatabase mydb = openOrCreateDatabase("eatandtreats", MODE_PRIVATE, null);
        mydb.execSQL("create table if not exists cart(email varchar,caketype varchar,cakename varchar,cakeprice varchar,cakediscount varchar,qty varchar,amount varchar,imageUrl varchar,weight varchar,cakemessage varchar)");
        Cursor mCount = mydb.rawQuery("select count(*) from cart", null);
        mCount.moveToFirst();
        sqlcount = mCount.getInt(0);
        mCount.close();

         gcaketype = new String[sqlcount];
         gcakename= new String[sqlcount];
         gcakeprice = new String[sqlcount];
         gcakediscount = new String[sqlcount];
         gqty = new String[sqlcount];
         gweight = new String[sqlcount];
         gcakemessage = new String[sqlcount];
         gamount = new String[sqlcount];

        for(int i=0;i<sqlcount;i++) {

            mCakeType = mydb.rawQuery("select caketype,cakename,cakeprice,cakediscount,qty,amount,weight,cakemessage from cart", null);
            mCakeType.moveToPosition(i);

            gcaketype[i] = mCakeType.getString(0);
            gcakename[i] = mCakeType.getString(1);
            gcakeprice[i] = mCakeType.getString(2);
            gcakediscount[i] = mCakeType.getString(3);
            gqty[i] = mCakeType.getString(4);
            gamount[i] = mCakeType.getString(5);
            gweight[i] = mCakeType.getString(6);
            gcakemessage[i] = mCakeType.getString(7);

        }

        mCakeType.close();

        /*Toast.makeText(frmFinish.this,String.valueOf(caketype[0]),Toast.LENGTH_SHORT).show();
        Toast.makeText(frmFinish.this,String.valueOf(cakename[0]),Toast.LENGTH_SHORT).show();
        Toast.makeText(frmFinish.this,String.valueOf(cakeprice[0]),Toast.LENGTH_SHORT).show();
        Toast.makeText(frmFinish.this,String.valueOf(cakediscount[0]),Toast.LENGTH_SHORT).show();
        Toast.makeText(frmFinish.this,String.valueOf(qty[0]),Toast.LENGTH_SHORT).show();
        Toast.makeText(frmFinish.this,String.valueOf(weight[0]),Toast.LENGTH_SHORT).show();
        Toast.makeText(frmFinish.this,String.valueOf(cakemessage[0]),Toast.LENGTH_SHORT).show();
        Toast.makeText(frmFinish.this,String.valueOf(amount[0]),Toast.LENGTH_SHORT).show();*/

         send();
    }

    //count id
    public void startupDb(){

        reff = FirebaseDatabase.getInstance().getReference().child("Order_header");

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxid = dataSnapshot.getChildrenCount();
                   // Toast.makeText(frmFinish.this,String.valueOf(maxid),Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //get email
    public void getEmail(){
        try {
            firebaseUser = firebaseAuth.getCurrentUser();
            email = firebaseUser.getEmail();
        }
        catch (Exception e)
        {
            Toast.makeText(frmFinish.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //send to tables
    public void send(){

        //-------------------send data to header----------------------------//

        oh=new Order_header();
        reff= FirebaseDatabase.getInstance().getReference().child("Order_header");

        id = Integer.parseInt(String.valueOf(maxid))+8080;

        Date c = Calendar.getInstance().getTime();
       // SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        formattedDate = df.format(c);

        oh.setOrder_id(id);
        oh.setCus_email(email);
        oh.setOrder_date(formattedDate);
        oh.setStatus_changed_date(formattedDate);
        oh.setDeliverycost(deliveryCost);
        oh.setItem_total(sqlamount);
        oh.setNet_total(sqlamount+deliveryCost);
        oh.setStatus("pending");
        oh.setCustomer_type(custype);
        oh.setReciver_name(recivername);
        oh.setReciver_contact_number(reciverphone);
        oh.setReciver_address(reciveraddress);
        oh.setReciver_city(recivercity);
        oh.setReciver_location_type(reciverlocationtype);
        oh.setDelivery_date(date);
        oh.setDelivery_time(time);
        oh.setDescription(discription);
        oh.setSender_name(sendername);
        oh.setSender_message(sendermessage);

        reff.push().setValue(oh);

        //------------------send data to detail-----------------------------//

        od=new Order_detail();
        reff= FirebaseDatabase.getInstance().getReference().child("Order_detail");

        for(int i = 0; i<sqlcount ; i++){

            od.setOrder_id(id);
            od.setItem_id(i+1);
            od.setCaketype(gcaketype[i]);
            od.setCakename(gcakename[i]);
            od.setCakeprice(gcakeprice[i]);
            od.setCakediscount(gcakediscount[i]);
            od.setQty(gqty[i]);
            od.setWeight(gweight[i]);
            od.setCakemessage(gcakemessage[i]);
            od.setAmount(gamount[i]);

            reff.push().setValue(od);


        }
        sendMail();
        //popup();

    }

    //send orderlist to admin by email
    public void sendMail(){
        buttonCount=1;
        StringBuilder productNamesBuilder = new StringBuilder();

        productNamesBuilder.append("Order ID : "+id+"\n"+"Customer Email : "+email+"\n"+"Order Date : "+formattedDate+"\n"
                +"Delivery Cost : "+deliveryCost+"\n"+"Item Total : "+sqlamount+"\n"

                +"Net Total : "+(sqlamount+deliveryCost)+"\n"+"Customer Type : "+custype+"\n"
                +"Reciver Name : "+recivername+"\n"+"Reciver Contact Number : "+reciverphone+"\n"
                +"Reciver Address : "+reciveraddress+"\n"+"Reciver City : "+recivercity+"\n"
                +"Reciver Location Type : "+reciverlocationtype+"\n"+"Delivery Date : "+date+"\n"
                +"Delivery Time : "+time+"\n"+"Description : "+discription+"\n"
                +"Sender Name : "+sendername+"\n"+"Sender Message : "+sendermessage+"\n"+"\n"
        );
        //productNamesBuilder.append(id+"\n"+gender+"\n"+"\n");

        for(int i = 0; i < sqlcount; i++){
            // productNamesBuilder.append((i + 1) +" . name - "+ " " + name[i]+" age - "+age[i] + "\n");

            productNamesBuilder.append("Item - "+(i+1)+ "\n");
            productNamesBuilder.append("Order ID - "+id+ "\n"+"Cake Type - "+gcaketype[i]+ "\n"+
                                       "Cake Name - "+ gcakename[i]+ "\n"+"Cake Price - "+gcakeprice[i]+ "\n"+
                                       "Cake Discount - "+ gcakediscount[i]+ "\n"+"Quantity - "+gqty[i]+ "\n"+
                                       "Weight - "+ gweight[i]+ "\n"+"Cake Message - "+gcakemessage[i]+ "\n"+
                                       "Amount - "+ gamount[i]+"\n"+"\n");

        }
        String message = productNamesBuilder.toString();


        String mail="kaveesha.james151@gmail.com";
        String subject="New Order from "+email+"";
        JavaMailAPI javaMailAPI=new JavaMailAPI(this ,mail,subject,message);
        javaMailAPI.execute();
        pDialog.dismiss();


        String mail2=email;
        String subject2="Hello from EatsAndTreats...";
        String message2="Dear valuable customer"+"\n"+"Your order id is "+id+" and it is successfully placed. We will bring it to your doors soon."+"\n"+"Your Faithfully EatsAndTreats Team";
        JavaMailAPI javaMailAPI2=new JavaMailAPI(this ,mail2,subject2,message2);
        javaMailAPI2.execute();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                popup();
            }
        }, 10000);


    }

    public void popup(){

        SQLiteDatabase mydb = openOrCreateDatabase("eatandtreats", MODE_PRIVATE, null);
        mydb.execSQL("create table if not exists cart(email varchar,caketype varchar,cakename varchar,cakeprice varchar,cakediscount varchar,qty varchar,amount varchar,imageUrl varchar,weight varchar,cakemessage varchar)");
        mydb.execSQL("delete from cart");


        //sweet alert
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Thank you")
                .setContentText("Your order is successfully placed.Do you want to see your order?")
                .setConfirmText("Yes").setConfirmButtonBackgroundColor(R.color.colorPrimary).setConfirmButtonTextColor(R.color.colorPrimary)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        startActivity(new Intent(frmFinish.this,frmMyOrders.class));
                        finish();
                        sDialog.dismissWithAnimation();

                    }
                }).setCancelButtonBackgroundColor(R.color.colorPrimary).setCancelButtonTextColor(R.color.colorPrimary)
                .setCancelText("Not now")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(new Intent(frmFinish.this,frmNewHome.class));
                        finish();
                        sweetAlertDialog.dismissWithAnimation();

                    }
                })
                .show();

    }


}
