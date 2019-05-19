package com.example.anasamin.chatme.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.anasamin.chatme.Fragments.Chats;
import com.example.anasamin.chatme.Fragments.NewsFeed;

public class CustomFragmentAdapter extends FragmentPagerAdapter {
    int count;
    public CustomFragmentAdapter(FragmentManager mg,int no_of_tabs){
        super(mg);
        count=no_of_tabs;
    }
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:{
                return new Chats();
                 }
                 case 1:{
                    return new NewsFeed();
                 }
            default:{
                return null;
            }
        }

    }

    @Override
    public int getCount() {
        return count;
    }
}
