package com.firefly.sunrise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class frmBeforeFinish extends AppCompatActivity {

    private RadioGroup rgtype;
    private RadioButton rbgift,rbme;
    private TextView tvname,tvphone;
    private Spinner splocationtype,city;
    private Button btncontinue;
    String [] locations={"House","Apartment","Office","Hospital","School","Funeral Home","Wedding Reception","Other(Including Hotels)"};

    //setCalender
    private ImageView imgdate;
    int year_x,month_x,day_x;
    static  final  int DIALOG_ID=0;
    Date date,dt;
    String ttt="default",email,time1="default";
    DatabaseReference reff;

    //setTime
    private ImageView imgTime;
    int hour_x,minute_x;
    static  final  int DIALOG_TIMEID=1;

    //city
    ArrayList<String> spinnerdatalist;
    ValueEventListener listner;
    ArrayAdapter<String> adapter;

    Button getBtncontinue;
    EditText tvrecivername,tvreciverphone,tvreciveraddress,tvreciveinstructions;
    RadioButton rbcustype;
    Calendar mCalendarOpeningTime,mCalendarClosingTime,finalTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_before_finish);


        getSupportActionBar().setTitle("Step 1 - Reciver's Details");
        setRadioButton();
        setLocations();
        setCalender();
        showDialog();
        showTimeDialog();
        getCity();
        btncontinue();
        //setOpeningAndClosingTimes();


    }

    //btnContinue
    public void btncontinue(){
        getBtncontinue= findViewById(R.id.btncontinue);
        getBtncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvrecivername= findViewById(R.id.txtname);
                tvreciverphone= findViewById(R.id.txtphone);
                tvreciveraddress= findViewById(R.id.txtaddress);
                tvreciveinstructions= findViewById(R.id.txtinstructions);


                if(rgtype.getCheckedRadioButtonId() == -1){
                    Toast.makeText(frmBeforeFinish.this,"Please select Reciver type",Toast.LENGTH_LONG).show();
                }
                else if(!isNameValid(tvrecivername.getText().toString())){
                    Toast.makeText(frmBeforeFinish.this,"Please enter a valid Name",Toast.LENGTH_LONG).show();
                }
                else if(tvreciverphone.getText().toString().equals("") ||tvreciverphone.getText().toString().length()<10){
                    Toast.makeText(frmBeforeFinish.this,"Please enter a valid Contact number",Toast.LENGTH_LONG).show();
                }
                else if(tvreciveraddress.getText().toString().equals("")){
                    Toast.makeText(frmBeforeFinish.this,"Please enter a valid Address",Toast.LENGTH_LONG).show();
                }
              //  else if(city.getSelectedItem()==null){
               //     Toast.makeText(frmBeforeFinish.this,"Please select a City",Toast.LENGTH_LONG).show();
              //  }
              //  else if(splocationtype.getSelectedItem() ==null){
               //     Toast.makeText(frmBeforeFinish.this,"Please select a Location type",Toast.LENGTH_LONG).show();
              //  }
               else if(ttt.equals("default")){
                    Toast.makeText(frmBeforeFinish.this,"Please select a Valid Date",Toast.LENGTH_LONG).show();
                }
                else if(time1.equals("default")){
                    Toast.makeText(frmBeforeFinish.this,"Please select a Valid delivery time)",Toast.LENGTH_LONG).show();
                }
                else{
                    String discription=tvreciveinstructions.getText().toString();
                    if(discription.equals("")){
                        discription="not given";
                    }

                    Intent intent = new Intent(frmBeforeFinish.this, frmBeforeFinishSenderDetails.class);
                    intent.putExtra("custype", rbcustype.getText().toString());
                    intent.putExtra("recivername", tvrecivername.getText().toString());
                    intent.putExtra("reciverphone", tvreciverphone.getText().toString());
                    intent.putExtra("reciveraddress", tvreciveraddress.getText().toString());
                    intent.putExtra("recivercity", city.getSelectedItem().toString());
                    intent.putExtra("reciverlocationtype", splocationtype.getSelectedItem().toString());
                    intent.putExtra("date", ttt);
                    intent.putExtra("time", time1);
                    intent.putExtra("discription", discription);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }

    //getCity
    public void getCity(){
        city= findViewById(R.id.spcity);

        Query get = FirebaseDatabase.getInstance().getReference("CostofSubregion");


        spinnerdatalist = new ArrayList<>();
        adapter=new ArrayAdapter<String>(frmBeforeFinish.this,android.R.layout.simple_spinner_dropdown_item,spinnerdatalist);

        city.setAdapter(adapter);

        listner=get.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot item:dataSnapshot.getChildren()){
                    //  spinnerdatalist.add(item.getValue().toString());

                    // spinnerdatalist.add(item.child("cusname").getValue().toString());
                    spinnerdatalist.add(item.child("subregion").getValue().toString());

                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    //setTime
    public void showTimeDialog(){
        imgTime=findViewById(R.id.imgTime);


        imgTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                hour_x=cal.get(Calendar.HOUR_OF_DAY);
                minute_x=cal.get(Calendar.MINUTE);
                showDialog(DIALOG_TIMEID);
            }
        });
    }


    //verify holiday
    public void verifyHoliday(){
        try {

            DatabaseReference reff= FirebaseDatabase.getInstance().getReference().child("HolyDays");

            reff.orderByChild("holiday").equalTo(ttt).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Toast.makeText(frmBeforeFinish.this,"This date is a holiday for our organization.",Toast.LENGTH_SHORT).show();
                        ttt="default";

                    } else {

                        //sendDate();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        catch (Exception e){
            Toast.makeText(frmBeforeFinish.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


    //date
    public void showDialog(){
        imgdate=findViewById(R.id.imgDate);

        imgdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DIALOG_ID);
            }
        });
    }
    Calendar tmp;
    //setTimeListner
    private TimePickerDialog.OnTimeSetListener tpickerListner = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            hour_x=hourOfDay;
            minute_x=minute;
/*
            tmp = Calendar.getInstance();
            tmp.clear();
            tmp.add(Calendar.HOUR_OF_DAY, hour_x);
            tmp.add(Calendar.MINUTE, minute_x);*/

            mCalendarOpeningTime = Calendar.getInstance();
            mCalendarOpeningTime.set(Calendar.HOUR, 8);
            mCalendarOpeningTime.set(Calendar.MINUTE, 00);
            mCalendarOpeningTime.set(Calendar.AM_PM, Calendar.AM);

            mCalendarClosingTime = Calendar.getInstance();
            mCalendarClosingTime.set(Calendar.HOUR, 6);
            mCalendarClosingTime.set(Calendar.MINUTE, 00);
            mCalendarClosingTime.set(Calendar.AM_PM, Calendar.PM);


            finalTime = Calendar.getInstance();
            finalTime.set(Calendar.HOUR_OF_DAY, hour_x);
            finalTime.set(Calendar.MINUTE, minute_x);
            // finalTime.set(Calendar.AM_PM, Calendar.PM);


            if (finalTime.get(Calendar.AM_PM) == Calendar.AM)
                //am_pm = "AM";
                finalTime.set(Calendar.AM_PM, Calendar.AM);

            else if (finalTime.get(Calendar.AM_PM) == Calendar.PM)
                //  am_pm = "PM";
                finalTime.set(Calendar.AM_PM, Calendar.PM);

            if(finalTime.after(mCalendarOpeningTime) && finalTime.before(mCalendarClosingTime))
            {
               // System.out.println("Your time is in range");
                //Toast.makeText(frmBeforeFinish.this,"Your time is in range",Toast.LENGTH_LONG).show();
                dt = finalTime.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                time1 = sdf.format(dt);

            }
            else
            {
                System.out.println("Your time is not Invalid");
                Toast.makeText(frmBeforeFinish.this,"Your time is not in range",Toast.LENGTH_LONG).show();
                time1="default";
            }


        }
    };

    //dialog
    @Override
    protected Dialog onCreateDialog(int id){
        if(id==DIALOG_ID){
            return new DatePickerDialog(this,dpickerListner,year_x,month_x,day_x);}
        else if(id==DIALOG_TIMEID){
            return new TimePickerDialog(this,tpickerListner,hour_x,minute_x,true);

        }
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

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MMM/dd");
            String today = df.format(c);


                if( date.compareTo(c) == 0 || date.compareTo(c) < 0){
                    Toast.makeText(frmBeforeFinish.this,"Please select a Future Date. You can't get delivery today.",Toast.LENGTH_LONG).show();
                    ttt="default";
                    //showDialog(DIALOG_ID);

                }

                else{
                    if( date.compareTo(c) == 0 || date.compareTo(c) < 0){
                        Toast.makeText(frmBeforeFinish.this,"Please select a Future Date. You can't get delivery today.",Toast.LENGTH_LONG).show();
                        ttt="default";}
                    //Toast.makeText(frmBeforeFinish.this,"right date",Toast.LENGTH_LONG).show();
                     verifyHoliday();
                }

        }
    };

    //setCalender
    public void setCalender(){
        final Calendar cal=Calendar.getInstance();
        year_x=cal.get(Calendar.YEAR);
        month_x=cal.get(Calendar.MONTH);
        day_x=cal.get(Calendar.DAY_OF_MONTH);
        hour_x=cal.get(Calendar.HOUR_OF_DAY);
        minute_x=cal.get(Calendar.MINUTE);

    }

    //setLocations
    public void setLocations(){

        splocationtype=findViewById(R.id.splocationtype);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,locations);
        splocationtype.setAdapter(adapter);

    }

    //setRadioButton
    public void setRadioButton(){
        rgtype= findViewById(R.id.rgtype);
        rbgift= findViewById(R.id.rbgift);
        rbme= findViewById(R.id.rbme);
        tvname= findViewById(R.id.tvname);
        tvphone= findViewById(R.id.tvphone);
        splocationtype= findViewById(R.id.splocationtype);




        rgtype.check(R.id.rbgift);

        int selectedId = rgtype.getCheckedRadioButtonId();
        rbcustype =  findViewById(selectedId);

        tvname.setText("Recipient's Name");
        tvphone.setText("Recipient's Phone");


        rbgift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvname.setText("Recipient's Name");
                tvphone.setText("Recipient's Phone");
            }
        });

        rbme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvname.setText("My Name");
                tvphone.setText("My Phone");

            }
        });
    }

    ///regX
    public boolean isNameValid(String text){

        return text.matches("^([A-Za-z]+)(\\s[A-Za-z]+)*\\s?$");
    }


}
