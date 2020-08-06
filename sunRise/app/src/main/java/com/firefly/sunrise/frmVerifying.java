package com.firefly.sunrise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.regex.Pattern;

public class frmVerifying extends AppCompatActivity {

    private TextView countdown,notify;
    private TextView countdownlabel;

    private EditText key;
    private Button verifybtn;
    ImageView imgsignup;
    EditText txtemail,txtpw,txtcpw;
    Button btnreg;
    private ProgressBar mProgressCircle;


    String email,pwd;
    String randomCode="empty",rerandomCode;

    int c=0;
    int a=0;

    long maxid=0;
    DatabaseReference reff;
    Regclass regclass;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_verifying);

        firebaseAuth=FirebaseAuth.getInstance();
        assign();
        mProgressCircle.setVisibility(View.GONE);
        hideContext();
        btnReg();
        btnVerify();

    }

    //assign
    public void assign(){
        countdown=findViewById(R.id.txttimer);
        notify=findViewById(R.id.tvnotify);
        countdownlabel=findViewById(R.id.txttimerremaning);
        key=findViewById(R.id.txtverifykey);
        verifybtn=findViewById(R.id.btnverify);
        imgsignup=findViewById(R.id.imageViewSignUp);
        txtemail=findViewById(R.id.txtemail2);
        txtpw=findViewById(R.id.txtpwd2);
        txtcpw=findViewById(R.id.txtcpwd2);
        btnreg=findViewById(R.id.btncreateaccount2);
        mProgressCircle=findViewById(R.id.progressverfy);

    }

    //hideContext
    public void hideContext(){
        countdown.setVisibility(View.INVISIBLE);
        notify.setVisibility(View.INVISIBLE);
        countdownlabel.setVisibility(View.INVISIBLE);
        key.setVisibility(View.INVISIBLE);
        verifybtn.setVisibility(View.INVISIBLE);

    }

    public void hideLlog(){
        txtemail.setVisibility(View.INVISIBLE);
        txtpw.setVisibility(View.INVISIBLE);
        txtcpw.setVisibility(View.INVISIBLE);
        btnreg.setVisibility(View.INVISIBLE);
        imgsignup.setVisibility(View.INVISIBLE);


    }
    //showContext
    public void showContext(){
        countdown.setVisibility(View.VISIBLE);
        notify.setVisibility(View.VISIBLE);
        countdownlabel.setVisibility(View.VISIBLE);
        key.setVisibility(View.VISIBLE);
        verifybtn.setVisibility(View.VISIBLE);
    }

    //assignDetails
    public void assignDetails(){
        email=txtemail.getText().toString();
        pwd=txtpw.getText().toString();
    }

    //Randam
    public void create_algorythm(){
        randomCode.equals(null);
        SecureRandom random = new SecureRandom();
        randomCode = new BigInteger(30, random).toString(32).toUpperCase();

    }

    //validation
    public void validattion(){

        int a=txtpw.getText().toString().length();

        if(! isEmailValid(txtemail.getText().toString())){
            Toast.makeText(frmVerifying.this,"Invalid email",Toast.LENGTH_SHORT).show();
            mProgressCircle.setVisibility(View.GONE);


        }
        else if( txtpw.getText().toString().equals("") || Integer.parseInt(txtpw.getText().toString())<6 ){
            Toast.makeText(frmVerifying.this,"Request strong password",Toast.LENGTH_SHORT).show();
            mProgressCircle.setVisibility(View.GONE);


        }
        else if( txtcpw.getText().toString().equals("")){
            Toast.makeText(frmVerifying.this,"Request comfirm password",Toast.LENGTH_SHORT).show();
            mProgressCircle.setVisibility(View.GONE);


        }
        else if( !txtcpw.getText().toString().equals(txtpw.getText().toString())){
            Toast.makeText(frmVerifying.this,"Password didn't match",Toast.LENGTH_SHORT).show();
            mProgressCircle.setVisibility(View.GONE);

        }
        else if(a<6){
            Toast.makeText(frmVerifying.this,"Password should be more than 6 charactress",Toast.LENGTH_SHORT).show();
            mProgressCircle.setVisibility(View.GONE);
        }

        else{
            //startActivity(new Intent(frmVerifying.this,frmNewHome.class));


            checkEmailExistsOrNot();

        }
    }

    //checkEmail
    void checkEmailExistsOrNot() {
        try{
        firebaseAuth.fetchSignInMethodsForEmail(txtemail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                //  Log.d(TAG,""+task.getResult().getSignInMethods().size());
                if (task.getResult().getSignInMethods().size() == 0) {
                    assignDetails();
                    hideLlog();
                    showContext();
                    create_algorythm();
                    sendMail();
                    mProgressCircle.setVisibility(View.GONE);
                    timer();
                } else {
                    Toast.makeText(frmVerifying.this, "Email already exhist", Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.GONE);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }catch(Exception e){
            Toast.makeText(frmVerifying.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
    //btnReg
    public void btnReg(){
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressCircle.setVisibility(View.VISIBLE);
                validattion();
            }
        });
    }

    //btnVerify
    public void btnVerify(){
    verifybtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(key.getText().toString().equals("")){

                Toast.makeText(frmVerifying.this,"Please enter keycode",Toast.LENGTH_SHORT).show();
            }
            else if(!key.getText().toString().equals(randomCode) ){

                Toast.makeText(frmVerifying.this,"Invalid code",Toast.LENGTH_SHORT).show();

            }
            else {
                mProgressCircle.setVisibility(View.VISIBLE);
                insert();
            }
        }
    });
    }



    public void timer(){

                new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    countdown.setText(millisUntilFinished / 1000 + " sec");
                    countdownlabel.setText("Time remainig");
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {

                    if (c == 1) {
                      //  countdown.setText("Try again later");
                        countdownlabel.setText("");
                        countdown.setText("");
                        Toast.makeText(frmVerifying.this, "please try again later", Toast.LENGTH_SHORT).show();



                    } else {
                        countdown.setText("Resend");
                        countdownlabel.setText("");

                        countdown.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(frmVerifying.this,"resend",Toast.LENGTH_SHORT).show();

                                if (c == 1) {
                                   // countdown.setText("");
                                    Toast.makeText(frmVerifying.this, "please try again later", Toast.LENGTH_SHORT).show();
                                } else {
                                    create_algorythm();
                                    sendMail();
                                    c++;
                                    timer();
                                }
                            }
                        });
                    }

                }}.start();
        }

    public void sendMail(){
        String mail=email;
        String message="Your new verification code is " +randomCode +". Don't share this code with others.";
        String subject="Verification";
        JavaMailAPI javaMailAPI=new JavaMailAPI(this ,mail,subject,message);
        javaMailAPI.execute();

    }

    public void insert(){
        try {

              firebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){
                  mProgressCircle.setVisibility(View.GONE);
                  startActivity(new Intent(frmVerifying.this,frmNewHome.class));
                  finish();

               }
              else{
                  Toast.makeText(frmVerifying.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

              }
            }
        });
    }
    catch(Exception e){
    Toast.makeText(frmVerifying.this, e.getMessage(), Toast.LENGTH_SHORT).show();

    }

    }

    public boolean isEmailValid(String text){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(text).matches();
    }


}
