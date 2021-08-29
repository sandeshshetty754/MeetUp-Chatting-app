package com.example.meetup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    private RecyclerView mRequestsList;

    private DatabaseReference mRequestsDatabase;

    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;


    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_request, container, false);

        mRequestsList = mMainView.findViewById(R.id.requests_list);

        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mRequestsDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req").child(mCurrent_user_id);
        mRequestsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mUsersDatabase.keepSynced(true);

        mRequestsList.setHasFixedSize(true);
        mRequestsList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate the layout for this fragment
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Requests> options=
                new FirebaseRecyclerOptions.Builder<Requests>()
                        .setQuery(mRequestsDatabase,Requests.class)
                        .setLifecycleOwner(this)
                        .build();

        FirebaseRecyclerAdapter requestsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Requests, RequestsViewHolder>(options) {

            @NonNull
            @Override
            public RequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ser_singlelayout, parent, false);
                return new RequestFragment.RequestsViewHolder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull final RequestsViewHolder requestsViewHolder, int position, @NonNull Requests requests) {

//                requestsViewHolder.setDate(requests.getDate());

                final String list_user_id = getRef(position).getKey();

                assert list_user_id != null;
                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("image").getValue().toString();

//                        if(dataSnapshot.hasChild("online")) {
//
//                            String userOnline = dataSnapshot.child("online").getValue().toString();
//                            requestsViewHolder.setUserOnline(userOnline);
//
//                        }

                        requestsViewHolder.setName(userName);
                        requestsViewHolder.setUserImage(userThumb, getContext());

                        requestsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent profileIntent = new Intent(getContext(), ProfilesOfUsers.class);
                                profileIntent.putExtra("user_id", list_user_id);
                                startActivity(profileIntent);

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mRequestsList.setAdapter(requestsRecyclerViewAdapter);
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction();
    }

    public static  class RequestsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public RequestsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }
//
//        public void setDate(String date){
//
//            TextView userStatusView = mView.findViewById(R.id.single_user_status);
//            userStatusView.setText(date);
//
//        }

        public void setName(String name){

            TextView userNameView =  mView.findViewById(R.id.single_user_name);
            userNameView.setText(name);

        }

        public void setUserImage(String thumb_image, Context ctx){

            CircleImageView userImageView =  mView.findViewById(R.id.single_user_profile);
            Picasso.get().load(thumb_image).placeholder(R.drawable.profile).into(userImageView);

        }

//        public void setUserOnline(String online_status) {
//
//            ImageView userOnlineView =  mView.findViewById(R.id.user);
//
//            if(online_status.equals("true")){
//
//                userOnlineView.setVisibility(View.VISIBLE);
//
//            } else {
//
//                userOnlineView.setVisibility(View.INVISIBLE);
//
//            }
//
//        }
    }
}