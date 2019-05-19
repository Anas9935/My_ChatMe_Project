package com.example.anasamin.chatme.GeneralUtilities;

import com.example.anasamin.chatme.Objects.postObject;
import com.example.anasamin.chatme.Objects.postObjectWithReference;

import java.util.Comparator;

public class ComparatorForPosts implements Comparator<postObjectWithReference> {
    @Override
    public int compare(postObjectWithReference o1, postObjectWithReference o2) {
        Long time1=o1.getObject().getTime();
        Long time2=o2.getObject().getTime();
        return time1.compareTo(time2);
    }
}
