package com.example.anasamin.chatme.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anasamin.chatme.Authentication.userId;
import com.example.anasamin.chatme.Objects.NotificationObjects;
import com.example.anasamin.chatme.Objects.userWithGender;
import com.example.anasamin.chatme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OtherProfileActivity extends AppCompatActivity {
String hisuid,myUid;
TextView likeTv,followTv,name,bio,lives,age,tags,friends;
ImageView img,like_btn,follow_btn;
List<String> like_people;
List<String> his_friends;
boolean isLiked=false;
boolean isFriends=false;
int hisGender;


ValueEventListener listener,like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        Intent intent=getIntent();
        hisuid=intent.getStringExtra("hisUid");
        myUid= FirebaseAuth.getInstance().getUid();

        getViews();
        getProfile();
    }
    private void getViews(){
        likeTv=findViewById(R.id.o_profile_no_likes);
        followTv=findViewById(R.id.o_profile_request_status);
        name=findViewById(R.id.o_profile_name);
        bio=findViewById(R.id.o_profile_bio);
        lives=findViewById(R.id.o_profile_lives);
        age=findViewById(R.id.o_profile_age);
        tags=findViewById(R.id.o_profile_tags);
        friends=findViewById(R.id.o_profile_no_friends);
        img=findViewById(R.id.o_profile_img);
        like_btn=findViewById(R.id.o_profile_like_btn);
        follow_btn=findViewById(R.id.o_profile_follow_btn);
    }
    private void getProfile(){
        if(listener==null){
            listener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userId id=dataSnapshot.getValue(userId.class);
                    Log.e("this","Child found");
                    if(id!=null){
                        like_people=id.getLikes();
                        if(like_people==null){
                            like_people=new ArrayList<>();
                        }
                        his_friends=id.getFriends();
                        if(his_friends==null){
                            his_friends=new ArrayList<>();
                        }
                        inflateViews(id);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            hisGender=getGender(hisuid);
            FirebaseDatabase.getInstance().getReference("userProfile").child(String.valueOf(hisGender)).child(hisuid).addValueEventListener(listener);
        }
    }
    private void inflateViews(userId user){
        if(user.getProfilePicUrl()!=null){
            Glide.with(OtherProfileActivity.this)
                    .load(user.getProfilePicUrl())
                    .into(img);
        }else{
            img.setImageResource(R.drawable.avatar_male);
        }
        name.setText(user.getName());
        if(user.getBioLine()!=null){
            bio.setText(user.getBioLine());
        }
        if(user.getCountry()!=null){
            lives.setText("Lives in "+user.getCountry());
        }
        if(user.getHashTags()!=null){
            List<String> hashes=user.getHashTags();
            tags.setText(" ");
            for( int i=0;i<hashes.size();i++){
                tags.append("#"+hashes.get(i)+"    ");
            }
        }
        if(user.getBirthday()!=null){
            int calc_age=getAge(user.getBirthday());
            age.setText("Age "+calc_age);
        }
        if(user.getFriends()!=null){
            List<String> friend_list=user.getFriends();
            friends.append(""+friend_list.size());
        }
        likeTv.setText(""+like_people.size()+" Likes");

        int flag=0;
        for( int i=0;i<like_people.size();i++){
            if(like_people.get(i).equals(myUid)){
                flag=1;
                break;
            }
        }
        if(flag==1){
            like_btn.setImageResource(R.drawable.red_heart);
            isLiked=true;
        }
        like_btn.setOnClickListener(new View.OnClickListener() {
            String key=null;
            @Override
            public void onClick(View v) {
                if(isLiked){
                    isLiked=false;
                    like_btn.setImageResource(R.drawable.empty_heart);
                    Log.e("this","Disliked");
                    //TODO delete My Uid from firebase with key
                    like_people.remove(myUid);
                    FirebaseDatabase.getInstance().getReference("userProfile").child(String.valueOf(hisGender)).child(hisuid).child("likes").setValue(like_people);
                    if(like==null){
                        like=new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        FirebaseDatabase.getInstance().getReference("userProfile").child(String.valueOf(hisGender)).child(hisuid).child("likes").addListenerForSingleValueEvent(like);
                    }
                    like=null;
                }else{
                    Log.e("","Liked");
                    isLiked=true;
                    like_btn.setImageResource(R.drawable.red_heart);
                    like_people.add(myUid);
                    FirebaseDatabase.getInstance().getReference("userProfile").child(String.valueOf(hisGender)).child(hisuid).child("likes").setValue(like_people);
                    long prestime=System.currentTimeMillis()/1000;
                    NotificationObjects obj=new NotificationObjects(myUid,prestime,1);
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Notification").child(hisuid);
                    key=ref.push().getKey();
                    if(key!=null)
                       ref.child(key).setValue(obj);
                }
            }
        });
        int flag2=0;
        for( int i=0;i<his_friends.size();i++){
            if(his_friends.get(i).equals(myUid)){
                flag2=1;
                break;
            }
        }
        if(flag2==0){       //not friend yet
            follow_btn.setImageResource(R.drawable.add_frnd);
            isFriends=false;
            followTv.setText("Follow Request");
        }else{          //already Friends
            follow_btn.setImageResource(R.drawable.blue_heart);
            isFriends=true;
            followTv.setText("Friends");
        }

        follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFriends){
                    isFriends=false;
                    follow_btn.setImageResource(R.drawable.add_frnd);
                    followTv.setText("Follow Request");
                    Log.e("","follow request");
                    //delete the friend from both the profiles
                }else{
                    isFriends=true;
                    follow_btn.setImageResource(R.drawable.blue_heart);
                    followTv.setText("Friends");
                    Log.e("this","followed");
                    //sends request
                    long time=System.currentTimeMillis()/1000;
                    NotificationObjects objects=new NotificationObjects(myUid,time,0);
                    FirebaseDatabase.getInstance().getReference("Notification").child(hisuid).push().setValue(objects);
                }
            }
        });

    }

    private int getAge(long dob){

        SimpleDateFormat formatter=new SimpleDateFormat("yyyy");
        int year= Calendar.getInstance().get(Calendar.YEAR);
        return year-Integer.valueOf(formatter.format(new Date(dob*1000)));
    }
    private int getGender(String id){
        final int[] gender = {0};
        FirebaseDatabase.getInstance().getReference("userGender").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userWithGender obj=dataSnapshot.getValue(userWithGender.class);
                gender[0] =obj.getGender();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return gender[0];
    }
}
