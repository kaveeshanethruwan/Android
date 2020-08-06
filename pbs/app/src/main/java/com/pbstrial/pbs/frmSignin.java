package com.pbstrial.pbs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class frmSignin extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button signin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_signin);

        assignVariables();
    }

    // check user is valid
    public boolean checkLogin(String username, String password) {

        SQLiteDatabase mydb = openOrCreateDatabase("pbs", MODE_PRIVATE, null);
        mydb.execSQL("create table if not exists Users(email varchar,password varchar)");

        Cursor c = mydb.rawQuery("SELECT * FROM Users WHERE " + username + " =? AND "+ password + " =?", null);

        if(c.getCount() <= 0) {
            c.close();
            mydb.close();
            return false;
        } else {
            c.close();
            mydb.close();
            return true;
        }
    }


    public void assignVariables(){
        email = findViewById(R.id.txtemail);
        password = findViewById(R.id.txtpassword);
        signin = findViewById(R.id.btnsignin);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().equals("")) {

                    Toast.makeText(frmSignin.this, "Provide your email", Toast.LENGTH_SHORT).show();

                } else if (!isEmailValid(email.getText().toString())) {

                    Toast.makeText(frmSignin.this, "Invalid email", Toast.LENGTH_SHORT).show();

                } else if (password.getText().toString().equals("")) {

                    Toast.makeText(frmSignin.this, "Invalid password", Toast.LENGTH_SHORT).show();

                }
                else{

                    SQLiteDatabase mydb = openOrCreateDatabase("pbs", MODE_PRIVATE, null);
                    mydb.execSQL("create table if not exists Users(email varchar,password varchar)");

                    Cursor cursor = null;
                    String checkQuery = "SELECT * FROM Users WHERE email= '"+email.getText().toString() + "' and password= '"+password.getText().toString() + "'";
                    cursor=mydb.rawQuery(checkQuery,null);

                    if(cursor.getCount() <= 0) {
                        cursor.close();
                        mydb.close();
                        Toast.makeText(frmSignin.this, "Invalid login", Toast.LENGTH_SHORT).show();

                    } else {
                        cursor.close();
                        mydb.close();

                        SharedPreferences preferences = getSharedPreferences("userToken", MODE_PRIVATE);
                        preferences.edit().putString("token", "auth").commit();


                        startActivity(new Intent(frmSignin.this,frmHome.class));
                        finish();
                        //Toast.makeText(frmSignin.this, "successful", Toast.LENGTH_SHORT).show();

                    }




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
