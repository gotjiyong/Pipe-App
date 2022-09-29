package com.jcy.chatapp2022;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Intro extends AppCompatActivity {
    Animation ani1;
    ImageView ivIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        //상단에 있는 액션바 숨겨주는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ivIntro = (ImageView) findViewById(R.id.intro);
        ani1 = AnimationUtils.loadAnimation(Intro.this, R.anim.alpha);
        ivIntro.startAnimation(ani1);


//        이벤트 핸들러를 통해서 몇초후의 동작을 시킴...
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent intent = new Intent (Intro.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);


    }

}
