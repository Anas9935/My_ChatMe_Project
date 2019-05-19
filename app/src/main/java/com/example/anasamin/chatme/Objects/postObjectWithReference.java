package com.example.anasamin.chatme.Objects;

import com.google.firebase.database.DatabaseReference;

public class postObjectWithReference {
    private postObject object;
    private DatabaseReference ref;
    public postObjectWithReference(postObject obj,DatabaseReference reference){
        object=obj;
        ref=reference;
    }

    public void setObject(postObject object) {
        this.object = object;
    }

    public void setRef(DatabaseReference ref) {
        this.ref = ref;
    }

    public DatabaseReference getRef() {
        return ref;
    }

    public postObject getObject() {
        return object;
    }
}
