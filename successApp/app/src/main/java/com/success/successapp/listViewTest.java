package com.success.successapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class listViewTest extends AppCompatActivity {

    ListView language;
    String [] lang={"swifgt","kotlin","java","pyhton","c++","object c","ruby"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_test);

        language=(ListView) findViewById(R.id.lstView1);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lang);

        language.setAdapter(adapter);

        //SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        //pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
       // pDialog.setTitleText("Loading");
        //pDialog.setCancelable(false);
        //pDialog.show();
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Log out")
                .setContentText("Log out of SunRise?")
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                    }
                })
                .setCancelButton("Discard", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
}
