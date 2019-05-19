package com.example.anasamin.chatme.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anasamin.chatme.Authentication.userId;
import com.example.anasamin.chatme.Objects.NotificationObjects;
import com.example.anasamin.chatme.Objects.userWithGender;
import com.example.anasamin.chatme.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
TextView name,email,phno;
TextView bio,lives,birthday,likes,follows,EmailEdit,hashTags;
TextView interest_edit_tv;
EditText interest_edit_ev;
ImageView interest_edit_save;
List<String> list;

ImageView like,follow,datePicker;
EditText nameedit,bioEdit,livesEdit,phnoEdit;
LinearLayout prflEntry,profileedits;
Button saveBtn;
ImageButton cam,gal;
ImageView ppric;
ProgressBar prog;
    String myUid;
    String nm,eml,biolines,live;
    Long phn,DOB;
    int gen,no_likes;
    String imageUrl=null;
    FirebaseDatabase db;
    DatabaseReference profile,notif;
    ValueEventListener listener;
    int PPICGET=101;

    FirebaseStorage storage;
    StorageReference ref;
    int hisGen,myGen;

    DatePickerDialog.OnDateSetListener dateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getViews();

        Intent intent=getIntent();
        myUid=intent.getStringExtra("myId");
        //test.setText(myUid);

        list=new ArrayList<>();


        db=FirebaseDatabase.getInstance();
        myGen=getGender(myUid);
        profile=db.getReference("userProfile").child(String.valueOf(myGen)).child(myUid);
        storage=FirebaseStorage.getInstance();
        ref=storage.getReference().child("profilePics").child(myUid);
        notif=db.getReference("Notification").child(myUid);

        setOnClickListeners();
        attachEventListener();
    }
    private void getViews(){
        name=(TextView)findViewById(R.id.profileName);
        email=(TextView)findViewById(R.id.profileEmail);
        phno=(TextView)findViewById(R.id.profileTelephone);
        cam=findViewById(R.id.cameraPic);
        gal=findViewById(R.id.galleryPic);
        ppric=findViewById(R.id.prflimg);
        prog=findViewById(R.id.imgProgress);
        bio=findViewById(R.id.profileBio);
        lives=findViewById(R.id.profile_liveIn);
        birthday=findViewById(R.id.profile_birthday);
        likes=findViewById(R.id.profile_likes);
        follows=findViewById(R.id.profile_following);
        like=findViewById(R.id.profile_like_btn);
        follow=findViewById(R.id.profile_addFrndbtn);
        nameedit=findViewById(R.id.profile_edit_name);
        datePicker=findViewById(R.id.profile_datePicker);
        bioEdit=findViewById(R.id.profile_edit_bio);
        livesEdit=findViewById(R.id.profile_edit_country);
        phnoEdit=findViewById(R.id.profile_edit_number);
        saveBtn=findViewById(R.id.profile_saveBtn);
        prflEntry=findViewById(R.id.profileEntry);
        profileedits=findViewById(R.id.profile_editViews);
        EmailEdit=findViewById(R.id.profile_edit_email);
        hashTags=findViewById(R.id.profile_hashes);
        interest_edit_ev=findViewById(R.id.profile_interests_ev);
        interest_edit_tv=findViewById(R.id.profile_interest_tv);
        interest_edit_save=findViewById(R.id.profile_interest_add_btn);
    }
    private void setOnClickListeners(){
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveedits();
                cam.setVisibility(View.INVISIBLE);
                gal.setVisibility(View.INVISIBLE);
                prflEntry.setVisibility(View.VISIBLE);
                profileedits.setVisibility(View.GONE);
                saveBtn.setVisibility(View.GONE);

            }
        });
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prog.setVisibility(View.VISIBLE);
                Intent getPicIntent=new Intent(Intent.ACTION_GET_CONTENT);
                getPicIntent.setType("image/jpeg");
                getPicIntent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(getPicIntent,PPICGET);
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_likes++;
                profile.setValue(new userId(nm,biolines,live,DOB,eml,phn,gen,imageUrl));
                Toast.makeText(ProfileActivity.this, "Liked", Toast.LENGTH_SHORT).show();
                like.setImageResource(R.drawable.blue_heart);
                likes.setText("Likes-"+no_likes);
                long time=System.currentTimeMillis()/1000;
                NotificationObjects objects=new NotificationObjects(myUid,time,1);
                FirebaseDatabase.getInstance().getReference("Notification").child(myUid).push().setValue(objects);
            }
        });
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long time= System.currentTimeMillis()/1000;
                String uid=FirebaseAuth.getInstance().getUid();
                notif.push().setValue(new NotificationObjects(uid,time,0));
            }
        });
        interest_edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String hash=interest_edit_ev.getText().toString();
            interest_edit_ev.setText("");
            if(!hash.equals(""))
                list.add(hash);
                interest_edit_tv.append("#"+hash+"   ");
            }
        });
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(ProfileActivity.this,
                        android.R.style.Theme_Holo_Dialog,
                        dateSetListener,year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date=dayOfMonth+"-"+month+"-"+year;
                DateFormat formatter=new SimpleDateFormat("dd-mm-yyyy");
                Date d=null;
                try {
                     d = (Date) formatter.parse(date);
                }catch (ParseException e){
                    e.printStackTrace();
                }
                Log.e("date-",""+d.getTime());
                DOB=d.getTime()/1000;

            }
        };
    }
    private void saveedits(){
        //updates the profile
        String newname=nameedit.getText().toString();
        String newBioline=bioEdit.getText().toString();
        String newCountry=livesEdit.getText().toString();
        Long newPhno=Long.valueOf(phnoEdit.getText().toString());
        nm=newname;
        biolines=newBioline;
        live=newCountry;
        name.setText(newname);
        phno.setText(String.valueOf(newPhno));
        bio.setText(newBioline);
        lives.setText(newCountry);
        birthday.setText(getdate(DOB));
        phn=newPhno;
        userId updatedId=new userId(newname,newBioline,newCountry,DOB,eml,newPhno,gen,imageUrl);
        updatedId.setHashTags(list);
        profile.setValue(updatedId);
        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
        if(list!=null){
            hashTags.setText("");
            for( int i=0;i<list.size();i++) {
                hashTags.append("#"+list.get(i) + "   ");
            }
        }
    }
    private void attachEventListener() {
        if (listener == null) {
            listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userId id = dataSnapshot.getValue(userId.class);
                            name.setText(id.getName());
                            nm=id.getName();
                            email.setText(id.getEmail());
                            eml=id.getEmail();
                            phno.setText(String.valueOf(id.getTel()));
                            phn=id.getTel();
                            gen=id.getGender();
                            imageUrl = id.getProfilePicUrl();
                            biolines=id.getBioLine();
                            if(biolines!=null)
                                bio.setText(biolines);
                            live=id.getCountry();
                            if(live!=null)
                                lives.setText(live);
                            //no_likes=id.getLikes();
                            //likes.setText("Likes-"+no_likes);
                            DOB=id.getBirthday();
                            if(DOB!=null)
                                birthday.setText(getdate(DOB));
                            List<String> hashtag=id.getHashTags();
                            list=hashtag;
                            if(list==null){
                                list=new ArrayList<>();
                            }
                            if(hashtag!=null){
                                for(int i=0;i<hashtag.size();i++){
                                    hashTags.append("#"+hashtag.get(i)+"   ");
                                }
                            }
                            if (imageUrl == null) {
                        ppric.setImageDrawable(getResources().getDrawable(R.drawable.avatar_male));
                        Log.e("AttacheventListener","imageIs null");
                    } else {
                        Glide.with(ppric.getContext())
                                .load(imageUrl)
                                .into(ppric);
                    }
                    }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            profile.addListenerForSingleValueEvent(listener);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PPICGET&&resultCode==RESULT_OK){
            Uri img=data.getData();
            ref.putFile(img).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    prog.setVisibility(View.GONE);
                    if(task.isSuccessful()){
                        Uri img=task.getResult();
                        userId updated=new userId(nm,biolines,live,DOB,eml,phn,gen,task.getResult().toString());
                        profile.setValue(updated);
                        Log.e("Profile Activity",img.toString());
                        Glide.with(ppric.getContext())
                                .load(img.toString())
                               .into(ppric);
                    }
                }
            });
        }
    }
private void populateEdits(){
        nameedit.setText(name.getText().toString());
        bioEdit.setText(bio.getText().toString());
        livesEdit.setText(lives.getText().toString());
        phnoEdit.setText(phno.getText().toString());
        EmailEdit.setText(email.getText().toString());
        for( int i=0;i<list.size();i++)
        interest_edit_tv.append(list.get(i).toString());
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit_profile:{
                cam.setVisibility(View.VISIBLE);
                gal.setVisibility(View.VISIBLE);
                prflEntry.setVisibility(View.GONE);
                profileedits.setVisibility(View.VISIBLE);
                populateEdits();
                saveBtn.setVisibility(View.VISIBLE);
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }
    }

    private String getdate(long timestamp){
        SimpleDateFormat format=new SimpleDateFormat("dd/mm/yyyy");
        return format.format(new Date(timestamp*1000));
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
//TODO  [unchecked] unchecked call to add(T) as a member of the raw type ArrayAdapter
//where T is a type-variable:
//T extends Object declared in class ArrayAdapter
//TODO [deprecation] Theme_Holo_Dialog in style has been deprecated
//[deprecation] getDrawable(int) in Resources has been deprecated