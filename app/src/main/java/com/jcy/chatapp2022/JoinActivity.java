package com.jcy.chatapp2022;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class JoinActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //회원가입에 필요한 변수
    EditText etIdJoin, etPwJoin,etPwJoinCh,etName,etHp;

    //회원가입에 필요한 변수(스피너)
    TextView txt_join_department,txt_join_rank;

    //스피너에서 정한 값 담아둘 가방변수
    String stJoin="";
    String stRank="";

    private FirebaseAuth mAuth;

    //스피너 변수 설정
    Spinner spinner_join_department,spinner_join_rank;

    ProgressBar joinProgressBar;

    //리얼타임데이터베이스(계정을 만들기위한 변수)
    FirebaseDatabase database;

    //파이어베이스 스토어(회원가입시 회원정보 내용 저장을 위한 변수)
    FirebaseFirestore dbStore;

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;

    //스피너에 들어갈 아이템 항목들
    String[] spDepartItem = {"","총무","인사","기획","영업","회계"};
    String[] spRankItem = {"","인턴","사원","대리","과장","차장","부장"};



//    String stUserUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //상단에 있는 액션바 숨겨주는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();

//        //파이어베이스 스토어 초기화
//        dbStore = FirebaseFirestore.getInstance();

        //리얼타임데이터베이스 초기화
        database = FirebaseDatabase.getInstance();

        //회원가입에 필요한 정보 아이디 가져오기
        etIdJoin = (EditText) findViewById(R.id.etIdJoin);
        etPwJoin = (EditText) findViewById(R.id.etPwJoin);
        etPwJoinCh = (EditText) findViewById(R.id.etPwJoinCh);
        etName = (EditText) findViewById(R.id.etName);
        etHp = (EditText) findViewById(R.id.etHp);

        //로딩바
        joinProgressBar = (ProgressBar)findViewById(R.id.join_progressBar);

        //스피너 텍스트에 넣어줄 아이디 선언
        txt_join_department = findViewById(R.id.txt_join_department);
        txt_join_rank = findViewById(R.id.txt_join_rank);

        //스피너 아이디 가져오기
        spinner_join_department = findViewById(R.id.spinner_join_department);
        spinner_join_rank = findViewById(R.id.spinner_join_rank);


        // 부서 스피너 값 세팅 코드
        // 글자 하나만 표시되는 ArrayAdapter의 경우 기본 제공되는 Adapter를 사용할 수 있음
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item, // 스피너 레이아웃 지정
                spDepartItem
        ) ;
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_join_department.setAdapter(adapter);


        spinner_join_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                txt_join_department.setText(spDepartItem[position]);
                stJoin = txt_join_department.getText().toString();
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.argb(0,0,0,0));


//                Toast.makeText(JoinActivity.this, "텍스트값이 세팅됨", Toast.LENGTH_LONG).show();
                Log.d(TAG, "txt_join_department 텍스트값 : " + txt_join_department.getText());
                Log.d(TAG, "stJoin 텍스트값 : " + stJoin);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                txt_join_department.setText("");
            }
        });

        // 직급 스피너 값 세팅 코드
        // 글자 하나만 표시되는 ArrayAdapter의 경우 기본 제공되는 Adapter를 사용할 수 있음
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item, // 스피너 레이아웃 지정
                spRankItem
        ) ;
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_join_rank.setAdapter(adapter2);


        spinner_join_rank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                txt_join_rank.setText(spRankItem[position]);
                stRank = txt_join_rank.getText().toString();
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.argb(0,0,0,0));

//                Toast.makeText(JoinActivity.this, "텍스트값이 세팅됨", Toast.LENGTH_LONG).show();
                Log.d(TAG, "stRank 텍스트값 : " + stRank);
                Log.d(TAG, "txt_join_rank 텍스트값 : " + txt_join_rank.getText());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                txt_join_rank.setText("");
            }
        });

        //가입하기 버튼
        Button btn_register = (Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stEmail = etIdJoin.getText().toString();
                String stPassword = etPwJoin.getText().toString();
                String stPwCh = etPwJoinCh.getText().toString();
                String stName = etName.getText().toString();
                String stHp = etHp.getText().toString();
                joinProgressBar.setVisibility(View.VISIBLE);

                if (stEmail.isEmpty()) {
                    joinProgressBar.setVisibility(View.GONE);
                    Toast.makeText(JoinActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (stPassword.isEmpty()) {
                    joinProgressBar.setVisibility(View.GONE);
                    Toast.makeText(JoinActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (stPwCh.isEmpty()) {
                    joinProgressBar.setVisibility(View.GONE);
                    Toast.makeText(JoinActivity.this, "비밀번호 확인을 위해 한번 더 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!stPassword.equals(stPwCh)) {
                    joinProgressBar.setVisibility(View.GONE);
                    Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (stName.isEmpty()) {
                    joinProgressBar.setVisibility(View.GONE);
                    Toast.makeText(JoinActivity.this, "이름을 적어주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (stHp.isEmpty()) {
                    joinProgressBar.setVisibility(View.GONE);
                    Toast.makeText(JoinActivity.this, "전화번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
//                joinProgressBar.setVisibility(View.VISIBLE);

                //Authentication 계정 만들기 코드
                mAuth.createUserWithEmailAndPassword(stEmail, stPassword)
                        .addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    FirebaseUser chat = mAuth.getCurrentUser();

                                    //회원가입시 유저 목록에 만들어지는 방법(user용)
                                    DatabaseReference myRef = database.getReference("users").child(user.getUid());

                                    Hashtable<String, String> numbers
                                            = new Hashtable<String, String>();
                                    numbers.put("email", user.getEmail());
                                    numbers.put("key", user.getUid());
                                    numbers.put("name", stName);
                                    numbers.put("department", stJoin);
                                    numbers.put("rank", stRank);
                                    numbers.put("hp", stHp);
                                    myRef.setValue(numbers);

                                    //회원가입시 유저 목록에 만들어지는 방법(chat용)
                                    DatabaseReference chRef = database.getReference("join").child(user.getUid());
                                    numbers.put("email", chat.getEmail());
                                    numbers.put("key", chat.getUid());
                                    numbers.put("name", stName);
                                    numbers.put("department", stJoin);
                                    numbers.put("rank", stRank);
                                    numbers.put("hp", stHp);
                                    chRef.setValue(numbers);

                                    Log.d(TAG, "numbers join : " +numbers);

                                    joinProgressBar.setVisibility(View.GONE);

                                    Toast.makeText(JoinActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                                    finish();
                                    Intent in = new Intent(JoinActivity.this, MainActivity.class);
                                    startActivity(in);

                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(JoinActivity.this, "회원가입 실패",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });


            }

        });

        Button btn_join_exit = (Button)findViewById(R.id.btn_join_exit);
        btn_join_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });


    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
}