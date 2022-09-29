package com.jcy.chatapp2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText etId, etPassword;
    private FirebaseAuth mAuth;

    //핸드폰 뒤로가기 버튼 제어 변수
    private final long finishTimeed = 1000;
    private long pressTime = 0;

    ProgressBar progressBar;

    FirebaseDatabase database;

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;

    String stUserUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_layout);

        //상단에 있는 액션바 숨겨주는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();

        //데이터베이스 초기화
        database = FirebaseDatabase.getInstance();

//        etId = (EditText) findViewById(R.id.etId);
//        etPassword = (EditText) findViewById(R.id.etPassword);
//
//        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        Button btn_main_login = (Button)findViewById(R.id.btn_main_login);

        btn_main_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(in);

            }
        });

        Button btn_main_join = (Button)findViewById(R.id.btn_main_join);
        btn_main_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(in);
            }

        });

        Button btn_main_exit = (Button)findViewById(R.id.btn_main_exit);
        btn_main_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }

        });

    }
    //핸드폰 뒤로가기 버튼을 누르면 실행되는 메서드
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - pressTime;

        if(0 <= intervalTime && finishTimeed >= intervalTime){
            finish();
        } else {
            pressTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 누르시면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();
        }
    }

}