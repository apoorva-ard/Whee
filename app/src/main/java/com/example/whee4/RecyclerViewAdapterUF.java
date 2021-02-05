package com.example.whee4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RecyclerViewAdapterUF extends RecyclerView.Adapter<RecyclerViewAdapterUF.ViewHolder> {

    Context context;
    List<UploadInfo> MainImageUploadInfoList;
    FirebaseStorage storage;
    DatabaseReference databaseReference;

    public RecyclerViewAdapterUF(Context context, List<UploadInfo> TempList) {
        this.MainImageUploadInfoList = TempList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_item_user_found, parent, false);
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

        storage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Found");

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete your upload?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String imgUrl = uploadInfo.getImageURL();
                        String defUrl = "https://firebasestorage.googleapis.com/v0/b/whee-c564b.appspot.com/o/Images%2Fdefault.png?alt=media&token=213d550e-5d70-43b6-968e-0a1f51357be9";
                        String id = uploadInfo.getKey();
                        if(imgUrl.equals(defUrl)){
                            databaseReference.child(id).removeValue();
                            Toast.makeText(context, "Item Deleted Successfully!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            StorageReference imgRef = storage.getReferenceFromUrl(uploadInfo.getImageURL());
                            imgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    databaseReference.child(id).removeValue();
                                    Toast.makeText(context, "Item Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            imgRef.delete().addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Deletion Failure!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "Deletion cancelled!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
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
        public Button arrow, delete;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageNameTextView = (TextView) itemView.findViewById(R.id.ImageNameTextView);
            textplace=(TextView) itemView.findViewById(R.id.TextPlace);
            textdate=(TextView) itemView.findViewById(R.id.TextDate);
            eview=(ConstraintLayout) itemView.findViewById(R.id.expandableView) ;
            textdetails=(TextView) itemView.findViewById(R.id.TextDetails);
            circleimage=(ImageView) itemView.findViewById(R.id.circleImage);
            arrow=(Button) itemView.findViewById(R.id.arrowBtn);
            delete = (Button) itemView.findViewById(R.id.delete_item);

            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UploadInfo uploadInfo = MainImageUploadInfoList.get(getAdapterPosition());
                    uploadInfo.setExpandable(!uploadInfo.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                    if(eview.getVisibility()==View.GONE)
                    {
                        arrow.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    }
                    else
                    {
                        arrow.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                    }
                }
            });

        }
    }
}
