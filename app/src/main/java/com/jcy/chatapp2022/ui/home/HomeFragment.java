package com.jcy.chatapp2022.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jcy.chatapp2022.R;
import com.jcy.chatapp2022.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    //사내 새소식 이미지 변수
    ImageView home_img_event;
    int banner = 0;
    //에니메니션
    Animation appear, disappear;

    //핸들러 변수
    final android.os.Handler handler = new android.os.Handler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

//        home_img_event = home_img_event.findViewById(R.id.home_img_event);
//        appear = AnimationUtils.loadAnimation(getActivity(), R.anim.translate);
//        disappear = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_out);
//
//        PeopleNowLay();

        //웹뷰를 띄워주는 코드
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        WebView webView = (WebView)v.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://cafe.naver.com/design4545");
        return v;

    }
//    void NewNews(){
//        handler.postDelayed(new Runnable(){
//            public void run() {
//
//                home_img_event.startAnimation(disappear);
//            }
//
//        },100);
//        handler.postDelayed(new Runnable(){
//            public void run() {
//                PeopleNowLay();
//            }
//        },1600);
//
//    }
//    void PeopleNowLay() {
//        if (banner == 0) {
//            home_img_event.setImageResource(R.drawable.homeimg2);
//            home_img_event.startAnimation(appear);
//            banner = 1;
//        } else if (banner == 1) {
//            home_img_event.setImageResource(R.drawable.homeimg3);
//            home_img_event.startAnimation(appear);
//            banner = 2;
//        } else if (banner == 2) {
//            home_img_event.setImageResource(R.drawable.homeimg1);
//            home_img_event.startAnimation(appear);
//            banner = 0;
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}