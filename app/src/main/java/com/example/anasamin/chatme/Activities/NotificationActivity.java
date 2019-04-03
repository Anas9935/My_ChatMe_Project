package com.example.anasamin.chatme.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.anasamin.chatme.Adapters.NotifAdapter;
import com.example.anasamin.chatme.GeneralUtilities.AddFriendFragment;
import com.example.anasamin.chatme.Objects.NotificationObjects;
import com.example.anasamin.chatme.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
String uid;
ArrayList<NotificationObjects> list;
ListView lv;
NotifAdapter adapter;
DatabaseReference notif_ref;
ChildEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        lv=findViewById(R.id.notif_listView);
        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");
//        test.append(uid);
        notif_ref= FirebaseDatabase.getInstance().getReference("Notification").child(uid);
        list=new ArrayList<>();
        //list.add(new NotificationObjects(uid,12354648L,0));
        adapter=new NotifAdapter(NotificationActivity.this,list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotificationObjects obj=list.get(position);
                obj.setViewed(1);
                String hisUid=obj.getMessage();
                switch(obj.getType()){
                    case 0:
                        AddFriendFragment dialog=new AddFriendFragment();
                        dialog.setUid(uid);
                        dialog.setHisUid(hisUid);
                        dialog.show(getSupportFragmentManager().beginTransaction(),"Dialog");
                        break;
                }
            }
        });
        setListener();
    }
    private void setListener(){
        if(listener==null){
            listener=new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    NotificationObjects obj=dataSnapshot.getValue(NotificationObjects.class);
                    list.add(obj);
                    adapter.notifyDataSetChanged();
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
            notif_ref.addChildEventListener(listener);
        }
    }
}
