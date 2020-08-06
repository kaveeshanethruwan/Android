package com.firefly.sunrise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class frmRateNew extends AppCompatActivity {

    String ttt,email;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_rate_new);

        getEmail();
    }

    public void getEmail(){
        try {
            firebaseUser = firebaseAuth.getCurrentUser();
            email = firebaseUser.getEmail();
            Toast.makeText(frmRateNew.this,email, Toast.LENGTH_SHORT).show();

        }
        catch (Exception e)
        {
            Toast.makeText(frmRateNew.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
