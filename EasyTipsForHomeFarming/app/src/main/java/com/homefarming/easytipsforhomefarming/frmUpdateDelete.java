package com.homefarming.easytipsforhomefarming;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class frmUpdateDelete extends AppCompatActivity {

    private String itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_update_delete);

        getSupportActionBar().setTitle("Item update/delete");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        itemName=getIntent().getStringExtra("itemName");

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
