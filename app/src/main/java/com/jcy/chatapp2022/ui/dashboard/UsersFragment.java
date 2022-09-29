package com.jcy.chatapp2022.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.jcy.chatapp2022.Chat;
import com.jcy.chatapp2022.ChatAdapter;
import com.jcy.chatapp2022.R;
import com.jcy.chatapp2022.User;
import com.jcy.chatapp2022.UserAdapter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class UsersFragment extends Fragment {
    private static final String TAG = "UsersFragment";
    FirebaseDatabase database;

    private FirebaseStorage mFirebaseStr;

    private DashboardViewModel dashboardViewModel;

    private RecyclerView recyclerView;
    UserAdapter mAdapter;
    ChatAdapter cAdapter;
    private RecyclerView.LayoutManager layoutManager;



    //유저값
    ArrayList<User> userArrayList;


    //채팅값
//    ArrayList<Chat> chatArrayList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        //데이터베이스 초기화
        database = FirebaseDatabase.getInstance();

        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        View root = inflater.inflate(R.layout.fragment_users, container, false);

        //로그인할때 사용한 이메일 값(계정)을 읽는 코드
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
        String stEmail = sharedPref.getString("email","");
        String stEmailUid= sharedPref.getString("key","");


        //유저어레이 리스트 선언
        userArrayList = new ArrayList<>();
        
        //리사이클 뷰로 유저 목록을 보여주는 코드
        recyclerView =(RecyclerView) root.findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //유저목록에 필요한 어뎁터
        mAdapter = new UserAdapter(userArrayList, stEmail, stEmailUid);
        recyclerView.setAdapter(mAdapter);

        //"users" 데이터베이스에 있는 값 불러오기
        DatabaseReference ref = database.getReference("users");
        //유저 값을 가져오기 위한 코드
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    Log.d(TAG, "dataSnapshot1"+dataSnapshot1.getValue().toString());
                    User user = dataSnapshot1.getValue(User.class);
                    userArrayList.add(user);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return root;
    }
}