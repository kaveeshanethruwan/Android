package com.homefarming.easytipsforhomefarming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class frmRequestLink extends AppCompatActivity {

    private String user;
    private Button requestLink;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ProgressBar mProgressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_request_link);

        getSupportActionBar().setTitle("Request password reset link");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user=getIntent().getStringExtra("user");
        mProgressCircle = findViewById(R.id.progressbarrequestlink);
        mProgressCircle.setVisibility(View.GONE);
        firebaseAuth=FirebaseAuth.getInstance();

        verify();

    }

    //verify
    public void verify() {
        try{
            requestLink =  findViewById(R.id.btnRequestLink);
            requestLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mProgressCircle.setVisibility(View.VISIBLE);
                    firebaseAuth.sendPasswordResetEmail(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mProgressCircle.setVisibility(View.GONE);
                                sweetAlert();


                            } else {
                                mProgressCircle.setVisibility(View.GONE);

                                Toast.makeText(frmRequestLink.this, "Something went wrong", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                }
            });

        }
        catch(Exception exception){
            Toast.makeText(frmRequestLink.this,exception.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    //sweetAlert
    public void sweetAlert(){
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success")
                .setContentText("Check your emails and follow the given link.")
                .setConfirmText("Ok").setConfirmButtonBackgroundColor(R.color.colorPrimary).setConfirmButtonTextColor(R.color.colorPrimary)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        finish();
                    }
                }).setCancelButtonBackgroundColor(R.color.colorPrimary).setCancelButtonTextColor(R.color.colorPrimary)

                .show();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
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
