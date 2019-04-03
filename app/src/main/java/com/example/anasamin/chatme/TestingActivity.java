package com.example.anasamin.chatme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.anasamin.chatme.Authentication.userId;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TestingActivity extends AppCompatActivity {
    TextView testingView1;

    String myuid=FirebaseAuth.getInstance().getUid();
    ArrayList<String> keys;

    FirebaseDatabase db;
    DatabaseReference ref,profilereference;
    ChildEventListener listener,nameAndUrllistener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasting_layout);
        testingView1=findViewById(R.id.testingMyuid);
        db=FirebaseDatabase.getInstance();
        ref=db.getReference("lastMessage").child(myuid);
        profilereference=db.getReference("userProfile");
        keys=new ArrayList<>();
        onattachEventListener();
    }
    public void onattachEventListener(){
        if(listener==null){
            listener=new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String key=dataSnapshot.getKey();
                    keys.add(key);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            ref.addChildEventListener(listener);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("tag","we're done");
                    for( int i=0;i<keys.size();i++){
                        String s=keys.get(i);
                        testingView1.append(s+"\n");

                    }
                    PullingProfiles();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public void PullingProfiles(){
        if(nameAndUrllistener==null){
            nameAndUrllistener=new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    for( int i=0;i<keys.size();i++){
                        String id=keys.get(i);
                        if(dataSnapshot.getKey().equals(id)){
                            userId user=dataSnapshot.getValue(userId.class);
                            testingView1.append(user.getName()+"\n");
                        }
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            profilereference.addChildEventListener(nameAndUrllistener);
        }
    }
}
