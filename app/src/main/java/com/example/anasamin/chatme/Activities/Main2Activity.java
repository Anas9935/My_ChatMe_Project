package com.example.anasamin.chatme.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anasamin.chatme.Adapters.mainAdapter;
import com.example.anasamin.chatme.Authentication.AuthenticationActivity;
import com.example.anasamin.chatme.Authentication.userId;
import com.example.anasamin.chatme.Objects.ItemsObjectForMain;
import com.example.anasamin.chatme.R;
import com.example.anasamin.chatme.Services.FirebaseIntentService;
import com.example.anasamin.chatme.TestingActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    ListView lv;
    TextView test;
    public static ArrayList<ItemsObjectForMain> arrayList;
    mainAdapter adapter;
    ProgressBar prog;
    public static String uid;             //My uid of firebase

    FirebaseDatabase mdatabase;
    DatabaseReference friends,FriendsProfile;
    ChildEventListener frndLastMessagesListener,FrndProfileListener;
    ValueEventListener whenComlpleteListener;
    Button not;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        lv=(ListView)findViewById(R.id.lview);
        prog=(ProgressBar)findViewById(R.id.progress_main);
        test=findViewById(R.id.testMain);

        Intent in=getIntent();
        uid=in.getStringExtra("uid");
        mdatabase=FirebaseDatabase.getInstance();
        friends=mdatabase.getReference("lastMessage").child(uid);
        FriendsProfile=mdatabase.getReference("userProfile");
        not=findViewById(R.id.notification);

        arrayList =new ArrayList<>();
        adapter=new mainAdapter(this,arrayList);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(Main2Activity.this, MessageActivity.class);
                intent.putExtra("myUid",uid);
                intent.putExtra("hisUid",arrayList.get(position).getUserId());
                startActivity(intent);
            }
        });
        populateFriends();
    }

    private void populateFriends(){
        if(frndLastMessagesListener==null){
            frndLastMessagesListener=new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String userId=dataSnapshot.getKey();
                    String lastmessages=dataSnapshot.getValue(String.class);
                    ItemsObjectForMain obj=new ItemsObjectForMain(null,lastmessages,null,userId);
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
            friends.addChildEventListener(frndLastMessagesListener);
        }
        if(whenComlpleteListener==null){
            whenComlpleteListener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    fetchProfiles();
                    prog.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            friends.addListenerForSingleValueEvent(whenComlpleteListener);
        }

    }
    private void fetchProfiles(){
        if(FrndProfileListener==null){
            FrndProfileListener=new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    for( int i=0;i<arrayList.size();i++){
                        ItemsObjectForMain userId=arrayList.get(i);
                        if(userId.getUserId().equals(dataSnapshot.getKey())){
                            userId id=dataSnapshot.getValue(userId.class);
                            userId.setName(id.getName());
                            userId.setImgLink(id.getProfilePicUrl());
                        }
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
            };
            FriendsProfile.addChildEventListener(FrndProfileListener);
        }

    }
    private void logout(){
        prog.setVisibility(
                View.VISIBLE
        );
        AuthUI.getInstance()
                .signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent=new Intent(Main2Activity.this, AuthenticationActivity.class);
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
        switch(item.getItemId()){
            case R.id.action_logout:{
               logout();
                finish();
                return true;
            }
            case R.id.action_profile:{
                Intent intent=new Intent(Main2Activity.this, ProfileActivity.class);
                intent.putExtra("myId",uid);
                startActivity(intent);
                break;
            }
            case R.id.action_showOthers:{
                Intent intent=new Intent(Main2Activity.this, AllUsersActivity.class);
                startActivity(intent);
                break;
                }
            case R.id.action_Notification:{
                Intent intent=new Intent(Main2Activity.this,NotificationActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
                break;
            }
            case R.id.action_newsFeed:{
                Intent intent=new Intent(Main2Activity.this,NewsFeed.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
    public void notifyMe(View view){
        Intent intent=new Intent(Main2Activity.this, FirebaseIntentService.class);
      //  intent.setAction(ReminderTasks.ACTION_CREATE_NOTIF);
        startService(intent);

    }
    public void cancel(View view){
        Intent intent=new Intent(Main2Activity.this,FirebaseIntentService.class);
        stopService(intent);
    }


}
