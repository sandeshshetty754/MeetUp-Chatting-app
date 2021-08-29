package com.example.meetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class registerActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private Toolbar mToolbar;

    private DatabaseReference mDatabase;


    private ProgressDialog mRegdialog;

    private TextInputLayout rName;
    private TextInputLayout rEmail;
    private TextInputLayout rPassword;
    private Button rbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mToolbar=findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegdialog=new ProgressDialog(this);


        rName=findViewById(R.id.reg_name_layout);
        rEmail=findViewById(R.id.reg_email_layout);
        rPassword=findViewById(R.id.reg_password_layout);
        rbtn=findViewById(R.id.reg_create_btn);

        rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=rName.getEditText().getText().toString();
                String email=rEmail.getEditText().getText().toString();
                String password=rPassword.getEditText().getText().toString();

                if (TextUtils.isEmpty(name)&&TextUtils.isEmpty(email)&&TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"fields cannot be empty",Toast.LENGTH_LONG).show();
                }else {
                    mRegdialog.setTitle("Registering user");
                    mRegdialog.setMessage("please wait while we create your account");
                    mRegdialog.setCanceledOnTouchOutside(false);
                    mRegdialog.show();

                    register_usr(name, email, password);
                }
            }
        });
    }

    private void register_usr(String name, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser currentuser= FirebaseAuth.getInstance().getCurrentUser();
                    if(currentuser!=null) {
                        String uid = currentuser.getUid();

                        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);


                        HashMap<String, String> userMap = new HashMap<>();
                        userMap.put("name", name);
                        userMap.put("status", "hey there, Iam using meetup app");
                        userMap.put("image", "default");
                        userMap.put("thumb_img", "default");

                        mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mRegdialog.dismiss();

                                    Intent mainIntent = new Intent(registerActivity.this, MainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            }
                        });
                    }


                }else {
                    mRegdialog.hide();
                    Toast.makeText(registerActivity.this,"cannot sign in please check the details",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
