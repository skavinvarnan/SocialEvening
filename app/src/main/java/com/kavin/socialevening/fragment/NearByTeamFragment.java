package com.kavin.socialevening.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.kavin.socialevening.adapter.NearByAdapter;
import com.kavin.socialevening.utils.Constants;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
        query.whereNotEqualTo(Constants.Parse.Team.JOINED_FRIENDS, ParseUser.getCurrentUser().getEmail());
        query.whereNotEqualTo(Constants.Parse.Team.FRIENDS_LIST, ParseUser.getCurrentUser().getEmail());
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

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mNearByAdapter.gotClickedOn(position);
            }
        });
        mList.setOnItemLongClickListener (new AdapterView.OnItemLongClickListener() {
            @SuppressWarnings("rawtypes")
            public boolean onItemLongClick(AdapterView parent, View view, final int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Join Team");
                alertDialog.setMessage("Do you like to join " + mNearByAdapter.getTeamObjectList().get(position).getString(Constants.Parse.Team.NAME));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                ParseObject parseObject = mNearByAdapter.getTeamObjectList().get(position);
                                List<String> mFriendsList = (List<String>) parseObject.get(Constants.Parse.Team.FRIENDS_LIST);
                                List<String> mJoinedList = (List<String>) parseObject.get(Constants.Parse.Team.JOINED_FRIENDS);
                                mFriendsList.add(ParseUser.getCurrentUser().getEmail());
                                mJoinedList.add(ParseUser.getCurrentUser().getEmail());
                                parseObject.remove(Constants.Parse.Team.FRIENDS_LIST);
                                parseObject.remove(Constants.Parse.Team.JOINED_FRIENDS);
                                parseObject.put(Constants.Parse.Team.FRIENDS_LIST, mFriendsList);
                                parseObject.put(Constants.Parse.Team.JOINED_FRIENDS, mJoinedList);
                                parseObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            mNearByAdapter.notifyDataSetChanged();

                                            Toast.makeText(getActivity(), "Joined " + mNearByAdapter.getTeamObjectList().get(position).getString(Constants.Parse.Team.NAME)
                                                    , Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Error occured while joining"
                                                    , Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return true;
            }
        });
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
