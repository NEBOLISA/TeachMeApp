package com.hfad.teach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.teach.Models.Notes;
import com.hfad.teach.Models.Users;

import java.util.ArrayList;
import java.util.List;

public class SignupActivity extends AppCompatActivity  {
    private static final String TAG = "SignupActivity";
    EditText nameText;
    EditText emailText;
    EditText passwordText;
    EditText reEnterPasswordText;
    private ProgressDialog progressDialog;
    Button signupButton;
    TextView loginLink;
    String userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setTitle("SignUp");
        setSupportActionBar(toolbar);
        final Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        String[] items = new String[]{"Teacher","Student"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                    case 1:
                        userType = parent.getItemAtPosition(position).toString();
                        break;
                    default:
                        break;

                }
                //Toast.makeText(getApplicationContext(),userType,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        nameText = (EditText) findViewById(R.id.input_name);
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        reEnterPasswordText = (EditText) findViewById(R.id.input_reenter);
        signupButton = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);
       loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               backToLoginScreen();
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick:attempting to register");
                        if(!isEmpty(emailText.getText().toString()) && !isEmpty(passwordText.getText().toString()) && !isEmpty(nameText.getText().toString())  )
                        {
                            if(doStringsMatch(passwordText.getText().toString(),reEnterPasswordText.getText().toString())){
                                registerNewEmail(emailText.getText().toString(), passwordText.getText().toString(),userType);

                            }
                            else{
                                Toast.makeText(SignupActivity.this,"Passwords don't match",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(SignupActivity.this,"You must fill out all fields",Toast.LENGTH_SHORT).show();
                        }
            }
            });
hideSoftKeyboard();
    }
private void sendVerificationEmail(){
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    if(user!=null){
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignupActivity.this,"Sent Verification Email",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignupActivity.this,"Couldn't send verification email",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void showDialog()
    {
         progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


    }
    private boolean isEmpty(String string){
        return string.equals("");
    }
    private boolean doStringsMatch(String s1, String s2){
        return s1.equals(s2);
    }
    private void registerNewEmail(final String email, String password, final String userType){
        showDialog();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "onComplete: onComplete:" + task.isSuccessful());
                showDialog();
                if(task.isSuccessful()) {
                    Log.d(TAG, "onComplete:AuthState:" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                    String name = nameText.getText().toString();
                    sendVerificationEmail();
                    Users user = new Users();
                    user.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    user.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    user.setName(name);
                    user.setUser_type(userType);
                    FirebaseDatabase.getInstance().getReference()
                            .child(getString(R.string.dbnode_users))
                            .child(FirebaseAuth.getInstance()
                                    .getCurrentUser().getUid()).setValue(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseAuth.getInstance().signOut();
                            backToLoginScreen();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().signOut();
                            backToLoginScreen();
                        }
                    });

                }
                if(!task.isSuccessful()){
                    Toast.makeText(SignupActivity.this, "Unable to Register", Toast.LENGTH_SHORT).show();
                }
                hideDialog();
            }
        });
    }

    private void backToLoginScreen() {
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen");
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void hideDialog(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
       }

    @Override
    public void onBackPressed() {
        // Disable going back to the HomeActivity
        moveTaskToBack(true);
    }

}