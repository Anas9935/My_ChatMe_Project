package com.example.anasamin.chatme.Objects;

public class userWithGender {
    private String  uid;
    private int gender;
    public userWithGender(){}
    public userWithGender(String id,int gen){
        uid=id;
        gender=gen;
    }

    public String getUid() {
        return uid;
    }

    public int getGender() {
        return gender;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
