package com.example.anasamin.chatme.GeneralUtilities;

import android.content.Context;

public class ReminderTasks {
    public static final String ACTION_CHECK_NEW_MESSAGES="check-new-messages";
    public static final String ACTION_CREATE_NOTIF="create-new-notif";
    public static void executeTask(Context context,String action){
        if(ACTION_CREATE_NOTIF.equals(action)){
            //PrefUtils.check context   uf return true then
            //Notification_utils.remind context
            NotifiactionUtils.newMessages(context,"This is a new message");
        }else if(ACTION_CHECK_NEW_MESSAGES.equals(action)){
            //PrefUtils.checknewMessages
        }
    }
}
