package com.success.successapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class firebase extends AppCompatActivity {

    private EditText id2;
    private EditText name2;
    private  Button btnretrive;


    private RatingBar rb;
    private EditText txtrate;
    private Button btnrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        retrive();
        rate();

    }

    public void connect(){

        DatabaseReference secondary = FirebaseDatabase.getInstance("https://my-application-49591.firebaseio.com").getReference();
    }

    public void retrive(){

        id2=(EditText) findViewById(R.id.txtid2);
        name2=(EditText) findViewById(R.id.txtname2);

        btnretrive=(Button) findViewById(R.id.btnretrive);

        btnretrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // DatabaseReference secondary = FirebaseDatabase.getInstance("https://my-application-49591.firebaseio.com").getReference();
               // DatabaseReference otherDB_data = FirebaseDatabase
               //         .getInstance("https://my-application-49591.firebaseio.com/customer")
               //         .getReference();

               // DatabaseReference secondary = FirebaseDatabase.getInstance("https://my-application-49591.firebaseio.com")
                      //  .getReference();
                DatabaseReference secondary= FirebaseDatabase.getInstance("https://my-application-49591.firebaseio.com").getReference().child("customer").child("1");


                secondary.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       String id=dataSnapshot.child("id").getValue().toString();
                        String name=dataSnapshot.child("name").getValue().toString();

                        id2.setText(id);
                        name2.setText(name);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                secondary= FirebaseDatabase.getInstance().getReference().child("customer").child("1");
             //   secondary.addValueEventListener(new ValueEventListener() {
             //       @Override
             //       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             //           String id=dataSnapshot.child("id").getValue().toString();
             //           String name=dataSnapshot.child("name").getValue().toString();

             //           id2.setText(id);
             //           name2.setText(name);

             //       }

              //      @Override
              //      public void onCancelled(@NonNull DatabaseError databaseError) {

              //      }
              //  });

            }
        });

    }

    public void rate(){

        rb=(RatingBar) findViewById(R.id.ratingBar);
        txtrate=(EditText) findViewById(R.id.txtrate);
        btnrate=(Button) findViewById(R.id.btnrate);

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                txtrate.setText(String.valueOf(rating));
            }
        });
    }
}
