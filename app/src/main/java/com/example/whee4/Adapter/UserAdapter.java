package com.example.whee4.Adapter;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.whee4.Model.ChatModel;
import com.example.whee4.Activity.MessageActivity;
import com.example.whee4.R;
import com.example.whee4.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<UserModel> mUsers;

    boolean newMessage;

    public UserAdapter(Context mContext, List<UserModel> mUsers){
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final UserModel user = mUsers.get(position);

        holder.username.setText(user.getUsername());
        holder.newMsg.setText("");

        if (user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.drawable.ic_baseline_person_24);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }

        newMessageCheck(user.getId(), holder.newMsg, holder.username);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;
        public TextView newMsg;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            newMsg = itemView.findViewById(R.id.newMsg);
        }
    }

    public void newMessageCheck(final String userid, TextView newMsg, TextView username){
        newMessage = false;
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatModel chat = snapshot.getValue(ChatModel.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getSender().equals(userid) && chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()) {
                            newMessage = true;
                            break;
                        }
                    }
                }

                if(newMessage){
                    username.setTypeface(null, Typeface.BOLD);
                    newMsg.setText("New Message");
                }

                newMessage = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}