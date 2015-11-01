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
import com.kavin.socialevening.activities.HomeScreen;
import com.kavin.socialevening.server.dto.PushDto;
import com.kavin.socialevening.utils.Constants;
import com.kavin.socialevening.utils.JsonUtils;

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
        PushDto pushDto = (PushDto) JsonUtils.convertJsonStringToObject(intent.getExtras().getString("com.parse.Data"), PushDto.class);
        if (pushDto.getPushType() == Constants.PushType.FRIEND_INVITED_TO_TEAM) {
            showNotification(context, "Invitation Received", pushDto.getMessage(), true);
        } else if (pushDto.getPushType() == Constants.PushType.FRIEND_ACCEPTED_INVITATION) {
            showNotification(context, "Invitation Accepted", pushDto.getMessage(), false);
        } else if (pushDto.getPushType() == Constants.PushType.FRIEND_DECLINED_INVITATION) {
            showNotification(context, "Invitation Declined", pushDto.getMessage(), false);
        } else if (pushDto.getPushType() == Constants.PushType.CHALLENGE_ACCEPTED) {
            showNotification(context, "Challenge Accepted", pushDto.getMessage(), false);
        } else if (pushDto.getPushType() == Constants.PushType.CHALLENGE_DECLINED) {
            showNotification(context, "Challenge Declined", pushDto.getMessage(), false);
        }

    }


    private void showNotification(Context context, String title, String message, boolean showAction) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Intent myIntent = new Intent(context, HomeScreen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, PendingIntent.FLAG_ONE_SHOT);

        final NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.moon)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon))
                .setPriority(Notification.DEFAULT_VIBRATE);

        if (showAction) {
            Intent yesReceive = new Intent(context, ActionBroadcast.class);
            yesReceive.setAction("yes");
            PendingIntent pendingIntentYes = PendingIntent.getBroadcast(context, 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.addAction(R.drawable.ic_done_black, "Accept", pendingIntentYes);

            Intent maybeReceive = new Intent(context, ActionBroadcast.class);
            maybeReceive.setAction("maybe");
            PendingIntent pendingIntentMaybe = PendingIntent.getBroadcast(context, 12345, maybeReceive, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.addAction(R.drawable.ic_clear_black, "Decline", pendingIntentMaybe);

        }
        notificationManager.notify(123, notification.build());
    }
}
