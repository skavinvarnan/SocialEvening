package com.kavin.socialevening.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kavin.socialevening.R;
import com.kavin.socialevening.utils.Constants;
import com.kavin.socialevening.views.RoundedImageView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TeamInfoActivity extends BaseActivity {

    private ParseObject mTeamObject;

    @Bind(R.id.image)
    protected ImageView mTeamImage;

    @Bind(R.id.address)
    protected TextView mAddress;

    @Bind(R.id.list)
    protected ListView mList;

    @Bind(R.id.requested)
    protected LinearLayout mRequested;

    @Bind(R.id.accept)
    protected ImageView mAccept;

    @Bind(R.id.reject)
    protected ImageView mReject;

    @Bind(R.id.main_layout)
    protected RelativeLayout mMainLayout;

    @Bind(R.id.progress_bar)
    protected ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Team");
        }
    }

    private void initViews() {
        ParseUser teamAdmin = mTeamObject.getParseUser(Constants.Parse.Team.TEAM_ADMIN);
        if (!teamAdmin.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
            List<String> friends = (List<String>) mTeamObject.get(Constants.Parse.Team.JOINED_FRIENDS);
            if (!friends.contains(ParseUser.getCurrentUser().getEmail())) {
                mRequested.setVisibility(View.VISIBLE);
            }
        }

        ParseFile imageFile = mTeamObject.getParseFile(Constants.Parse.Team.PICTURE);
        Picasso.with(this).load(imageFile.getUrl()).error(R.drawable.team_avatar).placeholder(R.drawable.team_avatar)
                .into(mTeamImage);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mTeamObject.getString(Constants.Parse.Team.NAME));
        }

        mAddress.setText("@ " + mTeamObject.getString(Constants.Parse.Team.LOCATION_NAME));

        List<String> joinedFriends = (List<String>) mTeamObject.get(Constants.Parse.Team.JOINED_FRIENDS);
        List<String> friendsList = (List<String>) mTeamObject.get(Constants.Parse.Team.FRIENDS_LIST);
        Log.d("asdf", "sadf");
    }

    @OnClick(R.id.accept)
    protected void accepted() {

    }

    @OnClick(R.id.reject)
    protected void rejected() {

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
