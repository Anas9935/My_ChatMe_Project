package com.example.anasamin.chatme.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anasamin.chatme.Adapters.messageAdapter;
import com.example.anasamin.chatme.GeneralUtilities.CustomComparator;
import com.example.anasamin.chatme.Objects.lastMessageFirebaseObject;
import com.example.anasamin.chatme.Objects.messageListObjects;
import com.example.anasamin.chatme.Objects.messageObject;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;


public class MessageActivity extends AppCompatActivity {
            EditText message;
            TextView test;
    ProgressBar imgProgress;
            ImageView sendBtn;
            ListView lv;
            String toId;
            String userid;
            messageAdapter adapter;
            ArrayList<messageListObjects> arrayList;
            ArrayList<String> name;
            String myname,hisname;
            ImageView imv;
            int IMAGE_REQ_CODE=100;
            int myGender,hisGender;

            FirebaseDatabase database;
            DatabaseReference myReference,hisReference,nameReference,name2Reference;
            DatabaseReference myLastmessage,hisLastMessage;
            ChildEventListener childEventListener;
            ValueEventListener valueEventListener;
            FirebaseStorage storage;
            StorageReference storeRef;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_message);
                //Views
                message=(EditText)findViewById(R.id.editMessageXML);
                sendBtn=(ImageView)findViewById(R.id.editSendXML);
                lv=(ListView)findViewById(R.id.listview_MessageXML);
                imv=findViewById(R.id.imagePick);
                imgProgress=findViewById(R.id.imgProgress);
                 test=(TextView)findViewById(R.id.testTv);
//intent for recieving the uids
                Intent intent=getIntent();
                toId=intent.getStringExtra("hisUid");
                userid=intent.getStringExtra("myUid");

                getGenders();
        database=FirebaseDatabase.getInstance();
        myReference=database.getReference("messages").child(userid).child(toId);
        hisReference=database.getReference("messages").child(toId).child(userid);
        nameReference=database.getReference("userProfile").child(String.valueOf(myGender)).child(userid);
        name2Reference=database.getReference("userProfile").child(String.valueOf(hisGender)).child(toId);
        myLastmessage=database.getReference("lastMessage").child(userid).child(toId);
        hisLastMessage=database.getReference("lastMessage").child(toId).child(userid);

        storage=FirebaseStorage.getInstance();
        storeRef=storage.getReference("picMessages").child(FirebaseAuth.getInstance().getUid());

        imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,"complete action using"),IMAGE_REQ_CODE);
            }
        });

        name=new ArrayList<>();
        arrayList=new ArrayList<>();
        Collections.sort(arrayList,new CustomComparator());
        adapter=new messageAdapter(this,arrayList);
        lv.setAdapter(adapter);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        //onAttachEventListener();
    }
public void  getGenders(){
                ValueEventListener lis=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userWithGender use=dataSnapshot.getValue(userWithGender.class);
                        if (use.getUid().equals(userid)){
                            myGender=use.getGender();
                        }else if(use.getUid().equals(toId)){
                            hisGender=use.getGender();
                            onAttachEventListener();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                FirebaseDatabase.getInstance().getReference("userGender").child(userid).addListenerForSingleValueEvent(lis);
                FirebaseDatabase.getInstance().getReference("userGender").child(toId).addListenerForSingleValueEvent(lis);
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQ_CODE&&resultCode==RESULT_OK){
            Uri selected_img=data.getData();
            String path=data.getData().getLastPathSegment();
          //  Log.e("this", "onActivityResult: "+path);
            final InputStream imagestream;
            final Bitmap imagev;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                imagestream = getContentResolver().openInputStream(selected_img);
                 imagev= BitmapFactory.decodeStream(imagestream);
                imagev.compress(Bitmap.CompressFormat.JPEG, 45, baos);
                byte[] imgData = baos.toByteArray();
                storeRef.child(path+".jpeg").putBytes(imgData).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        return storeRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Uri photoUri=task.getResult();
                            long time= System.currentTimeMillis()/1000;
                            messageObject obj=new messageObject(photoUri.toString(),toId,time);
                            //push it indatabase
                            myReference.push().setValue(obj);
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    public void send(){
        //sending to database
        String mess=message.getText().toString();
        if(mess.length()>0){
            long time= System.currentTimeMillis()/1000;
            messageObject obj=new messageObject(mess,toId,time);
            myReference.push().setValue(obj);
            lastMessageFirebaseObject object=new lastMessageFirebaseObject(mess,time);
            myLastmessage.setValue(object);
            hisLastMessage.setValue(object);
        }
        message.setText("");
    }
    public void onAttachEventListener(){
                if(valueEventListener==null){
                    valueEventListener=new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getKey().equals(userid)) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    if (child.getKey().equals("name")) {
                                        myname = child.getValue(String.class);

                                    }
                                }
                            }
                            else if(dataSnapshot.getKey().equals(toId)) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    if (child.getKey().equals("name")) {
                                        hisname = child.getValue(String.class);
                                        adapter.setName(hisname);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    nameReference.addListenerForSingleValueEvent(valueEventListener);
                    name2Reference.addListenerForSingleValueEvent(valueEventListener);
                }
        if(childEventListener==null){
            childEventListener=new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                messageObject messageObject=dataSnapshot.getValue(com.example.anasamin.chatme.Objects.messageObject.class);
                if(dataSnapshot.child("toId").getValue(String.class).equals(toId)){
                    adapter.add(new messageListObjects(messageObject,myname));

                    }
                else
                  adapter.add(new messageListObjects(messageObject,hisname));
                Collections.sort(arrayList,new CustomComparator());
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
            myReference.addChildEventListener(childEventListener);
            hisReference.addChildEventListener(childEventListener);

        }
    }

}
