package com.pbstrial.pbs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class frmSignup extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText cpassword;
    private Button signup;
    private ProgressBar mProgressCircle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_signup);

        assignVariables();
        mProgressCircle.setVisibility(View.GONE);

    }

    public void assignVariables(){
        email = findViewById(R.id.txtemail);
        password = findViewById(R.id.txtpassword);
        cpassword = findViewById(R.id.txtcpassword);
        signup = findViewById(R.id.btnsignup);
        mProgressCircle=findViewById(R.id.progressBar);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressCircle.setVisibility(View.INVISIBLE);


                if (email.getText().toString().equals("")) {

                    Toast.makeText(frmSignup.this, "Provide your email", Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.GONE);

                } else if (!isEmailValid(email.getText().toString())) {

                    Toast.makeText(frmSignup.this, "Invalid email", Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.GONE);

                } else if (password.getText().toString().equals("")) {

                    Toast.makeText(frmSignup.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.GONE);


                }
                else if( cpassword.getText().toString().equals("")){
                    Toast.makeText(frmSignup.this,"Request comfirm password",Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.GONE);


                }
                else if( !password.getText().toString().equals(cpassword.getText().toString())){
                    Toast.makeText(frmSignup.this,"Password didn't match",Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.GONE);

                }
                else{
                    SQLiteDatabase mydb = openOrCreateDatabase("pbs", MODE_PRIVATE, null);
                    mydb.execSQL("create table if not exists Users(email varchar,password varchar)");
                    mydb.execSQL("insert into Users values('" + email.getText().toString() + "','" + password.getText().toString() + "')");
                    startActivity(new Intent(frmSignup.this,frmHome.class));
                    finish();
                }

            }
        });

    }

    //email validation
    public boolean isEmailValid(String text){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(text).matches();
    }
}
