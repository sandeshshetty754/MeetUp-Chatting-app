package com.example.meetup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class sports_intrestFragment extends Fragment {

    private RecyclerView sportsRecyclerview;
    intrestAdapter adapter;

      @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
          View v=inflater.inflate(R.layout.fragment_sports_intrest,null);

          sportsRecyclerview=v.findViewById(R.id.recyclerviewofsportsintrest);
          sportsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

          FirebaseRecyclerOptions<model> options =
                  new FirebaseRecyclerOptions.Builder<model>()
                          .setQuery(FirebaseDatabase.getInstance().getReference().child("users").orderByChild("status").equalTo("sports"), model.class)
                          .build();

          adapter=new intrestAdapter(options);
          sportsRecyclerview.setAdapter(adapter);

        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
