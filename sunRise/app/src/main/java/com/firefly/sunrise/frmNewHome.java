package com.firefly.sunrise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class frmNewHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    String email;
    TextView lbluser;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    //slideShow
    private CarouselView carouselView;
    private int[] mImages=new int[]{
            R.drawable.b,R.drawable.image_cupcake,R.drawable.bb,R.drawable.bbb,R.drawable.bbbb
    };

    //touMight
    private RecyclerView mRecyclrView;
    private YouMightHolder mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private List<Model> mUploads;
    private List<Model> allUploads;

    //Reviews
    private RecyclerView reRecyclerView;
    private ReviewHolder reAdapter;
    private List<ReviewModel> reUploads;


    //newArrivals
    private RecyclerView newRecyclrView;

    //menuItems
    CircleImageView itemFunction;
    CircleImageView itemNormal;
    CircleImageView itemCupcake;
    CircleImageView itemPieces;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String getType="empty";
    ImageView imgcart,imgwhatsapp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_new_home);

         firebaseAuth=FirebaseAuth.getInstance();

      //  mAuth=FirebaseAuth.getInstance();
       // mAuthListener=new FirebaseAuth.AuthStateListener() {
         //   @Override
         //   public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
         //       if(firebaseAuth.getCurrentUser()!=null){
                    newArrivals();
                    youMightAlsoLike();
                    setReviews();
                    getEmail();

                    //setEmail
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    View hView =  navigationView.getHeaderView(0);
                    TextView nav_user = (TextView)hView.findViewById(R.id.lbluser);
                    nav_user.setText(email);

            //   }
            //    else{
             //     startActivity(new Intent(frmNewHome.this,MainActivity.class));
             //       finish();
          //      }
         //   }
      //  };
       // firebaseAuth=FirebaseAuth.getInstance();
       // FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
       // if(firebaseUser != null){

        //    newArrivals();
        //   youMightAlsoLike();
         //   setReviews();
         //   getEmail();

            //setEmail
       //     NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //    View hView =  navigationView.getHeaderView(0);
       //     TextView nav_user = (TextView)hView.findViewById(R.id.lbluser);
       //     nav_user.setText(email);
     //   }
     //   else{
     //       startActivity(new Intent(frmNewHome.this,MainActivity.class));
       //     finish();
      //  }
        menu();
        slidShow();
        callToMenuItem();
        addToCart();
        callWhatsapp();

    }

    //callWhatsapp
    public void callWhatsapp() {
        try{
        imgwhatsapp = findViewById(R.id.imgWhatsapp);
        imgwhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean installed = appInstalledOrNot("com.whatsapp");
                if(installed) {
                     Uri mUri = Uri.parse("smsto:+0773835115");
                     Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
                     mIntent.setPackage("com.whatsapp");
                     mIntent.putExtra("sms_body", "The text goes here");
                     mIntent.putExtra("chat", true);
                     startActivity(mIntent);
                } else {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.whatsapp")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.whatsapp")));

                    }
                }

            }
        });
    }
    catch(Exception e){
        Toast.makeText(this, "You have to install Whatssapp first!", Toast.LENGTH_SHORT).show();

    }

    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    //menu
    public void menu(){
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_layout);

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.darkblack));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    //imageCart
    public void addToCart(){
        imgcart=findViewById(R.id.imgCart);
        imgcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(frmNewHome.this,frmCart.class));

            }
        });

    }

    //callToMenuItem
    public void callToMenuItem(){
        itemFunction=findViewById(R.id.imgFunction);
        itemNormal=findViewById(R.id.imgBakery);
        itemCupcake=findViewById(R.id.imgCupcake);
        itemPieces=findViewById(R.id.imgPieces);

        //callFunction
        itemFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmNewHome.this, frmViewMyOrders.class);
                intent.putExtra("itemName", "Function");
                startActivity(intent);
            }
        });

        //callNormal
        itemNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmNewHome.this, frmViewMyOrders.class);
                intent.putExtra("itemName", "Normal");
                startActivity(intent);
            }
        });

        //callCupcake
        itemCupcake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmNewHome.this, frmViewMyOrders.class);
                intent.putExtra("itemName", "Cupcake");
                startActivity(intent);
            }
        });

        //callPieces
        itemPieces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frmNewHome.this, frmViewMyOrders.class);
                intent.putExtra("itemName", "Pieces");
                startActivity(intent);
            }
        });

    }

    //setYouMightAlsoLike
    public void youMightAlsoLike(){
        mRecyclrView=findViewById(R.id.recyclerViewYouMay);
        mRecyclrView.setHasFixedSize(true);
        mRecyclrView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        mProgressCircle = findViewById(R.id.progress_circle);

        allUploads = new ArrayList<>();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Items");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Model upload = postSnapshot.getValue(Model.class);
                    allUploads.add(upload);
                }


                mAdapter= new YouMightHolder(frmNewHome.this,allUploads);
                mRecyclrView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.GONE);

                mAdapter.setOnItemClickstner(new ViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        try {


                            TextView zname = view.findViewById(R.id.vName);
                            TextView zdes = view.findViewById(R.id.vDescription);
                            TextView zprice = view.findViewById(R.id.vPrice);
                            TextView zdis = view.findViewById(R.id.vDiscount);
                            ImageView zimage = view.findViewById(R.id.vImageView);



                            String xname = zname.getText().toString();
                            String xdes = zdes.getText().toString();
                            String xprice = zprice.getText().toString();
                            String xdis = zdis.getText().toString();
                            Drawable xdrawable = zimage.getDrawable();
                            Bitmap xbitmap = ((BitmapDrawable) xdrawable).getBitmap();



                                Intent intent = new Intent(view.getContext(), frmTakeOrder.class);

                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                xbitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] bytes = stream.toByteArray();

                                intent.putExtra("name", xname);
                                intent.putExtra("des", xdes);
                                intent.putExtra("price", xprice);
                                intent.putExtra("dis", xdis);
                                intent.putExtra("image", bytes);
                                startActivity(intent);

                        }
                        catch (Exception e){
                            Toast.makeText(frmNewHome.this,"Please wait",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(frmNewHome.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    //setReview
    public void setReviews(){
        reRecyclerView=findViewById(R.id.recyclerViewRewiews);
        reRecyclerView.setHasFixedSize(true);
        reRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

      //  mProgressCircle = findViewById(R.id.progress_circle);

        reUploads = new ArrayList<>();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Customer_Rate");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    ReviewModel upload = postSnapshot.getValue(ReviewModel.class);
                    reUploads.add(upload);
                }


                reAdapter= new ReviewHolder(frmNewHome.this,reUploads);
                reRecyclerView.setAdapter(reAdapter);
                //mProgressCircle.setVisibility(View.GONE);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(frmNewHome.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    //setNewArrivals
    public void newArrivals(){
        newRecyclrView=findViewById(R.id.recyclerViewNew);
        newRecyclrView.setHasFixedSize(true);
        newRecyclrView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

       // mDatabaseRef= FirebaseDatabase.getInstance().getReference("Items");

        Query getNew = FirebaseDatabase.getInstance().getReference("Items").orderByChild("showInFront").equalTo("yes");

        getNew.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Model upload = postSnapshot.getValue(Model.class);
                    mUploads.add(upload);
                }


                mAdapter= new YouMightHolder(frmNewHome.this,mUploads);
                newRecyclrView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.GONE);

                mAdapter.setOnItemClickstner(new ViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        try {
                            TextView zname = view.findViewById(R.id.vName);
                            TextView zdes = view.findViewById(R.id.vDescription);
                            TextView zprice = view.findViewById(R.id.vPrice);
                            TextView zdis = view.findViewById(R.id.vDiscount);
                            ImageView zimage = view.findViewById(R.id.vImageView);

                            String xname = zname.getText().toString();
                            String xdes = zdes.getText().toString();
                            String xprice = zprice.getText().toString();
                            String xdis = zdis.getText().toString();
                            Drawable xdrawable = zimage.getDrawable();
                            Bitmap xbitmap = ((BitmapDrawable) xdrawable).getBitmap();

                            Intent intent = new Intent(view.getContext(), frmTakeOrder.class);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            xbitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] bytes = stream.toByteArray();
                            intent.putExtra("name", xname);
                            intent.putExtra("des", xdes);
                            intent.putExtra("price", xprice);
                            intent.putExtra("dis", xdis);
                            intent.putExtra("image", bytes);
                            startActivity(intent);


                        }
                        catch (Exception e){
                            Toast.makeText(frmNewHome.this,"Please wait",Toast.LENGTH_SHORT).show(); }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(frmNewHome.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                //mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_myfav:
                 Intent i = new Intent(frmNewHome.this, frmMyFavourite.class);
                i.putExtra("itemName", email);
                startActivity(i);
                break;
            case R.id.nav_myorders:
                Intent j = new Intent(frmNewHome.this, frmMyOrders.class);
                j.putExtra("itemName", email);
                startActivity(j);
                break;
            case R.id.nav_profile:
                Intent ii = new Intent(frmNewHome.this, frmUserEdit.class);
                startActivity(ii);
                break;
            case R.id.nav_rate:
                Intent iii = new Intent(frmNewHome.this, frmRate.class);
                startActivity(iii);
                break;
            case R.id.nav_logout:
                logoutAlert();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //getEmail
    public void getEmail(){
        try {
            firebaseUser = firebaseAuth.getCurrentUser();
            email = firebaseUser.getEmail();
        }
        catch (Exception e)
        {
            Toast.makeText(frmNewHome.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //logout
    public void logoutAlert(){
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Log out")
                .setContentText("Log out of EatsAndTreats?")
                .setCustomImage(R.drawable.ic_power_settings_new_black_24dp)
                .setConfirmText("Logout").setConfirmButtonBackgroundColor(R.color.colorPrimary).setConfirmButtonTextColor(R.color.colorPrimary)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent(frmNewHome.this,MainActivity.class);
                        startActivity(intent);
                        finish();




                    }
                }).setCancelButtonBackgroundColor(R.color.colorPrimary).setCancelButtonTextColor(R.color.colorPrimary)
                .setCancelButton("Discard", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }



    //disable back
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START)&&doubleBackToExitPressedOnce){
            super.onBackPressed();
            return;
        }
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(doubleBackToExitPressedOnce){
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
