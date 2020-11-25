package com.hfad.teach;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    EditText emailText;
    EditText passwordText;
    Button loginBtn;
    TextView signupLink,forgot,resend;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setTitle("Login");
        setSupportActionBar(toolbar);
        emailText = (EditText)findViewById(R.id.input_email);
        passwordText =(EditText)findViewById(R.id.input_password);
        loginBtn = (Button)findViewById(R.id.btn_login);
        signupLink = (TextView)findViewById(R.id.link_signup);
        forgot = (TextView)findViewById(R.id.forgot);
        resend = (TextView)findViewById(R.id.resend);
        setupFirebaseAuth();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail(emailText.getText().toString());
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(emailText.getText().toString())
                        && !isEmpty(passwordText.getText().toString())){

                    //temporarily authenticate and resend verification email
                    authenticateAndResendEmail(emailText.getText().toString(),
                            passwordText.getText().toString());
                }else{
                    Toast.makeText(LoginActivity.this, "all fields must be filled out", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private boolean isEmpty(String string){
        return string.equals("");
    }
    private void showDialog()
    {
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }
    private void hideDialog(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    private void login() {
        if(!isEmpty(emailText.getText().toString()) && !isEmpty(passwordText.getText().toString())){
            Log.d(TAG,"onClick: attempting to authenticate");
            showDialog();
         FirebaseAuth.getInstance().signInWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         hideDialog();

                     }
                 }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                 hideDialog();
             }

         });
        } else{
            Toast.makeText(LoginActivity.this,"You didn't fill in all the fields",Toast.LENGTH_SHORT).show();
        }
    }
private void setupFirebaseAuth(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser User = firebaseAuth.getCurrentUser();
                if(User!= null){
                    if(User.isEmailVerified()){
                        Log.d(TAG, "onAuthStateChanged: signed_in:" + User.getUid());
                        Toast.makeText(LoginActivity.this,"Authenticated with: " + User.getEmail(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else{
                        Toast.makeText(LoginActivity.this,"Check your Email inbox for a verification link",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }


                }
                else{
                    Log.d(TAG,"onAuthStateChanged:signed_out");
                }
            }
        };
}
@Override
protected  void onStart(){
        super.onStart();
        if(mAuthListener!=null){
            FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
        }
}
@Override
protected void onStop(){
        super.onStop();
        if(mAuthListener!=null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
}
    private void onLoginSuccess() {
        loginBtn.setEnabled(true);
        finish();
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginBtn.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the HomeActivity
        moveTaskToBack(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }
    public void sendPasswordResetEmail(String email) {
        if (!isEmpty(emailText.getText().toString())) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: Password Reset Email sent.");
                                Toast.makeText(LoginActivity.this, "Password Reset Link Sent to Email",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "onComplete: No user associated with that email.");
                                Toast.makeText(LoginActivity.this, "No User is Associated with that Email",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
        else {
            Toast.makeText(LoginActivity.this, "Please Enter Your Email in the Email field",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void authenticateAndResendEmail(String email, String password) {
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: reauthenticate success.");
                            sendVerificationEmail();
                            FirebaseAuth.getInstance().signOut();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Invalid Credentials. \nReset your password and try again", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Sent Verification Email",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(LoginActivity.this,"Couldn't send verification email",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}