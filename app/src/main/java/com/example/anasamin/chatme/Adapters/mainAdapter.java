package com.example.anasamin.chatme.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anasamin.chatme.Objects.ItemsObjectForMain;
import com.example.anasamin.chatme.R;

import java.util.ArrayList;
public class mainAdapter extends ArrayAdapter<ItemsObjectForMain> {
    ArrayList<ItemsObjectForMain> list;


    public mainAdapter(Context context, ArrayList<ItemsObjectForMain> resource) {
        super(context,0,resource);
        list=resource;
            }

    @Override
    public View getView(int position,View convertView, final ViewGroup parent) {
        View view=convertView;
        if(view==null){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_todo,parent,false);
        }
        ItemsObjectForMain current=list.get(position);
        TextView tvname,lastMessage;
        final ImageView img=view.findViewById(R.id.main_img);
        tvname=(TextView)view.findViewById(R.id.nameXML);
        lastMessage=(TextView)view.findViewById(R.id.idXML);
        if(current.getName()!=null)
            tvname.setText(current.getName());
        lastMessage.setText(current.getLastMessage());
        if(current.getImgLink()==null){
            img.setImageResource(R.drawable.avatar_male);
        }else {
            Glide.with(img.getContext())
                    .load(current.getImgLink())
                    .into(img);
            }
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

}
