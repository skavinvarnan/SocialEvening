package com.kavin.socialevening.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kavin.socialevening.R;
import com.kavin.socialevening.adapter.ChallengeAdapter;
import com.kavin.socialevening.adapter.MyTeamAdapter;
import com.kavin.socialevening.utils.Constants;
import com.kavin.socialevening.views.RoundedImageView;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChallengeScreen extends BaseActivity {

    @Bind(R.id.progress_bar)
    protected ProgressBar mProgressBar;
    @Bind(R.id.team_name)
    protected TextView mTeamName;
    @Bind(R.id.created_at)
    protected TextView mAddress;
    @Bind(R.id.image)
    protected RoundedImageView mTeamImage;
    @Bind(R.id.spinner)
    protected Spinner mSpinner;
    @Bind(R.id.viewpager)
    protected ViewPager mViewPager;
    @Bind(R.id.button)
    protected Button mChallengeButton;
    @Bind(R.id.main_layout)
    protected LinearLayout mMainLayout;

    private ParseObject mTeamObject;

    private List<ParseObject> mMyTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_screen);
        ButterKnife.bind(this);
        setColors(R.color.color_primary, R.color.color_primary_dark, R.color.color_primary);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Challenge");
        }

        ButterKnife.bind(this);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getString(Constants.Intent.OBJECT_ID) != null) {
            mMainLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.Parse.Team.TEAM);
            query.getInBackground(getIntent().getExtras().getString(Constants.Intent.OBJECT_ID), new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        mTeamObject = object;
                        mMainLayout.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                        initViews();
                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        showQuickDialog("Error", "Unable to fetch data from server", true);
                    }
                }
            });
        } else {
            mProgressBar.setVisibility(View.GONE);
            showQuickDialog("Error", "Unknown error occurred", true);
        }
    }

    private void initViews() {
        ParseFile imageFile = mTeamObject.getParseFile(Constants.Parse.Team.PICTURE);
        Picasso.with(this).load(imageFile.getUrl()).error(R.drawable.team_avatar).placeholder(R.drawable.team_avatar)
                .into(mTeamImage);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Challenge " + mTeamObject.getString(Constants.Parse.Team.NAME));
        }
        mTeamName.setText(mTeamObject.getString(Constants.Parse.Team.NAME));
        mAddress.setText("@ " + mTeamObject.getString(Constants.Parse.Team.LOCATION_NAME));

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", ParseUser.getCurrentUser().getEmail());
        ParseCloud.callFunctionInBackground("myTeams", map, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                if (e == null) {
                    myTeamsReceived(object);
                } else {
                    showQuickDialog("Error", "Error fetching your teams", false);
                }
            }
        });

        mViewPager.setAdapter(new ChallengeAdapter(this));
    }


    private void myTeamsReceived(Object object) {
        List<ParseObject> parseObjects = (List<ParseObject>) object;
        mMyTeams = parseObjects;
        if (parseObjects.size() == 0) {
            showQuickDialog("No Teams", "You have no teams. Add one from the My Teams tab", false);
        } else {
            List<String> teamStrings = new ArrayList<String>();
            for (ParseObject parseObject: parseObjects) {
                teamStrings.add(parseObject.getString(Constants.Parse.Team.NAME));
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teamStrings);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(spinnerArrayAdapter);
        }
    }

    @OnClick(R.id.button)
    protected void challengeClicked() {
        ParseObject challenge = new ParseObject(Constants.Parse.Challenge.CHALLENGE);
        challenge.put(Constants.Parse.Challenge.CHALLENGE_TO, mTeamObject);
        challenge.put(Constants.Parse.Challenge.CHALLENGE_FROM, mMyTeams.get(mSpinner.getSelectedItemPosition()));
        challenge.put(Constants.Parse.Challenge.CHALLENGE_STRING, "Challenge #" + (mViewPager.getCurrentItem() + 1));
        challenge.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Challenge sent :)", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    showQuickDialog("Error", "Error creating challenge", false);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

