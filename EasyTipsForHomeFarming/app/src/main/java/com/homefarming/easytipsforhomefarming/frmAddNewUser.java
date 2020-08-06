package com.homefarming.easytipsforhomefarming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class frmAddNewUser extends AppCompatActivity {

    private EditText email;
    private EditText pwd;
    private Button addUser;
    private ProgressBar mProgressCircle;

    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_add_new_user);
        getSupportActionBar().setTitle("Add new user");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth=FirebaseAuth.getInstance();

        addUser = (Button) findViewById(R.id.btnAddUser);
        mProgressCircle = findViewById(R.id.mainprogresscycle);

        mProgressCircle.setVisibility(View.GONE);

        addEvent();

    }

    //Login
    public void addEvent() {
        try{

            email = (EditText) findViewById(R.id.txtemail);
            pwd = (EditText) findViewById(R.id.txtpwd);

            addUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (email.getText().toString().equals("")) {

                        Toast.makeText(frmAddNewUser.this, "Provide your email", Toast.LENGTH_SHORT).show();

                    } else if (!isEmailValid(email.getText().toString())) {
                        Toast.makeText(frmAddNewUser.this, "Invalid email", Toast.LENGTH_SHORT).show();

                    } else if (pwd.getText().toString().equals("") || pwd.getText().toString().length()<6) {
                        Toast.makeText(frmAddNewUser.this, "Password must be include atleast  6 charactors", Toast.LENGTH_SHORT).show();

                    } else {
                        mProgressCircle.setVisibility(View.VISIBLE);
                        checkEmailExistsOrNot();
                    }

                }
            });

        }
        catch(Exception e){
            Toast.makeText(frmAddNewUser.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    //checkEmail
    void checkEmailExistsOrNot(){
        firebaseAuth.fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult().getSignInMethods().size() == 0){

                    createUser();


                }else {
                    Toast.makeText(frmAddNewUser.this, "Email already exists", Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.GONE);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    //create user
    public void createUser(){
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),pwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    email.setText("");
                    pwd.setText("");
                    mProgressCircle.setVisibility(View.GONE);
                    finishSweetAlert();
                }
                else{
                    Toast.makeText(frmAddNewUser.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    //finished sweet alert
    public void finishSweetAlert(){
        //sweet alert
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success")
                .setContentText("User created successfully!")
                .setConfirmText("Okay").setConfirmButtonBackgroundColor(R.color.colorPrimary).setConfirmButtonTextColor(R.color.colorPrimary)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        startActivity(new Intent(frmAddNewUser.this,frmHome.class));
                        finish();
                        sDialog.dismissWithAnimation();

                    }
                })
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
