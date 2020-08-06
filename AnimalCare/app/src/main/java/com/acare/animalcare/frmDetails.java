package com.acare.animalcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class frmDetails extends AppCompatActivity {

    private TextView textViewtitle;
    private TextView textViewdes;
    private String title,description;
    private ImageView imageView;
    byte[] bytes;
    Bitmap bmp;
    private static  final  String URL_DATA = "https://dl.dropboxusercontent.com/s/6nt7fkdt7ck0lue/hotels.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_details);

        setFromRec();
        requestDescription();
    }

    //Recive data from recycleview
    public void setFromRec(){
        textViewtitle=findViewById(R.id.dtitle);
        textViewdes=findViewById(R.id.ddescription);


        //bytes=getIntent().getByteArrayExtra("image");
        title=getIntent().getStringExtra("title");

       // bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);

        textViewtitle.setText(title);
       // imageView.setImageBitmap(bmp);
    }

    //Retrive description
     public void requestDescription(){

        StringRequest request=new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("data");

                    for(int i=0;i<array.length();i++){
                        JSONObject o = array.getJSONObject(i);

                        String titleCoppy=o.getString("title");

                        if(title==titleCoppy)  // compare title here
                        {
                            description=o.getString("description");
                            textViewdes.setText(description);

                        }
                    }

                }
                catch(Exception e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
