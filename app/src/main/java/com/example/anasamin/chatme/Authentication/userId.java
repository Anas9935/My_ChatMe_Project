package com.example.anasamin.chatme.Authentication;

import android.os.Parcelable;

import java.io.Serializable;

public class userId implements Serializable {
    private String name;
    private String bioLine;
    private String Country;
    private Long birthday;
    private int likes;
    private String email;
    private Long tel;
    private int gender;
    private String ProfilePicUrl;
    public userId(){
    }
    public userId(String name,String bio,String lives,Long DOB,int likes,String email,Long tel,int gender,String ProfUrl){
        this.name=name;
        this.email=email;
        this.gender=gender;
        this.tel=tel;
        this.ProfilePicUrl=ProfUrl;
        this.bioLine=bio;
        this.Country=lives;
        this.birthday=DOB;
        this.likes=likes;
    }

    public int getLikes() {
        return likes;
    }

    public Long getBirthday() {
        return birthday;
    }

    public String getBioLine() {
        return bioLine;
    }

    public String getCountry() {
        return Country;
    }

    public void setBioLine(String bioLine) {
        this.bioLine = bioLine;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getName() {
        return name;
    }
    public int getGender() {
        return gender;
    }
    public Long getTel() {
        return tel;
    }
    public String getEmail() {
        return email;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setProfilePicUrl(String profilePicUrl) {
        ProfilePicUrl = profilePicUrl;
    }
    public String getProfilePicUrl() {
        return ProfilePicUrl;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public void setTel(Long tel) {
        this.tel = tel;
    }
}
