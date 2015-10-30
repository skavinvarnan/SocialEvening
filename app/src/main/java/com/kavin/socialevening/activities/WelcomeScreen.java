package com.kavin.socialevening.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
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
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kavin.socialevening.R;
import com.kavin.socialevening.server.controller.RetrofitSingleton;
import com.kavin.socialevening.server.dto.Address;
import com.kavin.socialevening.server.dto.MapLocationResponse;
import com.kavin.socialevening.server.service.MapService;
import com.kavin.socialevening.utils.Constants;
import com.parse.Parse;
import com.parse.ParseUser;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WelcomeScreen extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private MapService mMapService;

    @Bind(R.id.welcome)
    protected TextView mWelcome;
    @Bind(R.id.name)
    protected TextView mName;
    @Bind(R.id.to)
    protected TextView mTo;
    @Bind(R.id.social_evening)
    protected TextView mSocialEvening;
    @Bind(R.id.address)
    protected TextView mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        ButterKnife.bind(this);
        setColors(R.color.color_primary, R.color.color_primary, R.color.color_primary);
        mMapService = RetrofitSingleton.getRestAdapter(RetrofitSingleton.UrlType.OPEN_STREET_MAPS).create(MapService.class);
        checkIfGpsOnAndRequestLocationInfo();
        if (ParseUser.getCurrentUser().get(Constants.Parse.User.FB_NAME) != null) {
            mName.setText(ParseUser.getCurrentUser().get(Constants.Parse.User.FB_NAME).toString());
        } else if (ParseUser.getCurrentUser().get(Constants.Parse.User.NAME) != null) {
            mName.setText(ParseUser.getCurrentUser().get(Constants.Parse.User.NAME).toString());
        }


    }

    @OnClick(R.id.create_a_team)
    protected void createATeam() {
        startActivity(new Intent(this, CreateTeamScreen.class));
    }

    private void checkIfGpsOnAndRequestLocationInfo() {
        mGoogleApiClient = new GoogleApiClient.Builder(getBaseContext())
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
                                    WelcomeScreen.this, 1000);
                            mAddress.setText("Location turned off");
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
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
    public void onLocationChanged(Location location) {
        mAddress.setText("Fetching location");
        mMapService.getLocation("json", Double.toString(location.getLatitude()), Double.toString(location.getLongitude()),
                new Callback<MapLocationResponse>() {
                    @Override
                    public void success(MapLocationResponse mapLocationResponse, Response response) {
                        receivedMapLocation(mapLocationResponse);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mAddress.setText("Unable to fetch location");
                    }
                });
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    private void receivedMapLocation(MapLocationResponse mapLocationResponse) {
         mAddress.setText("you are @ " + getSimpleAddress(mapLocationResponse.getAddress()));
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
        } else {
            return "Unable to fetch location";
        }
        return null;
    }
}
