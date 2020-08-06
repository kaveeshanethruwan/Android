package com.pbstrial.pbs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class frmHome extends AppCompatActivity {

    private Button logut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_home);

        logut = findViewById(R.id.btnlogout);
        logut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                SharedPreferences preferences = getSharedPreferences("userToken", MODE_PRIVATE);
                preferences.edit().remove("auth").commit();
                preferences.edit().clear();
                startActivity(new Intent(frmHome.this,MainActivity.class));
                finish();
            }
        });

    }
}
