package com.easytipsforhomefarming.easytipsforhomefarmingclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Pattern;

public class frmLogin extends AppCompatActivity {

    private EditText email;
    private EditText pwd;
    private Button login;
    int attempt_counter=5;
    private Button signup;

    private TextView forgotP;
    private ProgressBar mProgressCircle;


    long maxid=0;

    FirebaseAuth firebaseAuth;
    //googleSignIn
    private SignInButton mGoogleBtn;
    private  static  final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG="MAIN_ACTIVITY";

    private TextView forgotPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_login);

        mProgressCircle = findViewById(R.id.mainprogressbar);
        mAuth=FirebaseAuth.getInstance();
        mProgressCircle.setVisibility(View.GONE);

        mGoogleBtn=(SignInButton) findViewById(R.id.googltBtn);
        email=(EditText) findViewById(R.id.txtemail);
        pwd=(EditText) findViewById(R.id.txtpwd);
        login=(Button) findViewById(R.id.btnlogin);
        gSignIn();
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        setGooglePlusButtonText(mGoogleBtn,"Sign in with Google");
        mProgressCircle.setVisibility(View.GONE);

        callSignup();
        login();
        forgotPasswrd();
    }
    //forgot password
    public void forgotPasswrd(){
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(frmLogin.this,frmForgotpassword.class));
            }
        });
    }


    //Login
    public void login() {
        try{

            email = (EditText) findViewById(R.id.txtemail);
            forgotPassword =  findViewById(R.id.tvForgotPassword);
            pwd = (EditText) findViewById(R.id.txtpwd);
            login = (Button) findViewById(R.id.btnlogin);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (email.getText().toString().equals("")) {

                        Toast.makeText(frmLogin.this, "Provide your email", Toast.LENGTH_SHORT).show();

                    } else if (!isEmailValid(email.getText().toString())) {
                        Toast.makeText(frmLogin.this, "Invalid email", Toast.LENGTH_SHORT).show();

                    } else if (pwd.getText().toString().equals("")) {
                        Toast.makeText(frmLogin.this, "Invalid password", Toast.LENGTH_SHORT).show();

                    } else {
                        mProgressCircle.setVisibility(View.VISIBLE);
                        checkEmailExistsOrNot();
                    }

                }
            });

        }
        catch(Exception e){
            Toast.makeText(frmLogin.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    //checkEmail
    void checkEmailExistsOrNot(){
        firebaseAuth.fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                //  Log.d(TAG,""+task.getResult().getSignInMethods().size());
                if (task.getResult().getSignInMethods().size() == 0){
                    Toast.makeText(frmLogin.this, "No user found", Toast.LENGTH_SHORT).show();
                    mProgressCircle.setVisibility(View.GONE);

                }else {
                    singIn();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    //singIn
    public void singIn(){
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), pwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    startActivity(new Intent(frmLogin.this,frmHome.class));
                    //  Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(frmLogin.this, "Invalid password", Toast.LENGTH_LONG).show();
                    mProgressCircle.setVisibility(View.GONE);


                }
            }
        });
    }
    //email validation
    public boolean isEmailValid(String text){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(text).matches();
    }

    //call signup
    public void callSignup(){
        signup=findViewById(R.id.btnsignup);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(frmLogin.this,frmSignup.class));
            }
        });

    }

    //changeText
    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    //gSignIn
    public void gSignIn(){
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(frmLogin.this,"You got a error",Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
      //  mAuth.addAuthStateListener(mAuthListener);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account=result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                startActivity(new Intent(frmLogin.this,frmHome.class));

            }
            else{

            }

        }
    }

    private  void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"signWithCredetial:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG,"signWithCredetial:onComplete:",task.getException());
                            Toast.makeText(frmLogin.this,"Authentication failed",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(frmLogin.this,frmHome.class));


                        }

                        // ...
                    }
                });
    }

}
