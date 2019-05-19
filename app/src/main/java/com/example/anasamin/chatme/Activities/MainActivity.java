package com.example.anasamin.chatme.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.anasamin.chatme.Adapters.mainAdapter;
import com.example.anasamin.chatme.Authentication.AuthenticationActivity;
import com.example.anasamin.chatme.Objects.ItemsObjectForMain;
import com.example.anasamin.chatme.Objects.lastMessageFirebaseObject;
import com.example.anasamin.chatme.Objects.userWithGender;
import com.example.anasamin.chatme.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String uid;
    ListView testing;
    mainAdapter adapter;
    public static ArrayList<ItemsObjectForMain> list;
    FirebaseDatabase db;
    DatabaseReference ref;
    ProgressBar bar;

    Button findfrnd;
    Toolbar toolbar;
    int gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testing=findViewById(R.id.lview);
        bar=findViewById(R.id.progress_main);
        list=new ArrayList<>();
        adapter=new mainAdapter(this,list,1);
        testing.setAdapter(adapter);
        findfrnd=findViewById(R.id.main_testing_btn);
        toolbar=findViewById(R.id.Maintoolbar);

        toolbar.setTitle("Chats");

        uid=FirebaseAuth.getInstance().getUid();

        db=FirebaseDatabase.getInstance();
        getGender();
        testing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemsObjectForMain cur=list.get(position);
                Intent intent=new Intent(MainActivity.this, MessageActivity.class);
                intent.putExtra("myUid",uid);
                intent.putExtra("hisUid",cur.getUserId());
                startActivity(intent);
            }
        });
        findfrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,FindFrnds.class);
                startActivity(intent);
            }
        });

//        populateList();
    }
    public void getGender(){
        ChildEventListener ls=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                userWithGender ug=dataSnapshot.getValue(userWithGender.class);
                if(ug.getUid().equals(uid)){
                    gender=ug.getGender();
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
        db.getReference("userGender").child(uid).addChildEventListener(ls);
        db.getReference("userGender").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ref=db.getReference("userProfile").child(String.valueOf(gender)).child(uid);
                populateList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        FirebaseDatabase.getInstance().getReference("userProfile").child(String.valueOf(gender)).child(obj.getUserId()).addChildEventListener(listener);
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
    private void logout(){

        AuthUI.getInstance()
                .signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent=new Intent(MainActivity.this, AuthenticationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:{
                logout();
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
