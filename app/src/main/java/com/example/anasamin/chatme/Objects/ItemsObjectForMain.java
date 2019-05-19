package com.example.anasamin.chatme.Objects;

public class ItemsObjectForMain {
    private String name;
    private lastMessageFirebaseObject lastMessage;
    private String imgLink;
    private String userId;
    private int type;
    public ItemsObjectForMain(){
    }
    public ItemsObjectForMain(String nameArg,lastMessageFirebaseObject messAgr,String imgLinkArg,String uid){
        name=nameArg;
        lastMessage=messAgr;
        imgLink=imgLinkArg;
        type=0;     //normal        1//randomFriends
        userId=uid;
    }

    public String getName() {
        return name;
    }

    public String getImgLink() {
        return imgLink;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public lastMessageFirebaseObject getLastMessage() {
        return lastMessage;
    }

    public String getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public void setLastMessage(lastMessageFirebaseObject lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
