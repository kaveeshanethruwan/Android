package com.success.successapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class employeeRegister extends AppCompatActivity {

    private EditText no;
    private EditText name;
    private RadioGroup gender;
    private RadioButton radioButton;

    private EditText age;
    private Button submit;

    private Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_register);
        btn_submit();
        btn_update();
    }

    public void btn_submit(){

        no=(EditText) findViewById(R.id.txtempno);
        name=(EditText) findViewById(R.id.txtempname);
        gender=(RadioGroup) findViewById(R.id.grpgender);
        age=(EditText) findViewById(R.id.txtempage);
        submit=(Button) findViewById(R.id.btnsubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    int selectedId = gender.getCheckedRadioButtonId();
                    radioButton = (RadioButton) findViewById(selectedId);


                    SQLiteDatabase mydb = openOrCreateDatabase("company", MODE_PRIVATE, null);
                    mydb.execSQL("create table if not exists employee(empno varchar,empname varchar,empgender varchar,empage varchar)");
                    mydb.execSQL("insert into employee values('" + no.getText().toString() + "','" + name.getText().toString() + "','" + radioButton.getText().toString() + "','" + age.getText().toString() + "')");

                    no.setText("");
                    name.setText("");
                    gender.clearCheck();
                    age.setText("");


                    Toast.makeText(getApplicationContext(), "successfully Registerd!", Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Fll all the feilds", Toast.LENGTH_LONG).show();

                }



            }
        });

    }

    public void btn_update(){

        update=(Button) findViewById(R.id.btnupdate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(".empUpdate");
                startActivity(intent);
            }
        });


    }
}
