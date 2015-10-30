package com.kavin.socialevening.push;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.kavin.socialevening.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 30-10-2015
 * Author     : Kavin Varnan
 */
public class PushNotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "MyCustomReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if("yes".equals(intent.getAction())) {
            Log.v("shuffTest","Pressed YES");
            return;
        } else if("maybe".equals(intent.getAction())) {
            Log.v("shuffTest","Pressed NO");
            return;
        }


        System.out.println("Opened the broadcast reciver");
        try {
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.parse.Channel");
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
            Iterator itr = json.keys();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                Log.d(TAG, "..." + key + " => " + json.getString(key));
            }

            //dispatchTakePictureIntent();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            final NotificationCompat.Builder notif = new NotificationCompat.Builder(context)
                    .setContentTitle("title")
                    .setContentText("text")
                            //      .setTicker(getString(R.string.tick)) removed, seems to not show at all
                            //      .setWhen(System.currentTimeMillis()) removed, match default
                            //      .setContentIntent(contentIntent) removed, I don't neet it
                    .setColor(context.getResources().getColor(R.color.color_primary)) //ok
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(R.drawable.facebook_login_blue) //ok

                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.team_avatar))
                            //      .setCategory(Notification.CATEGORY_CALL) does not seem to make a difference
                    .setPriority(Notification.DEFAULT_VIBRATE); //does not seem to make a difference
            //      .setVisibility(Notification.VISIBILITY_PRIVATE); //does not seem to make a difference


            //Yes intent
            Intent yesReceive = new Intent();
            yesReceive.setAction("yes");
            PendingIntent pendingIntentYes = PendingIntent.getBroadcast(context, 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
            notif.addAction(R.drawable.ic_done_black, "Yes", pendingIntentYes);

//Maybe intent
            Intent maybeReceive = new Intent();
            maybeReceive.setAction("maybe");
            PendingIntent pendingIntentMaybe = PendingIntent.getBroadcast(context, 12345, maybeReceive, PendingIntent.FLAG_UPDATE_CURRENT);
            notif.addAction(R.drawable.ic_clear_black, "Partly", pendingIntentMaybe);




            notificationManager.notify(123, notif.build());
        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }
    }
}
