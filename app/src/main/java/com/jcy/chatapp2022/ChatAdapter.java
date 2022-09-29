package com.jcy.chatapp2022;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private FirebaseStorage mFirebaseStr;
    private StorageReference mStorageRef;
    private StorageReference mStorageImgRef;

    FirebaseDatabase database;

    private ArrayList<Chat> cDataset;

    String stMyEmail = "";
    String stMyName = "";
    String stMyDep = "";
    String stMyRank = "";

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public TextView txt_chat_name;
        public TextView txt_chat_dep;
        public TextView txt_chat_rank;
        public ImageView iv_user_chat;
        public MyViewHolder(View v){
            super(v);
            textView = v.findViewById(R.id.tv_chat);
            iv_user_chat = v.findViewById(R.id.iv_user_chat);
            txt_chat_name = v.findViewById(R.id.txt_chat_name);
            txt_chat_dep = v.findViewById(R.id.txt_chat_dep);
            txt_chat_rank = v.findViewById(R.id.txt_chat_rank);
        }
    }

    //채팅 좌우 확인
    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        if(cDataset.get(position).email.equals(stMyEmail)){
            return 1;
        } else {
            return 2;
        }
    }
//,String stName,String stDep, String stRank
    public ChatAdapter(ArrayList<Chat> myDataset, String stEmail, String stName, String stDep, String stRank) {
        cDataset = myDataset;
        this.stMyEmail = stEmail;
        this.stMyName = stName;
        this.stMyDep = stDep;
        this.stMyRank = stRank;
    }


    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        //채팅 화면 내쪽
        if(viewType == 1){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.right_text_view, parent, false);
        }
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //데이터베이스 초기화
        database = FirebaseDatabase.getInstance();

        holder.textView.setText(cDataset.get(holder.getAbsoluteAdapterPosition()).getText());


        //프로필 사진 보여주는 곳
        mFirebaseStr = FirebaseStorage.getInstance();
        mStorageRef = mFirebaseStr.getReference();

        //mStorageImgRef = mStorageRef.child("users/" + dataSnapshot1.getValue().toString()+"/profile.jpg");
        mStorageImgRef = mStorageRef.child("users/" + cDataset.get(holder.getAbsoluteAdapterPosition()).getEmail() + "/profile.jpg");
        if(mStorageImgRef != null){
            mStorageImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(holder.itemView.getContext()).load(uri).into(holder.iv_user_chat);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Uri1"+e);
//                    ivUserNone.setImageResource(R.drawable.noimg);
                }

            });
        }


        Log.d(TAG, "txt_chat_name : "+ cDataset.get(holder.getAbsoluteAdapterPosition()).getName());
        Log.d(TAG, "txt_chat_dep : "+ cDataset.get(holder.getAbsoluteAdapterPosition()).getRank());
        Log.d(TAG, "txt_chat_rank : "+ cDataset.get(holder.getAbsoluteAdapterPosition()).getDepartment());

        if(cDataset.get(holder.getAbsoluteAdapterPosition()).email.equals(stMyEmail)){
            holder.txt_chat_name.setText(stMyName);
            holder.txt_chat_dep.setText(stMyDep);
            holder.txt_chat_rank.setText(stMyRank);
        }else{
            holder.txt_chat_name.setText(cDataset.get(holder.getAbsoluteAdapterPosition()).getName());
            holder.txt_chat_dep.setText(cDataset.get(holder.getAbsoluteAdapterPosition()).getRank());
            holder.txt_chat_rank.setText(cDataset.get(holder.getAbsoluteAdapterPosition()).getDepartment());
        }

    }

    @Override
    public int getItemCount() {
        return cDataset.size();
    }
}
