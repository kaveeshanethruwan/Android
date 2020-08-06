package com.easytipsforhomefarming.easytipsforhomefarmingclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class frmForgotpassword extends AppCompatActivity {

    private EditText email;
    private Button btnverify;

    FirebaseAuth firebaseAuth;
    ProgressBar mProgressCircle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_forgotpassword);
        getSupportActionBar().setTitle("Reset password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressCircle = findViewById(R.id.resetprogressbar);
        mProgressCircle.setVisibility(View.GONE);
        firebaseAuth=FirebaseAuth.getInstance();
        verify();

    }

    //verify
    public void verify() {
        try{
            email = (EditText) findViewById(R.id.txtemail);
            btnverify = (Button) findViewById(R.id.btnverify);

            btnverify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(email.getText().toString().equals("")){

                        Toast.makeText(frmForgotpassword.this, "Provide your email", Toast.LENGTH_SHORT).show();

                    }
                    else if(! isEmailValid(email.getText().toString())){
                        Toast.makeText(frmForgotpassword.this, "Invalid email", Toast.LENGTH_SHORT).show();

                    }

                    else{
                        mProgressCircle.setVisibility(View.VISIBLE);
                        checkEmailExistsOrNot();

                    }
                }
            });

        }
        catch(Exception exception){
            Toast.makeText(frmForgotpassword.this,exception.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    //checkEmail
    void checkEmailExistsOrNot(){
        firebaseAuth.fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                //  Log.d(TAG,""+task.getResult().getSignInMethods().size());
                if (task.getResult().getSignInMethods().size() == 0){
                    Toast.makeText(frmForgotpassword.this, "No user found", Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.GONE);

                }else {
                    sendLink();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    //send link
    public void sendLink(){
        firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mProgressCircle.setVisibility(View.GONE);
                    sweetAlert();

                } else {
                    Toast.makeText(frmForgotpassword.this, "Some thing went wrong", Toast.LENGTH_LONG).show();
                    mProgressCircle.setVisibility(View.GONE);

                }
            }
        });
    }

    //sweetAlert
    public void sweetAlert(){
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success")
                .setContentText("Check your email and follow the given link.")
                .setConfirmText("Ok").setConfirmButtonBackgroundColor(R.color.colorPrimary).setConfirmButtonTextColor(R.color.colorPrimary)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        email.setText("");
                        finish();
                    }
                }).setCancelButtonBackgroundColor(R.color.colorPrimary).setCancelButtonTextColor(R.color.colorPrimary)

                .show();
    }

    //email validation
    public boolean isEmailValid(String text){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(text).matches();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
