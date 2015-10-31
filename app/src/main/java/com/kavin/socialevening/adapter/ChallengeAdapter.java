package com.kavin.socialevening.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 31/10/15
 * Author     : Kavin Varnan
 */
public class ChallengeAdapter extends PagerAdapter {

    Activity mActivity;

    public ChallengeAdapter(Activity activity) {
        mActivity = activity;
    }

    public int getCount() {
        return 10;
    }

    public Object instantiateItem(View collection, int position) {
        TextView view = new TextView(mActivity);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setText("Challenge #" + (position + 1));
        ((ViewPager) collection).addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
