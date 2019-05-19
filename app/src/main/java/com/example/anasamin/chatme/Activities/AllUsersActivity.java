package com.example.anasamin.chatme.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.anasamin.chatme.Adapters.mainAdapter;
import com.example.anasamin.chatme.Authentication.userId;
import com.example.anasamin.chatme.Objects.ItemsObjectForMain;
import com.example.anasamin.chatme.Objects.lastMessageFirebaseObject;
import com.example.anasamin.chatme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllUsersActivity extends AppCompatActivity {
ListView lv;
ProgressBar bar;

ArrayList<ItemsObjectForMain> list;
ArrayAdapter adapter;
FirebaseDatabase db;
DatabaseReference refprofiles;
ChildEventListener listener;
ValueEventListener valueListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        lv=findViewById(R.id.allUsersListView);
        bar=findViewById(R.id.allUsersProgressBar);

        db=FirebaseDatabase.getInstance();
        refprofiles=db.getReference("userProfile");
        list=new ArrayList<>();
        adapter=new mainAdapter(this,list,0);
        lv.setAdapter(adapter);

        populateView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemsObjectForMain obj=list.get(position);
                Intent intent=new Intent(AllUsersActivity.this,OtherProfileActivity.class);
                intent.putExtra("hisUid",obj.getUserId());
                startActivity(intent);
            }
        });
    }
    public void populateView(){
        if(listener==null){
            listener=new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())){
                        userId user=dataSnapshot.getValue(userId.class);
                        lastMessageFirebaseObject object=new lastMessageFirebaseObject(user.getBioLine(),0);
                        ItemsObjectForMain obj=new ItemsObjectForMain(user.getName(),object,user.getProfilePicUrl(),dataSnapshot.getKey());
                        adapter.add(obj);
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
            refprofiles.child("1").addChildEventListener(listener);
            refprofiles.child("2").addChildEventListener(listener);
        }
        if(valueListener==null){
            valueListener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            refprofiles.addListenerForSingleValueEvent(valueListener);
        }
    }
}
