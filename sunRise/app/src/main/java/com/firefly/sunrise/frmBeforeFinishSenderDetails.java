package com.firefly.sunrise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class frmBeforeFinishSenderDetails extends AppCompatActivity {

    String custype,recivername,reciverphone,reciveraddress,recivercity,reciverlocationtype,date,time,discription,sendermessage,sendername;
    EditText sname,smessage;
    CheckBox senderoption,senderoptionMessage;
    Button btncontinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_before_finish_sender_details);

        getSupportActionBar().setTitle("Step 2 - Sender's Details");
        setFromBack();
        setVar();
        btnContinue();
    }

    //btnContinue
    public void btnContinue(){

        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sname.getText().toString().equals("")){
                    Toast.makeText(frmBeforeFinishSenderDetails.this,"Please Fill Sender Neme",Toast.LENGTH_LONG).show();
                }
                else if(smessage.getText().toString().equals("")){
                    Toast.makeText(frmBeforeFinishSenderDetails.this,"Please Fill Sender Message",Toast.LENGTH_LONG).show();
                }
                else{
                    sendermessage=smessage.getText().toString();
                    sendername=sname.getText().toString();

                    Intent intent = new Intent(frmBeforeFinishSenderDetails.this, frmFinish.class);
                    intent.putExtra("custype", custype);
                    intent.putExtra("recivername", recivername);
                    intent.putExtra("reciverphone", reciverphone);
                    intent.putExtra("reciveraddress", reciveraddress);
                    intent.putExtra("recivercity", recivercity);
                    intent.putExtra("reciverlocationtype", reciverlocationtype);
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    intent.putExtra("discription", discription);
                    intent.putExtra("sendername", sendername);
                    intent.putExtra("sendermessage", sendermessage);
                    startActivity(intent);
                    finish();

                }
            }
        });



    }
    //setVar
    public void  setVar(){
        sname= findViewById(R.id.txtsendername);
        smessage= findViewById(R.id.txtsendermessage);
        senderoption= findViewById(R.id.checkname);
        btncontinue= findViewById(R.id.btncontinue);

        senderoption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    sname.setText("Anonymous (No Sender Name)");
                }
                else{
                    sname.setText("");
                }

            }
        });

        senderoptionMessage= findViewById(R.id.checkmessage);
        senderoptionMessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    smessage.setText("No Message/Greetings");
                }
                else{
                    smessage.setText("");
                }

            }
        });
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
    }
}
