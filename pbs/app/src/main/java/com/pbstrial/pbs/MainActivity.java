package com.pbstrial.pbs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView txtsignup;
    private Button btnsignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignVariables();


        SharedPreferences preferences = getSharedPreferences("userToken", MODE_PRIVATE);
        String token = preferences.getString("token","");

        if(token!=null){
            startActivity(new Intent(MainActivity.this,frmHome.class));
            Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
        }
        else {

        }


    }

    public void assignVariables(){
        txtsignup = findViewById(R.id.txtsignup);
        btnsignin = findViewById(R.id.btnsignin);

        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,frmSignup.class));
            }
        });

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,frmSignin.class));
            }
        });


    }


}
