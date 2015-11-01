package com.kavin.socialevening.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kavin.socialevening.R;
import com.kavin.socialevening.adapter.HomeFragmentAdapter;
import com.kavin.socialevening.fragment.MyTeamsFragment;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HomeScreen extends BaseActivity {

    @Bind(R.id.viewpager)
    protected ViewPager mViewPager;
    @Bind(R.id.sliding_tabs)
    protected TabLayout mTabLayout;
    private HomeFragmentAdapter mHomeFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ButterKnife.bind(this);
        setColors(R.color.color_primary, R.color.color_primary_dark, R.color.color_primary);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Social Evening");
        }

        mHomeFragmentAdapter = new HomeFragmentAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mHomeFragmentAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.onBackPressed();
                return true;
            case 1001:
                startActivity(new Intent(this, RewardActivity.class));
                return true;
            case 1002:
                ParseUser.logOut();
                finish();
                startActivity(new Intent(this, IntroScreen.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1001, Menu.NONE, "Rewards").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(Menu.NONE, 1002, Menu.NONE, "Logout").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        return true;
    }
}
