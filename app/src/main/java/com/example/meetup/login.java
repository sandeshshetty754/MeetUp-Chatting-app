package com.example.meetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class login extends AppCompatActivity {

    private Toolbar mToolbar;

    private FirebaseAuth mAuth;

    private TextInputLayout nLoginemail;
    private TextInputLayout nPassword;
    private Button nLoginbtn;

    private ProgressDialog nPD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        mToolbar=findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("LOGIN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nPD=new ProgressDialog(this);

        nLoginemail=(TextInputLayout) findViewById(R.id.login_email);
        nPassword=(TextInputLayout) findViewById(R.id.login_password);
        nLoginbtn=(Button) findViewById(R.id.login_create_btn);



        nLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=nLoginemail.getEditText().getText().toString();
                String password=nPassword.getEditText().getText().toString();
                if (TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    Toast.makeText(login.this,"fields cannot be empty",Toast.LENGTH_LONG).show();
                }else{
                    nPD.setTitle("Logging in");
                    nPD.setMessage("please wait till we check your credentials");
                    nPD.setCanceledOnTouchOutside(false);
                    nPD.show();
                    Login(email,password);
                }
            }
        });
    }

    private void Login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    nPD.dismiss();
                    Intent mainIntent=new Intent(login.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
                else {
                    nPD.hide();
                    Toast.makeText(login.this,"cannot log in please check",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
