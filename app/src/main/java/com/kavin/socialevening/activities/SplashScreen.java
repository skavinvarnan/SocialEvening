package com.kavin.socialevening.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.kavin.socialevening.R;
import com.kavin.socialevening.utils.Constants;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setColors(R.color.color_primary, R.color.color_primary, R.color.color_primary);
        //moveToNextScreen();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Parse.Challenge.CHALLENGE);
        query.include(Constants.Parse.Challenge.CHALLENGE_TO);
        query.include(Constants.Parse.Challenge.CHALLENGE_FROM);
        query.whereEqualTo(Constants.Parse.Challenge.CHALLENGE_TO + ":" + Constants.Parse.Team.JOINED_FRIENDS, ParseUser.getCurrentUser().getEmail());
        query.whereEqualTo(Constants.Parse.Challenge.CHALLENGE_FROM + ":" + Constants.Parse.Team.JOINED_FRIENDS, ParseUser.getCurrentUser().getEmail());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.d("asdf", "Asdf");
            }
        });
    }

    private void moveToNextScreen() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (ParseUser.getCurrentUser() != null) {
                    finish();
                    startActivity(new Intent(SplashScreen.this, WelcomeScreen.class));
                } else {
                    finish();
                    startActivity(new Intent(SplashScreen.this, IntroScreen.class));
                }
            }
        }.execute();

    }
}
