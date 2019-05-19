package com.example.anasamin.chatme.GeneralUtilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anasamin.chatme.Activities.ProfileActivity;
import com.example.anasamin.chatme.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddFriendFragment extends DialogFragment {
    String muid,huid;
    String hisName;
    List<String> newMyList=new ArrayList<>();
    List<String> newHisList=new ArrayList<>();
    ProgressBar bar;
    boolean dismiss=false;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view= LayoutInflater.from(getContext()).inflate(R.layout.add_new_friend_dialog,null);
        builder.setView(view);
        TextView tv=view.findViewById(R.id.dialog_add_name);
        TextView bio=view.findViewById(R.id.dialog_notif_bio);
        ImageView imv=view.findViewById(R.id.dialog_notif_img);
        bar=view.findViewById(R.id.dialog_add_new_progress);
        tv.setText(hisName);
        builder.setTitle("Friend Request");
        builder.setPositiveButton("Add Friend", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "New Friend Added", Toast.LENGTH_SHORT).show();
                newHisList.add(muid);
                newMyList.add(huid);
                addFriend();
                dismiss=true;

                dismiss();

            }
        }).setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss=true;
                dismiss();
            }
        });
        getName(tv,imv,bio);
        setclickListener(tv);
        return builder.create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(dismiss){
            this.dismiss();
        }
    }

    public void setUid(String id){
        muid=id;
    }
    private void getName(final TextView view,final ImageView imageView,final TextView bio){
        ValueEventListener listener=new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(getActivity()==null){
                    return;
                }
                String url=null;
               for(DataSnapshot child:dataSnapshot.getChildren()){
                   if(child.getKey().equals("name")){
                       String n=child.getValue(String.class);
                       view.setText(n);
                   }
                   if(child.getKey().equals("profilePicUrl")){
                       url=child.getValue(String.class);
                       if(url!=null){
                           Glide.with(getContext())
                                .load(url)
                                .into(imageView);
                       }else{
                           imageView.setImageResource(R.drawable.avatar_male);
                       }
                   }
                   if(child.getKey().equals("friends")){
                       GenericTypeIndicator<List<String>> indicator=new GenericTypeIndicator<List<String>>(){};
                       newHisList=child.getValue(indicator);

                   }
                   if(child.getKey().equals("bioLine"))
                   {
                       String biol=child.getValue(String.class);
                       bio.setText(biol);
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference("userProfile").child(huid).addValueEventListener(listener);
        FirebaseDatabase.getInstance().getReference("userProfile").child(muid).child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(getActivity()==null){
                    return;
                }
                GenericTypeIndicator<List<String>> indicator=new GenericTypeIndicator<List<String>>() {};
                newMyList=dataSnapshot.getValue(indicator);
                if(newMyList==null){
                    Log.e("this","List is null");
                    newMyList=new ArrayList<>();
                }
                bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void addFriend(){
        FirebaseDatabase.getInstance().getReference("userProfile").child(muid).child("friends").setValue(newMyList);
        FirebaseDatabase.getInstance().getReference("userProfile").child(huid).child("friends").setValue(newHisList);
    }
    public void setHisUid(String id){
        huid=id;
    }
    private void setclickListener(TextView view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("myId",huid);
                startActivity(intent);
            }
        });
    }
}

