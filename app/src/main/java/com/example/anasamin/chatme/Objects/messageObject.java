package com.example.anasamin.chatme.Objects;

public class messageObject {
    private String message;
    private String toId;
    private long timestamp;
    public messageObject(){}
    public messageObject(String m,String t,long ts){
        message=m;
        toId=t;
        timestamp=ts;
    }

    public long getTimestamp() {
        return timestamp;
    }


    public String getMessage() {
        return message;
    }

    public String getToId() {
        return toId;
    }

    public String String () {
        return toId;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTo(String to) {
        this.toId = to;
    }
}
