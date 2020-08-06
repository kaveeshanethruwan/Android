package com.success.successapp;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class empUpdate extends AppCompatActivity {

    private Button search;
    private EditText empno;
    private EditText empname;
    private EditText empage;
    private EditText gender;
    private Button update;
    private  Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_update);

        btn_disable();
        btn_search();
        btn_update();
        btn_delete();
    }

    public void btn_disable(){
        update=(Button) findViewById(R.id.btnupdate);
        delete=(Button) findViewById(R.id.btndelete);
        gender=(EditText) findViewById(R.id.txtempgender);
        empname=(EditText) findViewById(R.id.txtempname);
        empage=(EditText) findViewById(R.id.txtempage);

        update.setEnabled(false);
        delete.setEnabled(false);
        gender.setEnabled(false);
        empname.setEnabled(false);
        empage.setEnabled(false);

    }

    public void btn_search(){

        search=(Button) findViewById(R.id.btnsearch);
        empno=(EditText) findViewById(R.id.txtempno);
        gender=(EditText) findViewById(R.id.txtempgender);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    SQLiteDatabase mydb = openOrCreateDatabase("company", MODE_PRIVATE, null);
                    Cursor rs = mydb.rawQuery("select empname,empage,empgender  from employee where empno='" + empno.getText().toString() + "'", null);
                    rs.moveToFirst();
                    String name, age,g;


                    name = rs.getString(0);
                    age = rs.getString(1);
                    g= rs.getString(2);


                    empname.setText(name.toString());
                    empage.setText(age.toString());
                    gender.setText(g.toString());

                    update.setEnabled(true);
                    delete.setEnabled(true);
                    empname.setEnabled(true);
                    empage.setEnabled(true);
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    public void btn_update(){
        update=(Button) findViewById(R.id.btnupdate);
        empname=(EditText) findViewById(R.id.txtempname);
        empage=(EditText) findViewById(R.id.txtempage);
        empno=(EditText) findViewById(R.id.txtempno);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SQLiteDatabase mydb = openOrCreateDatabase("company", MODE_PRIVATE, null);
                    mydb.execSQL("update employee set empname='" + empname.getText().toString() + "', empage='" + empage.getText().toString() + "' where empno='" + empno.getText().toString() + "' ");

                    empno.setText("");
                    empname.setText("");
                    empage.setText("");
                    update.setEnabled(false);
                    delete.setEnabled(false);

                    Toast.makeText(getApplicationContext(), "Successfully updated!", Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    public void btn_delete(){

        delete=(Button) findViewById(R.id.btndelete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SQLiteDatabase mydb = openOrCreateDatabase("company", MODE_PRIVATE, null);
                    mydb.execSQL("delete from employee where empno='" + empno.getText().toString() + "'");

                    empno.setText("");
                    empname.setText("");
                    empage.setText("");
                    update.setEnabled(false);
                    delete.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Successfully deleted!!", Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });


    }
}
