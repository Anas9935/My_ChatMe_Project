package com.example.anasamin.chatme.GeneralUtilities;

import com.example.anasamin.chatme.Objects.postObject;

import java.util.Comparator;

public class ComparatorForPosts implements Comparator<postObject> {
    @Override
    public int compare(postObject o1, postObject o2) {
        Long time1=o1.getTime();
        Long time2=o2.getTime();
        return time1.compareTo(time2);
    }
}
