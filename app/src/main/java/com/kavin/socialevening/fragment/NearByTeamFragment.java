package com.kavin.socialevening.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kavin.socialevening.R;
import com.kavin.socialevening.activities.CreateTeamScreen;
import com.kavin.socialevening.adapter.MyTeamAdapter;
import com.kavin.socialevening.adapter.NearByAdapter;
import com.kavin.socialevening.utils.Constants;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearByTeamFragment extends Fragment {


    private View mRootView;

    @Bind(R.id.list)
    protected ListView mList;

    @Bind(R.id.progress_bar)
    protected ProgressBar mProgressBar;

    @Bind(R.id.no_item)
    protected TextView mNoItem;

    @Bind(R.id.my_fab)
    protected FloatingActionButton mAddTeam;

    private NearByAdapter mNearByAdapter;

    public NearByTeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_near_by_team, container, false);
        ButterKnife.bind(this, mRootView);
        loadTeams();
        return mRootView;
    }

    private void loadTeams() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", ParseUser.getCurrentUser().getEmail());
        mProgressBar.setVisibility(View.VISIBLE);
        mNoItem.setVisibility(View.GONE);
        mList.setVisibility(View.GONE);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Parse.Team.TEAM);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    receivedTeams(objects);
                } else {
                    mNoItem.setVisibility(View.VISIBLE);
                    mNoItem.setText("It looks like some error occurred :( Try restarting the application");
                    mList.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick(R.id.my_fab)
    protected void mapClicked() {
//        startActivity(new Intent(getActivity(), CreateTeamScreen.class));
    }

    @Override
    public void onResume() {
        loadTeams();
        super.onResume();
    }

    private void receivedTeams(List<ParseObject> parseObjectList ) {
        if (parseObjectList.size() == 0) {
            mNoItem.setVisibility(View.VISIBLE);
            mNoItem.setText("You don't have any team. Go ahead and create a team by pressing the add button");
            mList.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
        } else {
            mNearByAdapter = new NearByAdapter(getActivity(), parseObjectList);
            mList.setAdapter(mNearByAdapter);
            mNoItem.setVisibility(View.GONE);
            mList.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }


}
