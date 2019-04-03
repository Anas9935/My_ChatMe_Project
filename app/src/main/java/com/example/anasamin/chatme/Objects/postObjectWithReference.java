package com.example.anasamin.chatme.Objects;

import com.google.firebase.database.DatabaseReference;

public class postObjectWithReference {
    private postObject object;
    private DatabaseReference ref;
    public postObjectWithReference(postObject obj,DatabaseReference reference){
        object=obj;
        ref=reference;
    }
}
