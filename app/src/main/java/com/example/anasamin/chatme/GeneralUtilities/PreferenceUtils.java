package com.example.anasamin.chatme.GeneralUtilities;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.anasamin.chatme.Activities.Main2Activity;
import com.example.anasamin.chatme.Objects.messageObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public final class PreferenceUtils {
    FirebaseDatabase db;
    DatabaseReference ref;
    String uid= Main2Activity.uid;
    String newmess="";
    private ValueEventListener vListener;
public static String getNewMessage(Context context){

    return null;
}
private String firebaseDb(){
    db=FirebaseDatabase.getInstance();
    ref=db.getReference("messages").child(uid);
    if(vListener==null){
        vListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot frnds:dataSnapshot.getChildren()){
                    messageObject unread=frnds.getValue(messageObject.class);
                   newmess=unread.getMessage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    }
    ref.addValueEventListener(vListener);
    return null;
}
}
