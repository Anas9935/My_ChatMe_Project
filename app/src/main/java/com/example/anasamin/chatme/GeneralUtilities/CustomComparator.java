package com.example.anasamin.chatme.GeneralUtilities;

import com.example.anasamin.chatme.Objects.messageListObjects;

import java.util.Comparator;

public class CustomComparator implements Comparator<messageListObjects> {

    @Override
    public int compare(messageListObjects o1, messageListObjects o2) {
        Long time1=o1.getMessages().getTimestamp();
        Long time2=o2.getMessages().getTimestamp();
        return time1.compareTo(time2);

    }
}
