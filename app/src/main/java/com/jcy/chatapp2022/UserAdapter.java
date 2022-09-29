package com.jcy.chatapp2022;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private static final String TAG = "UsersAdapter";

    //파이어베이스 스토어(회원가입시 회원정보 내용 저장을 위한 변수)
    FirebaseFirestore dbStore;

    private FirebaseStorage mFirebaseStr;
    private StorageReference mStorageRef;
    private StorageReference mStorageImgRef;

    FirebaseDatabase database;

    private ArrayList<User> mDataset;
    String stMyEmail = "";
    String stMyFriend = "";
    String getNewMessage = "";


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUser;
        public ImageView ivUser;
        public TextView goChat;
        public TextView newMessage;
        public TextView user_item_name;
        public TextView user_item_rank;
        public TextView user_item_dep;
//        public TextView search_txt;

        public MyViewHolder(View v){
            super(v);
            tvUser = v.findViewById(R.id.tvUser);
            ivUser = v.findViewById(R.id.ivUser);
            goChat = v.findViewById(R.id.goChat);
            newMessage = v.findViewById(R.id.new_message);
            user_item_name = v.findViewById(R.id.user_item_name);
            user_item_rank = v.findViewById(R.id.user_item_rank);
            user_item_dep = v.findViewById(R.id.user_item_dep);
//            search_txt = v.findViewById(R.id.search_txt);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);

    }



    public UserAdapter(ArrayList<User> myDataset, String stEmail, String stFriend) {
        mDataset = myDataset;
        this.stMyEmail = stEmail;
        this.stMyFriend = stFriend;
        Log.d(TAG, "UserAdapter key stMyEmail"+stMyEmail);
        Log.d(TAG, "UserAdapter key stMyFriend"+stMyFriend);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //데이터베이스 초기화
        database = FirebaseDatabase.getInstance();

        //프로필 사진 보여주는 곳
        mFirebaseStr = FirebaseStorage.getInstance();
        mStorageRef = mFirebaseStr.getReference();

        //mStorageImgRef = mStorageRef.child("users/" + dataSnapshot1.getValue().toString()+"/profile.jpg");
        mStorageImgRef = mStorageRef.child("users/" + mDataset.get(holder.getAbsoluteAdapterPosition()).getEmail() + "/profile.jpg");
        if(mStorageImgRef != null){
            mStorageImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(holder.itemView.getContext()).load(uri).into(holder.ivUser);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Uri1"+e);
//                    ivUserNone.setImageResource(R.drawable.noimg);
                }

            });
        }
        holder.tvUser.setText(mDataset.get(holder.getAbsoluteAdapterPosition()).getEmail());

        String name = mDataset.get(holder.getAbsoluteAdapterPosition()).getName();
        holder.user_item_name.setText(name);

        String rank = mDataset.get(holder.getAbsoluteAdapterPosition()).getRank();
        holder.user_item_rank.setText(rank);

        String dep = mDataset.get(holder.getAbsoluteAdapterPosition()).getDepartment();
        holder.user_item_dep.setText(dep);

//        String nowKey = mDataset.get(holder.getAbsoluteAdapterPosition()).getKey();
//        String nowName = holder.user_item_name.getText().toString();
//        String nowRank = holder.user_item_rank.getText().toString();
//        String nowDep = holder.user_item_dep.getText().toString();

        holder.goChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "intent before key stMyEmail"+stMyEmail);
                Log.d(TAG, "intent before key stMyFriend"+ mDataset.get(holder.getAbsoluteAdapterPosition()).getKey());

                Intent intent = new Intent(((Activity)v.getContext()).getApplicationContext(),ChatActivity.class);
                intent.putExtra("email", stMyEmail);
                Log.d(TAG, "intent after key stMyEmail"+stMyEmail);

                intent.putExtra("key",  mDataset.get(holder.getAbsoluteAdapterPosition()).getKey());
                Log.d(TAG, "intent after key stMyFriend"+mDataset.get(holder.getAbsoluteAdapterPosition()).getKey());


//                intent.putExtra("name", nowName);
//                Log.d(TAG, "intent after key name"+mDataset.get(holder.getAbsoluteAdapterPosition()).getName());
//
//                intent.putExtra("department", nowDep);
//                intent.putExtra("rank", nowRank);

                ((Activity)v.getContext()).startActivity(intent);
            }
        });
    }

//    // 데이터 필터 검색 Filterable implement
//    public Filter getFilter() {
//        return exampleFilter;
//    }
//    private Filter exampleFilter = new Filter() {
//        //Automatic on background thread
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            ArrayList<User> filteredList = new ArrayList<>();
//
//            if (constraint == null || constraint.length() == 0) {
//                filteredList.addAll(mDataset);
//            } else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//                for (User item : mDataset) {
//                    //TODO filter 대상 setting
//                    if (item.getName().toLowerCase().contains(filterPattern)) {
//                        filteredList.add(item);
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//            return results;
//        }
//
//        //Automatic on UI thread
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            mDataset.clear();
//            mDataset.addAll((List) results.values);
//            notifyDataSetChanged();
//        }
//    };

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}