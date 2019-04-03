package com.example.anasamin.chatme.Objects;

import java.util.List;

public class postObject {
    private String uid;
    private long time;
    private String body;
    private String UrlImage;
    private int likes;
    private int shares;
    private boolean has_text;
    private boolean has_image;
    private List<messageObject> comments;
    public postObject(){}
    public postObject(String id,long tm,String text,String imgUrl){
        uid=id;
        time=tm;
        body=text;
        UrlImage=imgUrl;
        likes=0;
        shares=0;
        has_image=false;
        has_text=false;
    }

    public void setHas_image(boolean has_image) {
        this.has_image = has_image;
    }

    public void setHas_text(boolean has_text) {
        this.has_text = has_text;
    }

    public boolean isHas_image() {
        return has_image;
    }

    public boolean isHas_text() {
        return has_text;
    }

    public int getLikes() {
        return likes;
    }

    public int getShares() {
        return shares;
    }

    public List<messageObject> getComments() {
        return comments;
    }

    public long getTime() {
        return time;
    }

    public String getBody() {
        return body;
    }

    public String getUid() {
        return uid;
    }

    public String getUrlImage() {
        return UrlImage;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setComments(List<messageObject> comments) {
        this.comments = comments;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public void setUrlImage(String urlImage) {
        UrlImage = urlImage;
    }
}
