package com.example.anasamin.chatme.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anasamin.chatme.Activities.Main2Activity;
import com.example.anasamin.chatme.Activities.OtherProfileActivity;
import com.example.anasamin.chatme.Objects.ItemsObjectForMain;
import com.example.anasamin.chatme.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class mainAdapter extends ArrayAdapter<ItemsObjectForMain> {
    ArrayList<ItemsObjectForMain> list;
    int mode;

    boolean isMenuOpened=false;


    public mainAdapter(Context context, ArrayList<ItemsObjectForMain> resource,int m) {
        super(context,0,resource);
        list=resource;
        mode=m;
            }



    @Override
    public View getView(int position,View convertView, final ViewGroup parent) {
        View view=convertView;
        if(view==null){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_todo,parent,false);
        }
        final ItemsObjectForMain current=list.get(position);
        TextView tvname,lastMessage,tvtime;

        //------------------------------------------------------------------------------------
        ImageView menu=view.findViewById(R.id.main_menu_btn);
        final ImageView img=view.findViewById(R.id.main_img);
        tvname=(TextView)view.findViewById(R.id.nameXML);
        tvtime=view.findViewById(R.id.main_todo_time);
        lastMessage=(TextView)view.findViewById(R.id.main_bottom_Text);
        //------------------------------------------------------------------------------------

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


        setupImageListener(img,current);
        //-----------------------------------------------------------------------------------
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
                      // String timeAgo=calcTime(timeofmeg);
                     //   Log.e("This", "getView: "+timeAgo);
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
        //-----------------------------------------------------------------------------------

        if(mode==1){
            menu.setVisibility(View.VISIBLE);
            populateMenu(menu,current);
        }
        if(current.getType()==1){
            //do what you need to do
        }
        return view;
    }
    private void setupImageListener(ImageView img,final ItemsObjectForMain current){
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View imgView=LayoutInflater.from(getContext()).inflate(R.layout.img_layout,null);
                ImageView profImg=imgView.findViewById(R.id.img_layout);
                if(current.getImgLink()==null){
                    profImg.setImageResource(R.drawable.avatar_male);
                }else {
                    Glide.with(profImg.getContext())
                            .load(current.getImgLink())
                            .into(profImg);
                }
                TextView cross=imgView.findViewById(R.id.img_layout_cross);
                final View finalImgView = imgView;
                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                imgView=null;
            }
        });
    }
    private void populateMenu(ImageView menu,final ItemsObjectForMain current){
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pMenu=new PopupMenu(getContext(),v);
                if(isMenuOpened)
                {isMenuOpened=false;
                    //close menu
                    pMenu.dismiss();
                }
                else{isMenuOpened=true;
                    //open menu
                    pMenu.getMenuInflater().inflate(R.menu.menu_main_todo,pMenu.getMenu());
                    pMenu.show();
                    pMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.action_main_item_delete:{
                                    //delete this from the list
                                   break;
                                }
                                case R.id.action_main_item_profile:{
                                    Intent intent=new Intent(getContext(),OtherProfileActivity.class);
                                    intent.putExtra("hisUid",current.getUserId());
                                    getContext().startActivity(intent);
                                    break;
                                }
                            }
                            return  true;
                        }
                    });
                }
                pMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        isMenuOpened=false;
                    }
                });
            }
        });

    }
 /*   public String calcTime(long time){
        String tm=new SimpleDateFormat("mm").format(new Date(time));


            long prest=System.currentTimeMillis();
          String pres=new SimpleDateFormat("mm").format(new Date(prest));
          return String.valueOf(Integer.valueOf(pres)-Integer.valueOf(tm));

    }*/
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

}
