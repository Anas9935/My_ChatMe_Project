package com.example.anasamin.chatme.Activities;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anasamin.chatme.Objects.ItemsObjectForMain;
import com.example.anasamin.chatme.Objects.lastMessageFirebaseObject;
import com.example.anasamin.chatme.Objects.messageObject;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class FindFrnds extends AppCompatActivity {
Button findFrnd;
TextView test;
TextView tokens,addTokens;

String uid;
FirebaseDatabase db;
DatabaseReference ref;


ArrayList<String> list;
Toolbar toolbar;

double count;
int token;
int gen;
int genderPref;
List<String> tempFriends=new ArrayList<>();
List<String> mytempFriends=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_frnds);

        uid=FirebaseAuth.getInstance().getUid();
        findFrnd=findViewById(R.id.find_new_friend);
        test=findViewById(R.id.testing);
        toolbar=findViewById(R.id.find_frnd_toolbar);
        tokens=findViewById(R.id.ffTokens);
        addTokens=findViewById(R.id.ffaddTokens);

        toolbar.setTitle("Find Friend");

        list=new ArrayList<>();
        list.add(uid);
        db= FirebaseDatabase.getInstance();
        ref=db.getReference("userProfile").child(String.valueOf(gen)).child(uid);
        findFrnd.setActivated(false);

      //  ids=new ArrayList<>();
        findFrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandom();
//                token--;
//                tokens.setText(String.valueOf(token));
//                setTokens();
            }
        });
        addTokens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adding the way to add more tokens
                token++;
                tokens.setText(String.valueOf(token));
                setTokens();
            }
        });
    getFriends();
    getCount();
    gettokensAndTempFriends();
    }

    public void setTokens(){
        db.getReference("userProfile").child(String.valueOf(gen)).child(uid).child("tokens").setValue(token);
    }
    public void gettokensAndTempFriends(){
        ValueEventListener vel= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot child:dataSnapshot.getChildren()){
                    if(child.getKey().equals("tokens")){
                        token=child.getValue(Integer.class);
                        tokens.setText(String.valueOf(token));
                    }
                    if(child.getKey().equals("tempfriends")){
                        for(DataSnapshot child2:dataSnapshot.getChildren()){
                            mytempFriends.add(child2.getValue(String.class));
                        }
                    }
                    if(child.getKey().equals("gender")){
                        gen=child.getValue(Integer.class);
                        if(gen==1){
                            genderPref=2;
                        }else genderPref=1;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        db.getReference("userProfile").child("1").child(uid).addListenerForSingleValueEvent(vel);
        db.getReference("userProfile").child("2").child(uid).addListenerForSingleValueEvent(vel);
    }
    public void getFriends(){
        ValueEventListener listener;
        listener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //   Log.e("this", "onDataChange: "+dataSnapshot.getKey() );
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    String frnds=child.getValue(String.class);
                   list.add(frnds);
                }
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
               findFrnd.setActivated(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getCount(){
        db.getReference("userCount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count=dataSnapshot.getValue(Double.class);
               // getRandom();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getRandom(){
        final int[] flag = {0};
        final String[] id = new String[1];
            int random = new Random().nextInt((int) count) + 1;
            random/=2;
            final int[] count = {0};

        final int finalRandom = random;
        db.getReference("userProfile").child(String.valueOf(genderPref)).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        count[0]++;
                        if(count[0]== finalRandom){
                            id[0] =dataSnapshot.getKey();
                            if(checkFrnd(id[0])){
                                flag[0] =1;
                                //id[0] is the required id
                                //calling alert function
                                test.append(id[0]+"\n");
                                setAlertWindow(id[0]);
                            }else{
                                getRandom();
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
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }
    public boolean checkFrnd(String id){
        int flag=0;
        for( int i=0;i<list.size();i++){
            if(!list.get(i).equals(id)){
                flag++;
            }
        }
        if(flag==list.size()){
            return true;
        }else{
            return false;
        }
    }

    public void setAlertWindow(final String id){
        final AlertDialog.Builder buider=new AlertDialog.Builder(this);
        View view=null;
        view= LayoutInflater.from(FindFrnds.this).inflate(R.layout.dialog_random_friend,null);
        //adding views
        final ImageView img,sendBtn;
        final TextView age,tags,bio;
        final EditText message;

        //initialising the views
        img=view.findViewById(R.id.rfImg);
        sendBtn=view.findViewById(R.id.rfSendBtn);
        age=view.findViewById(R.id.rfAge);
        tags=view.findViewById(R.id.rfTags);
        bio=view.findViewById(R.id.rfBio);
        message=view.findViewById(R.id.rfMessage);

        //getting the info from the database
        db.getReference("userProfile").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    if(child.getKey().equals("bioLine")){
                        String biol=child.getValue(String.class);
                        bio.setText(biol);
                    }
                    if(child.getKey().equals("profilePicUrl")){
                        String url=child.getValue(String.class);
                        Glide.with(FindFrnds.this)
                                .load(url)
                                .into(img);
                    }if(child.getKey().equals("hashTags")){
                        List<String> hashes=new ArrayList<>();
                        for(DataSnapshot ch:child.getChildren()){
                            hashes.add(ch.getValue(String.class));
                        }
                        tags.setText(" ");
                        for( int i=0;i<hashes.size();i++){
                            tags.append("#"+hashes.get(i)+"  ");
                        }
                    }
                    if(child.getKey().equals("birthday")){
                        Long birthStamp=child.getValue(Long.class);
                        int calc_age=getAge(birthStamp);
                        age.append(String.valueOf(calc_age));
                    }
                    if(child.getKey().equals("tempfriends")){
                        for(DataSnapshot c:child.getChildren()){
                            tempFriends.add(c.getValue(String.class));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mess=message.getText().toString();
                token--;
                tokens.setText(String.valueOf(token));
                setTokens();
                DatabaseReference myReference=db.getReference("messages").child(uid).child(id);
                DatabaseReference myLastmessage=db.getReference("lastMessage").child(uid).child(id);
                DatabaseReference hisLastMessage=db.getReference("lastMessage").child(id).child(uid);
                if(mess.length()>0){
                    long time= System.currentTimeMillis()/1000;
                    messageObject obj=new messageObject(mess,id,time);
                    myReference.push().setValue(obj);
                    lastMessageFirebaseObject object=new lastMessageFirebaseObject(mess,time);
                    myLastmessage.setValue(object);
                    hisLastMessage.setValue(object);
                    //adding this to my temp friends
                    tempFriends.add(uid);
                    mytempFriends.add(id);
                    db.getReference("userProfile").child(String.valueOf(gen)).child(uid).child("tempfriends").setValue(mytempFriends);
                    db.getReference("userProfile").child(String.valueOf(genderPref)).child(id).child("tempfriends").setValue(tempFriends);
                    buider.create().dismiss();

                }
                message.setText("");
            }
        });

        buider.setView(view);
        buider.create().show();
    }
    private int getAge(long dob){

        SimpleDateFormat formatter=new SimpleDateFormat("yyyy");
        int year= Calendar.getInstance().get(Calendar.YEAR);
        return year-Integer.valueOf(formatter.format(new Date(dob*1000)));
    }
}
