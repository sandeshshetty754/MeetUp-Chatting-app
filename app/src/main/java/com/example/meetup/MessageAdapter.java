package com.example.meetup;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AkshayeJH on 24/07/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Messages> messagesList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public MessageAdapter(List<Messages> messagesList) {
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_msg, parent, false);
            return new MessageViewHolder(view, "left");
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.their_msg, parent, false);
            return new MessageViewHolder(view, "right");
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        Messages message = messagesList.get(position);
        long time = message.getTime();
        SimpleDateFormat jdf = new SimpleDateFormat("dd-MM-yy H:mm");
        jdf.setTimeZone(TimeZone.getDefault());
        String timeOfText = jdf.format(time);
        holder.timestamp.setText(timeOfText);
        switch (message.getType()) {
            case "text":
                holder.messageBody.setText(message.getMessage());
                holder.messageBody.setVisibility(View.VISIBLE);
                holder.messageImage.setVisibility(View.GONE);
                ((RelativeLayout.LayoutParams) holder.timestamp.getLayoutParams()).addRule(RelativeLayout.BELOW, holder.messageBody.getId());
                break;

            case "image":
                holder.messageBody.setVisibility(View.GONE);
                holder.messageImage.setVisibility(View.VISIBLE);
                Picasso.get().load(message.getMessage())
                        .placeholder(R.drawable.profile)
                        .into(holder.messageImage);

                ((RelativeLayout.LayoutParams) holder.timestamp.getLayoutParams()).addRule(RelativeLayout.BELOW, holder.messageImage.getId());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        String currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Messages m = messagesList.get(position);
        if (m.getFrom().equals(currentUserId)) {
            return 1;
        } else {
            return 0;
        }
//        return super.getItemViewType(position);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageBody;
        TextView timestamp;
        AppCompatImageView messageImage;

        public MessageViewHolder(@NonNull View itemView, String paddingDirection) {
            super(itemView);
            messageBody = itemView.findViewById(R.id.message_body);
            timestamp = itemView.findViewById(R.id.timestamp);
            messageImage = itemView.findViewById(R.id.message_image);
        }
    }
}