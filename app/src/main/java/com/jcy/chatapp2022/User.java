package com.jcy.chatapp2022;


import android.net.Uri;

public class User {

    //유저 목록에 뿌려줄 내용(가입된 유저들을 리사이클뷰로 보여주게하는 것)

    Uri image;

    String email;

    String key;

    String name;
    String department;
    String rank;
    String hp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}