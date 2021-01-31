package com.example.whee4;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<UploadInfo> MainImageUploadInfoList;

    public RecyclerViewAdapter(Context context, List<UploadInfo> TempList) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UploadInfo uploadInfo = MainImageUploadInfoList.get(position);
        holder.imageNameTextView.setText(uploadInfo.getImageName());
        Glide.with(context).load(uploadInfo.getImageURL()).into(holder.imageView);

        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uId = uploadInfo.getUserId();
                if(uId!=null){
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("userid", uId);
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context, "user id empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView imageNameTextView;
        public Button chat;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageNameTextView = (TextView) itemView.findViewById(R.id.ImageNameTextView);
            chat = (Button) itemView.findViewById(R.id.send_msg);
        }
    }
}
