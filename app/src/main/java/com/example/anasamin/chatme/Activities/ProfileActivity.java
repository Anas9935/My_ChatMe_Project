package com.example.anasamin.chatme.Activities;

import android.content.Intent;
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
import com.pkmmte.view.CircularImageView;

public class ProfileActivity extends AppCompatActivity {
TextView test,name,email,phno;
TextView bio,lives,birthday,likes,follows,EmailEdit;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        test=(TextView)findViewById(R.id.testTV);
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
        bioEdit=findViewById(R.id.profile_edit_bio);
        livesEdit=findViewById(R.id.profile_edit_country);
        phnoEdit=findViewById(R.id.profile_edit_number);
        saveBtn=findViewById(R.id.profile_saveBtn);
        prflEntry=findViewById(R.id.profileEntry);
        profileedits=findViewById(R.id.profile_editViews);
        EmailEdit=findViewById(R.id.profile_edit_email);

        Intent intent=getIntent();
        myUid=intent.getStringExtra("myId");
        //test.setText(myUid);

        db=FirebaseDatabase.getInstance();
        profile=db.getReference("userProfile").child(myUid);
        storage=FirebaseStorage.getInstance();
        ref=storage.getReference().child("profilePics").child(myUid);
        notif=db.getReference("Notification").child(myUid);

        setOnClickListeners();
        attachEventListener();
    }
    private void setOnClickListeners(){
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveedits();
                cam.setVisibility(View.GONE);
                gal.setVisibility(View.GONE);
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
                profile.setValue(new userId(nm,biolines,live,DOB,no_likes,eml,phn,gen,imageUrl));
                Toast.makeText(ProfileActivity.this, "Liked", Toast.LENGTH_SHORT).show();
                like.setImageResource(R.drawable.blue_heart);
                likes.setText("Likes-"+no_likes);
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
    }
    private void saveedits(){
        //updates the profile
        String newname=nameedit.getText().toString();
        String newBioline=bioEdit.getText().toString();
        String newCountry=livesEdit.getText().toString();
        Long newPhno=Long.valueOf(phnoEdit.getText().toString());
        Long newDob=Long.valueOf("1542645");
        nm=newname;
        biolines=newBioline;
        live=newCountry;
        name.setText(newname);
        phno.setText(String.valueOf(newPhno));
        bio.setText(newBioline);
        lives.setText(newCountry);
        birthday.setText(String.valueOf(newDob));
        phn=newPhno;
        DOB=newDob;
        userId updatedId=new userId(newname,newBioline,newCountry,newDob,no_likes,eml,newPhno,gen,imageUrl);
        profile.setValue(updatedId);
        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
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
                            bio.setText(biolines);
                            live=id.getCountry();
                            lives.setText(live);
                            no_likes=id.getLikes();
                            likes.setText("Likes-"+no_likes);
                            DOB=id.getBirthday();
                            birthday.setText(String.valueOf(DOB));
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
                        userId updated=new userId(nm,biolines,live,DOB,no_likes,eml,phn,gen,task.getResult().toString());
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
}
