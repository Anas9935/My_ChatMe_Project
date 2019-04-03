package com.example.anasamin.chatme.Objects;

import com.example.anasamin.chatme.Objects.messageObject;

public class messageListObjects {
    private messageObject messages;
    private String from;
    public messageListObjects(){

    }
    public messageListObjects(messageObject obj,String from){
        messages=obj;
        this.from=from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setMessages(messageObject messages) {
        this.messages = messages;
    }

    public String getFrom() {
        return from;
    }

    public messageObject getMessages() {
        return messages;
    }
}
