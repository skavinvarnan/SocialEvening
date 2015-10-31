package com.kavin.socialevening.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.kavin.socialevening.R;

import butterknife.ButterKnife;

public class ChallengeScreen extends BaseActivity {

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_screen);
        ButterKnife.bind(this);
        setColors(R.color.color_primary, R.color.color_primary_dark, R.color.color_primary);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Challenge");
        }
    }
}
