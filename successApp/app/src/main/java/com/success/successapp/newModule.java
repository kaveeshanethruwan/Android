package com.success.successapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class newModule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_module);
        Toast.makeText(getApplicationContext(),"onCreate", Toast.LENGTH_LONG).show();

    }

    protected  void onStart(){
        super.onStart();
        Toast.makeText(getApplicationContext(),"onStart", Toast.LENGTH_LONG).show();

    }

   protected  void onResume(){
        super.onResume();
        Toast.makeText(getApplicationContext(),"onResume", Toast.LENGTH_LONG).show();

    }

    protected  void onPause(){
        super.onPause();
        Toast.makeText(getApplicationContext(),"onPause", Toast.LENGTH_LONG).show();

    }

    protected  void onStop(){
        super.onStop();
        Toast.makeText(getApplicationContext(),"onStop", Toast.LENGTH_LONG).show();
    }

    protected  void onRestart(){
        super.onRestart();
        Toast.makeText(getApplicationContext(),"onRestart", Toast.LENGTH_LONG).show();
    }

    protected  void onDestroy(){
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"onDestroy", Toast.LENGTH_LONG).show();
    }




}
