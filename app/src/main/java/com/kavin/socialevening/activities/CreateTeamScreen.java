package com.kavin.socialevening.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kavin.socialevening.R;

import butterknife.ButterKnife;

public class CreateTeamScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team_screen);
        ButterKnife.bind(this);
        setColors(R.color.color_primary, R.color.color_primary, R.color.color_primary);
    }
}
