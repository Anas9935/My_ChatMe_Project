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
import com.example.anasamin.chatme.Objects.NotificationObjects;
import com.example.anasamin.chatme.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotifAdapter extends ArrayAdapter<NotificationObjects> {
    ValueEventListener listener;
    ArrayList<NotificationObjects> list;
    public NotifAdapter(Context context, ArrayList<NotificationObjects> objects) {
        super(context, 0,objects);
        list=objects;
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
       View view=convertView;
       if(view==null){
           view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notif_item,parent,false);
       }
       NotificationObjects current=list.get(position);
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
        Log.e("list-",""+list.size());
        switch (current.getType()){
            case 0://name.setText(current.getMessage());
                   // message.setText(" Wants To Be Your Friend");
                    icon.setImageResource(R.drawable.add_frnd);
                break;              //FirendRequest
        }
        if(current.getViewed()==0){
            layout.setBackgroundResource(R.color.colorAccent);
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
            FirebaseDatabase.getInstance().getReference("userProfile").child(id).addValueEventListener(listener);
        }
        time.setText(String.valueOf(current.getTime()));

       return view;
    }
}
