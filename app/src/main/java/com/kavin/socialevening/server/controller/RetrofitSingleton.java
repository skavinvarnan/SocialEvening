package com.kavin.socialevening.server.controller;

import com.kavin.socialevening.utils.Constants;

import retrofit.RestAdapter;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 30/10/15
 * Author     : Kavin Varnan
 */
public class RetrofitSingleton {
    private static RestAdapter restAdapter;
    private static final String BASE_URL = Constants.BASE_URL;
    private static final String OPEN_STREET_BASE_URL = Constants.OPEN_STREET_BASE_URL;

    /**
     * Creating a RestAdapter is a very costly operation. So make this as a Singleton
     * @return RestAdapter
     */
    public static RestAdapter getRestAdapter(UrlType urlType) {
        if (restAdapter == null) {
            if (urlType == UrlType.OPEN_STREET_MAPS) {
                restAdapter = new RestAdapter.Builder()
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setEndpoint(OPEN_STREET_BASE_URL)
                        .build();
            } else {
                restAdapter = new RestAdapter.Builder()
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setEndpoint(BASE_URL)
                        .build();
            }
        }
        return restAdapter;
    }

    public enum UrlType {
        OWN_SERVER, OPEN_STREET_MAPS
    }
}
