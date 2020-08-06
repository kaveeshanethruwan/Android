package com.firefly.sunrise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class frmUserEdit extends AppCompatActivity {


    String email;
    private Button btnverify;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ProgressBar mProgressCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_user_edit);

        mProgressCircle = findViewById(R.id.progressbarupdate);

        mProgressCircle.setVisibility(View.GONE);

        firebaseAuth=FirebaseAuth.getInstance();

       // mProgressCircle.bringToFront();

        getEmail();
        verify();

    }

    //getEmail
    public void getEmail(){
        try {
            firebaseUser = firebaseAuth.getCurrentUser();
            email = firebaseUser.getEmail();
        }
        catch (Exception e)
        {
            Toast.makeText(frmUserEdit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //verify
    public void verify() {
        try{
            btnverify =  findViewById(R.id.btnupdate);

            btnverify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        mProgressCircle.setVisibility(View.VISIBLE);


                        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mProgressCircle.setVisibility(View.GONE);
                                    sweetAlert();


                                } else {
                                    mProgressCircle.setVisibility(View.GONE);

                                    Toast.makeText(frmUserEdit.this, "No user avalable", Toast.LENGTH_LONG).show();

                                }
                            }
                        });

                }
            });

        }
        catch(Exception exception){
            Toast.makeText(frmUserEdit.this,exception.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    //sweetAlert
    public void sweetAlert(){
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success")
                .setContentText("Check your Email and follow the given link.")
                .setConfirmText("Ok").setConfirmButtonBackgroundColor(R.color.colorPrimary).setConfirmButtonTextColor(R.color.colorPrimary)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

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
