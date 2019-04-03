package com.example.anasamin.chatme.GeneralUtilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anasamin.chatme.Objects.postObject;
import com.example.anasamin.chatme.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import static android.app.Activity.RESULT_OK;


public class NewPostDialog extends DialogFragment {
    postObject newPost;
    String uid;
    String key;
    ImageView myPic,messPic,gallery,camera;
    TextView name;
    EditText message;
    RadioGroup rg;
    RadioButton local,global;
    Uri image;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("New Post");
        View view= LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_post,null);
        builder.setView(view);
        referenceViews(view);
        newPost=new postObject();
        initializeViews();

        builder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long tm=System.currentTimeMillis()/1000;
                newPost.setTime(tm);
                newPost.setUid(uid);
                String s=message.getText().toString();
                if(!s.equals("")){
                    newPost.setBody(s);
                    newPost.setHas_text(true);
                }

                switch (rg.getCheckedRadioButtonId()){
                    case R.id.dialog_new_post_RB_local:
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("LocalPosts").child(uid);
                        key=ref.push().getKey();
                        saveImgInDatabase(ref);
                        ref.child(key).setValue(newPost);

                        break;
                    case R.id.dialog_new_post_RB_Global:
                        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference("GlobalPosts");
                        key=ref2.push().getKey();
                        Log.e("this",key);
                        saveImgInDatabase(ref2);
                        ref2.child(key).setValue(newPost);
                        break;
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
    private void referenceViews(View view){
        myPic=view.findViewById(R.id.dialog_new_post_uImg);
        messPic=view.findViewById(R.id.dialog_new_post_img);
        gallery=view.findViewById(R.id.dialog_new_post_gallery);
        camera=view.findViewById(R.id.dialog_new_post_camera);
        name=view.findViewById(R.id.dialog_new_post_name);
        message=view.findViewById(R.id.dialog_new_post_message);
        rg=view.findViewById(R.id.dialog_new_post_radioGroup);
        local=view.findViewById(R.id.dialog_new_post_RB_local);
        global=view.findViewById(R.id.dialog_new_post_RB_Global);

    }
    private void initializeViews(){
        FirebaseDatabase.getInstance().getReference("userProfile").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    if(child.getKey().equals("name")){
                        String n=child.getValue(String.class);
                        name.setText(n);
                    }
                    if(child.getKey().equals("profilePicUrl")){
                        String url=child.getValue(String.class);
                        Glide.with(getContext())
                                .load(url)
                                .into(myPic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(local.isChecked()){
            global.setChecked(false);
        }else{
            local.setChecked(false);
        }
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(intent,105);
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
    public void setUid(String id){
        uid=id;
    }
private void saveImgInDatabase(final DatabaseReference ref){
        if(image!=null){
            final StorageReference refe =FirebaseStorage.getInstance().getReference("Posts/"+uid);
            refe.putFile(image).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        Toast.makeText(getContext(),"Image cant be saved in database",Toast.LENGTH_LONG).show();
                       throw task.getException();
                    }
                    return refe.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri img=task.getResult();
                    newPost.setHas_image(true);
                    newPost.setUrlImage(img.toString());
                    ref.child(key).setValue(newPost);
                    Log.e("ImageSaving","Image is saved at -"+img.toString());
                }
            });
        }
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==105&&resultCode==RESULT_OK){
             image=data.getData();
                messPic.setVisibility(View.VISIBLE);
            try {
                final InputStream imagestream = getContext().getContentResolver().openInputStream(image);
                final Bitmap imagev= BitmapFactory.decodeStream(imagestream);
                messPic.setImageBitmap(imagev);
            }catch (NullPointerException e){
                Log.e("Null pointer Exception",e.getLocalizedMessage());
            }catch (FileNotFoundException f){
                Toast.makeText(getContext(), "Image not Found", Toast.LENGTH_SHORT).show();
                f.printStackTrace();
            }


        }
    }
}
