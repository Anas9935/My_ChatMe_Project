package com.example.anasamin.chatme.Objects;

public class NotificationObjects {
    private String message;
    private Long time;
    private int type;
    private int viewed;
    public NotificationObjects(){}
    public NotificationObjects(String msg,Long tme,int tp){
        message=msg;
        time=tme;
        type=tp;
        viewed=0;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public int getViewed() {
        return viewed;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }

    public Long getTime() {
        return time;
    }
}
