package com.kavin.socialevening.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kavin.socialevening.fragment.ChallengesFragment;
import com.kavin.socialevening.fragment.MyTeamsFragment;
import com.kavin.socialevening.fragment.NearByTeamFragment;
import com.kavin.socialevening.fragment.RewardFragment;

/**
 * Copyright 2015 (C) Virtual Applets
 * Created on : 31/10/15
 * Author     : Kavin Varnan
 */
public class HomeFragmentAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] {"My Teams", "Near by", "Challenges", "Reward"};
    private Context mContext;

    private MyTeamsFragment mMyTeamsFragment;
    private NearByTeamFragment mNearByTeamFragment;
    private ChallengesFragment mChallengesFragment;
    private RewardFragment mRewardFragment;

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
        switch (position) {
            case 0:
                if (mMyTeamsFragment == null) {
                    mMyTeamsFragment = new MyTeamsFragment();
                }
                return mMyTeamsFragment;
            case 1:
                if (mNearByTeamFragment == null) {
                    mNearByTeamFragment = new NearByTeamFragment();
                }
                return mNearByTeamFragment;
            case 2:
                if (mChallengesFragment == null) {
                    mChallengesFragment = new ChallengesFragment();
                }
                return mChallengesFragment;
            case 3:
                if (mRewardFragment == null) {
                    mRewardFragment = new RewardFragment();
                }
                return mRewardFragment;
            default:
                return null;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
