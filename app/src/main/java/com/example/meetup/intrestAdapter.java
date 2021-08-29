package com.example.meetup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class intrestAdapter extends FirebaseRecyclerAdapter<model,intrestAdapter.myViewHolder> {
    public intrestAdapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull model model) {
        holder.name.setText(model.getName());
        holder.status.setText("Intrested in: "+model.getStatus());
//        Glide.with(holder.profileImg.getContext()).load(model.getPurl()).into(holder.profileImg);



        Picasso.get().load(model.getImage()).placeholder(R.drawable.profile).into(holder.profileImg);

        String user_id=getRef(position).getKey();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), ProfilesOfUsers.class);
                intent.putExtra("user_id",user_id);
                v.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ser_singlelayout,parent,false);
         return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView name,status;
        CircleImageView profileImg;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            mView=itemView;

            profileImg=itemView.findViewById(R.id.single_user_profile);
            name=itemView.findViewById(R.id.single_user_name);
            status=itemView.findViewById(R.id.single_user_status);
        }
    }
}
