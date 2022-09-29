package com.jcy.chatapp2022;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jcy.chatapp2022.ui.dashboard.UsersFragment;

import java.util.Hashtable;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText etIdLogin, etPasswordLogin;
    private FirebaseAuth mAuth;

    ProgressBar loginProgressBar;

    FirebaseDatabase database;

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;

    String stUserEmail;
    String stUserUid;
    String stUserDbName;
    String stUserDep;
    String stUserRank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //상단에 있는 액션바 숨겨주는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();

        //데이터베이스 초기화
        database = FirebaseDatabase.getInstance();

        etIdLogin = (EditText) findViewById(R.id.etIdLogin);
        etPasswordLogin = (EditText) findViewById(R.id.etPasswordLogin);

        //로딩바 아이디
        loginProgressBar = (ProgressBar)findViewById(R.id.login_progressBar);

        //로그인 버튼
        Button btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stEmail = etIdLogin.getText().toString();
                String stPassword = etPasswordLogin.getText().toString();
                if (stEmail.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (stPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                loginProgressBar.setVisibility(View.VISIBLE);

                //이메일로 로그인(버튼을 눌렀을 때)
                mAuth.signInWithEmailAndPassword(stEmail, stPassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                loginProgressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    stUserEmail = user.getEmail();
                                    stUserUid = user.getUid();

                                    String stUserName = user.getDisplayName();

                                    Log.d(TAG, "stUserUid: "+stUserUid);

                                    Log.d(TAG, "stUserEmail: "+stUserEmail+", stUserName: "+stUserName);

                                    //로그인 할때 이메일을 저장하여 각 이메일의 유저 프로필을 저장하는 코드
                                    SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("email", stUserEmail);
                                    editor.putString("key", stUserUid);
                                    editor.apply();

                                    Log.d(TAG, "before intent stUserUid: "+stUserUid);
                                    //Toast.makeText(MainActivity.this,"Login",Toast.LENGTH_LONG).show();
                                    Intent in = new Intent(LoginActivity.this, TabActivity.class);
                                    in.putExtra("email", stEmail);
                                    in.putExtra("key", stUserUid);

//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("key", stUserUid);
//                                    UsersFragment.setArguments(bundle);
                                    Log.d(TAG, "after intent stUserUid: "+stUserUid);
                                    startActivity(in);

                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "아이디 비밀번호를 확인해주세요.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });

                etIdLogin.setText("");
                etPasswordLogin.setText("");

            }
        });
        Button btn_login_back = (Button)findViewById(R.id.btn_login_back);
        btn_login_back.setOnClickListener(new View.OnClickListener() {
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