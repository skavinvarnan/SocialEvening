package com.kavin.socialevening.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 30-10-2015
 * Author     : Kavin Varnan
 */
public class ActionBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if("yes".equals(intent.getAction())) {
            Log.v("shuffTest", "Pressed YES");
            return;
        } else if("maybe".equals(intent.getAction())) {
            Log.v("shuffTest","Pressed NO");
            return;
        }
    }
}
