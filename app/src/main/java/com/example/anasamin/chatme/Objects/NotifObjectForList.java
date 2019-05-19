package com.example.anasamin.chatme.Objects;

import com.google.firebase.database.DatabaseReference;

public class NotifObjectForList {
    private NotificationObjects obj;
    private DatabaseReference ref;
    public NotifObjectForList(NotificationObjects objects,DatabaseReference reference){
        obj=objects;
        ref=reference;
    }

    public DatabaseReference getRef() {
        return ref;
    }

    public NotificationObjects getObj() {
        return obj;
    }

    public void setRef(DatabaseReference ref) {
        this.ref = ref;
    }

    public void setObj(NotificationObjects obj) {
        this.obj = obj;
    }
}
