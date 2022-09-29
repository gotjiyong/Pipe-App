package com.jcy.chatapp2022.ui.notifications;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jcy.chatapp2022.ChatActivity;
import com.jcy.chatapp2022.MainActivity;
import com.jcy.chatapp2022.R;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ProfileFragment extends Fragment {

    int REQUEST_IMAGE_CODE = 1001;
    int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1002;

    private StorageReference mStorageRef;

    //프레임레이아웃으로 프로필과 카메라 모양을 겹치기위해 필요한 변수
    LinearLayout profileImg, profileCamera;

    //전화걸기 관련 변수
    EditText editTel;
    Button btnTel;

    //프로필사진 변수
    ImageView ivUser;

    //프로필 현제 로그인 사람 이름 변수
    TextView nowName;

    //로그인한 사람 이름 담을변수
    String stName;

    //이미지를 저장할때 로그인한 계정의 이메일을 담을 변수
    String stEmail;

    //파일경로를 물어보기위한 변수
    File localFile;

    //로그아웃에 필요한 변수
    Button btn_logout;

    //버튼을 가리키는 이미지 변수
    ImageView imgFirArrow, imgSecArrow, imgThrArrow,imgFouArrow,imgFivArrow,imgSixArrow;

    //중앙에 버튼 마다의 기능을 보여주는 프레임 레이아웃 변수
    LinearLayout proFir,proSec,proThr,proFou,proFiv,proSix;

    //중앙에 프레임 레이아웃을 묶어주고 있는 레이아웃 변수
    LinearLayout centerWorkPlace;

    //음식추천에 필요한 변수
    TextView txtSecLay;
    int ran;
    final android.os.Handler handler = new android.os.Handler();

    //에니메이션
    Animation repeat;


//    ArrayList<Position> positionArrayList;

//    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        //전화하기 아이디 선언
        editTel = root.findViewById(R.id.edit_tel);
        btnTel= root.findViewById(R.id.btn_tel);
        root.findViewById(R.id.btn_tel).setOnClickListener(mClick);

        //전화번호 표시로 변경해주기
        editTel.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        btnTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tel_number = "tel:"+editTel.getText().toString();
//                Intent intent = new Intent(((Activity)v.getContext()).getApplicationContext(),ChatActivity.class);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tel_number));
                ((Activity)v.getContext()).startActivity(intent);
                startActivity(intent);
                editTel.setText("");
            }//onClick
        }); // setOnClick

        //마지막 텍스트 깜빡이는 애니메이션 아이디 선언
        repeat = AnimationUtils.loadAnimation(getActivity(), R.anim.repeat_txt);

        //음식 추천 버튼
        txtSecLay = (TextView) root.findViewById(R.id.txt_sec_lay);
        txtSecLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhatEat();

            }//onClick
        }); // setOnClick


        //버튼을 가르키는 이미지 아이디
        imgFirArrow = (ImageView) root.findViewById(R.id.img_fir_arrow);
        imgSecArrow = (ImageView) root.findViewById(R.id.img_sec_arrow);
        imgThrArrow = (ImageView) root.findViewById(R.id.img_thr_arrow);
        imgFouArrow = (ImageView) root.findViewById(R.id.img_fou_arrow);
        imgFivArrow = (ImageView) root.findViewById(R.id.img_fiv_arrow);
        imgSixArrow = (ImageView) root.findViewById(R.id.img_six_arrow);

        //중앙에 버튼 마다의 기능을 보여주는 프레임 레이아웃 아이디
        proFir = (LinearLayout) root.findViewById(R.id.pro_fir_lay);
        proSec = (LinearLayout) root.findViewById(R.id.pro_sec_lay);
        proThr = (LinearLayout) root.findViewById(R.id.pro_thr_lay);
        proFou = (LinearLayout) root.findViewById(R.id.pro_fou_lay);
        proFiv = (LinearLayout) root.findViewById(R.id.pro_fiv_lay);
        proSix = (LinearLayout) root.findViewById(R.id.pro_six_lay);

        //중앙에 프레임 레이아웃을 묶어주고 있는 레이아웃 아이디
        centerWorkPlace = (LinearLayout) root.findViewById(R.id.center_work_place);

        //버튼 아이디
        root.findViewById(R.id.btnPhone).setOnClickListener(mClick);
        root.findViewById(R.id.btnFood).setOnClickListener(mClick);
        root.findViewById(R.id.btnNone1).setOnClickListener(mClick);
        root.findViewById(R.id.btnNone2).setOnClickListener(mClick);
        root.findViewById(R.id.btnNone3).setOnClickListener(mClick);
        root.findViewById(R.id.btnNone4).setOnClickListener(mClick);

        //프로필사진 프래임 레이아웃 아이디 가져오기
        profileImg = (LinearLayout) root.findViewById(R.id.profile_img);
        profileCamera = (LinearLayout) root.findViewById(R.id.profile_camera);

        profileImg.setVisibility(View.VISIBLE);
        profileCamera.setVisibility(View.VISIBLE);

        //로그인할때 사용한 이메일 값(계정)을 읽는 코드
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
        stEmail = sharedPref.getString("email","");
        stName = sharedPref.getString("name","");


        //현재 로그인 한 아이디의 이름 보여주는 텍스트 아이디
        nowName = (TextView) root.findViewById(R.id.txt_now_name);
        nowName.setText(stName);

        //파이어베이스에 이미지 저장
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //권한 설정
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)){

            } else  {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE_PERMISSION);
            }
        } else {

        }

        //프로필 이미지뷰 아이디 찾기
        ivUser = root.findViewById(R.id.ivUser);

        //프로필 이미지뷰 클릭 이벤트
        ivUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(in, REQUEST_IMAGE_CODE);
            }
        });

        //프로필 사진으로 업로드한 사진을 임시적으로 다운로드(불러오기 위한 코드)
        try {
            localFile = File.createTempFile("images", "jpg");
            StorageReference riversRef = mStorageRef.child("users").child(stEmail).child("profile.jpg");
            riversRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ivUser.setImageBitmap(bitmap);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }



        //로그아웃 버튼 아이디
        btn_logout = (Button) root.findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(((Activity)view.getContext()).getApplicationContext(), "로그아웃 되었습니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(((Activity)view.getContext()).getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ((Activity)view.getContext()).startActivity(intent);

            }
        });

        Visible();
        return root;

    }
    //이미지 선택
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode == REQUEST_IMAGE_CODE) {
            Uri image = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
                ivUser.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //선택된 이미지 업로드
//            Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
            StorageReference riversRef = mStorageRef.child("users").child(stEmail).child("profile.jpg");

            riversRef.putFile(image)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Uri downloadUrl = taskSnapshot.getDownUrl();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }
    View.OnClickListener mClick = new View.OnClickListener(){
        public void onClick(View ch) {
            if(ch.getId()== R.id.btnPhone){
                centerWorkPlace.setVisibility(View.VISIBLE);
                imgFirArrow.setVisibility(View.VISIBLE);
                proFir.setVisibility(View.VISIBLE);

                imgSecArrow.setVisibility(View.INVISIBLE);
                imgThrArrow.setVisibility(View.INVISIBLE);
                imgFouArrow.setVisibility(View.INVISIBLE);
                imgFivArrow.setVisibility(View.INVISIBLE);
                imgSixArrow.setVisibility(View.INVISIBLE);

                proSec.setVisibility(View.INVISIBLE);
                proThr.setVisibility(View.INVISIBLE);
                proFou.setVisibility(View.INVISIBLE);
                proFiv.setVisibility(View.INVISIBLE);
                proSix.setVisibility(View.INVISIBLE);

                return;

            }else if(ch.getId()== R.id.btnFood) {
                centerWorkPlace.setVisibility(View.VISIBLE);
                imgSecArrow.setVisibility(View.VISIBLE);
                proSec.setVisibility(View.VISIBLE);

                imgFirArrow.setVisibility(View.INVISIBLE);
                imgThrArrow.setVisibility(View.INVISIBLE);
                imgFouArrow.setVisibility(View.INVISIBLE);
                imgFivArrow.setVisibility(View.INVISIBLE);
                imgSixArrow.setVisibility(View.INVISIBLE);

                proFir.setVisibility(View.INVISIBLE);
                proThr.setVisibility(View.INVISIBLE);
                proFou.setVisibility(View.INVISIBLE);
                proFiv.setVisibility(View.INVISIBLE);
                proSix.setVisibility(View.INVISIBLE);


                return;

            }else if(ch.getId()== R.id.btnNone1) {
                centerWorkPlace.setVisibility(View.VISIBLE);
                imgThrArrow.setVisibility(View.VISIBLE);
                proThr.setVisibility(View.VISIBLE);

                imgFirArrow.setVisibility(View.INVISIBLE);
                imgSecArrow.setVisibility(View.INVISIBLE);
                imgFouArrow.setVisibility(View.INVISIBLE);
                imgFivArrow.setVisibility(View.INVISIBLE);
                imgSixArrow.setVisibility(View.INVISIBLE);

                proFir.setVisibility(View.INVISIBLE);
                proSec.setVisibility(View.INVISIBLE);
                proFou.setVisibility(View.INVISIBLE);
                proFiv.setVisibility(View.INVISIBLE);
                proSix.setVisibility(View.INVISIBLE);

                return;

            }else if(ch.getId()== R.id.btnNone2) {
                centerWorkPlace.setVisibility(View.VISIBLE);
                imgFouArrow.setVisibility(View.VISIBLE);
                proFou.setVisibility(View.VISIBLE);

                imgFirArrow.setVisibility(View.INVISIBLE);
                imgSecArrow.setVisibility(View.INVISIBLE);
                imgThrArrow.setVisibility(View.INVISIBLE);
                imgFivArrow.setVisibility(View.INVISIBLE);
                imgSixArrow.setVisibility(View.INVISIBLE);

                proFir.setVisibility(View.INVISIBLE);
                proSec.setVisibility(View.INVISIBLE);
                proThr.setVisibility(View.INVISIBLE);
                proFiv.setVisibility(View.INVISIBLE);
                proSix.setVisibility(View.INVISIBLE);

                return;

            }else if(ch.getId()== R.id.btnNone3) {
                centerWorkPlace.setVisibility(View.VISIBLE);
                imgFivArrow.setVisibility(View.VISIBLE);
                proFiv.setVisibility(View.VISIBLE);

                imgFirArrow.setVisibility(View.INVISIBLE);
                imgSecArrow.setVisibility(View.INVISIBLE);
                imgThrArrow.setVisibility(View.INVISIBLE);
                imgFouArrow.setVisibility(View.INVISIBLE);
                imgSixArrow.setVisibility(View.INVISIBLE);

                proFir.setVisibility(View.INVISIBLE);
                proSec.setVisibility(View.INVISIBLE);
                proThr.setVisibility(View.INVISIBLE);
                proFou.setVisibility(View.INVISIBLE);
                proSix.setVisibility(View.INVISIBLE);

                return;

            }else if(ch.getId()== R.id.btnNone4) {
                centerWorkPlace.setVisibility(View.VISIBLE);
                imgSixArrow.setVisibility(View.VISIBLE);
                proSix.setVisibility(View.VISIBLE);

                imgFirArrow.setVisibility(View.INVISIBLE);
                imgSecArrow.setVisibility(View.INVISIBLE);
                imgThrArrow.setVisibility(View.INVISIBLE);
                imgFouArrow.setVisibility(View.INVISIBLE);
                imgFivArrow.setVisibility(View.INVISIBLE);

                proFir.setVisibility(View.INVISIBLE);
                proSec.setVisibility(View.INVISIBLE);
                proThr.setVisibility(View.INVISIBLE);
                proFou.setVisibility(View.INVISIBLE);
                proFiv.setVisibility(View.INVISIBLE);

                return;

            }
        }
    };
    public void Visible(){
        //버튼누르기전까지 완전히 사라지게 하기위한 선언
        centerWorkPlace.setVisibility(View.GONE);

        //버튼 클릭전에는 안보여주기 위한 선언
        imgFirArrow.setVisibility(View.INVISIBLE);
        imgSecArrow.setVisibility(View.INVISIBLE);
        imgThrArrow.setVisibility(View.INVISIBLE);
        imgFouArrow.setVisibility(View.INVISIBLE);
        imgFivArrow.setVisibility(View.INVISIBLE);
        imgSixArrow.setVisibility(View.INVISIBLE);

        proFir.setVisibility(View.INVISIBLE);
        proSec.setVisibility(View.INVISIBLE);
        proThr.setVisibility(View.INVISIBLE);
        proFou.setVisibility(View.INVISIBLE);
        proFiv.setVisibility(View.INVISIBLE);
        proSix.setVisibility(View.INVISIBLE);
    }

    //음식 추천 관련 랜덤으로 보여주는 매서드
    void WhatEat(){
        ran = (int)(Math.random()*4);
        if(ran==0){
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("한");
                    txtSecLay.clearAnimation();
                }
            },100);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("한식");
                }
            },200);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("한식 어");
                }
            },300);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("한식 어때");
                }
            },400);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("한식 어때요");
                }
            },500);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("한식 어때요?");
                }
            },600);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("Retry?");
                    txtSecLay.startAnimation(repeat);
                }
            },1500);
        }
        else if(ran==1){
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("중");
                    txtSecLay.clearAnimation();
                }
            },100);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("중식");
                }
            },200);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("중식 어");
                }
            },300);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("중식 어때");
                }
            },400);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("중식 어때요");
                }
            },500);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("중식 어때요?");
                }
            },600);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("Retry?");
                    txtSecLay.startAnimation(repeat);
                }
            },1500);

        }
        else if(ran==2){
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("일");
                    txtSecLay.clearAnimation();
                }
            },100);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("일식");
                }
            },200);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("일식 어");
                }
            },300);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("일식 어때");
                }
            },400);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("일식 어때요");
                }
            },500);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("일식 어때요?");
                }
            },600);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("Retry?");
                    txtSecLay.startAnimation(repeat);
                }
            },1500);
        }
        else if(ran==3){
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("양");
                    txtSecLay.clearAnimation();
                }
            },100);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("양식");
                }
            },200);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("양식 어");
                }
            },300);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("양식 어때");
                }
            },400);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("양식 어때요");
                }
            },500);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("양식 어때요?");
                }
            },600);
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    txtSecLay.setText("Retry?");
                    txtSecLay.startAnimation(repeat);
                }
            },1500);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}