package com.homefarming.easytipsforhomefarming;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class frmAddItem extends AppCompatActivity {

    private Button btnUpload;
//    private Button btnRemoveVideo;
    private VideoView videoView;
    private static final int PICK_VIDEO_REQUEST=3;
    private Uri videoUri=null;
    private MediaController mc;

    private Button btnSelectImage;
    private Button btnRemoveImage;
    private ImageView imgVF;
    public Uri imgUri=null;

    private Button btnSubmitForm;
    private StorageReference mStorageRef;
    private StorageTask uploadTask;
    private Uri videoDownloadUrl=null,imageDownloadUrl=null;


    private EditText iName;
    private EditText iClimate;
    private EditText iSoil;
    private EditText iTimeDutration;
    private EditText iRocommendedSources;
    private EditText iSeedsLimitations;
    private EditText iSeedsNursery;
    private EditText iFertilizers;
    private EditText iWatiring;
    private EditText iHarvest;

    private SweetAlertDialog pDialog;

    DatabaseReference reff;
    ModelItem modelItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_add_item);

        getSupportActionBar().setTitle("Upload New Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        assignValues();
        getVideo();
        getImage();
        submitForm();


    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    //assign values
    public void assignValues(){
        iName=findViewById(R.id.txtname);
        iClimate=findViewById(R.id.txtclimate);
        iSoil=findViewById(R.id.txtsoil);
        iTimeDutration=findViewById(R.id.txttimeduration);
        iRocommendedSources=findViewById(R.id.txtrecommendedsources);
        iSeedsLimitations=findViewById(R.id.txtseedslimitations);
        iSeedsNursery=findViewById(R.id.txtnersery);
        iFertilizers=findViewById(R.id.txtfertilizers);
        iWatiring=findViewById(R.id.txtwatering);
        iHarvest=findViewById(R.id.txtHarvest);
    }

    //selece video
    public void getVideo(){

        btnUpload=findViewById(R.id.btnUpload);
//        btnRemoveVideo=findViewById(R.id.btnRemoveVideo);
        videoView=findViewById(R.id.videoView);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mc = new MediaController(frmAddItem.this);
                        videoView.setMediaController(mc);
                        mc.setAnchorView(videoView);
                    }
                });
            }
        });
        videoView.start();
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setType("video/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"select a video"),PICK_VIDEO_REQUEST);
            }
        });


//        btnRemoveVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                videoView.setVisibility(View.GONE);
////                videoView.setVisibility(View.VISIBLE);
//               // videoView.setVideoURI(null);
//                videoView.stopPlayback();
//                videoView.seekTo(0);
//
//                videoUri=null;
//
//            }
//        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_VIDEO_REQUEST && resultCode==RESULT_OK && data!= null){
            videoUri=data.getData();
            videoView.setVideoURI(videoUri);
        }
        else if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null ){
            imgUri=data.getData();
            imgVF.setImageURI(imgUri);
        }
    }

    //select image
    public void getImage(){
        btnSelectImage=findViewById(R.id.btnSelectImage);
        imgVF=findViewById(R.id.imgVF);
        btnRemoveImage=findViewById(R.id.btnRemoveImage);


        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
            }
        });

        btnRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgVF.setImageResource(0);
                Drawable drawable  = getResources().getDrawable(R.drawable.ic_file_upload_black_24dp);
                imgVF.setImageDrawable(drawable);
                imgUri=null;

            }
        });
    }

    //fieChooser
    public void fileChooser(){
        Intent intent=new Intent();
        intent.setType("image/'");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
        //imgVF.setVisibility(View.VISIBLE);
    }

    //upload image
    public void uploadImage(){
        final StorageReference riversRef = mStorageRef.child("images/"+iName.getText().toString());

        uploadTask = riversRef.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // getting image uri and converting into string
                                imageDownloadUrl = uri;
//                                iHarvest.setText(String.valueOf(imageDownloadUrl));
//                                Toast.makeText(frmAddItem.this,String.valueOf(imageDownloadUrl),Toast.LENGTH_SHORT).show();


                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(frmAddItem.this,"error",Toast.LENGTH_SHORT).show();

                        // ...
                    }
                });
    }

    //upload video
    public void uploadVideo(){
        final StorageReference riversRef = mStorageRef.child("videoes/"+iName.getText().toString());

        uploadTask = riversRef.putFile(videoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // getting image uri and converting into string
                                videoDownloadUrl = uri;
//                                iHarvest.setText(String.valueOf(videoDownloadUrl));
//                                Toast.makeText(frmAddItem.this,String.valueOf(videoDownloadUrl),Toast.LENGTH_SHORT).show();

//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        popup();
//                                    }
//                                }, 30000);
                                if(uploadTask!=null && uploadTask.isInProgress()){
                                   // Toast.makeText(frmAddItem.this,"Upload in progress",Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    //popup();
                                    sendToDb();
                                }


                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(frmAddItem.this,"error",Toast.LENGTH_SHORT).show();

                        // ...
                    }
                });
    }

    public void popup(){
        pDialog.dismiss();
        Toast.makeText(frmAddItem.this,"30sec gone"+"image = "+String.valueOf(imageDownloadUrl)+" "+"video :"+videoDownloadUrl,Toast.LENGTH_SHORT).show();

    }

    //progressing
    public void progressing(){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Uploading...");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    //validate fields
    public void validateFields(){
        if(videoUri==null){
            Toast.makeText(frmAddItem.this,"Please select a video",Toast.LENGTH_SHORT).show();
        }else if(! isNameValid(iName.getText().toString()) || iName.getText().toString().equals("") ){
            Toast.makeText(frmAddItem.this,"Invalid item name",Toast.LENGTH_SHORT).show();
        }else if(iClimate.getText().toString().equals("")){
            Toast.makeText(frmAddItem.this,"Please provide a description for a climate",Toast.LENGTH_SHORT).show();
        }else if(iSoil.getText().toString().equals("")){
            Toast.makeText(frmAddItem.this,"Please provide a description for a soil",Toast.LENGTH_SHORT).show();
        }else if(iTimeDutration.getText().toString().equals("")){
            Toast.makeText(frmAddItem.this,"Please provide a description for a timeduration",Toast.LENGTH_SHORT).show();
        }else if(iRocommendedSources.getText().toString().equals("")){
            Toast.makeText(frmAddItem.this,"Please provide a description for a recommended sources",Toast.LENGTH_SHORT).show();
        }else if(iSeedsLimitations.getText().toString().equals("")){
            Toast.makeText(frmAddItem.this,"Please provide a description for a seeds limitations",Toast.LENGTH_SHORT).show();
        }else if(iSeedsNursery.getText().toString().equals("")){
            Toast.makeText(frmAddItem.this,"Please provide a description for a seeds nursery",Toast.LENGTH_SHORT).show();
        }else if(iFertilizers.getText().toString().equals("")){
            Toast.makeText(frmAddItem.this,"Please provide a description for a fertilizers",Toast.LENGTH_SHORT).show();
        }else if(iWatiring.getText().toString().equals("")){
            Toast.makeText(frmAddItem.this,"Please provide a description for a watering",Toast.LENGTH_SHORT).show();
        }else if(iHarvest.getText().toString().equals("")){
            Toast.makeText(frmAddItem.this,"Please provide a description for a harvesting",Toast.LENGTH_SHORT).show();
        }else if(imgUri==null){
            Toast.makeText(frmAddItem.this,"Please select a front image",Toast.LENGTH_SHORT).show();
        }
        else {

            verifyItem();

        }
    }

    //send to db
    public void sendToDb(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        final String formattedDate = df.format(c);

        modelItem=new ModelItem();
        reff= FirebaseDatabase.getInstance().getReference().child("Items");


        modelItem.setIdate(formattedDate);
        modelItem.setiName(iName.getText().toString());
        modelItem.setiClimate(iClimate.getText().toString());
        modelItem.setiSoil(iSoil.getText().toString());
        modelItem.setiTimeDutration(iTimeDutration.getText().toString());
        modelItem.setiRocommendedSources(iRocommendedSources.getText().toString());
        modelItem.setiSeedsLimitations(iSeedsLimitations.getText().toString());
        modelItem.setiSeedsNursery(iSeedsNursery.getText().toString());
        modelItem.setiFertilizers(iFertilizers.getText().toString());
        modelItem.setiWatiring(iWatiring.getText().toString());
        modelItem.setiHarvest(iHarvest.getText().toString());
        modelItem.setiImgUrl(String.valueOf(imageDownloadUrl));
        modelItem.setiVideoUrl(String.valueOf(videoDownloadUrl));

        reff.push().setValue(modelItem);
        pDialog.dismiss();
        finishSweetAlert();


    }

    //finished sweet alert
    public void finishSweetAlert(){
        //sweet alert
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success")
                .setContentText("Your item is successfully uploaded!")
                .setConfirmText("Okay").setConfirmButtonBackgroundColor(R.color.colorPrimary).setConfirmButtonTextColor(R.color.colorPrimary)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        startActivity(new Intent(frmAddItem.this,frmHome.class));
                        finish();
                        sDialog.dismissWithAnimation();

                    }
                })
                .show();
    }

    //verify item
    public void verifyItem(){
        try {

            DatabaseReference reff= FirebaseDatabase.getInstance().getReference().child("Items");

            reff.orderByChild("iName").equalTo(iName.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Toast.makeText(frmAddItem.this,"Item name already exhist",Toast.LENGTH_SHORT).show();

                    } else {

                        progressing();
                        uploadImage();
                        uploadVideo();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        catch (Exception e){
            Toast.makeText(frmAddItem.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


    //submit form
    public void submitForm(){
        btnSubmitForm=findViewById(R.id.btnSubmitForm);

        btnSubmitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadTask!=null && uploadTask.isInProgress()){
                    Toast.makeText(frmAddItem.this,"Upload in progress",Toast.LENGTH_SHORT).show();

                }
                else{
                    validateFields();
//                    progressing();
//                    uploadImage();
//                    uploadVideo();
                }


            }
        });

    }

    //regex
    public boolean isNameValid(String text){

        return text.matches("^([A-Za-z]+)(\\s[A-Za-z]+)*\\s?$");
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
