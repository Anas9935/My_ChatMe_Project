package com.example.anasamin.chatme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anasamin.chatme.Objects.ItemsObjectForMain;
import com.example.anasamin.chatme.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchAdapter extends ArrayAdapter<ItemsObjectForMain> {
    ArrayList<ItemsObjectForMain> list;
    public SearchAdapter( Context context, ArrayList<ItemsObjectForMain> resource) {
        super(context, 0,resource);
        list=resource;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ItemsObjectForMain> results = new ArrayList<>();
                if(constraint==null||constraint.length()==0){
                    oReturn.values=list;
                    oReturn.count=list.size();
                }else{
                    for(ItemsObjectForMain obj:list){
                        if(obj.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                            results.add(obj);
                        }
                    }
                    oReturn.values=results;
                    oReturn.count=results.size();
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results.count==0){
                    notifyDataSetInvalidated();
                }else{
                    list=(ArrayList<ItemsObjectForMain>) results.values;
                    SearchAdapter.this.notifyDataSetChanged();
                }
            }
        };

    }
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View view=convertView;
        if(view==null){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_todo,parent,false);
        }
        ItemsObjectForMain current=list.get(position);
        TextView tvname,lastMessage,tvtime;
        final ImageView img=view.findViewById(R.id.main_img);
        tvname=(TextView)view.findViewById(R.id.nameXML);
        tvtime=view.findViewById(R.id.main_todo_time);
        lastMessage=(TextView)view.findViewById(R.id.main_bottom_Text);
        if(current.getName()!=null){
            tvname.setText(current.getName());}
        if(current.getLastMessage()!=null){
            lastMessage.setText(current.getLastMessage().getMessage());
        }
        else{
            lastMessage.setText("");
        }
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
        if(current.getLastMessage()!=null){
            if(current.getLastMessage().getTime()==0){
                tvtime.setText("");
            }else{
                long timeofmeg=current.getLastMessage().getTime();
                long presentTime=System.currentTimeMillis()/1000;
                String time;
                switch(isToday(timeofmeg,presentTime)){
                    case 0:{
                        time=gettime(timeofmeg);
                        tvtime.setText(time);

                        break;
                    }
                    case 1:{
                        tvtime.setText("yesterday");
                        break;
                    }
                    case 2:{
                        time=getdate(timeofmeg);
                        tvtime.setText(time);
                        break;

                    }

                }

            }
        }else{
            tvtime.setText("");
        }
        return view;
    }
    private int isToday(long tom,long today){
        SimpleDateFormat formatter =new SimpleDateFormat("dd MM");
        String messtime=formatter.format(new Date(tom*1000));
        String todtime=formatter.format(new Date(today*1000));
        if(messtime.equals(todtime)){
            return 0;
        }
        else if(Integer.valueOf(messtime.substring(0,2))+1 == Integer.valueOf(todtime.substring(0,2))){
            return 1;
        }else{
            return 2;
        }
    }
    private String gettime(Long timestamp){
        SimpleDateFormat formatter =new SimpleDateFormat("hh:mm a");
        return formatter.format(new Date(timestamp*1000));
    }
    private String getdate(long time){
        SimpleDateFormat formatter =new SimpleDateFormat("dd MMM");
        return formatter.format(new Date(time*1000));
    }

    @Override
    public int getCount() {
        return list.size();
    }
}


