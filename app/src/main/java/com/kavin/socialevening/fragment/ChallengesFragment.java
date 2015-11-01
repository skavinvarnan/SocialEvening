package com.kavin.socialevening.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kavin.socialevening.R;
import com.kavin.socialevening.adapter.ChallengeListAdapter;
import com.kavin.socialevening.adapter.NearByAdapter;
import com.kavin.socialevening.utils.Constants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengesFragment extends Fragment {


    private List<ParseObject> mChallengesWhereImInvolved = new ArrayList<ParseObject>();
    private List<Boolean> mDidISendTheChallenge = new ArrayList<Boolean>();

    private View mRootView;

    @Bind(R.id.list)
    protected ListView mList;

    @Bind(R.id.progress_bar)
    protected ProgressBar mProgressBar;

    @Bind(R.id.no_item)
    protected TextView mNoItem;

    private ChallengeListAdapter mChallengeListAdapter;

    public ChallengesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_challenges, container, false);
        ButterKnife.bind(this, mRootView);
        loadChallenges();
        return mRootView;
    }

    private void loadChallenges() {

        mProgressBar.setVisibility(View.VISIBLE);
        mNoItem.setVisibility(View.GONE);
        mList.setVisibility(View.GONE);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Parse.Challenge.CHALLENGE);
        query.include(Constants.Parse.Challenge.CHALLENGE_TO);
        query.include(Constants.Parse.Challenge.CHALLENGE_FROM);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    mChallengesWhereImInvolved = null;
                    mChallengesWhereImInvolved = new ArrayList<ParseObject>();
                    mDidISendTheChallenge = null;
                    mDidISendTheChallenge = new ArrayList<Boolean>();
                    for (ParseObject parseObject : objects) {

                        List<String> challengeToFriends = (List<String>) parseObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_TO)
                                .get(Constants.Parse.Team.JOINED_FRIENDS);

                        List<String> challengeFromFriends = (List<String>) parseObject.getParseObject(Constants.Parse.Challenge.CHALLENGE_FROM)
                                .get(Constants.Parse.Team.JOINED_FRIENDS);

                        if (challengeFromFriends.contains(ParseUser.getCurrentUser().getEmail())) {
                            mChallengesWhereImInvolved.add(parseObject);
                            mDidISendTheChallenge.add(true);
                        } else if (challengeToFriends.contains(ParseUser.getCurrentUser().getEmail())) {
                            mChallengesWhereImInvolved.add(parseObject);
                            mDidISendTheChallenge.add(false);
                        }
                        receivedChallenges();
                    }
                } else {
                    mNoItem.setVisibility(View.VISIBLE);
                    mNoItem.setText("It looks like some error occurred :( Try restarting the application");
                    mList.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private void receivedChallenges() {
        if (mChallengesWhereImInvolved.size() == 0) {
            mNoItem.setVisibility(View.VISIBLE);
            mNoItem.setText("You don't have any challenges. Go to near by team tab to create a challenge");
            mList.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
        } else {
            mChallengeListAdapter = new ChallengeListAdapter(getActivity(), mChallengesWhereImInvolved, mDidISendTheChallenge);
            mList.setAdapter(mChallengeListAdapter);
            mNoItem.setVisibility(View.GONE);
            mList.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        loadChallenges();
        super.onResume();
    }

}
