package com.example.meetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccSettingsActivity extends AppCompatActivity {
    private DatabaseReference muserDatabase;
    private FirebaseUser mCurrentuser;

    private CircleImageView mCircleimgview;
    private TextView mName;
    private TextView mStatus;
    private Button statusbtn;
    private Button imgbtn;
    StorageReference filepath;

    private static final int Gallery_pick=1;

//    storage firebase
    private StorageReference imgStorage;

    private ProgressDialog mProgressdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_settings);

        mCircleimgview=findViewById(R.id.profile_image);
        mName=findViewById(R.id.display_name);
        mStatus=findViewById(R.id.status_txt);

        statusbtn=findViewById(R.id.status_change_btn);
        imgbtn=findViewById(R.id.img_change_btn);

        imgStorage= FirebaseStorage.getInstance().getReference();

        mCurrentuser= FirebaseAuth.getInstance().getCurrentUser();
        String currentuserid=mCurrentuser.getUid();

        muserDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(currentuserid);

        muserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name= snapshot.child("name").getValue().toString();
                String image= snapshot.child("image").getValue().toString();
                String status= snapshot.child("status").getValue().toString();
                String thumb_img= snapshot.child("thumb_img").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);

                if(!image.equals("default")) {

                    Picasso.get().load(image).placeholder(R.drawable.profile).into(mCircleimgview);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        statusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status_value=mStatus.getText().toString();
                Intent status_btn_intent=new Intent(AccSettingsActivity.this,stutus_Activity.class);
                status_btn_intent.putExtra("status_value", status_value);
                startActivity(status_btn_intent);
            }
        });
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .start(AccSettingsActivity.this);

                Intent gallery_intent= new Intent();
                gallery_intent.setType("image/*");
                gallery_intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery_intent,"Select image"),Gallery_pick);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_pick && resultCode==RESULT_OK) {
            Uri imgUrl= data.getData();
            CropImage.activity(imgUrl)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mProgressdialog=new ProgressDialog(AccSettingsActivity.this);
                mProgressdialog.setTitle("Uploading image...");
                mProgressdialog.setMessage("please wait while we upload and process your image...");
                mProgressdialog.setCanceledOnTouchOutside(false);
                mProgressdialog.show();

                Uri resultUri = result.getUri();

                String current_user_id=mCurrentuser.getUid();




                StorageReference filepath=imgStorage.child("Profile_images").child(current_user_id+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            mProgressdialog.dismiss();
                            Toast.makeText(AccSettingsActivity.this,"successfully uploaded",Toast.LENGTH_LONG).show();

                                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String download_url=uri.toString();
                                                muserDatabase.child("image").setValue(download_url);
                                            }
                                        });
                                    }
                                    else{
                                        mProgressdialog.dismiss();
                                        Toast.makeText(AccSettingsActivity.this,"error in uploading",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }




            }

    }
//    public static String random() {
//        Random generator = new Random();
//        StringBuilder randomStringBuilder = new StringBuilder();
//        int randomLength = generator.nextInt(20);
//        char tempChar;
//        for (int i = 0; i < randomLength; i++){
//            tempChar = (char) (generator.nextInt(96) + 32);
//            randomStringBuilder.append(tempChar);
//        }
//        return randomStringBuilder.toString();
//    }

}
