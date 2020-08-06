package com.easytipsforhomefarming.easytipsforhomefarmingclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button btnGetStarted;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth= FirebaseAuth.getInstance();
//        mAuthListener=new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if(firebaseAuth.getCurrentUser()!=null){
//                    startActivity(new Intent(MainActivity.this,frmHome.class));
//                    finish();
//                    Toast.makeText(MainActivity.this,"already logeed",Toast.LENGTH_LONG).show();
//
//                }
//                else{
//                    btnGetStarted = findViewById(R.id.btnGetStarted);
//                    btnGetStarted.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            startActivity(new Intent(MainActivity.this,frmLogin.class));
//                            Toast.makeText(MainActivity.this,"not login",Toast.LENGTH_LONG).show();
//
//                        }
//                    });
//                }
//            }
//        };

        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        if(firebaseUser != null){
           // mProgressCircle.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this,frmHome.class));
            finish();
        }
        else{
            btnGetStarted = findViewById(R.id.btnGetStarted);
            btnGetStarted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,frmLogin.class));
                }
            });
        }


        btnGetStarted = findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,frmLogin.class));
            }
        });

    }
}
