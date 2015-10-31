package com.kavin.socialevening.helper;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kavin.socialevening.interfaces.GpsLocationListener;
import com.kavin.socialevening.server.controller.RetrofitSingleton;
import com.kavin.socialevening.server.dto.Address;
import com.kavin.socialevening.server.dto.MapLocationResponse;
import com.kavin.socialevening.server.service.MapService;
import com.kavin.socialevening.utils.Constants;
import com.kavin.socialevening.utils.SharedPreferenceUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 30/10/15
 * Author     : Kavin Varnan
 */
public class GpsHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private MapService mMapService;

    private Activity mActivity;

    private GpsLocationListener mGpsLocationListener;


    public void setGpsLocationListener(GpsLocationListener mGpsLocationListener) {
        this.mGpsLocationListener = mGpsLocationListener;
    }

    public GpsHelper(Activity activity) {
        this.mActivity = activity;
        mMapService = RetrofitSingleton.getRestAdapter(RetrofitSingleton.UrlType.OPEN_STREET_MAPS).create(MapService.class);
    }

    public void checkIfGpsOnAndRequestLocationInfo() {
        long lastFetchedLocation = 0;

        if (SharedPreferenceUtils.getSharedPreferenceValue(Constants.Location.LAST_FETCHED_TIME) != null) {
            lastFetchedLocation = Long.parseLong(SharedPreferenceUtils.getSharedPreferenceValue(Constants.Location.LAST_FETCHED_TIME));
        }

        if ((System.currentTimeMillis() - lastFetchedLocation) > 1000 * 60 * 5) {
            mGoogleApiClient = new GoogleApiClient.Builder(mActivity.getBaseContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();

            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(10 * 1000);
            mLocationRequest.setFastestInterval(1000);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(
                                        mActivity, 1000);
                                mGpsLocationListener.onGpsLocationObtainedListener("Location turned off", 0, 0);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            });
        } else {
            fetchLocationFromCache();
        }
    }

    private void fetchLocationFromCache() {
        String locationName = SharedPreferenceUtils.getSharedPreferenceValue(Constants.Location.LAST_FETCHED_NAME);
        double latitude = Double.parseDouble(SharedPreferenceUtils.getSharedPreferenceValue(Constants.Location.LAST_FETCHED_LAT));
        double longitude = Double.parseDouble(SharedPreferenceUtils.getSharedPreferenceValue(Constants.Location.LAST_FETCHED_LONG));
        mGpsLocationListener.onGpsLocationObtainedListener(locationName, latitude, longitude);
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(final Location location) {
        mGpsLocationListener.onGpsLocationObtainedListener("Fetching location", 0, 0);
        mMapService.getLocation("json", Double.toString(location.getLatitude()), Double.toString(location.getLongitude()),
                new Callback<MapLocationResponse>() {
                    @Override
                    public void success(MapLocationResponse mapLocationResponse, Response response) {
                        receivedMapLocation(mapLocationResponse, location);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mGpsLocationListener.onGpsLocationObtainedListener("Unable to fetch location", 0, 0);
                    }
                });
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    private void receivedMapLocation(MapLocationResponse mapLocationResponse, Location location) {
        String locationName = getSimpleAddress(mapLocationResponse.getAddress());
        if (locationName != null) {
            SharedPreferenceUtils.updateSharedPreference(Constants.Location.LAST_FETCHED_LAT, Double.toString(location.getLatitude()));
            SharedPreferenceUtils.updateSharedPreference(Constants.Location.LAST_FETCHED_LONG, Double.toString(location.getLongitude()));
            SharedPreferenceUtils.updateSharedPreference(Constants.Location.LAST_FETCHED_TIME, Long.toString(System.currentTimeMillis()));
            SharedPreferenceUtils.updateSharedPreference(Constants.Location.LAST_FETCHED_NAME, locationName);
            mGpsLocationListener.onGpsLocationObtainedListener(locationName, location.getLatitude(), location.getLongitude());
        } else {
            mGpsLocationListener.onGpsLocationObtainedListener("Unable to fetch location", 0, 0);
        }

    }

    private String getSimpleAddress(Address address) {
        if (address != null) {
            if (address.getSuburb() != null && address.getCity() != null && address.getState() != null) {
                return address.getSuburb() + ", " + address.getCity() + ", " + address.getState();
            } else if (address.getCity() != null && address.getState() != null) {
                return address.getCity() + ", " + address.getState();
            } else if (address.getCounty() != null && address.getState() != null) {
                return address.getCounty() + ", " + address.getState();
            } else if (address.getTown() != null && address.getState() != null) {
                return address.getTown() + ", " + address.getState();
            }
        }
        return null;
    }
}
