package com.example.anasamin.chatme.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.anasamin.chatme.Adapters.postAdapter;
import com.example.anasamin.chatme.GeneralUtilities.ComparatorForPosts;
import com.example.anasamin.chatme.GeneralUtilities.NewPostDialog;
import com.example.anasamin.chatme.Objects.postObject;
import com.example.anasamin.chatme.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class NewsFeed extends AppCompatActivity {
String uid;
ListView listview;
ImageView addBtn;
ArrayList<postObject> list;
postAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");

        listview=findViewById(R.id.newsFeed_postsLv);
        addBtn=findViewById(R.id.newsFeed_addPost);

        list=new ArrayList<>();
        Collections.sort(list,new ComparatorForPosts());
        Collections.reverse(list);
        adapter=new postAdapter(this,list);
        listview.setAdapter(adapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPostDialog dialog=new NewPostDialog();
                dialog.setUid(uid);
                dialog.show(getSupportFragmentManager().beginTransaction(),"newPost");
            }
        });
        populatePosts();
    }
    private void  populatePosts(){
        ChildEventListener listener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                postObject obj=dataSnapshot.getValue(postObject.class);
                adapter.add(obj);
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
        FirebaseDatabase.getInstance().getReference("GlobalPosts").addChildEventListener(listener);
        FirebaseDatabase.getInstance().getReference("LocalPosts").child(uid).addChildEventListener(listener);

    }
}
