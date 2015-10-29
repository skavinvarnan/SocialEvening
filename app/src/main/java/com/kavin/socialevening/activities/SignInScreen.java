package com.kavin.socialevening.activities;

import android.content.Intent;
import android.os.Bundle;

import com.kavin.socialevening.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);
        ButterKnife.bind(this);
        setColors(R.color.color_primary, R.color.color_primary, R.color.color_primary);
    }

    @OnClick(R.id.sign_in_button)
    protected void signInClicked() {
        startActivity(new Intent(this, WelcomeScreen.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, SplashScreen.class));
    }
}
