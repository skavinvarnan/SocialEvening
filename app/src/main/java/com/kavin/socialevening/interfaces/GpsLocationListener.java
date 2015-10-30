package com.kavin.socialevening.interfaces;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 30/10/15
 * Author     : Kavin Varnan
 */
public interface GpsLocationListener {
    void onGpsLocationObtainedListener(String location, double latitude, double longitude);
}
