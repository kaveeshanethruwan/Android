package com.easytipsforhomefarming.easytipsforhomefarmingclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Pattern;

public class frmSignup extends AppCompatActivity {

    private Button btnreg;
    private ProgressBar mProgressCircle;
    private EditText email,pwd,cpwd;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_signup);

        assign();
        firebaseAuth=FirebaseAuth.getInstance();
        mProgressCircle.setVisibility(View.GONE);
        btnReg();
    }

    //assign
    public void assign(){
        email=findViewById(R.id.txtemail);
        pwd=findViewById(R.id.txtpwd);
        cpwd=findViewById(R.id.txtcpwd);
        btnreg=findViewById(R.id.btncreateaccount);
        mProgressCircle=findViewById(R.id.progressverfy);

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

    //validation
    public void validattion(){

        int a=pwd.getText().toString().length();

        if(! isEmailValid(email.getText().toString())){
            Toast.makeText(frmSignup.this,"Invalid email",Toast.LENGTH_SHORT).show();
            mProgressCircle.setVisibility(View.GONE);


        }
        else if( pwd.getText().toString().equals("") || Integer.parseInt(pwd.getText().toString())<6 ){
            Toast.makeText(frmSignup.this,"Request strong password",Toast.LENGTH_SHORT).show();
            mProgressCircle.setVisibility(View.GONE);


        }
        else if( cpwd.getText().toString().equals("")){
            Toast.makeText(frmSignup.this,"Request comfirm password",Toast.LENGTH_SHORT).show();
            mProgressCircle.setVisibility(View.GONE);


        }
        else if( !cpwd.getText().toString().equals(cpwd.getText().toString())){
            Toast.makeText(frmSignup.this,"Password didn't match",Toast.LENGTH_SHORT).show();
            mProgressCircle.setVisibility(View.GONE);

        }
        else if(a<6){
            Toast.makeText(frmSignup.this,"Password should be more than 6 charactress",Toast.LENGTH_SHORT).show();
            mProgressCircle.setVisibility(View.GONE);
        }

        else{
            checkEmailExistsOrNot();
        }
    }

    //checkEmail
    void checkEmailExistsOrNot() {
        try{
            firebaseAuth.fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    //  Log.d(TAG,""+task.getResult().getSignInMethods().size());
                    if (task.getResult().getSignInMethods().size() == 0) {
                        insert();
                       // mProgressCircle.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(frmSignup.this, "Email already exhist", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(frmSignup.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    //crete user
    public void insert(){
        try {

            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),pwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mProgressCircle.setVisibility(View.GONE);
                        startActivity(new Intent(frmSignup.this,frmHome.class));
                        finish();

                    }
                    else{
                        Toast.makeText(frmSignup.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
        catch(Exception e){
            Toast.makeText(frmSignup.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    public boolean isEmailValid(String text){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(text).matches();
    }


}
