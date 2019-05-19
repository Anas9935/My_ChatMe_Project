package com.example.anasamin.chatme.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anasamin.chatme.Adapters.CustomFragmentAdapter;
import com.example.anasamin.chatme.Authentication.AuthenticationActivity;
import com.example.anasamin.chatme.Authentication.userId;
import com.example.anasamin.chatme.R;
import com.example.anasamin.chatme.TestingActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.example.anasamin.chatme.Fragments.Chats.list;

public class Main2Activity extends AppCompatActivity {

   View view;
   Button test;
    List<String> myTempFriends;
    String[] id_random=new String[1];

    Toolbar toolbar;
    ViewPager mViewPager;
    TabLayout mtabLayout;
    TabItem item1,item2;
    String uid;
    TextView tokens,tokenAdd;
    userId id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        uid= FirebaseAuth.getInstance().getUid();
        tokens=findViewById(R.id.main_token);
        tokenAdd=findViewById(R.id.main_token_add);



        test=findViewById(R.id.main_testing_btn);

        toolbar=findViewById(R.id.main_toolbar);
        mViewPager=findViewById(R.id.main_viewPager);
        mtabLayout=findViewById(R.id.main_tablayout);
        item1=findViewById(R.id.main_item_chats);
        item2=findViewById(R.id.main_item_news);
        toolbar.setTitle("ChatMe");
        setSupportActionBar(toolbar);//ActionBar(toolbar);
        CustomFragmentAdapter fragadapter=new CustomFragmentAdapter(getSupportFragmentManager(),mtabLayout.getTabCount());
        mViewPager.setAdapter(fragadapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mtabLayout));



    tokenAdd.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        if(id!=null){
            id.setTokens(id.getTokens()+1);
        }
        FirebaseDatabase.getInstance().getReference("userProfile").child(uid).setValue(id);
        }
    });
    FirebaseDatabase.getInstance().getReference("userProfile").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             id=dataSnapshot.getValue(userId.class);
            tokens.setText(""+id.getTokens());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    test.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent intent=new Intent(Main2Activity.this,CameraActivity.class);
//            if(ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED){
//                ActivityCompat.requestPermissions(Main2Activity.this,new String[]{Manifest.permission.CAMERA},102);
//            }
//            startActivity(intent);
            Intent  intent=new Intent(Main2Activity.this,MainActivity.class);
            startActivity(intent);
        }
    });
    }


    private void findUser(final int random){
        //final ArrayList<userId> allusers=new ArrayList<>();
        final int[] count = {0};
        FirebaseDatabase.getInstance().getReference("userProfile").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                count[0]++;
                if(count[0]==random){
                    userId id=dataSnapshot.getValue(userId.class);
                    id_random[0]=dataSnapshot.getKey();
                    int flag=0;
                    //assert id != null;
                    //List<String> temp=id.getTempfriends();
                    if(myTempFriends!=null){
                        for(int i=0;i<myTempFriends.size();i++){
                            if(id_random[0].equals(myTempFriends.get(i))) {
                                flag = 1;
                                Log.e("this","Already in temp");
                                break;
                            }
                        }
                    }
                    for(int i=0;i<list.size();i++){
                        if(list.get(i).getUserId().equals(id_random[0])){
                            flag=1;
                            break;
                        }
                    }
                    if(id_random[0].equals(uid)){
                        flag=1;
                    }
                    if(flag==0)
                        inflateAlertView(id);
                    else{
                        Log.e("this","Already a friend");
                    }
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
            public void onCancelled(@NonNull DatabaseError databaseError)
            {Log.e("cancel","database error ");
            }
        });

    }
    private void inflateAlertView( userId user){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Match");
        if(view==null){
            view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.main_todo,null);
        }
        TextView name=view.findViewById(R.id.nameXML);
        ImageView img=view.findViewById(R.id.main_img);
        TextView biol=view.findViewById(R.id.main_bottom_Text);
        name.setText(user.getName());
        if(user.getProfilePicUrl()!=null){
            Glide.with(getApplicationContext())
                    .load(user.getProfilePicUrl())
                    .into(img);
        }
        else{
            img.setImageResource(R.drawable.avatar_male);
        }
        if(user.getBioLine()!=null) {
            biol.setText(user.getBioLine());
        }
        else{
            biol.setText("");
            Log.e("this","No bio line");
        }
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main2Activity.this,OtherProfileActivity.class);
                intent.putExtra("hisUid",id_random[0]);
                startActivity(intent);
            }
        });
        builder.setView(view);
        if(myTempFriends==null){
            myTempFriends=new ArrayList<>();
            Log.e("this","temp is null");
        }
        //      myTempFriends.add(id_random[0]);
        List<String> randomTempFriends;
        randomTempFriends=user.getTempfriends();
        if(randomTempFriends==null){
            randomTempFriends=new ArrayList<>();
        }
//        randomTempFriends.add(uid);
        final List<String> finalTemp1 = myTempFriends;
        final List<String> finalTemp2 = randomTempFriends;
        builder.setPositiveButton("Message", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // populateTempFriends();
                Intent intent=new Intent(Main2Activity.this,MessageActivity.class);
                intent.putExtra("myUid",uid);
                //pushing the temp list in the db
                finalTemp1.add(id_random[0]);
                finalTemp2.add(uid);
                FirebaseDatabase.getInstance().getReference("userProfile").child(uid).child("tempfriends").setValue(finalTemp1);
                FirebaseDatabase.getInstance().getReference("userProfile").child(id_random[0]).child("tempfriends").setValue(finalTemp2);
                intent.putExtra("hisUid",id_random[0]);
                startActivity(intent);
            }
        }).setNegativeButton("Cancel",null);
        builder.create().show();
        view=null;
    }

    private void logout(){

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
                startActivity(intent);
                break;
            }
            case R.id.action_random_user:{
                FirebaseDatabase.getInstance().getReference("userCount").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Double count = dataSnapshot.getValue(double.class);
                        if (count != null) {
                            int n=(int)count.longValue();
                            int random = new Random().nextInt(n) + 1;
                            findUser(random);
                           // testView.append(""+random);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            }
//            case R.id.action_main_search:{
//                if(!isSearchOn) {
//                    isSearchOn=true;
//                    search_Layout.setVisibility(View.VISIBLE);
//                    search.requestFocus();
//                    lv.clearFocus();
//                }else{
//                    isSearchOn=false;
//                    search_Layout.setVisibility(View.GONE);
//                    lv.requestFocus();
//                    search.clearFocus();
//                }
//                break;
//            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
