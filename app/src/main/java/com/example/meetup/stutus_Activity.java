package com.example.meetup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class stutus_Activity extends AppCompatActivity {
    private Toolbar mToolbar;

    private TextInputLayout mInput;
    private Button mBtn;
    private Spinner status_selectSpinner;

//    firebase
    private DatabaseReference mstatusdb;
    private FirebaseUser mCurrentuser;

    private ProgressDialog mProgressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stutus_);

        status_selectSpinner=findViewById(R.id.status_change_spinner);
        String[] intrest_items=getResources().getStringArray(R.array.intrest_items);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,intrest_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status_selectSpinner.setAdapter(adapter);

        mToolbar=findViewById(R.id.status_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        String status_value=getIntent().getStringExtra("status_value");

//
        mCurrentuser= FirebaseAuth.getInstance().getCurrentUser();
        String current_usr=mCurrentuser.getUid();

        mstatusdb= FirebaseDatabase.getInstance().getReference().child("users").child(current_usr);

//        mInput=findViewById(R.id.status_input);
        mBtn=findViewById(R.id.status_update_btn);


        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressdialog=new ProgressDialog(stutus_Activity.this);
                mProgressdialog.setTitle("Saving changes");
                mProgressdialog.setMessage("please wait while we save the changes");
                mProgressdialog.show();

                String status=status_selectSpinner.getSelectedItem().toString();
                mstatusdb.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mProgressdialog.dismiss();
                        }else {
                            Toast.makeText(getApplicationContext(),"there is some error in saving changes",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }
}
