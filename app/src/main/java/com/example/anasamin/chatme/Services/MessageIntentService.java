package com.example.anasamin.chatme.Services;

import android.app.IntentService;
import android.content.Intent;

import com.example.anasamin.chatme.GeneralUtilities.ReminderTasks;

public class MessageIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *  name Used to name the worker thread, important only for debugging.
     */
    public MessageIntentService() {
        super("");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        String action =intent.getAction();
        ReminderTasks.executeTask(this,action);
    }
}
