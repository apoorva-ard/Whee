package com.example.whee4.Adapter;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.whee4.Activity.MessageActivity;
import com.example.whee4.R;
import com.example.whee4.Activity.UploadInfo;

import java.util.List;

public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewAdapter1.ViewHolder> {

    Context context;
    List<UploadInfo> MainImageUploadInfoList;

    public RecyclerViewAdapter1(Context context, List<UploadInfo> TempList) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_item1, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UploadInfo uploadInfo = MainImageUploadInfoList.get(position);

        holder.imageNameTextView.setText(uploadInfo.getImageName());

        holder.textplace.setText(uploadInfo.getPlace());
        holder.textdate.setText(uploadInfo.getDate());
        holder.textdetails.setText(uploadInfo.getDetails());
        Glide.with(context).load(uploadInfo.getImageURL()).into(holder.imageView);
        Glide.with(context).load(uploadInfo.getImageURL()).into(holder.circleimage);
        boolean isExpandable=MainImageUploadInfoList.get(position).isExpandable();
        holder.eview.setVisibility(isExpandable?View.VISIBLE:View.GONE);
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

        public ImageView imageView,circleimage;
        public TextView imageNameTextView,textplace,textdetails,textdate;
        public ConstraintLayout eview;
        public Button arrow, chat;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView1);
            imageNameTextView = (TextView) itemView.findViewById(R.id.ImageNameTextView1);
            textplace=(TextView) itemView.findViewById(R.id.TextPlace1);
            textdate=(TextView) itemView.findViewById(R.id.TextDate1);
            eview=(ConstraintLayout) itemView.findViewById(R.id.expandableView1) ;
            textdetails=(TextView) itemView.findViewById(R.id.TextDetails1);
            circleimage=(ImageView) itemView.findViewById(R.id.circleImage1);
            arrow=(Button) itemView.findViewById(R.id.arrowBtn1);
            chat = (Button) itemView.findViewById(R.id.send_msg1);

            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UploadInfo uploadInfo=MainImageUploadInfoList.get(getAdapterPosition());
                    uploadInfo.setExpandable(!uploadInfo.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                    if(eview.getVisibility()==View.GONE){
                        arrow.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    }else{
                        arrow.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                    }
                }
            });
        }
    }
}
