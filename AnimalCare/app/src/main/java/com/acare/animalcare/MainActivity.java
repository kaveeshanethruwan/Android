package com.acare.animalcare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView info,txtuseranme,txtemail;
    private LoginButton login;
    CallbackManager callbackManager;

    private static  final  String URL_DATA = "https://dl.dropboxusercontent.com/s/6nt7fkdt7ck0lue/hotels.json";
    private ViewHolder adapter;
    private List<Model> listItems;

    private RecyclerView mRecyclrView;
    private ProgressBar mProgressCircle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkloginStatus();
        fbLogin();
    }

    //facbook login
    public void fbLogin(){
        info=findViewById(R.id.txtinfo);
        txtuseranme=findViewById(R.id.txtusername);
        txtemail=findViewById(R.id.txtemail);
        login=findViewById(R.id.btnlogin);
        mProgressCircle = findViewById(R.id.animalprogress_circle);

        mProgressCircle.setVisibility(View.GONE);

        callbackManager = CallbackManager.Factory.create();


        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                info.setText("User Id : " +loginResult.getAccessToken().getUserId());

                //send request to json
                sendRequest();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    //send request to json to fetch data
    public void sendRequest(){

        mRecyclrView=findViewById(R.id.animalrecyclerView);
        mRecyclrView.setHasFixedSize(true);
        mRecyclrView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();

        StringRequest request=new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("data");

                    for(int i=0;i<array.length();i++){
                        JSONObject o = array.getJSONObject(i);

                       // JSONObject jsonImage = o.getJSONObject("image");
                       // String nameValue = jsonImage.getString("small");

                      //  String nameV = o.getJSONObject("data").getJSONObject("image").getString("small");

                        Model model = new Model(o.getString("title"),o.getString("address"),o.getString("image"),o.getString("description"));
                        listItems.add(model);
                    }

                    adapter= new ViewHolder(MainActivity.this,listItems);
                    mRecyclrView.setAdapter(adapter);
                    mProgressCircle.setVisibility(View.GONE);

                    adapter.setOnItemClickstner(new ViewHolder.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            TextView zname = view.findViewById(R.id.aname);
                           // ImageView zimage = view.findViewById(R.id.aImageView);

                            String xname = zname.getText().toString();

                          //  Drawable xdrawable = zimage.getDrawable();
                          //  Bitmap xbitmap = ((BitmapDrawable) xdrawable).getBitmap();

                            Intent intent = new Intent(view.getContext(), frmDetails.class);

                           // ByteArrayOutputStream stream = new ByteArrayOutputStream();
                           // xbitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            //byte[] bytes = stream.toByteArray();
                            intent.putExtra("title", xname);
                           // intent.putExtra("image", bytes);

                            startActivity(intent);
                        }
                    });

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
        RequestQueue requestQueue =Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    //set username and email
    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){
                txtemail.setText("");
                txtuseranme.setText("");
            }
            else{
                loaduserProfile(currentAccessToken);
            }
        }
    };

    private void loaduserProfile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");

                    txtemail.setText(first_name +" "+last_name );
                    txtemail.setText(email);




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    //checkUserStates
    public void checkloginStatus(){
        if(AccessToken.getCurrentAccessToken()!=null){
            loaduserProfile(AccessToken.getCurrentAccessToken());
        }
    }
}
