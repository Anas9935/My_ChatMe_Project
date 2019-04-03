package com.example.anasamin.chatme.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.anasamin.chatme.Activities.Main2Activity;
import com.example.anasamin.chatme.GeneralUtilities.ReminderTasks;
import com.example.anasamin.chatme.Objects.messageObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseIntentService extends Service {
    public FirebaseDatabase db;
    public DatabaseReference ref;
    public FirebaseAuth mAuth;
    public ChildEventListener vListener;
    public String uid;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            getMessages();
        return START_STICKY;
    }

    private void getMessages(){
        Log.e("Firebase","You are in the service");
        db=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null)
            uid=mAuth.getCurrentUser().getUid();
        ref=db.getReference("messages");
        if(vListener==null){
            vListener=new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.e("FirebaseService1","Intent Started");
                    Log.e("Firebase",dataSnapshot.getKey());

                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s){
                    Intent intent=new Intent(FirebaseIntentService.this,MessageIntentService.class);
                    intent.setAction(ReminderTasks.ACTION_CREATE_NOTIF);
                    startService(intent);
                    Log.e("FirebaseService2","Intent Started");
                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("FirebaseService3","Intent Started");
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.e("FirebaseService4","Intent Started");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FirebaseService5","Intent Started");
                    Log.e("Firebase",databaseError.getMessage());
                }
            };
        }
        ref.addChildEventListener(vListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Firebase","Service stopped");
    }
}
