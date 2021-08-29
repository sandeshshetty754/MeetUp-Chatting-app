package com.example.meetup;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

class UserPagerAdapter extends FragmentPagerAdapter {

    int tabcount;
    public UserPagerAdapter(@NonNull FragmentManager fm,int behaviour) {
        super(fm);
        tabcount=behaviour;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new sports_intrestFragment();

            case 1:
                return new music_intrestFragment();

            case 2:
                return new FitnessintrestFragment();

            case 3:
                return new MoviesIntrestFragment();

            case 4:
                return new CookingFragment();

            case 5:
                return new PhotographyFragment();

            case 6:
                return new GadgetsFragment();

            case 7:
                return new TravelFragment();

            case 8:
                return new Video_gamesFragment();

            case 9:
                return new ReadingFragment();

            default: return  null;
        }


    }



    @Override
    public int getCount() {
        return tabcount;
    }




}
