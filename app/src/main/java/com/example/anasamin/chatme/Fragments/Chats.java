package com.example.anasamin.chatme.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anasamin.chatme.Activities.Main2Activity;
import com.example.anasamin.chatme.Activities.MessageActivity;
import com.example.anasamin.chatme.Activities.OtherProfileActivity;
import com.example.anasamin.chatme.Adapters.SearchAdapter;
import com.example.anasamin.chatme.Adapters.mainAdapter;
import com.example.anasamin.chatme.Authentication.userId;
import com.example.anasamin.chatme.Objects.ItemsObjectForMain;
import com.example.anasamin.chatme.Objects.lastMessageFirebaseObject;
import com.example.anasamin.chatme.R;
import com.example.anasamin.chatme.Services.FirebaseIntentService;
import com.example.anasamin.chatme.TestingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chats extends Fragment {

  ListView testing;
    mainAdapter adapter;
    public static  ArrayList<ItemsObjectForMain> list;
    FirebaseDatabase db;
    DatabaseReference ref;
    String uid;
    ProgressBar bar;

    public Chats() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chats, container, false);
        testing=view.findViewById(R.id.lview);
        bar=view.findViewById(R.id.progress_main);
        list=new ArrayList<>();
        adapter=new mainAdapter(getContext(),list,1);
        testing.setAdapter(adapter);

        uid=FirebaseAuth.getInstance().getUid();
        db=FirebaseDatabase.getInstance();
        ref=db.getReference("userProfile").child(uid);

        testing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemsObjectForMain cur=list.get(position);
                Intent intent=new Intent(getContext(), MessageActivity.class);
                intent.putExtra("myUid",uid);
                intent.putExtra("hisUid",cur.getUserId());
                startActivity(intent);
            }
        });

        populateList();

        return view;
    }

    private void populateList(){
        ValueEventListener listener;
        listener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             //   Log.e("this", "onDataChange: "+dataSnapshot.getKey() );
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    String frnds=child.getValue(String.class);
                    ItemsObjectForMain obj=new ItemsObjectForMain(null,null,null,frnds);
                    /// Log.e("this", "onDataChange: "+frnds );
                    list.add(obj);
                    getDetails(obj);
                    getLastMessage(obj);
                    if(dataSnapshot.getKey().equals("tempfriends")){
                        obj.setType(1);
                    }

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        ref.child("friends").addListenerForSingleValueEvent(listener);

        ref.child("tempfriends").addListenerForSingleValueEvent(listener);
        ref.child("tempfriends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getDetails(final ItemsObjectForMain obj){

        ChildEventListener listener=new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Log.e("this", "onChildAdded: "+dataSnapshot.getKey() );
                if(dataSnapshot.getKey().equals("name")){
                    obj.setName(dataSnapshot.getValue(String.class));
                }
                if(dataSnapshot.getKey().equals("profilePicUrl")){
                    obj.setImgLink(dataSnapshot.getValue(String.class));
                }
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
        FirebaseDatabase.getInstance().getReference("userProfile").child(obj.getUserId()).addChildEventListener(listener);
    }
    private void getLastMessage(final ItemsObjectForMain obj){
        FirebaseDatabase.getInstance().getReference("lastMessage").child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Log.e("testing", "onChildAdded: "+dataSnapshot.getKey() );

                if(dataSnapshot.getKey().equals(obj.getUserId())){
                    lastMessageFirebaseObject last=dataSnapshot.getValue(lastMessageFirebaseObject.class);
                    obj.setLastMessage(last);
                    adapter.notifyDataSetChanged();
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
        });
    }
}
