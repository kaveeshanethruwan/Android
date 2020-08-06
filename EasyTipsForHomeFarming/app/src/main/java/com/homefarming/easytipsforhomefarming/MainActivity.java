package com.homefarming.easytipsforhomefarming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText pwd;
    private Button login;
    private ProgressBar mProgressCircle;

    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView forgotPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();

        login = (Button) findViewById(R.id.btnlogin);
        forgotPassword = findViewById(R.id.tvForgotPassword);
        mProgressCircle = findViewById(R.id.mainprogresscycle);


        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            mProgressCircle.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this,frmHome.class));
            finish();
        }
        else{
            mProgressCircle.setVisibility(View.GONE);
            login();
            forgotPasswrd();
        }




    }

    //forgot password
    public void forgotPasswrd(){
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,frmResetPassword.class));
            }
        });
    }

    //Login
    public void login() {
        try{

            email = (EditText) findViewById(R.id.txtemail);
            pwd = (EditText) findViewById(R.id.txtpwd);
            login = (Button) findViewById(R.id.btnlogin);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (email.getText().toString().equals("")) {

                        Toast.makeText(MainActivity.this, "Provide your email", Toast.LENGTH_SHORT).show();

                    } else if (!isEmailValid(email.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();

                    } else if (pwd.getText().toString().equals("")) {
                        Toast.makeText(MainActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();

                    } else {
                        mProgressCircle.setVisibility(View.VISIBLE);
                        checkEmailExistsOrNot();
                    }

                }
            });

        }
        catch(Exception e){
            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    //checkEmail
    void checkEmailExistsOrNot(){
        firebaseAuth.fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                //  Log.d(TAG,""+task.getResult().getSignInMethods().size());
                if (task.getResult().getSignInMethods().size() == 0){
                    Toast.makeText(MainActivity.this, "No user found", Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.GONE);

                }else {
                    singIn();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    //singIn
    public void singIn(){
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), pwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    startActivity(new Intent(MainActivity.this,frmHome.class));
                    //  Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(MainActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
                    mProgressCircle.setVisibility(View.GONE);


                }
            }
        });
    }
    //email validation
    public boolean isEmailValid(String text){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(text).matches();
    }

}
