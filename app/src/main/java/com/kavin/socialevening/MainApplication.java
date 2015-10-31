package com.kavin.socialevening;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.splunk.mint.Mint;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 29/10/15
 * Author     : Kavin Varnan
 */
public class MainApplication extends Application {

    private static Context context;

    /**
     * Get the global context of the application. Can be used in places where the activity context is not needed
     * NOTE: Don't use UI operation using this context
     * @return application context
     */
    public static Context getGlobalContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Mint.initAndStartSession(getApplicationContext(), "8355157a");
        // Clearing context just in case if there is any memory leak since we are using static
        context = null;
        context = getApplicationContext();
        // Init parse library
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "jwgveVvg5Cy4L4pzR2pP5ZdnFk1j4prMfzIqooFu", "Z8RyMOrbTnKKlzNY3tcGJU5mVtltIESOJ07ACh2o");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseFacebookUtils.initialize(this);
    }
}
