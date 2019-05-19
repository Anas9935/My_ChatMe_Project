package com.example.anasamin.chatme.Objects;

public class lastMessageFirebaseObject {
    String message;
    long time;
    public lastMessageFirebaseObject(){}
    public lastMessageFirebaseObject(String mess,long timestamp){
        message=mess;
        time=timestamp;
    }
    public long getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
