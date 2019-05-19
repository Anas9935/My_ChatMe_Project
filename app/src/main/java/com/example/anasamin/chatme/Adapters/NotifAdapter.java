package com.example.anasamin.chatme.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anasamin.chatme.Objects.NotifObjectForList;
import com.example.anasamin.chatme.Objects.NotificationObjects;
import com.example.anasamin.chatme.Objects.userWithGender;
import com.example.anasamin.chatme.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotifAdapter extends ArrayAdapter<NotifObjectForList> {
    ValueEventListener listener;
    ArrayList<NotifObjectForList> list;
    public NotifAdapter(Context context, ArrayList<NotifObjectForList> objects) {
        super(context, 0,objects);
        list=objects;
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
       View view=convertView;
       if(view==null){
           view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notif_item,parent,false);
       }
       NotificationObjects current=list.get(position).getObj();
        final ImageView profile,icon;
        final TextView name,message,time;
        LinearLayout layout=view.findViewById(R.id.notif_bkgrnd);
        profile=view.findViewById(R.id.notif_img);
        String id;
        icon=view.findViewById(R.id.notif_icon);
        name=view.findViewById(R.id.notif_name);
        message=view.findViewById(R.id.notif_message);
        time=view.findViewById(R.id.notif_time);
        id=current.getMessage();
        int gen=getGender(id);
        switch (current.getType()){
            case 0: icon.setImageResource(R.drawable.add_frnd);
                break;              //FirendRequest
            case 1://image Liked
                message.setText(" Liked Your Profile Picture");
                break;
        }
        if(current.getViewed()==0){
            layout.setBackgroundResource(R.color.colorAccent);
        }else{
            layout.setBackgroundResource(R.color.colorPrimary);
        }
        if(listener==null){
            listener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String url=null;
                    String n=null;
                    for(DataSnapshot child:dataSnapshot.getChildren()){
                        if(child.getKey().equals("profilePicUrl"))
                             url=child.getValue(String.class);
                        if (child.getKey().equals("name"))
                                 n=child.getValue(String.class);
                                name.setText(n);
                    }

                    if(url!=null){
                        Glide.with(profile.getContext())
                                .load(url)
                                .into(profile);
                    }else{
                        Log.e("this","URL IS NULL");
                        profile.setImageResource(R.drawable.avatar_male);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            FirebaseDatabase.getInstance().getReference("userProfile").child(String.valueOf(gen)).child(id).addValueEventListener(listener);
        }
        listener=null;
        time.setText(gettime(current.getTime()));
       return view;
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
    private String gettime(Long timestamp){
        SimpleDateFormat formatter =new SimpleDateFormat("h:mm a dd MMM");
        return formatter.format(new Date(timestamp*1000));
    }
}
