package com.kavin.socialevening.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kavin.socialevening.fragment.MyTeamsFragment;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 31/10/15
 * Author     : Kavin Varnan
 */
public class HomeFragmentAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] {"My Teams", "Near by", "Challenges"};
    private Context mContext;

    private MyTeamsFragment mMyTeamsFragment;

    public HomeFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
//        if (mMyTeamsFragment == null) {
//            mMyTeamsFragment = new MyTeamsFragment();
//        }
        return new MyTeamsFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
