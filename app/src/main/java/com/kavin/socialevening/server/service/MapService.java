package com.kavin.socialevening.server.service;

import com.kavin.socialevening.server.dto.MapLocationResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 30/10/15
 * Author     : Kavin Varnan
 */
public interface MapService {

    @GET("/reverse")
    void getLocation(@Query("format") String format, @Query("lat") String lat, @Query("lon") String lon
                ,Callback<MapLocationResponse> mapLocationResponseCallback);
}
