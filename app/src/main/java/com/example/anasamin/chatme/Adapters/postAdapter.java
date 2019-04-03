package com.example.anasamin.chatme.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anasamin.chatme.Objects.postObject;
import com.example.anasamin.chatme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;

public class postAdapter extends ArrayAdapter<postObject> {
    private ArrayList<postObject> list;

    boolean isCommentActive=false;
    boolean isLiked=false;


    private ImageView userPic,myPic,messagePic;
    private TextView userName,messageText,time,no_likes,no_comments,no_shares,viewAllComments,postButton,add_comment_tv,likeTv;
    private LinearLayout likeBtn,shareBtn;
    private EditText add_comment_ev;
    private FrameLayout fl;
    public postAdapter(Context context,  ArrayList<postObject> objects) {
        super(context, 0, objects);
        list=objects;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        View view=convertView;
        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.post_item,parent,false);
        }
        postObject current=list.get(position);
        initialiseViews(view);
        getProfileImages(current);
        time.setText(gettime(current.getTime()));
        if(current.isHas_image()){
            messagePic.setVisibility(View.VISIBLE);
            String url=current.getUrlImage();
            Glide.with(getContext())
                    .load(url)
                    .into(messagePic);
        }
        if(current.isHas_text()){
            messageText.setVisibility(View.VISIBLE);
            messageText.setText(current.getBody());
        }
        if(!current.isHas_text()){
            messageText.setVisibility(GONE);
        }
        if(!current.isHas_image()){
            messagePic.setVisibility(GONE);
        }
        no_likes.setText(""+current.getLikes());
        no_comments.setText(""+current.getComments()+" Comments");
        no_shares.setText(""+current.getShares()+" Shares");
        setOnClickListeners(current);
        String comment=add_comment_ev.getText().toString();
        send(comment);

        return view;
    }
    private void setOnClickListeners(final postObject objs){
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLiked){
                objs.setLikes(objs.getLikes()+1);
//                FirebaseDatabase.getInstance().getReference("LocalPosts").setValue(objs);
                    isLiked=true;
                    likeTv.setText("Liked");
            }
            else{
                    objs.setLikes(objs.getLikes()-1);
                    //               FirebaseDatabase.getInstance().getReference("LocalPosts").setValue(objs);
                    isLiked=false;
            }
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //how to share the same data
                objs.setShares(objs.getShares()+1);
            }
        });
        add_comment_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCommentActive=true;
                add_comment_ev.setVisibility(View.VISIBLE);
                add_comment_tv.setVisibility(GONE);
                postButton.setVisibility(View.VISIBLE);
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //push a comment
            }
        });
        viewAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //open the comments through comment list
            }
        });
        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCommentActive=false;
                add_comment_tv.setVisibility(View.VISIBLE);
                add_comment_ev.setVisibility(GONE);
                postButton.setVisibility(GONE);
            }
        });
    }
    private void send(String comment){

    }
    private void getProfileImages(postObject obj){
        DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("userProfile");
        dbref.child(obj.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url=null;
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    if(child.getKey().equals("name")){
                        String n=child.getValue(String.class);
                        userName.setText(n);
                    }
                    if(child.getKey().equals("profilePicUrl")){
                        url=child.getValue(String.class);
                        Glide.with(getContext())
                                .load(url)
                                .into(userPic);
                    }
                }
                if(url==null){
                    userPic.setImageResource(R.drawable.avatar_male);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dbref.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = null;
                for(DataSnapshot child:dataSnapshot.getChildren()) {

                    if (child.getKey().equals("profilePicUrl")) {
                        url = child.getValue(String.class);
                        Glide.with(getContext())
                                .load(url)
                                .into(myPic);
                    }
                }
                if (url == null) {
                    userPic.setImageResource(R.drawable.avatar_male);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private String gettime(Long timestamp){
        SimpleDateFormat formatter =new SimpleDateFormat("h:mm a");
        return formatter.format(new Date(timestamp*1000));
    }
    private void initialiseViews(View view){
        userPic=view.findViewById(R.id.post_user_img);
        myPic=view.findViewById(R.id.post_your_image);
        messagePic=view.findViewById(R.id.post_message_img);

        userName=view.findViewById(R.id.post_user_name);
        messageText=view.findViewById(R.id.post_message_text);
        time=view.findViewById(R.id.post_time);
        no_likes=view.findViewById(R.id.post_no_likes);
        no_comments=view.findViewById(R.id.post_no_comments);
        no_shares=view.findViewById(R.id.post_no_shares);
        viewAllComments=view.findViewById(R.id.post_view_all_comments);
        postButton=view.findViewById(R.id.post_send_comment_button);
        add_comment_tv=view.findViewById(R.id.post_add_comment_TV);
        likeTv=view.findViewById(R.id.post_like_tv);

        likeBtn=view.findViewById(R.id.post_like_LinearL);
        shareBtn=view.findViewById(R.id.post_share_LinearL);

        add_comment_ev=view.findViewById(R.id.post_add_comment_EV);

        fl=view.findViewById(R.id.post_frameLayout);
    }
}
