package com.success.successapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class newSession extends AppCompatActivity {

    private Button btnsend;

    private TextView lbloverview;
    private TextView lbltogline;
    private TextView lbloriginaltitle;
    private ImageView img;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_session);

        btnsend=(Button) findViewById(R.id.btnsend);
        getAPI();



    }

    public void getAPI(){
        lbloverview=findViewById(R.id.txtoverview);
        lbltogline=findViewById(R.id.txttogline);
        lbloriginaltitle=findViewById(R.id.txtoriginaltitle);
        img=(ImageView) findViewById(R.id.imageView2);


        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        sendRequest();

    }
    public void sendRequest(){

        RequestQueue requestQueue= Volley.newRequestQueue(newSession.this);
        String url="https://api.themoviedb.org/3/movie/550?api_key=04d3a169c1d6bd9f3e7af2516e169b30";
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


              //  Log.e("Response",response);
              //  Toast.makeText(newSession.this,response,Toast.LENGTH_LONG).show();
                pDialog.dismiss();
                try{
                    JSONObject jMovie=new JSONObject(response);
                    lbloverview.setText(jMovie.getString("overview"));
                    lbloriginaltitle.setText(jMovie.getString("original_title"));
                    lbltogline.setText(jMovie.getString("tagline"));

                    String url="http://image.tmdb.org/t/p/w185" + jMovie.getString("backdrop_path");
                    //Picasso.get().load(jMovie.getString("backdrop")).into(img);
                    Log.e("Image",url);
                   // http://image.tmdb.org/t/p/w185/mMZRKb3NVo5ZeSPEIaNW9buLWQ0.jpg
                   Picasso.get().load("http://image.tmdb.org/t/p/w185/mMZRKb3NVo5ZeSPEIaNW9buLWQ0.jpg").into(img);


                }
                catch(Exception e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
            requestQueue.add(request);
    }

}
