package com.homefarming.easytipsforhomefarming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.collection.LLRBNode;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class frmHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private String email;

    PieChart pUsers;
    PieChart pRate;
    BarChart itemBarChart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_home);
        firebaseAuth=FirebaseAuth.getInstance();


        menu();
        getEmail();
        setUsersCount();
        setRateCount();
        setItemChart();
    }

//    menu
    public void menu(){
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        NavigationView navigationView=findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
//        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.darkblack));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_myfav:
                Intent i = new Intent(frmHome.this, frmAddItem.class);
//                i.putExtra("itemName", email);
                startActivity(i);
                break;
            case R.id.nav_modifications:
                Intent j = new Intent(frmHome.this, frmModification.class);
                startActivity(j);
                break;
            case R.id.nav_AddUser:
                Intent ii = new Intent(frmHome.this, frmAddNewUser.class);
                startActivity(ii);
                break;
            case R.id.nav_resetPassword:
                Intent iii = new Intent(frmHome.this, frmRequestLink.class);
                iii.putExtra("user", email);
                startActivity(iii);
                break;
            case R.id.nav_logout:
                logoutAlert();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    //---------------------------------------------------Custom code-------------------------------------------------------------------

    //getEmail
    public void getEmail(){
        try {
            firebaseUser = firebaseAuth.getCurrentUser();
            email = firebaseUser.getEmail();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View hView =  navigationView.getHeaderView(0);
            TextView nav_user = (TextView)hView.findViewById(R.id.lbluser);
            nav_user.setText(email);
        }
        catch (Exception e)
        {
            Toast.makeText(frmHome.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //logout
    public void logoutAlert(){
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Log out")
                .setContentText("Log out of Easy Tips For Home Farming?")
                .setCustomImage(R.drawable.ic_power_settings_new_black_24dp)
                .setConfirmText("Logout").setConfirmButtonBackgroundColor(R.color.blue_btn_bg_color).setConfirmButtonTextColor(R.color.blue_btn_bg_pressed_color)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent(frmHome.this,MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }).setCancelButtonBackgroundColor(R.color.blue_btn_bg_color).setCancelButtonTextColor(R.color.blue_btn_bg_pressed_color)
                .setCancelButton("Discard", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    //set users count
    public void setUsersCount(){
        ValueFormatter vf = new ValueFormatter() { //value format here, here is the overridden method
            @Override
            public String getFormattedValue(float value) {
                return ""+(int)value;
            }
        };

        pUsers = findViewById(R.id.pUsers);
        ArrayList<PieEntry> users = new ArrayList<>();

        users.add(new PieEntry(5,"Clients"));
        users.add(new PieEntry(1,"Admin"));


        PieDataSet usersPieDataSet = new PieDataSet(users,"Users");
        usersPieDataSet.setColors(getResources().getColor(R.color.whatsapp),getResources().getColor(R.color.colorPrimaryDark));

        usersPieDataSet.setValueTextColor(Color.WHITE);
        usersPieDataSet.setValueTextSize(25f);

        PieData pieData = new PieData(usersPieDataSet);
        pieData.setValueFormatter(vf);
        pUsers.setData(pieData);
        pUsers.getDescription().setEnabled(false);
        pUsers.setCenterText("Users");
        pUsers.setCenterTextSize(22f);
        pUsers.animate();

        pUsers.getLegend().setEnabled(false);

    }

    //set users count
    public void setRateCount(){
        ValueFormatter vf = new ValueFormatter() { //value format here, here is the overridden method
            @Override
            public String getFormattedValue(float value) {
                return ""+(int)value;
            }
        };

        pRate = findViewById(R.id.pRate);
        ArrayList<PieEntry> users = new ArrayList<>();

        users.add(new PieEntry( 5,"Users"));
        PieDataSet usersPieDataSet = new PieDataSet(users,"Reviews");
        usersPieDataSet.setColors(getResources().getColor(R.color.whatsapp),getResources().getColor(R.color.colorPrimaryDark));

        usersPieDataSet.setValueTextColor(Color.WHITE);
        usersPieDataSet.setValueTextSize(25f);

        PieData pieData = new PieData(usersPieDataSet);
        pieData.setValueFormatter(vf);
        pRate.setData(pieData);
        pRate.getDescription().setEnabled(false);
        pRate.setCenterText("Reviews");
        pRate.setCenterTextSize(22f);
        pRate.animate();

        pRate.getLegend().setEnabled(false);


    }

    //set item chart
    public void setItemChart() {
        itemBarChart = findViewById(R.id.itemBarChart);

        ArrayList<BarEntry> items = new ArrayList<>();
        items.add(new BarEntry(2020, 450));
        items.add(new BarEntry(2021, 150));
        items.add(new BarEntry(2022, 250));
        items.add(new BarEntry(2023, 300));

        BarDataSet barDataSet = new BarDataSet(items, "Reviews");
        barDataSet.setColors(getResources().getColor(R.color.whatsapp), getResources().getColor(R.color.colorPrimaryDark));
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        itemBarChart.setFitBars(true);
        itemBarChart.setData(barData);
        itemBarChart.getDescription().setEnabled(false);
        itemBarChart.animateY(2000);




    }





}
