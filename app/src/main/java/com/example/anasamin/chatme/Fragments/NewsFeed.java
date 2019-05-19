package com.example.anasamin.chatme.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.anasamin.chatme.Activities.Main2Activity;
import com.example.anasamin.chatme.Adapters.postAdapter;
import com.example.anasamin.chatme.GeneralUtilities.ComparatorForPosts;
import com.example.anasamin.chatme.GeneralUtilities.NewPostDialog;
import com.example.anasamin.chatme.Objects.postObject;
import com.example.anasamin.chatme.Objects.postObjectWithReference;
import com.example.anasamin.chatme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeed extends Fragment {
    String uid;
    ListView listview;
    ImageView addBtn;
    ArrayList<postObjectWithReference> list;
    postAdapter adapter;

    public NewsFeed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_news_feed, container, false);

            uid= FirebaseAuth.getInstance().getUid();
        listview=view.findViewById(R.id.newsFeed_postsLv);
        addBtn=view.findViewById(R.id.newsFeed_addPost);

        list=new ArrayList<>();
        Collections.sort(list,new ComparatorForPosts());
        Collections.reverse(list);
        adapter=new postAdapter(getContext(),list);
        listview.setAdapter(adapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPostDialog dialog=new NewPostDialog();
                dialog.setUid(uid);
                dialog.show(getFragmentManager().beginTransaction(),"newPost");
            }
        });
        populatePosts();

        return view;
    }
    private void  populatePosts(){
        ChildEventListener listener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                postObject obj=dataSnapshot.getValue(postObject.class);
                postObjectWithReference obj2=new postObjectWithReference(obj,dataSnapshot.getRef());
                adapter.add(obj2);
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
