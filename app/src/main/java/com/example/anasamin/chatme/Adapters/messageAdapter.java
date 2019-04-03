package com.example.anasamin.chatme.Adapters;

import android.content.Context;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anasamin.chatme.Activities.MessageActivity;
import com.example.anasamin.chatme.Objects.messageListObjects;
import com.example.anasamin.chatme.Objects.messageObject;
import com.example.anasamin.chatme.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class messageAdapter extends ArrayAdapter<messageListObjects> {
    ArrayList<messageListObjects> arrayList;
    String name;
    int m=0;
    public messageAdapter(Context context, ArrayList<messageListObjects> list){
        super(context,0,list);
        arrayList=list;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_todo, parent, false);
        }
        messageListObjects current = arrayList.get(position);


        TextView message, from, time;
        ImageView messagePic;
        RelativeLayout rl = view.findViewById(R.id.relId);
        message = (TextView) view.findViewById(R.id.messageMain);
        from = (TextView) view.findViewById(R.id.userNameMessage);
        time = (TextView) view.findViewById(R.id.timeMessage);
        messagePic = view.findViewById(R.id.messagePic);

        messageObject messobj = current.getMessages();
        Log.d("TAG---------------", current.getFrom() + "   " + name);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) from.getLayoutParams();
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) rl.getLayoutParams();
        if (name.equals(current.getFrom())) {

            rl.setBackgroundResource(R.drawable.bubble_left);
            params.removeRule(RelativeLayout.ALIGN_PARENT_END);
            params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

            //params1.removeRule(RelativeLayout.ALIGN_PARENT_END);
            //params1.addRule(RelativeLayout.ALIGN_PARENT_START,RelativeLayout.TRUE);
            params1.removeRule(RelativeLayout.START_OF);
            params1.addRule(RelativeLayout.END_OF, R.id.userNameMessage);


        } else {
            rl.setBackgroundResource(R.drawable.bubble);
            params.removeRule(RelativeLayout.ALIGN_PARENT_START);
            params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);

            // params1.removeRule(RelativeLayout.ALIGN_PARENT_START);
            //   params1.addRule(RelativeLayout.ALIGN_PARENT_END,RelativeLayout.TRUE);
            params1.removeRule(RelativeLayout.END_OF);
            params1.addRule(RelativeLayout.START_OF, R.id.userNameMessage);
        }
        from.setLayoutParams(params);
        rl.setLayoutParams(params1);
        RelativeLayout.LayoutParams pars = (RelativeLayout.LayoutParams) time.getLayoutParams();
        if (messobj.getMessage().length() > 100) {
            message.setVisibility(View.GONE);
            messagePic.setVisibility(View.VISIBLE);
            Glide.with(messagePic.getContext())
                    .load(messobj.getMessage())
                    .into(messagePic);
            pars.removeRule(RelativeLayout.BELOW);
            pars.addRule(RelativeLayout.BELOW, R.id.frame);

        } else {
            message.setVisibility(View.VISIBLE);
            messagePic.setVisibility(View.GONE);
            message.setText(messobj.getMessage());
            pars.removeRule(RelativeLayout.BELOW);
            pars.addRule(RelativeLayout.BELOW, R.id.messageMain);
        }

        from.setText(current.getFrom());
        time.setText(gettime(messobj.getTimestamp()));

        return view;
    }
    private String gettime(Long timestamp){
        SimpleDateFormat formatter =new SimpleDateFormat("h:mm a");
        return formatter.format(new Date(timestamp*1000));
    }
    public void setName(String n){
        name=n;
    }
}
