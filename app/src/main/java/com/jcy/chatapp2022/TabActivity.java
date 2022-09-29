package com.jcy.chatapp2022;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jcy.chatapp2022.databinding.ActivityTabBinding;

public class TabActivity extends AppCompatActivity {

    private ActivityTabBinding binding;

    //핸드폰 뒤로가기 버튼 제어 변수
    private final long finishTimeed = 1000;
    private long pressTime = 0;

    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //상단에 있는 액션바 숨겨주는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
        String stEmailUid= sharedPref.getString("key","");

        binding = ActivityTabBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_tab);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //데이터베이스 초기화
        database = FirebaseDatabase.getInstance();

        ref = database.getReference();
        ref.child("users").child(stEmailUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //로그인한 나의 정보를 SharedPreferences 에디터에 저장해서 넘겨주는 코드
                if(snapshot.getValue(User.class) != null){
                    User user = snapshot.getValue(User.class);
                    Log.w("FireBaseData", "getData" + user.toString());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("department", user.getDepartment());
                    editor.putString("hp", user.getHp());
                    editor.putString("name", user.getName());
                    editor.putString("rank", user.getRank());
                    editor.apply();

                    Log.d("KJW 값확인1 : ", sharedPref.getString("department",""));
                    Log.d("KJW 값확인2 : ", sharedPref.getString("hp",""));
                    Log.d("KJW 값확인3 : ", sharedPref.getString("name",""));
                    Log.d("KJW 값확인4 : ", sharedPref.getString("rank",""));
                } else {
                    Toast.makeText(TabActivity.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    //핸드폰 뒤로가기 버튼을 누르면 실행되는 메서드
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - pressTime;

        if(0 <= intervalTime && finishTimeed >= intervalTime){
            FirebaseAuth.getInstance().signOut();
            finish();
        } else {
            pressTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 누르시면 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
        }
    }

}