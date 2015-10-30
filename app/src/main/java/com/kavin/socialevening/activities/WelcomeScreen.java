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
import com.kavin.socialevening.helper.GpsHelper;
import com.kavin.socialevening.interfaces.GpsLocationListener;
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

public class WelcomeScreen extends BaseActivity implements GpsLocationListener {

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

    private GpsHelper mGpsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        ButterKnife.bind(this);
        setColors(R.color.color_primary, R.color.color_primary, R.color.color_primary);
        mGpsHelper = new GpsHelper(this);
        mGpsHelper.setGpsLocationListener(this);
        mGpsHelper.checkIfGpsOnAndRequestLocationInfo();
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


    @Override
    public void onGpsLocationObtainedListener(String location, double latitude, double longitude) {
        mAddress.setText("you are @ " + location);
    }
}
