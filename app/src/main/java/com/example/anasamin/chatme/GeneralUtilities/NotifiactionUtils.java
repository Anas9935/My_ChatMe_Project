package com.example.anasamin.chatme.GeneralUtilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.anasamin.chatme.Activities.Main2Activity;
import com.example.anasamin.chatme.R;
import com.firebase.ui.auth.data.model.Resource;

public class NotifiactionUtils {
    private static final int CHECK_REQUEST_CODE=101;
    private static final int ACTION_CHECK_MESSAGES=102;

    public static String CHECK_CHANNEL_ID="Check-notif-channel";
    public static String lastMessage;
    public static int CHECK_REM_ID=103;
    public static void clearAllNotifs(Context context){
        NotificationManager manager=(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }
    public static void newMessages(Context context,String message){
        lastMessage=message;
        NotificationManager manager=(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CHECK_CHANNEL_ID,"check_channel",
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,CHECK_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context,R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_send_black_24dp)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("Chat Me")
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        manager.notify(CHECK_REM_ID,builder.build());
    }
    private static PendingIntent contentIntent(Context context){
        Intent intent=new Intent(context, Main2Activity.class);
        return PendingIntent.getActivity(context,CHECK_REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private static Bitmap largeIcon(Context context){
        Resources resource=context.getResources();
        Bitmap icon= BitmapFactory.decodeResource(resource, R.drawable.ic_send_black_24dp);
        return icon;
    }
}
