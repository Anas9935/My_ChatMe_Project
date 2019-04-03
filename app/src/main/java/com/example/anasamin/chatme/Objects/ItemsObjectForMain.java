package com.example.anasamin.chatme.Objects;

public class ItemsObjectForMain {
    private String name;
    private String lastMessage;
    private String imgLink;
    private String userId;
    public ItemsObjectForMain(){
    }
    public ItemsObjectForMain(String nameArg,String messAgr,String imgLinkArg,String uid){
        name=nameArg;
        lastMessage=messAgr;
        imgLink=imgLinkArg;

        userId=uid;
    }

    public String getName() {
        return name;
    }

    public String getImgLink() {
        return imgLink;
    }

    public String getLastMessage() {
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

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
