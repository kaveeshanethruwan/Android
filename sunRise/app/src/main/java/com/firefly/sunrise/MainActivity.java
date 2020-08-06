package com.firefly.sunrise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressCircle = findViewById(R.id.mainprogressbar);


        // try{ firebaseAuth=FirebaseAuth.getInstance();
     //  FirebaseUser firebaseUsercheck=firebaseAuth.getCurrentUser();
      //  if(firebaseUsercheck != null){
      //     startActivity(new Intent(MainActivity.this,frmNewHome.class));
      //      finish();
      //  }
      //  else{
     //       loginbutton();
      //      signupCall();
      //     resetPassword();
      //  }}
    // catch (Exception e){
      //   Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();



        mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if(firebaseAuth.getCurrentUser()!=null){
                startActivity(new Intent(MainActivity.this,frmNewHome.class));
                finish();
            }
            else{
                mProgressCircle.setVisibility(View.GONE);

                loginbutton();
                signupCall();
                resetPassword();
            }
           }
        };

        mGoogleBtn=(SignInButton) findViewById(R.id.googltBtn);
        email=(EditText) findViewById(R.id.txtemail);
        pwd=(EditText) findViewById(R.id.txtpwd);
        login=(Button) findViewById(R.id.btnlogin);
        gSignIn();
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        setGooglePlusButtonText(mGoogleBtn,"Sign in with Google");


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
                        Toast.makeText(MainActivity.this,"You got a error",Toast.LENGTH_LONG).show();
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
        mAuth.addAuthStateListener(mAuthListener);
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
                            Toast.makeText(MainActivity.this,"Authentication failed",Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });
    }

    public void loginHome(){
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),pwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    startActivity(new Intent(MainActivity.this,frmNewHome.class));
                    finish();

                }
                else{
                    Toast.makeText(MainActivity.this,"Incorrect password",Toast.LENGTH_LONG).show();
                    attempt_counter--;
                    mProgressCircle.setVisibility(View.GONE);

                }
            }
        });
    }

    //checkEmail
    void checkEmailExistsOrNot(){
        firebaseAuth.fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                //  Log.d(TAG,""+task.getResult().getSignInMethods().size());
                if (task.getResult().getSignInMethods().size() == 0){
                    Toast.makeText(MainActivity.this, "No user found", Toast.LENGTH_SHORT).show();
                    attempt_counter--;
                    mProgressCircle.setVisibility(View.GONE);

                }else {
                    loginHome();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    //login button
    public void loginbutton(){
        email=(EditText) findViewById(R.id.txtemail);
        pwd=(EditText) findViewById(R.id.txtpwd);
        login=(Button) findViewById(R.id.btnlogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(attempt_counter==0){
                        Toast.makeText(MainActivity.this,"You got maximum loggin limit.Try again later!",Toast.LENGTH_SHORT).show();
                        login.setEnabled(false);

               }
               else{

                   if(email.getText().toString().equals("")){

                       Toast.makeText(MainActivity.this, "Fill an Email", Toast.LENGTH_SHORT).show();

                   }
                   else if(! isEmailValid(email.getText().toString())){
                       Toast.makeText(MainActivity.this, "Please enter an email correct format", Toast.LENGTH_SHORT).show();

                   }
                   else if(pwd.getText().toString().equals("")){
                       Toast.makeText(MainActivity.this, "Fill password", Toast.LENGTH_SHORT).show();

                   }
                   else{
                       mProgressCircle.setVisibility(View.VISIBLE);
                       checkEmailExistsOrNot();

                   }

                   }
            }
        });

    }




        //sign up textViewer
        public void signupCall() {
            signup =  findViewById(R.id.tvsignup);
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,frmVerifying.class));


                }
            });

        }

        //resetPasword
        public void resetPassword(){
            try{
                forgotP =  findViewById(R.id.tvsignup2);
                forgotP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this,frmResetPassword.class));

                    }
                });

            }
            catch (Exception e){
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

            }
        }




    public boolean isUsernameValid(String text){

        return text.matches("^[a-z0-9_-]{3,15}$");
    }
    public boolean isPasswordValid(String text){

        // return text.matches("((?=.*[a-z])(?=.*\\\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})");
        return text.matches(" ^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{4,}$");

    }
    public boolean isEmailValid(String text){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(text).matches();
    }
}
