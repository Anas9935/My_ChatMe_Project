package com.example.anasamin.chatme.Activities;

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
import com.example.anasamin.chatme.Objects.postObjectWithReference;
import com.example.anasamin.chatme.Objects.userWithGender;
import com.example.anasamin.chatme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class NewsFeed extends AppCompatActivity {

    String uid;
    ListView listview;
    ImageView addBtn;
    ArrayList<postObjectWithReference> list;
    postAdapter adapter;
    public static  int myGender,hisGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);


        uid= FirebaseAuth.getInstance().getUid();
        listview=findViewById(R.id.newsFeed_postsLv);
        addBtn=findViewById(R.id.newsFeed_addPost);

        list=new ArrayList<>();
        Collections.sort(list,new ComparatorForPosts());
        Collections.reverse(list);
        adapter=new postAdapter(NewsFeed.this,list);
        listview.setAdapter(adapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPostDialog dialog=new NewPostDialog();
                dialog.setUid(uid);
                dialog.show(getSupportFragmentManager().beginTransaction(),"newPost");
            }
        });
        getGender();
        populatePosts();
    }
public void getGender(){
    ValueEventListener lis=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            userWithGender use=dataSnapshot.getValue(userWithGender.class);
            if (use.getUid().equals(uid)){
                myGender=use.getGender();
            }//else if(use.getUid().equals(toId)){
               // hisGender=use.getGender();
                //onAttachEventListener();
           // }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    FirebaseDatabase.getInstance().getReference("userGender").child(uid).addListenerForSingleValueEvent(lis);
  //  FirebaseDatabase.getInstance().getReference("userGender").child(toId).addListenerForSingleValueEvent(lis);
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
