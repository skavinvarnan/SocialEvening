package com.kavin.socialevening.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kavin.socialevening.R;
import com.kavin.socialevening.activities.CreateTeamScreen;
import com.kavin.socialevening.adapter.MyTeamAdapter;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTeamsFragment extends Fragment {

    private View mRootView;

    @Bind(R.id.list)
    protected ListView mList;

    @Bind(R.id.progress_bar)
    protected ProgressBar mProgressBar;

    @Bind(R.id.no_item)
    protected TextView mNoItem;

    @Bind(R.id.my_fab)
    protected FloatingActionButton mAddTeam;

    private MyTeamAdapter mMyTeamAdapter;

    public MyTeamsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_my_teams, container, false);
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
        ParseCloud.callFunctionInBackground("myTeams", map, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                if (e == null) {
                    receivedTeams(object);
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
    protected void addTeamClicked() {
        startActivity(new Intent(getActivity(), CreateTeamScreen.class));
    }

    @Override
    public void onResume() {
        loadTeams();
        super.onResume();
    }

    private void receivedTeams(Object object) {
        List<ParseObject> parseObjects = (List<ParseObject>) object;
        if (parseObjects.size() == 0) {
            mNoItem.setVisibility(View.VISIBLE);
            mNoItem.setText("You don't have any team. Go ahead and create a team by pressing the add button");
            mList.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
        } else {
            mMyTeamAdapter = new MyTeamAdapter(getActivity(), parseObjects);
            mList.setAdapter(mMyTeamAdapter);
            mNoItem.setVisibility(View.GONE);
            mList.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

}
