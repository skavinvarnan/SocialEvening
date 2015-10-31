package com.kavin.socialevening.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kavin.socialevening.R;
import com.parse.ParseUser;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
        ButterKnife.bind(this);
        setColors(R.color.color_primary, R.color.color_primary, R.color.color_primary);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
    }

    @OnClick(R.id.sign_up)
    protected void signUp() {
        startActivity(new Intent(this, SignUpScreen.class));
        finish();
    }

    @OnClick(R.id.sign_in)
    protected void signIn() {
        startActivity(new Intent(this, SignInScreen.class));
        finish();
    }
}
