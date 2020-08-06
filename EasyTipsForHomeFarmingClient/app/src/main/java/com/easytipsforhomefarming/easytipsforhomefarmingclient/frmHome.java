
package com.easytipsforhomefarming.easytipsforhomefarmingclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class frmHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private String email;
    private Button btnExplore;

    //slideShow
    private CarouselView carouselView;
    private int[] mImages=new int[]{
            R.drawable.slideone,R.drawable.slidetwo,R.drawable.slidethree,R.drawable.slidefour,R.drawable.slidefive
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_home);

        firebaseAuth=FirebaseAuth.getInstance();


        menu();
        bottomMenu();
        slidShow();

    }

    //slideShow
    public void slidShow(){
        carouselView=(CarouselView)findViewById(R.id.carousel);
        carouselView.setPageCount(mImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(mImages[position]);
            }
        });
        btnExplore=findViewById(R.id.btnExplore);
        btnExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(frmHome.this,frmViewItem.class));
                finish();
            }
        });


    }

    //------------------------------------------------------app drawer-----------------------------------------------------------------------

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
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.dingo));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
//            case R.id.nav_myfav:
//                Intent i = new Intent(frmHome.this, frmAddItem.class);
////                i.putExtra("itemName", email);
//                startActivity(i);
//                break;
//            case R.id.nav_modifications:
//                Intent j = new Intent(frmHome.this, frmModification.class);
//                startActivity(j);
//                break;
//            case R.id.nav_AddUser:
//                Intent ii = new Intent(frmHome.this, frmAddNewUser.class);
//                startActivity(ii);
//                break;
//            case R.id.nav_resetPassword:
//                Intent iii = new Intent(frmHome.this, frmRequestLink.class);
//                iii.putExtra("user", email);
//                startActivity(iii);
//                break;
            case R.id.nav_logout:
                logoutAlert();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //----------------------------------------------------------------------- bottom menu --------------------------------------------------

    public void bottomMenu(){
        //initialize and assign variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.home);
        //perform itemselectedlistene
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(),frmViewItem.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        return true;
                }
                return false;
            }
        });


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

    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }
}
