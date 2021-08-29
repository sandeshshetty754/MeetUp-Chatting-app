package com.example.meetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.view.Menu;
import android.view.MenuItem;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
//    firebase
    private FirebaseAuth mAuth;

//    toolbar
    private Toolbar mToolbar;

//    tabs
    private ViewPager mViewpager;
    private SectionPagerAdapter mSectionPagerAdapter;
    private TabLayout mTablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        firebase
        mAuth=FirebaseAuth.getInstance();

//        toolbar
        mToolbar= findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("MEETUP");

//        Tabs
        mViewpager=findViewById(R.id.main_view_pager);
        mSectionPagerAdapter=new SectionPagerAdapter(getSupportFragmentManager());

        mViewpager.setAdapter(mSectionPagerAdapter);

        mTablayout=findViewById(R.id.main_tabs);
        mTablayout.setupWithViewPager(mViewpager);

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser==null){
            sendToStart();
        }
    }

    private void sendToStart() {
        Intent startIntent= new Intent(MainActivity.this, startActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
         if (item.getItemId()==R.id.main_log_ot_btn){
             FirebaseAuth.getInstance().signOut();
             sendToStart();
         }

         if(item.getItemId()==R.id.main_ac_setting_btn){
             Intent acc_intent=new Intent(MainActivity.this, AccSettingsActivity.class);
             startActivity(acc_intent);
         }
        if(item.getItemId()==R.id.main_user_btn){
            Intent user_intent=new Intent(MainActivity.this, UserActivity.class);
            startActivity(user_intent);
        }

        return true;
    }
}
