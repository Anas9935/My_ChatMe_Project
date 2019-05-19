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
import com.example.anasamin.chatme.Objects.NotifObjectForList;
import com.example.anasamin.chatme.Objects.NotificationObjects;
import com.example.anasamin.chatme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationActivity extends AppCompatActivity {
String uid;
ArrayList<NotifObjectForList> list;
ListView lv;
NotifAdapter adapter;
DatabaseReference notif_ref;
ChildEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        lv=findViewById(R.id.notif_listView);
        uid= FirebaseAuth.getInstance().getUid();
//        test.append(uid);
        notif_ref= FirebaseDatabase.getInstance().getReference("Notification").child(uid);
        list=new ArrayList<>();
        //list.add(new NotificationObjects(uid,12354648L,0));
        adapter=new NotifAdapter(NotificationActivity.this,list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotifObjectForList obj=list.get(position);
                obj.getObj().setViewed(1);
                obj.getRef().setValue(obj.getObj());
                String hisUid=obj.getObj().getMessage();
                switch(obj.getObj().getType()){
                    case 0:
                        AddFriendFragment dialog=new AddFriendFragment();
                        dialog.setUid(uid);
                        dialog.setHisUid(hisUid);
                        dialog.show(getSupportFragmentManager().beginTransaction(),"Dialog");
                        break;
                    case 1:
                        Intent intent=new Intent(NotificationActivity.this,OtherProfileActivity.class);
                        intent.putExtra("hisUid",obj.getObj().getMessage());
                        startActivity(intent);
                        break;
                }
                obj.getObj().setViewed(1);
                obj.getRef().setValue(obj.getObj());

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
                    NotifObjectForList listObj=new NotifObjectForList(obj,dataSnapshot.getRef());
                    list.add(listObj);
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
