package com.jcy.chatapp2022;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private RecyclerView recyclerView;
    ChatAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    EditText etText;
    Button btnSend;

    String stEmail;
    String stChatId;

    String stName;
    String stDep;
    String stRank;
    SharedPreferences sharedPref;
    String myEmail;
    String myEmailUid;

    String stPartnerChatId;

    FirebaseDatabase database;

    ArrayList<Chat> chatArrayList;
//    ArrayList<User> userArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //상단에 있는 액션바 숨겨주는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);

        stPartnerChatId= getIntent().getStringExtra("key");

        stChatId= sharedPref.getString("key","");
        stEmail = sharedPref.getString("email","");
        stName = sharedPref.getString("name","");
        stDep = sharedPref.getString("department","");
        stRank = sharedPref.getString("rank","");

        //데이터베이스 초기화
        database = FirebaseDatabase.getInstance();

        chatArrayList = new ArrayList<>();

//        userArrayList = new ArrayList<>();


        Button btnFinish= (Button)findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        etText = (EditText)findViewById(R.id.etText);


        //리사이클 뷰로 채팅창을 보여주는 코드
        recyclerView =(RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        String[] myDataset = {"test1", "test2", "test3", "test4"};

        //채팅에 필요한 어뎁터
        mAdapter = new ChatAdapter(chatArrayList, stEmail,stName,stDep,stRank);

        recyclerView.setAdapter(mAdapter);




        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Chat chat = dataSnapshot.getValue(Chat.class);
                chatArrayList.add(chat);
                mAdapter.notifyDataSetChanged();
                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.


                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.


                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(ChatActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        DatabaseReference ref = database.getReference("users").child(stChatId).child("from").child(stPartnerChatId).child("chat");
        ref.addChildEventListener(childEventListener);


        btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stText = etText.getText().toString();

//                Toast.makeText(ChatActivity.this,"MSG : "+stText, Toast.LENGTH_LONG).show();
                // Write a message to the database

                Calendar c = Calendar.getInstance();
                SimpleDateFormat dataformat = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss");
                String datatime = dataformat.format(c.getTime());

//                DatabaseReference myRef = database.getReference("users").child(stChatId).child("chat").child(datatime);
                DatabaseReference myRef = database.getReference("users").child(stChatId).child("from").child(stPartnerChatId).child("chat").child(datatime);
                DatabaseReference partnerRef = database.getReference("users").child(stPartnerChatId).child("from").child(stChatId).child("chat").child(datatime);

                Hashtable<String, String> numbers
                        = new Hashtable<String, String>();
                numbers.put("email", stEmail);
                numbers.put("text", stText);
                numbers.put("key", stChatId);
                numbers.put("name", stName);
                numbers.put("department", stDep);
                numbers.put("rank", stRank);
                myRef.setValue(numbers);
                partnerRef.setValue(numbers);
                etText.setText("");
            }
        });

    }
}