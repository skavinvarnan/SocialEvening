package com.kavin.socialevening;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 29/10/15
 * Author     : Kavin Varnan
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Init parse library
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "jwgveVvg5Cy4L4pzR2pP5ZdnFk1j4prMfzIqooFu", "Z8RyMOrbTnKKlzNY3tcGJU5mVtltIESOJ07ACh2o");
        ParseFacebookUtils.initialize(this);
    }
}